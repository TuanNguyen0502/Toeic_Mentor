package intern.nhhtuan.toeic_mentor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRequest {
    private int question_count;
    private List<Integer> part;
    private List<String> topic;
}