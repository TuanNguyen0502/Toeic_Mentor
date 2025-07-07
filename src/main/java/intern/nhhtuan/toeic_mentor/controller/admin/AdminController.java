package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.response.AdminDashboardResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationTypeAction;
import intern.nhhtuan.toeic_mentor.service.interfaces.IPartService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ITestService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final IUserService userService;
    private final IPartService partService;
    private final ITestService testService;
    private final IQuestionService questionService;

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
        model.addAttribute("eNotificationTypeActions", ENotificationTypeAction.values());
        return "admin/setting";
    }
}
