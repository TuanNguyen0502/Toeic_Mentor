package intern.nhhtuan.toeic_mentor.dto;

import intern.nhhtuan.toeic_mentor.entity.enums.EGender;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class ProfileDTO {
    private String email;
    @NotBlank(message = "Full name is required")
    private String fullName;
    private EGender gender;
    private String avatarUrl;
    private MultipartFile avatar;
    private String createdAt;
}
