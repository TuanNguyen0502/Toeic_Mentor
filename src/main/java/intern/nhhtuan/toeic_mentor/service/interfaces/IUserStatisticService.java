package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.UserStatisticResponse;

import java.util.List;

public interface IUserStatisticService {
    List<UserStatisticResponse> getAllUserStatistics(String email);

    UserStatisticResponse getLatestUserStatistic(String email);

    UserStatisticResponse calculateEstimatedScore(String email);
}
