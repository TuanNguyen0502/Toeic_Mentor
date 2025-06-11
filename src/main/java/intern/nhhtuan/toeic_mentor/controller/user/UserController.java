package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.TestRequest;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class UserController {
    private final IQuestionService questionService;

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
