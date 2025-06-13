package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.TestRequest;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class UserController {
    private final IQuestionService questionService;

    @GetMapping("")
    public String index() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return "redirect:/chat/" + email;
    }

    @GetMapping("/{email}")
    public String chatbot(@PathVariable String email, Model model) {
        model.addAttribute("email", email);
        return "user/index";
    }

    @PostMapping("/generate-test")
    public String generateTest(@RequestBody TestRequest request, Model model) {
        List<QuestionResponse> questions = questionService.generateTest(request);
        model.addAttribute("questions", questions);
        return "user/take-test";
    }
}
