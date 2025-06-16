package intern.nhhtuan.toeic_mentor.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService implements IChatService {
    private final ChatClient chatClient;
    private final ChatModel chatModel;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

    @Value("classpath:/prompts/system-message.txt")
    private Resource systemMessageResource;
    @Value("classpath:/prompts/define-question-part-prompt.txt")
    private Resource defineQuestionPartPromptResource;

    public ChatService(ChatClient.Builder builder,
                       JdbcChatMemoryRepository jdbcChatMemoryRepository,
                       ChatModel chatModel) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;
        this.chatModel = chatModel;
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public Flux<String> getChatResponse(String message, String conversationId) {
        var systemMessage = new SystemMessage(systemMessageResource);
        var userMessage = new UserMessage(message);
        return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage)))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    @Override
    public String createTest(InputStream imageInputStream, String contentType, List<String> imageUrls, String part7PreviousContent) {
        // Prepare the prompt with the image URLs
        // Convert the list of image URLs to a JSON string
        ObjectMapper mapper = new ObjectMapper();
        String imageUrlsJson;
        try {
            imageUrlsJson = mapper.writeValueAsString(imageUrls);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String prompt = """
                    Analyze the provided TOEIC Reading image and extract every question into a standardized JSON format.
                    
                    Step 1: Identify the question part
                    For each question, determine its TOEIC Reading part based on structure:
                    - If the question is a single sentence with a blank and four answer choices, assign "Part 5".
                    - If there is a short passage with 4 blanks and 4 questions, assign "Part 6".
                    - If there is a longer passage (e.g., email, article, notice, advertisement) with multiple comprehension questions, assign "Part 7".
                    
                    Step 2: For each question, extract the following fields:
                    {
                      "question_number": <number>,
                      "question_text": <text of the question>,
                      "options": {
                        "A": <text>,
                        "B": <text>,
                        "C": <text>,
                        "D": <text>
                      },
                      "correct_answer": <A/B/C/D>,
                      "tags": [<topic1>, <topic2>, ...],
                      "passage": <text if available, or null>,
                      "passage_image_urls": <if Part 7, use this: %s; if Part 5 or 6, set to null>,
                      "part": <number of the part, e.g., 5, 6, or 7>
                    }
                    Field instructions:
                    - question_text: For Part 6 and 7, extract the actual question sentence. For Part 5, use the sentence with the blank.
                    - If the image belongs to Part 7, combine that passage with the visual content in the image when extracting and answering the questions.
                    - Additional passage text (if applicable): %s
                    - correct_answer: Analyze and choose the best answer using grammar and context.
                    - tags: Always include relevant topics such as: "grammar", "vocabulary", "pronoun", "transition", "verb tense", "article", ...
                    - passage: Use only for Part 6 and 7, keep the original passage with blanks. For Part 7, combine the detected passage from the image and the input above (if provided) For Part 5, set this to null.
                    - passage_image_url: For Part 7, replace with: %s. For Part 5 and 6, set to null.
                    
                    Step 3: Return the result
                    Return ONLY the raw JSON array.
                    Do NOT include any text, explanation, markdown formatting (e.g. triple backticks ```) or surrounding quotes.
                    The output MUST be a valid JSON array.
                    """.formatted(imageUrlsJson, part7PreviousContent, imageUrlsJson);
        String result = ChatClient.create(chatModel).prompt()
                .user(user -> user
                        .text(prompt)
                        .media(MimeTypeUtils.parseMimeType(contentType), new InputStreamResource(imageInputStream)))
                .call()
                .content();
        return result.replaceAll("^```json\\s*", "").replaceAll("```$", "");
    }

    @Override
    public String definePart(InputStream imageInputStream, String contentType) {
        return ChatClient.create(chatModel).prompt()
                .user(user -> user
                        .text(defineQuestionPartPromptResource)
                        .media(MimeTypeUtils.parseMimeType(contentType), new InputStreamResource(imageInputStream)))
                .call()
                .content();
    }

    @Override
    public List<String> getChatHistory(String conversationId) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
        return chatMemory.get(conversationId).stream().map(Message::getText).toList();
    }

    @Override
    public String buildToeicAnalysisPrompt(List<QuestionResponse> responses) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Convert the list of QuestionResponse to JSON
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responses);

        return String.format("""
        Tôi vừa hoàn thành một bài thi TOEIC. Dưới đây là kết quả của tôi:

        ```json
        %s
        ```

        Vui lòng thực hiện các bước sau:
        1. Bước 1: Chấm điểm
        - Hiển thị số câu đúng / tổng số câu hỏi.
        - Tính phần trăm chính xác.
        
        2. Bước 2: Phân tích các câu trả lời sai
        - Liệt kê danh sách các câu sai (số câu, part, câu hỏi, đáp án đúng, đáp án tôi chọn).
        - Phân tích lý do vì sao đáp án tôi chọn là sai (ví dụ: sai ngữ pháp, chọn sai vì không hiểu từ vựng, hiểu sai nội dung đoạn văn, v.v.).
        - Nếu cần, phân tích cấu trúc câu hỏi hoặc ngữ cảnh đoạn văn để giải thích thêm.
        
        3. Bước 3: Đề xuất luyện tập cá nhân hóa
        - Dựa trên các lỗi sai ở Bước 2, đề xuất các chủ điểm cần cải thiện. Ví dụ:
        + Ngữ pháp (mạo từ, thì động từ, đại từ, liên từ, v.v.)
        + Từ vựng (từ đồng nghĩa, collocations, v.v.)
        + Kỹ năng đọc hiểu (scan, skim, suy luận,...)
        + ...
        - Gợi ý cụ thể dạng bài tập nên luyện thêm (ví dụ: luyện Part 5 ngữ pháp, luyện đọc email Part 7,...)
        - Nếu có thể, gợi ý tài liệu hoặc website/nguồn học đáng tin cậy.
        
        Hiển thị kết quả rõ ràng, dễ đọc.
        """, json);
    }

    @Override
    public String mergeJsonResponses(List<String> jsonResponses) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> mergedList = new ArrayList<>(); // Danh sách để lưu trữ các phần tử đã được phân tích từ các chuỗi JSON

        for (String jsonResponse : jsonResponses) {
            try {
                // Parse từng chuỗi và add phần tử vào mergedList
                mergedList.addAll(objectMapper.readValue(jsonResponse, new TypeReference<List<Object>>(){}));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Invalid JSON response: " + jsonResponse, e);
            }
        }

        String mergedJsonString; // Chuỗi JSON cuối cùng sẽ chứa tất cả các phần tử đã được phân tích
        try {
            // Chuyển đổi mergedList thành chuỗi JSON
            mergedJsonString = objectMapper.writeValueAsString(mergedList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON response: ", e);
        }

        return mergedJsonString;
    }
}

