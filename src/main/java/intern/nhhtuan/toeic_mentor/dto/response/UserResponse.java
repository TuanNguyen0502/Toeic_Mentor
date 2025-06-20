package intern.nhhtuan.toeic_mentor.dto.response;

import intern.nhhtuan.toeic_mentor.entity.enums.EGender;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long userId;

    private String fullName;

    private String image;

    private String roleName;
}
