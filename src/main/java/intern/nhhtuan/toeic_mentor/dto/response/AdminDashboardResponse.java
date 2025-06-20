package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private int totalUsers;
    private int totalParts;
    private int totalTests;
    private int totalQuestions;
    private List<PartResponse> parts;
}
