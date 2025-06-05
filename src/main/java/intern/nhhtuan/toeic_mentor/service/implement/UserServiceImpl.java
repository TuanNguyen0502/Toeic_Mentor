package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.ForgotPasswordRequest;
import intern.nhhtuan.toeic_mentor.dto.request.RegisterRequest;
import intern.nhhtuan.toeic_mentor.entity.EGender;
import intern.nhhtuan.toeic_mentor.entity.ERole;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.repository.RoleRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IUserService;
import intern.nhhtuan.toeic_mentor.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ImageUtil imageUtil;

    @Override
    public boolean register(RegisterRequest registerRequest) throws IOException {
        validateEmailUnique(registerRequest);

        if (!isPasswordConfirmed(registerRequest.getPassword(), registerRequest.getConfirmPassword())) {
            throw new BadCredentialsException("Password not match");
        }

        // Create new User
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new IllegalArgumentException("Role not found")));
        user.setFullName(registerRequest.getFullName());
        user.setGender(EGender.valueOf(registerRequest.getGender()));
        user.setActive(true);

        // Xử lý ảnh
        if (registerRequest.getImage() != null && !registerRequest.getImage().isEmpty()) {
            if (imageUtil.isValidSuffixImage(Objects.requireNonNull(registerRequest.getImage().getOriginalFilename()))) {
                user.setAvatarUrl(imageUtil.saveImage(registerRequest.getImage()));
            } else {
                throw new IllegalArgumentException("Invalid image format.");
            }
        } else {
            user.setAvatarUrl("https://res.cloudinary.com/toeic-mentor/image/upload/v1749023234/default-user_szbwye.jpg");
        }

        // Lưu vào database
        userRepository.save(user);
        return true;
    }

    public void validateEmailUnique(RegisterRequest registerRequest) {
        User existingUserByEmail = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if (existingUserByEmail != null) {
            throw new BadCredentialsException("Email already exists");
        }
    }

    @Override
    public boolean updatePassword(ForgotPasswordRequest forgotPasswordRequest) {
        // Kiểm tra email có tồn tại không
        User userEntity = userRepository.findByEmail(forgotPasswordRequest.getEmail()).orElse(null);

        if (userEntity == null) {
            throw new BadCredentialsException("Email does not exist");
        }

        if (!isPasswordConfirmed(forgotPasswordRequest.getPassword(), forgotPasswordRequest.getConfirmPassword())) {
            throw new BadCredentialsException("Password not match");
        }

        userEntity.setPassword(bCryptPasswordEncoder.encode(forgotPasswordRequest.getPassword()));
        userRepository.save(userEntity);
        return true;
    }

    public boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }
}
