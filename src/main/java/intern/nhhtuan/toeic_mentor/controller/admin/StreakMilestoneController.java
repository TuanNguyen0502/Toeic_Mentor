package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.StreakMilestoneDTO;
import intern.nhhtuan.toeic_mentor.service.interfaces.IStreakMilestoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("{id}")
    public ResponseEntity<?> updateStreakMilestone(@PathVariable Long id,
                                                   @Valid @RequestBody StreakMilestoneDTO streakMilestoneDTO) {
        boolean isUpdated = streakMilestoneService.updateStreakMilestone(id, streakMilestoneDTO);
        if (isUpdated) {
            return ResponseEntity.ok("Streak milestone updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update streak milestone");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteStreakMilestone(@PathVariable Long id) {
        boolean isDeleted = streakMilestoneService.deleteStreakMilestone(id);
        if (isDeleted) {
            return ResponseEntity.ok("Streak milestone deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete streak milestone");
        }
    }
}
