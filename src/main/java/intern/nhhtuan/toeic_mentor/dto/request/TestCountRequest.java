package intern.nhhtuan.toeic_mentor.dto.request;

import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCountRequest {
    private List<EPart> parts;
    private int percent;
    private EStatus status;
    private EType type;

    public enum EStatus {
        CORRECT, INCORRECT
    }

    public enum EType {
        COMBINE, SEPARATE
    }
}
