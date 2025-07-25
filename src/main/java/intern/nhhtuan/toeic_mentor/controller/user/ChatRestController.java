package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.response.ChatbotResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRestController {
    private final IChatService chatService;

    @PostMapping(path = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ChatbotResponse> chatWithStream(@RequestParam String message,
                                                @RequestParam String conversationId,
                                                @RequestParam(value = "image", required = false) MultipartFile image) {
        Flux<ChatbotResponse> response;
        if (image == null || image.isEmpty()) {
            response = chatService.getChatResponse(message, conversationId);
        } else {
            try (InputStream inputStream = image.getInputStream()) {
                response = chatService.getChatResponse(message, conversationId, inputStream, image.getContentType());
            } catch (Exception e) {
                response = Flux.error(new RuntimeException("Error processing image input stream", e));
            }
        }
        return response;
    }

    @PostMapping("/conversation-id")
    public Flux<String> generateConversationId(@RequestParam("message") String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        Flux<String> conversationId = chatService.generateConversationId(message, email);
        // Ensure conversationId does not contain any newline characters
        conversationId = conversationId.map(id -> id
                .replace("\n", "")
                .replace("\r", ""));
        return conversationId;
    }

    @GetMapping("/conversation-ids")
    public List<String> getConversationIds(@RequestParam String email) {
        return chatService.getConversationIdsByEmail(email);
    }

    @GetMapping(path = "/conversation")
    public List<ChatbotResponse> getChatHistory(@RequestParam String conversationId) {
        return chatService.getChatHistory(conversationId);
    }

    @DeleteMapping("/conversation")
    public void deleteConversation(@RequestParam String conversationId) {
        chatService.deleteByConversationId(conversationId);
    }

    @PutMapping("/conversation-title")
    public String updateConversationName(@RequestParam String conversationId, @RequestParam String newName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        String newConversationId = email + "_" + newName;
        boolean result = chatService.renameConversation(conversationId, newConversationId);
        if (result) {
            return newConversationId;
        } else {
            return "Failed to update conversation name. Please check the conversation.";
        }
    }
}
