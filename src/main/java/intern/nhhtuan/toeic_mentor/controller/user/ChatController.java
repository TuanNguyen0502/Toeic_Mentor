package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.service.implement.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {
    private final ChatClient chatClient;
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
        this.chatClient = chatService.getChatClient();
    }

    @GetMapping("/stream")
    public Flux<String> chatWithStream(@RequestParam String message,
                                       @RequestParam String email) {
        // Sử dụng Flux để trả về phản hồi theo luồng
//        Flux<String> chatHistory = chatService.getChatHistory(email);
//        Flux<String> response = chatClient.prompt()
//                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, email))
//                .user(message)
//                .stream()
//                .content();
//        return Flux.concat(response, chatHistory);
        return chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, email))
                .user(message)
                .stream()
                .content();
    }
}
