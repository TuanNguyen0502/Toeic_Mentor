package intern.nhhtuan.toeic_mentor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFilterDTO {
    private Map<String, String> filters;
    private String sortBy;
    private String sortDirection;
    private int page;
    private int size;
} 