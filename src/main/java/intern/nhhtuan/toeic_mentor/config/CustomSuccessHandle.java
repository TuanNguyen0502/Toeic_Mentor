package intern.nhhtuan.toeic_mentor.config;

import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandle implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        var authorities = authentication.getAuthorities();
        var roleName = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");
        User user = (User) authentication.getPrincipal();
        intern.nhhtuan.toeic_mentor.entity.User userEntity = userRepository.findByEmail(user.getUsername()).orElse(null);

        if (userEntity == null || !userEntity.isActive()) {
            request.getSession().invalidate();
            request.getSession(true).setAttribute("LOGIN_ERROR", "Account is locked");
            response.sendRedirect("/login?error");
            return;
        }

        if (roleName.contains("ROLE_USER")) {
            response.sendRedirect("/home");
        } else if (roleName.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else {
            response.sendRedirect("/error");
        }
    }
}
