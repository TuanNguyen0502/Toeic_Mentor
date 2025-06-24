package intern.nhhtuan.toeic_mentor.dto;

import intern.nhhtuan.toeic_mentor.entity.enums.ESectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionUpdateDTO {
    private Long id;
    private String title;
    private ESectionStatus status;
    private String parts;
}
