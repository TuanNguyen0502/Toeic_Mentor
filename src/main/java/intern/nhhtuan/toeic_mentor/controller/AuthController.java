package intern.nhhtuan.toeic_mentor.controller;

import intern.nhhtuan.toeic_mentor.dto.request.LoginRequest;
import intern.nhhtuan.toeic_mentor.dto.request.RegisterRequest;
import intern.nhhtuan.toeic_mentor.entity.EGender;
import intern.nhhtuan.toeic_mentor.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        RegisterRequest registerRequest = new RegisterRequest();
        model.addAttribute("registerRequest", registerRequest);
        model.addAttribute("genders", EGender.values());
        return "user/register";
    }

    @PostMapping("/register")
    public String postRegisterPage(@Validated @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "user/register";
        }
        try {
            if (userService.register(registerRequest)) {
                return "redirect:/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/register";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model,
                               @RequestParam(value = "error", required = false) String error,
                               HttpSession session) {
        LoginRequest loginRequest = new LoginRequest();
        model.addAttribute("loginRequest", loginRequest);

        if (error != null) {
            String errorMessage = (String) session.getAttribute("LOGIN_ERROR");
            session.removeAttribute("LOGIN_ERROR"); // tránh lặp lại
            if (errorMessage != null && errorMessage.contains("Account is locked")) {
                model.addAttribute("error", "Account is locked. Please contact administrator.");
            } else {
                model.addAttribute("error", "Invalid username or password");
            }
        }

        return "user/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "Logout successful";
    }
}
