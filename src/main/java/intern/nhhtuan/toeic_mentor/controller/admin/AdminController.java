package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.StreakMilestoneDTO;
import intern.nhhtuan.toeic_mentor.dto.response.AdminDashboardResponse;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationSettingResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final IUserService userService;
    private final IPartService partService;
    private final ITestService testService;
    private final IQuestionService questionService;
    private final INotificationSettingService notificationSettingService;
    private final IStreakMilestoneService streakMilestoneService;

    @GetMapping("")
    public String index(Model model) {
        AdminDashboardResponse adminDashboardResponse = new AdminDashboardResponse();
        adminDashboardResponse.setTotalUsers(userService.getTotalUsers());
        adminDashboardResponse.setTotalParts(partService.getTotalParts());
        adminDashboardResponse.setTotalTests(testService.getTotalTests());
        adminDashboardResponse.setTotalQuestions(questionService.getTotalQuestions());
        adminDashboardResponse.setParts(partService.getPartResponse());

        model.addAttribute("adminDashboardResponse", adminDashboardResponse);
        return "admin/index";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<NotificationSettingResponse> notificationSettingResponses;
        try {
            notificationSettingResponses = notificationSettingService.getNotificationSettingsByEmail(email);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/setting";
        }
        model.addAttribute("notificationSettingResponses",
                notificationSettingResponses);
        return "admin/setting";
    }

    @GetMapping("/streak-milestones")
    public String streakMilestones(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "DESC") String direction) {
        Page<StreakMilestoneDTO> streakMilestonesPage = streakMilestoneService.getStreakMilestones(page, size, direction);
        model.addAttribute("streakMilestonesPage", streakMilestonesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", streakMilestonesPage.getTotalPages());
        model.addAttribute("direction", direction);
        model.addAttribute("pageSize", size);
        return "admin/streak-milestone/streak-milestone-list";
    }

    @GetMapping("/streak-milestones/create")
    public String createStreakMilestone() {
        return "admin/streak-milestone/new-streak-milestone";
    }
}
