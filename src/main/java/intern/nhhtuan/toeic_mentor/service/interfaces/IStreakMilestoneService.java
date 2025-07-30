package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.StreakMilestoneDTO;
import org.springframework.data.domain.Page;

public interface IStreakMilestoneService {
    StreakMilestoneDTO getStreakMilestoneById(Long id);

    Page<StreakMilestoneDTO> getStreakMilestones(int page, int size, String direction);

    boolean createStreakMilestone(StreakMilestoneDTO streakMilestoneDTO);

    boolean updateStreakMilestone(Long id, StreakMilestoneDTO streakMilestoneDTO);
}

