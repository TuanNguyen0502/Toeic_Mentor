package intern.nhhtuan.toeic_mentor.controller;

import intern.nhhtuan.toeic_mentor.dto.request.ForgotPasswordRequest;
import intern.nhhtuan.toeic_mentor.dto.request.LoginRequest;
import intern.nhhtuan.toeic_mentor.dto.request.RegisterRequest;
import intern.nhhtuan.toeic_mentor.entity.enums.EGender;
import intern.nhhtuan.toeic_mentor.service.implement.AuthService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;
    private final AuthService authService;

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
            model.addAttribute("genders", EGender.values());
            return "user/register";
        }
        try {
            if (userService.register(registerRequest)) {
                return "redirect:/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("genders", EGender.values());
        return "user/register";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model,
                               @RequestParam(value = "error", required = false) String error,
                               HttpSession session) {
        // ✅ Nếu đã đăng nhập, redirect theo role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                return "redirect:/admin";
            } else {
                String email = authentication.getName();
                return "redirect:/chat/" + email;
            }
        }

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

    @PostMapping("/loginProcess")
    public String postLoginPage(@Validated @ModelAttribute("loginRequest") LoginRequest loginRequest,
                                BindingResult bindingResult,
                                HttpServletResponse response,
                                HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "user/login";
        }

        try {
            String redirectUrl = authService.loginAndReturnRedirectUrl(loginRequest, response);
            return "redirect:" + redirectUrl;
        } catch (RuntimeException e) {
            httpSession.setAttribute("LOGIN_ERROR", e.getMessage());
            return "redirect:/login?error";
        }
    }

    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(Model model) {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        model.addAttribute("forgotPasswordRequest", forgotPasswordRequest);
        return "user/password-reset";
    }

    @PostMapping("/forgot-password")
    public String postForgotPasswordPage(@Validated @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequest forgotPasswordRequest,
                                         BindingResult bindingResult,
                                         Model model) {
        if (bindingResult.hasErrors()) {
            return "user/password-reset";
        }

        try {
            if (userService.updatePassword(forgotPasswordRequest)) {
                return "redirect:/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/password-reset";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/chat/{email}")
    public String chatbot(@PathVariable String email, Model model) {
        model.addAttribute("email", email);
        return "user/index";
    }
}
