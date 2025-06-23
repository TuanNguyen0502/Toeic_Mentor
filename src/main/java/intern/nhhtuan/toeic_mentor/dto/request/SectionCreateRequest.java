package intern.nhhtuan.toeic_mentor.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateRequest {
    @NotBlank(message = "Please enter title!")
    private String title;
}
