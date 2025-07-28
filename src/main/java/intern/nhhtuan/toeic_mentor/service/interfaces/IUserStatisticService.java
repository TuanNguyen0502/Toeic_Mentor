package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.UserStatisticResponse;

public interface IUserStatisticService {
    UserStatisticResponse getLatestUserStatistic(String email);

    UserStatisticResponse calculateEstimatedScore(String email);
}
