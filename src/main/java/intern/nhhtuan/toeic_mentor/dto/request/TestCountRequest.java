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
    private EPercentChoice percentChoice;
    private int lowerRange;
    private int upperRange;
    private EStatus status;
    private EType type;

    public enum EPercentChoice {
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL ,
        LESS_THAN,
        LESS_THAN_OR_EQUAL,
        EQUAL_TO,
        BETWEEN
    }
    public enum EStatus {
        CORRECT, INCORRECT
    }

    public enum EType {
        COMBINE, SEPARATE
    }
}
