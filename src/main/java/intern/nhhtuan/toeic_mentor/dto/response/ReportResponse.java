package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponse {
    private Long id;
    private String email;
    private Long question_id;
    private String category;
    private String status;
    private String create_at;
}
