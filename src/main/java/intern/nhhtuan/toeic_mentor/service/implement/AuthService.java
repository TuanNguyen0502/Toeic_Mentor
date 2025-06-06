package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    // Service for authentication and JWT handling
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailServiceImpl userDetailsService;

    public String loginAndReturnRedirectUrl(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // 1. Xác thực thông tin đăng nhập
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // 2. Lấy thông tin người dùng sau khi xác thực
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // 3. Tạo JWT
            String jwt = jwtService.generateToken(userDetails.getUsername());

            // 4. Gửi JWT về client dưới dạng cookie
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            response.addCookie(cookie);

            // 5. Redirect dựa theo quyền
            for (GrantedAuthority authority : userDetails.getAuthorities()) {
                if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                    return "/admin";
                }
            }

            return "/"; // mặc định nếu không phải admin

        } catch (AuthenticationException e) {
            throw new RuntimeException("Email hoặc mật khẩu không chính xác");
        }
    }
}
