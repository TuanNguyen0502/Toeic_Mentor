package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.GoalCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.GoalResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {
    private final IGoalService goalService;

    @GetMapping("/today")
    public ResponseEntity<List<GoalResponse>> todayGoals() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        String email = authentication.getName();
        List<GoalResponse> goals = goalService.getTodayGoals(email);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GoalResponse>> allGoals() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        String email = authentication.getName();
        List<GoalResponse> goals = goalService.getAllGoals(email);
        return ResponseEntity.ok(goals);
    }

    @PostMapping("")
    public ResponseEntity<String> createGoal(@Valid @RequestBody GoalCreateRequest goalCreateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        String email = authentication.getName();
        boolean isCreated = goalService.createGoal(email, goalCreateRequest);
        if (isCreated) {
            return ResponseEntity.ok("Goal created successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to create goal");
        }
    }
}
