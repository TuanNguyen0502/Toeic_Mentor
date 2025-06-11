package intern.nhhtuan.toeic_mentor.service.implement;

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
import java.util.List;

@Service
public class ChatService implements IChatService {
    private final ChatClient chatClient;
    private final ChatModel chatModel;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

    @Value("classpath:/prompts/system-message.txt")
    private Resource systemMessageResource;

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
    public String createTest(InputStream imageInputStream, String contentType, String imageUrl) {
        String prompt = """
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
                      "tags": [<topic1>, <topic2>, ...],
                      "passage": <text if available, or null>,
                      "passage_image_url": <URL if passage cannot be extracted, or null>
                      "part": <number of the part, e.g., 5, 6, or 7>
                    }
                    correct_answer: Analyze and choose the best answer using grammar and context.
                    tags: Always include relevant topics such as: "grammar", "vocabulary", "pronoun", "transition", "verb tense", "article", "business", "school", "daily", ...
                    passage: Use only for Part 6 and 7. For Part 5, set this to null.
                    passage_image_url: Keep "%s" and do not change it.
                    
                    Step 3: Return the result
                    Return ONLY the raw JSON array.
                    Do NOT include any text, explanation, markdown formatting (e.g. triple backticks ```) or surrounding quotes.
                    The output MUST be a valid JSON array.
                    """.formatted(imageUrl);
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
                        .text("""
                                Given an image of a TOEIC Reading question, determine whether the image represents a Part 7 question.
                                Criteria for identifying Part 7:
                                - Part 7 questions are based on longer passages such as emails, notices, articles, advertisements, etc.
                                - There are usually multiple questions (often 2 to 5) referring to the same passage.
                                - The passage may include headers like "From:", "Subject:", or formatting similar to a real-world document.
                                - There are no blanks to fill in — all questions are comprehension-based multiple choice.
                                Your task:
                                1. Analyze the content in the image.
                                2. Return only one of the following values:
                                - TRUE if it is a Part 7 reading comprehension question.
                                - ELSE if it is not (e.g., Part 5 or Part 6).
                                Do not return any explanation or extra text — only `TRUE` or `ELSE`.
                                """)
                        .media(MimeTypeUtils.parseMimeType(contentType), new InputStreamResource(imageInputStream)))
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

