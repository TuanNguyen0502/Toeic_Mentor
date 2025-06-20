package intern.nhhtuan.toeic_mentor.controller;

import intern.nhhtuan.toeic_mentor.dto.response.UserResponse;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {
    private final IUserService userService;

    @ModelAttribute("user")
    public UserResponse getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
            User user = userService.findByEmail(email);
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(user.getId());
            userResponse.setRoleName(user.getRole().getName().toString());
            userResponse.setFullName(user.getFullName());
            userResponse.setImage(user.getAvatarUrl());
            return userResponse;
        }
        return null;
    }
}
