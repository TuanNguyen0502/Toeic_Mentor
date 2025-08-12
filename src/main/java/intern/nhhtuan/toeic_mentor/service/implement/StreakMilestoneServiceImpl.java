package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.StreakMilestoneDTO;
import intern.nhhtuan.toeic_mentor.entity.StreakAchievement;
import intern.nhhtuan.toeic_mentor.entity.StreakMilestone;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.exception.ResourceNotFoundException;
import intern.nhhtuan.toeic_mentor.repository.StreakAchievementRepository;
import intern.nhhtuan.toeic_mentor.repository.StreakMilestoneRepository;
import intern.nhhtuan.toeic_mentor.repository.StudyStreakRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.INotificationService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IStreakMilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreakMilestoneServiceImpl implements IStreakMilestoneService {
    private final StreakMilestoneRepository streakMilestoneRepository;
    private final StreakAchievementRepository streakAchievementRepository;
    private final StudyStreakRepository studyStreakRepository;
    private final INotificationService notificationService;

    private void awardMilestoneToEligibleUsers(StreakMilestone milestone) {
        List<User> users = studyStreakRepository.findEligibleUsersForMilestone(milestone.getDayTarget(), milestone);
        if (users.isEmpty()) return;

        List<StreakAchievement> newAchievements = new ArrayList<>();
        for (User user : users) {
            newAchievements.add(StreakAchievement.builder()
                    .user(user)
                    .streakMilestone(milestone)
                    .achievedAt(LocalDate.now())
                    .build());
            notificationService.createUserStreakAchievementNotifications(user.getEmail(), milestone.getTitle(), milestone.getDayTarget());
        }
        streakAchievementRepository.saveAll(newAchievements);
    }

    private void revokeMilestoneFromIneligibleUsers(StreakMilestone milestone, int oldTarget) {
        List<StreakAchievement> toRevoke = streakAchievementRepository.findAchievementsToRevoke(milestone, milestone.getDayTarget());
        if (toRevoke.isEmpty()) return;

        streakAchievementRepository.deleteAllInBatch(toRevoke);
        for (StreakAchievement sa : toRevoke) {
            notificationService.createUserStreakAchievementRevokedNotification(sa.getUser().getEmail(), milestone.getTitle(), oldTarget);
        }
    }

    @Override
    public StreakMilestoneDTO getStreakMilestoneById(Long id) {
        // Fetch the streak milestone by ID
        StreakMilestone streakMilestone = streakMilestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Streak milestone", "id", id));

        // Convert entity to DTO
        return StreakMilestoneDTO.builder()
                .id(streakMilestone.getId())
                .dayTarget(streakMilestone.getDayTarget())
                .title(streakMilestone.getTitle())
                .description(streakMilestone.getDescription())
                .createdAt(streakMilestone.getCreatedAt().toString())
                .updatedAt(streakMilestone.getUpdatedAt().toString())
                .build();
    }

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
    @Transactional
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
        // Award the milestone to eligible users
        awardMilestoneToEligibleUsers(streakMilestone);

        return true;
    }

    @Override
    @Transactional
    public boolean updateStreakMilestone(Long id, StreakMilestoneDTO streakMilestoneDTO) {
        // Check if the streak milestone exists
        StreakMilestone existingMilestone = streakMilestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Streak milestone", "id", id));

        int oldTarget = existingMilestone.getDayTarget();
        int newTarget = streakMilestoneDTO.getDayTarget();
        boolean targetChanged = oldTarget != newTarget;

        // Check if the day target is being updated and if it already exists
        // If the day target is not being updated, we skip this check
        if (targetChanged &&
                streakMilestoneRepository.existsByDayTarget(newTarget)) {
            throw new ResourceNotFoundException("Streak milestone", "day target",newTarget);
        }

        // Update fields
        existingMilestone.setDayTarget(streakMilestoneDTO.getDayTarget());
        existingMilestone.setTitle(streakMilestoneDTO.getTitle());
        existingMilestone.setDescription(streakMilestoneDTO.getDescription());
        streakMilestoneRepository.save(existingMilestone);

        if (targetChanged) {
            if (newTarget < oldTarget) {
                // reducing target ⇒ award to eligible users and revoke from ineligible users
                awardMilestoneToEligibleUsers(existingMilestone);
            } else { // newTarget > oldTarget
                // increasing target ⇒ revoke from ineligible users
                revokeMilestoneFromIneligibleUsers(existingMilestone, oldTarget);
            }
        }

        return true;
    }

    @Override
    public boolean deleteStreakMilestone(Long id) {
        StreakMilestone existingMilestone = streakMilestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Streak milestone", "id", id));

        List<User> users = streakAchievementRepository.findUsersByStreakMilestoneId(id);

        if (users != null && !users.isEmpty()) {
            Set<String> uniqueEmails = users.stream()
                    .map(User::getEmail)
                    .collect(Collectors.toSet());
            for (String email : uniqueEmails) {
                notificationService.createUserStreakAchievementRevokedNotification(
                        email,
                        existingMilestone.getTitle(),
                        existingMilestone.getDayTarget()
                );
            }
        }

        // Xóa milestone
        streakMilestoneRepository.delete(existingMilestone);
        return true;
    }
}
