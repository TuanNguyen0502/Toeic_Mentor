package intern.nhhtuan.toeic_mentor.config;

import intern.nhhtuan.toeic_mentor.service.implement.JwtService;
import intern.nhhtuan.toeic_mentor.service.implement.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailServiceImpl userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    String email = jwtService.extractUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    if (jwtService.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        accessor.setUser(auth); // attach Principal
                    }
                } catch (Exception _) {
                    // Handle the exception if needed, e.g., log it or ignore it
                    // This is where you can handle invalid tokens or other issues
                    // For example, you might want to log the error or send an error response
                    // System.err.println("Invalid JWT token: " + _.getMessage());
                    // You can also throw a custom exception if you want to handle it differently
                    // throw new JwtAuthenticationException("Invalid JWT token", _);
                    // In this case, we simply ignore the error and do not set the user
                    // This means that if the token is invalid, the user will not be authenticated
                }
            }
        }
        return message;
    }
}