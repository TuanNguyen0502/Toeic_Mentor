package intern.nhhtuan.toeic_mentor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Please enter your email!")
    @Pattern(regexp = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}", message = "Email must have @ and .")
    private String email;

    @NotBlank(message = "Please enter your password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[.@#$%^&+=])(?=\\S+$).{8,20}$", message = "Invalid password")
    private String password;

    @NotBlank(message = "Please enter your confirm password")
    private String confirmPassword;

    @NotBlank(message = "Please enter your full name!")
    private String fullName;

    private String gender;

    private MultipartFile image;
}
