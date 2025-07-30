package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.StreakMilestoneDTO;
import intern.nhhtuan.toeic_mentor.service.interfaces.IStreakMilestoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/streak-milestones")
@RequiredArgsConstructor
public class StreakMilestoneController {
    private final IStreakMilestoneService streakMilestoneService;

    @PostMapping("")
    public ResponseEntity<?> createStreakMilestone(@Valid @RequestBody StreakMilestoneDTO streakMilestoneDTO) {
        boolean isCreated = streakMilestoneService.createStreakMilestone(streakMilestoneDTO);
        if (isCreated) {
            return ResponseEntity.ok("Streak milestone created successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to create streak milestone");
        }
    }
}
