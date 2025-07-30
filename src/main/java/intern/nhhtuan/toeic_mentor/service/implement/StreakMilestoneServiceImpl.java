package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.StreakMilestoneDTO;
import intern.nhhtuan.toeic_mentor.entity.StreakMilestone;
import intern.nhhtuan.toeic_mentor.exception.ResourceNotFoundException;
import intern.nhhtuan.toeic_mentor.repository.StreakMilestoneRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IStreakMilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreakMilestoneServiceImpl implements IStreakMilestoneService {
    private final StreakMilestoneRepository streakMilestoneRepository;

    @Override
    public Page<StreakMilestoneDTO> getStreakMilestones(int page, int size, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), "dayTarget");
        // Fetch paginated streak milestones from the repository
        Page<StreakMilestone> milestonesPage = streakMilestoneRepository.findAll(PageRequest.of(page, size, sort));

        // Convert entities to DTOs
        return milestonesPage.map(milestone -> StreakMilestoneDTO.builder()
                .id(milestone.getId())
                .dayTarget(milestone.getDayTarget())
                .title(milestone.getTitle())
                .description(milestone.getDescription())
                .createdAt(milestone.getCreatedAt().toString())
                .updatedAt(milestone.getUpdatedAt().toString())
                .build());
    }

    @Override
    public boolean createStreakMilestone(StreakMilestoneDTO streakMilestoneDTO) {
        // Check if the day target already exists
        if (streakMilestoneRepository.existsByDayTarget(streakMilestoneDTO.getDayTarget())) {
            throw new ResourceNotFoundException("Streak milestone", "day target", streakMilestoneDTO.getDayTarget());
        }

        // Convert DTO to entity
        StreakMilestone streakMilestone = new StreakMilestone();
        streakMilestone.setDayTarget(streakMilestoneDTO.getDayTarget());
        streakMilestone.setTitle(streakMilestoneDTO.getTitle());
        streakMilestone.setDescription(streakMilestoneDTO.getDescription());
        streakMilestoneRepository.save(streakMilestone);
        return true;
    }
}
