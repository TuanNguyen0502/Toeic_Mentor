package intern.nhhtuan.toeic_mentor.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.request.QuestionRequest;
import intern.nhhtuan.toeic_mentor.repository.ChatMemoryRepository;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final ChatMemoryRepository chatMemoryRepository;

    @Value("classpath:/prompts/system-message.txt")
    private Resource systemMessageResource;
    @Value("classpath:/prompts/define-question-part-prompt.txt")
    private Resource defineQuestionPartPromptResource;
    @Value("classpath:/prompts/identify-toeic-test.txt")
    private Resource identifyToeicTestPromptResource;

    public ChatService(ChatClient.Builder builder,
                       JdbcChatMemoryRepository jdbcChatMemoryRepository,
                       ChatModel chatModel,
                       ChatMemoryRepository chatMemoryRepository) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.chatModel = chatModel;
        this.chatMemoryRepository = chatMemoryRepository;
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
    public Flux<String> getChatResponse(String message, String conversationId, InputStream imageInputStream, String contentType) {
        return ChatClient.create(chatModel).prompt()
                .system(systemMessageResource)
                .user(user -> user
                        .text(message)
                        .media(MimeTypeUtils.parseMimeType(contentType), new InputStreamResource(imageInputStream)))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    @Override
    public List<QuestionRequest> createTest(InputStream imageInputStream, String contentType, List<String> imageUrls, String part7PreviousContent) {
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
                Analyze the provided TOEIC Reading image and extract every question into a entity.
                
                Step 1: Identify the question part
                For each question, determine its TOEIC Reading part based on structure:
                - If the question is a single sentence with a blank and four answer choices, assign "Part 5".
                - If there is a short passage with 4 blanks and 4 questions, assign "Part 6".
                - If there is a longer passage (e.g., email, article, notice, advertisement) with multiple comprehension questions, assign "Part 7".
                
                Step 2: For each question, extract the following fields:
                {
                  "questionNumber": <number>,
                  "questionText": <text of the question>,
                  "options": {
                    "A": <text>,
                    "B": <text>,
                    "C": <text>,
                    "D": <text>
                  },
                  "correctAnswer": <A/B/C/D>,
                  "tags": [<topic1>, <topic2>, ...],
                  "passage": <text for Part 6 and 7, or Part 5 null>,
                  "passageImageUrls": <if Part 7, use this: %s; if Part 5 or 6, set to null>,
                  "part": <number of the part, e.g., 5, 6, or 7>
                }
                Field instructions:
                - questionText: For Part 6 and 7, extract the actual question sentence. For Part 5, use the sentence with the blank.
                - If the image belongs to Part 7, combine that passage with the visual content in the image when extracting and answering the questions.
                - Additional passage text (if applicable): %s
                - correctAnswer: Analyze and choose the best answer using grammar and context.
                - tags: Always include relevant topics such as: "grammar", "vocabulary", "pronoun", "transition", "verb tense", "article", ...
                - passage: Use only for Part 6 and 7, keep the original passage with blanks. For Part 7, combine the detected passage from the image and the input above (if provided) For Part 5, set this to null.
                - passageImageUrl: For Part 7, replace with: %s. For Part 5 and 6, set to null.
                
                Step 3: Return the result
                Do NOT include any text, explanation, markdown formatting (e.g. triple backticks ```) or surrounding quotes.
                """.formatted(imageUrlsJson, part7PreviousContent, imageUrlsJson);
        return ChatClient.create(chatModel).prompt()
                .user(user -> user
                        .text(prompt)
                        .media(MimeTypeUtils.parseMimeType(contentType), new InputStreamResource(imageInputStream)))
                .call()
                .entity(new ParameterizedTypeReference<List<QuestionRequest>>() {});
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
    public String identifyToeicTest(InputStream imageInputStream, String contentType) {
        return ChatClient.create(chatModel).prompt()
                .user(user -> user
                        .text(identifyToeicTestPromptResource)
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
    public List<String> getConversationIdsByEmail(String email) {
        List<String> conversationIds = chatMemoryRepository.findAll()
                .stream()
                .map(intern.nhhtuan.toeic_mentor.entity.ChatMemory::getConversationId)
                .distinct()
                .toList();
        return conversationIds.stream()
                .filter(id -> id.contains(email)) // Filter conversation IDs that contain the user's email
                .toList();
    }

    @Override
    public String buildTestAnalysisPrompt(List<AnswerRequest> answerRequests) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Convert the list of QuestionResponse to JSON
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(answerRequests);

        return String.format("""
                You are an expert TOEIC evaluator and personalized language coach.
                This is user's TOEIC test results in JSON format, follow these steps precisely:
                
                ```json
                %s
                ```
                
                ---
                
                Step 1: Score the test
                - Count the number of correct answers.
                - Display the score as: "<Correct>/<Total> correct answers".
                - Calculate and show the accuracy rate as a percentage.
                
                ---
                
                Step 2: Analyze incorrect responses
                For each incorrect answer, provide the following information in a clear list format:
                - Question number
                - TOEIC Part (e.g., Part 5, 6, or 7)
                - Question text
                - User’s selected answer
                - Correct answer
                - Error analysis:
                  • Explain why the selected answer is incorrect.
                  • Identify the nature of the mistake (e.g., grammar error, vocabulary misunderstanding, incorrect inference).
                  • Include brief context or sentence breakdowns if necessary (e.g., subject-verb agreement, misplaced modifier, etc.).
                
                ---
                
                Step 3: Personalized study recommendations
                Based on patterns of errors in Step 2, recommend targeted areas for improvement:
                - Grammar topics (e.g., articles, verb tenses, transitions, pronouns, modifiers).
                - Vocabulary skills (e.g., synonyms, collocations, word forms).
                - Reading skills (e.g., scanning, skimming, inference).
                - Suggest specific TOEIC Parts and question types to focus on for practice.
                - If possible, suggest reliable resources (e.g., websites, books, YouTube channels) for self-study.
                
                ---
                
                Format the output to be highly readable, using headings and bullet points.
                Keep the tone helpful and focused on improvement.
                Do not include unnecessary explanations or repeat the input JSON.
                """, json);
    }

    @Override
    public Flux<String> analyzeTestResult(List<AnswerRequest> answerRequests, String conversationId) {
        String prompt;
        try {
            prompt = buildTestAnalysisPrompt(answerRequests);
        } catch (JsonProcessingException e) {
            return Flux.error(new RuntimeException("Error building TOEIC analysis prompt", e));
        }

        String systemMessage = prompt;
        String userMessage = "I’ve just completed a TOEIC test. Please evaluate my performance according to the steps provided earlier.";
        return chatClient.prompt()
                .system(systemMessage)
                .user(userMessage)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    @Override
    public String mergeJsonResponses(List<String> jsonResponses) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> mergedList = new ArrayList<>(); // Danh sách để lưu trữ các phần tử đã được phân tích từ các chuỗi JSON

        for (String jsonResponse : jsonResponses) {
            try {
                // Parse từng chuỗi và add phần tử vào mergedList
                mergedList.addAll(objectMapper.readValue(jsonResponse, new TypeReference<List<Object>>() {
                }));
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

    @Override
    @Transactional
    public void deleteByConversationId(String conversationId) {
        chatMemoryRepository.deleteByConversationId(conversationId);
    }

    @Override
    public String generateConversationId(String message, String email) {
        String prompt = String.format("""
                    You are an assistant that generates a concise and meaningful title for a conversation based on the user’s initial question.
                
                    Instructions:
                    1. Analyze the user's question and determine the main topic or purpose.
                    2. Generate a short and clear conversation title using 2–5 words (lowercase, hyphen-separated if needed, no special characters).
                    3. Return the final result in this format: <email>_<generated-title>
                
                    Input:
                    Email: %s
                    User question: "%s"
                
                    Output:
                    <email>_<generated-title>
                """, email, message);
        return ChatClient.create(chatModel).prompt()
                .user(user -> user
                        .text(prompt))
                .call()
                .content();
    }

    @Override
    public boolean renameConversation(String oldConversationId, String newConversationId) {
        // Check if the new conversation ID already exists
        if (chatMemoryRepository.existsChatMemoryByConversationId(newConversationId)) {
            return false; // New conversation ID already exists, cannot rename
        }

        // Rename the conversation by updating the conversation ID in the repository
        List<intern.nhhtuan.toeic_mentor.entity.ChatMemory> chatMemories = chatMemoryRepository.findAllByConversationId(oldConversationId);
        if (chatMemories.isEmpty()) {
            return false; // No conversation found with the old ID
        }
        for (intern.nhhtuan.toeic_mentor.entity.ChatMemory chatMemory : chatMemories) {
            chatMemory.setConversationId(newConversationId);
        }
        chatMemoryRepository.saveAll(chatMemories); // Save the updated chat memories
        return true; // Successfully renamed
    }
}