package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.UserStatisticResponse;

public interface IUserStatisticService {
    UserStatisticResponse calculateEstimatedScore(String email);
}
