package intern.nhhtuan.toeic_mentor.config;

import intern.nhhtuan.toeic_mentor.service.implement.JwtService;
import intern.nhhtuan.toeic_mentor.service.implement.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwtFromCookies(request);
        String email = null;
        try {
            email = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Log the exception or handle it as needed
            System.out.println("JWT extraction failed: " + e.getMessage());
        }

        // If JWT is valid and the user is not authenticated, set the authentication in the context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the UserDetailServiceImpl
            UserDetails userDetails = applicationContext.getBean(UserDetailServiceImpl.class)
                    .loadUserByUsername(email);

            // Check if the JWT is valid
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Create an authentication token and set it in the security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                // This is necessary for the security context to recognize the user as authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }
}
