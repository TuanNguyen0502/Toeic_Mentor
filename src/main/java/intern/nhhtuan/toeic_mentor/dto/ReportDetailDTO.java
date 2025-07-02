package intern.nhhtuan.toeic_mentor.dto;

import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportDetailDTO {
    private Long id;
    private String email;
    private QuestionUpdateDTO questionUpdateDTO;
    private String category;
    private String description;
    private EReportStatus status;
    private String admin_response;
    private String create_at;
}
