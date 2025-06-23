package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {
    private Long id;
    private String title;
    private String status;
    private String updatedAt;
    private int questionCount;
    private String parts;
}
