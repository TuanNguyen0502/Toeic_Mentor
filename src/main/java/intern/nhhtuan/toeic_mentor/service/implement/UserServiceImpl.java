package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.ProfileDTO;
import intern.nhhtuan.toeic_mentor.dto.request.ForgotPasswordRequest;
import intern.nhhtuan.toeic_mentor.dto.request.RegisterRequest;
import intern.nhhtuan.toeic_mentor.entity.enums.EGender;
import intern.nhhtuan.toeic_mentor.entity.enums.ERole;
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
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ImageUtil imageUtil;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("User not found with email: " + email));
    }

    @Override
    public int getTotalUsers() {
        return userRepository.findAll().size();
    }

    @Override
    public boolean register(RegisterRequest registerRequest) throws IOException {
        // Kiểm tra email đã tồn tại chưa
        validateEmailUnique(registerRequest.getEmail());

        // Kiểm tra mật khẩu và xác nhận mật khẩu khớp nhau không
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
            // Kiểm tra định dạng ảnh hợp lệ
            if (imageUtil.isValidSuffixImage(Objects.requireNonNull(registerRequest.getImage().getOriginalFilename()))) {
                user.setAvatarUrl(imageUtil.saveImage(registerRequest.getImage()));
            } else {
                throw new IllegalArgumentException("Invalid image format.");
            }
        } else {
            // Nếu không có ảnh, sử dụng ảnh mặc định
            user.setAvatarUrl("https://res.cloudinary.com/toeic-mentor/image/upload/v1749023234/default-user_szbwye.jpg");
        }

        // Lưu vào database
        userRepository.save(user);
        return true;
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

    @Override
    public ProfileDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found with email: " + email));

        return ProfileDTO.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
    }

    @Override
    public boolean updateProfile(ProfileDTO profileDTO) {
        User user = userRepository.findByEmail(profileDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found with email: " + profileDTO.getEmail()));
        user.setFullName(profileDTO.getFullName());
        user.setGender(profileDTO.getGender());
        if (profileDTO.getAvatar() != null && !profileDTO.getAvatar().isEmpty()) {
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                // Xóa ảnh cũ nếu có
                try {
                    imageUtil.deleteImage(user.getAvatarUrl());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // Kiểm tra định dạng ảnh hợp lệ
            if (imageUtil.isValidSuffixImage(Objects.requireNonNull(profileDTO.getAvatar().getOriginalFilename()))) {
                try {
                    user.setAvatarUrl(imageUtil.saveImage(profileDTO.getAvatar()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("Invalid image format.");
            }
        } else {
            // Nếu không có ảnh, giữ nguyên ảnh cũ
            user.setAvatarUrl(user.getAvatarUrl());
        }
        userRepository.save(user);
        return true;
    }

    private void validateEmailUnique(String email) {
        User existingUserByEmail = userRepository.findByEmail(email).orElse(null);
        if (existingUserByEmail != null) {
            throw new BadCredentialsException("Email already exists");
        }
    }

    private boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }
}
