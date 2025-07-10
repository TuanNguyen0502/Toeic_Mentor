package intern.nhhtuan.toeic_mentor.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
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
import java.util.List;
import java.util.Objects;

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
    @Value("classpath:/prompts/test-analysis-prompt.txt")
    private Resource testAnalysisPromptResource;

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
    public List<QuestionDTO> createTest(InputStream imageInputStream, String contentType, List<String> imageUrls, String part7PreviousContent) {
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
                You are a TOEIC Reading question analyzer.
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
                  "answerExplanation": <why the correct answer is correct>,
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
                - answerExplanation: Provide a short analysis of why the correct answer is correct, using grammar, vocabulary, or context.
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
                .entity(new ParameterizedTypeReference<List<QuestionDTO>>() {
                });
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
    public TestResultResponse analyzeTestResult(List<AnswerRequest> answerRequests) {
        ObjectMapper objectMapper = new ObjectMapper();
        String answerRequestsJson;
        try {
            answerRequestsJson = objectMapper.writeValueAsString(answerRequests);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String prompt = """
                You are an expert TOEIC evaluator and personalized language coach.
                Objective:
                Analyze a TOEIC test submission and generate a comprehensive result summary including correctness,
                detailed explanations for all options (A, B, C, D) explaining why each option is correct or incorrect,
                performance evaluation in descriptive text, and improvement recommendations.
                
                Input:
                A JSON array of AnswerRequest objects in the following format:
                [
                  {
                    "id": Long,
                    "questionText": String,
                    "correctAnswer": String, // This is always the option key, such as "A", "B", "C", or "D"
                    "answerExplanation": String, // This is the explanation for the correct answer
                    "userAnswer": String, // This is always the option key, such as "A", "B", "C", or "D"
                    "part": Integer,
                    "options": [
                      {"key": String, "value": String}
                    ],
                    "tags": [String],
                    "timeSpent": Integer,           // Time spent in seconds
                    "difficulty": Integer           // From 1 (easiest) to 5 (hardest)
                  }
                ]
                
                ---
                
                Instructions:
                
                1. Evaluate and Score:
                - For each AnswerRequest:
                    - Compare userAnswer with correctAnswer strictly by key value.
                    - If userAnswer equals correctAnswer, set isCorrect = true.
                    - Otherwise, set isCorrect = false.
                - Count the total number of correct answers.
                - Compute:
                    - score: total correct answers
                    - correctPercent = (correct / total) × 100, rounded to the nearest integer.
                
                2. Transform AnswerRequests into AnswerResponses:
                - For each AnswerRequest, create a corresponding AnswerResponse object:
                    - Copy all fields from the original AnswerRequest.
                    - Add:
                        - isCorrect: as determined above.
                        - answerExplanation: convert to a List of Strings containing explanations for all options A–D:
                            - For each option key in options, generate a sentence explaining:
                                - If the option is the correct answer: why it is correct.
                                - If the option is incorrect: why it is not correct.
                            - Example:
                                "answerExplanation": [
                                  "Option A ('reviewed'): Correct because it is the past tense indicating completed action.",
                                  "Option B ('reviews'): Incorrect because it is present tense.",
                                  "Option C ('was reviewing'): Incorrect because it suggests an ongoing past action.",
                                  "Option D ('has reviewed'): Incorrect because it indicates relevance to the present."
                                ]
                
                3. Performance Evaluation:
                - Analyze:
                    - Overall correctPercent.
                    - Average timeSpent.
                    - Average difficulty.
                - Generate a short paragraph summarizing:
                    - Accuracy level.
                    - Speed relative to question difficulty.
                    - Balanced pacing or need for improvement.
                - Example outputs:
                    - High accuracy and fast speed: "Bạn đạt tỷ lệ chính xác cao và hoàn thành các câu hỏi nhanh chóng, kể cả với mức độ khó cao. Đây là hiệu suất rất tốt."
                    - Medium accuracy and slow speed: "Bạn trả lời chính xác khoảng 60% câu hỏi, nhưng mất khá nhiều thời gian, đặc biệt ở câu hỏi khó. Bạn nên luyện tập tăng tốc độ đọc hiểu."
                    - Low accuracy and fast speed: "Bạn hoàn thành bài thi nhanh, nhưng tỷ lệ đúng thấp. Cần cân nhắc đọc kỹ hơn để cải thiện độ chính xác."
                
                4. Recommendations:
                - Generate a concise recommendation (2–3 sentences) suggesting study focus areas, e.g.,
                    - Grammar topics (articles, tenses, passive voice).
                    - Vocabulary (collocations, synonyms).
                    - Reading strategies.
                    - Suggested resources for improvement.
                - Mention relevant grammar or vocabulary topics, and optionally suggest learning resources.
                - Include 2–3 reference URLs to credible resources for practice.
                
                5. Output:
                Return a JSON object of type TestResultResponse in the following format:
                {
                  "testId": null,           // Optional
                  "score": Integer,         // total correct answers
                  "correctPercent": Integer,
                  "answerResponses": [
                    {
                      "id": Long,
                      "questionText": String,
                      "correctAnswer": String,
                      "userAnswer": String,
                      "part": Integer,
                      "options": [
                        {"key": String, "value": String}
                      ],
                      "tags": [String],
                      "timeSpent": Integer, // Time spent in seconds
                      "isCorrect": Boolean,
                      "answerExplanation": [String]   // List of 4 explanations (one per option)
                    }
                  ],
                  "recommendations": String,     // concise improvement suggestions
                  "performance": String,         // descriptive paragraph combining correctness, speed, difficulty
                  "referenceUrls": [String]      // list of URLs
                }
                
                6. Constraints:
                - Do NOT include any text or commentary outside the JSON.
                - Ensure all fields are populated as specified.
                - Use only the options.key to determine correctness.
                - Explanations must be clear and specific.""";
        String fullPrompt = prompt + "\n\nInput JSON:\n" + answerRequestsJson;
        TestResultResponse testResultResponse = ChatClient.create(chatModel).prompt()
                .user(fullPrompt)
                .call()
                .entity(TestResultResponse.class);
        for (TestResultResponse.AnswerResponse answerResponse : testResultResponse.getAnswerResponses()) {
            answerResponse.setCorrect(Objects.equals(answerResponse.getUserAnswer(), answerResponse.getCorrectAnswer()));
        }
        return testResultResponse;
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