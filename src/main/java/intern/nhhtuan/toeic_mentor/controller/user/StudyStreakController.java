package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.service.interfaces.IStudyStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study-streaks")
@RequiredArgsConstructor
public class StudyStreakController {
    private final IStudyStreakService studyStreakService;

    @GetMapping("/current-streak")
    public int getCurrentStreak() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String email = auth.getName();
        return studyStreakService.getCurrentStreak(email);
    }
}
