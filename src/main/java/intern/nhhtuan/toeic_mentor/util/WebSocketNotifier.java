package intern.nhhtuan.toeic_mentor.util;

import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.entity.enums.ERole;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiConsumer;

@Component
@RequiredArgsConstructor
public class WebSocketNotifier {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    /**
     * Send a WebSocket message to a single user.
     *
     * @param email      the email (username) of the user
     * @param destination the STOMP destination (e.g., "/queue/notifications")
     * @param payload     the message content
     */
    public <T> void sendToUser(String email, String destination, T payload) {
        messagingTemplate.convertAndSendToUser(email, destination, payload);
    }

    /**
     * Send the same WebSocket message to multiple users.
     *
     * @param emails      list of user emails
     * @param destination shared destination
     * @param payload     the message content to send to all
     */
    public <T> void sendToUsers(List<String> emails, String destination, T payload) {
        emails.forEach(email -> messagingTemplate.convertAndSendToUser(email, destination, payload));
    }

    /**
     * Send personalized messages to multiple users using a handler function.
     *
     * @param emails     list of user emails
     * @param payloads   list of corresponding payloads (must match size of emails)
     * @param destination the shared destination
     * @param handler    function that receives (email, payload) for sending
     */
    public <T> void sendToUsers(List<String> emails, List<T> payloads, String destination, BiConsumer<String, T> handler) {
        for (int i = 0; i < emails.size(); i++) {
            handler.accept(emails.get(i), payloads.get(i));
        }
    }

    /**
     * Send the same WebSocket message to all users with a specific role.
     *
     * @param role        the role (e.g., ROLE_ADMIN)
     * @param destination the destination to send to (e.g., "/queue/admin/notifications")
     * @param payload     the message to broadcast to the role group
     */
    public <T> void sendToRole(ERole role, String destination, T payload) {
        List<User> users = userRepository.findAllByRole_Name(role);
        List<String> emails = users.stream().map(User::getEmail).toList();
        sendToUsers(emails, destination, payload);
    }

    /**
     * Send personalized messages to each user in a specific role.
     *
     * @param role        the role (e.g., ROLE_ADMIN)
     * @param payloads    a list of messages, one for each user
     * @param destination the destination (e.g., "/queue/admin/notifications")
     * @param handler     function to apply (email, payload)
     */
    public <T> void sendToRoleWithPayloads(ERole role, List<T> payloads, String destination, BiConsumer<String, T> handler) {
        List<User> users = userRepository.findAllByRole_Name(role);
        List<String> emails = users.stream().map(User::getEmail).toList();
        sendToUsers(emails, payloads, destination, handler);
    }
}
