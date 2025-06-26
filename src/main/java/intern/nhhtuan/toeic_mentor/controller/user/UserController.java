package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.ProfileDTO;
import intern.nhhtuan.toeic_mentor.dto.request.ForgotPasswordRequest;
import intern.nhhtuan.toeic_mentor.entity.enums.EGender;
import intern.nhhtuan.toeic_mentor.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("/profile")
    public String profile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("eGender", EGender.values());
        model.addAttribute("profile", userService.getProfile(email));
        return "user/profile";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequest());
        return "user/password-reset";
    }

    @PostMapping("/profile")
    public String updateProfile(@Validated @ModelAttribute ProfileDTO profileDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eGender", EGender.values());
            model.addAttribute("profile", profileDTO);
            return "user/profile";
        }
        try {
            if (userService.updateProfile(profileDTO)) {
                return "redirect:/profile";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("profile", profileDTO);
        return "user/profile";
    }
}
