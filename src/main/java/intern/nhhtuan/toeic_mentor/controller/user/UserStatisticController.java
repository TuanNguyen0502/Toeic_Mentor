package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.response.UserStatisticResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IUserStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class UserStatisticController {
    private final IUserStatisticService userStatisticService;

    @GetMapping("/latest")
    public UserStatisticResponse getLatestUserStatistic() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String email = authentication.getName();
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("User email is not available");
        }
        return userStatisticService.getLatestUserStatistic(email);
    }

    @GetMapping("/estimated-score")
    public UserStatisticResponse getEstimatedScore() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String email = authentication.getName();
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("User email is not available");
        }
        return userStatisticService.calculateEstimatedScore(email);
    }
}
