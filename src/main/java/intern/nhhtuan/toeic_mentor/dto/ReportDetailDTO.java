package intern.nhhtuan.toeic_mentor.dto;

import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportDetailDTO {
    private Long id;
    private String email;

    @Valid
    private QuestionUpdateDTO questionUpdateDTO;
    private String category;
    private String description;
    private EReportStatus status;

    @NotBlank(message = "Admin response cannot be blank")
    private String admin_response;
    private String create_at;
}
