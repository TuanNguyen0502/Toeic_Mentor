package intern.nhhtuan.toeic_mentor.dto.request;

import intern.nhhtuan.toeic_mentor.entity.enums.EReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportRequest {
    private Long questionId;

    @NotNull(message = "Please select a report category")
    private EReportType category;

    @NotBlank(message = "Please enter a description for the report")
    private String description;
}
