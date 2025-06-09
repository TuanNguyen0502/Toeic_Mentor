package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.util.ImageUtil;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final ChatModel chatModel;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessageResource;

    private final ImageUtil imageUtil;

    public ChatService(ChatClient.Builder builder,
                       JdbcChatMemoryRepository jdbcChatMemoryRepository,
                       ChatModel chatModel,
                       ImageUtil imageUtil) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;
        this.chatModel = chatModel;
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.imageUtil = imageUtil;
    }

    public Flux<String> getChatResponse(String message, String conversationId) {
        var systemMessage = new SystemMessage(systemMessageResource);
        var userMessage = new UserMessage(message);
        return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage)))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    public String createTest(MultipartFile imageFile) throws IOException {
        String imageUrl = imageUtil.saveImageLocally(imageFile);
        return ChatClient.create(chatModel).prompt()
                .user(user -> user
                        .text("""
                                Analyze the provided TOEIC Reading image and extract every question into a standardized JSON format.

                                Step 1: Identify the question part
                                For each question, determine its TOEIC Reading part based on structure:
                                If the question is a single sentence with a blank, assign "Part 5".
                                If there is a short passage with 4 blanks and 4 questions, assign "Part 6".
                                If there is a longer passage with multiple comprehension questions, assign "Part 7".

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
                                  "tags": [<"Part 5/6/7">, <topic>],
                                  "passage": <text if available, or null>,
                                  "passage_image_url": <URL if passage cannot be extracted, or null>
                                }
                                correct_answer: Analyze and choose the best answer using grammar and context.
                                tags: Always include the part (e.g., "Part 6") and relevant topics such as: "grammar", "vocabulary", "pronoun", "transition", "verb tense", "article", etc.
                                passage: Use only for Part 6 and 7. For Part 5, set this to null.
                                passage_image_url: Only for Part 7 when the passage text cannot be extracted (use a placeholder like "image_url_here" if needed).

                                Step 3: Return the result
                                Return a valid JSON array, where each element represents a single question.
                                Do not return any extra text or explanation — only the array.
                                """)
                        .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource(imageUrl)))
                .call()
                .content();
    }

    public Flux<String> getChatHistory(String conversationId) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
        List<String> messages = chatMemory.get(conversationId).stream().map(Message::getText).toList();
        return Flux.fromIterable(messages);
    }
}

