package intern.nhhtuan.toeic_mentor.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class UserController {

    @GetMapping("/{email}")
    public String chatbot(@PathVariable String email, Model model) {
        model.addAttribute("email", email);
        return "user/index";
    }
}
