package intern.nhhtuan.toeic_mentor;

import intern.nhhtuan.toeic_mentor.entity.*;
import intern.nhhtuan.toeic_mentor.entity.enums.EGender;
import intern.nhhtuan.toeic_mentor.entity.enums.ERole;
import intern.nhhtuan.toeic_mentor.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ToeicMentorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToeicMentorApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
                               UserRepository userRepository,
                               StudyStreakRepository studyStreakRepository,
                               StreakHistoryRepository streakHistoryRepository,
                               NotificationSettingRepository notificationSettingRepository,
                               RoleNotificationRepository roleNotificationRepository) {
        return args -> {
            // Tạo role nếu chưa có
            for (ERole roleName : ERole.values()) {
                roleRepository.findByName(roleName).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
            }

            // Tạo user admin mặc định nếu chưa có
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String adminEmail = "admin@toeic.mentor.com";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                User admin = User.builder()
                        .email(adminEmail)
                        .password(bCryptPasswordEncoder.encode("Tuantp2004@"))
                        .role(adminRole)
                        .fullName("Admin Toeic Mentor")
                        .gender(EGender.OTHER)
                        .isActive(true)
                        .build();

                userRepository.save(admin);

                // Tạo StudyStreak cho người dùng mới
                StudyStreak studyStreak = new StudyStreak();
                studyStreak.setCurrentStreak(1);
                studyStreak.setMaxStreak(1);
                studyStreak.setLastStudyDate(LocalDate.now());
                studyStreak.setUser(admin);
                studyStreakRepository.save(studyStreak);

                // Tạo StreakHistory mới cho người dùng
                StreakHistory streakHistory = new StreakHistory();
                streakHistory.setStartStreak(LocalDate.now());
                streakHistory.setUser(admin);
                streakHistoryRepository.save(streakHistory);

                // Tạo NotificationSettings cho người dùng mới
                // Fetch all notification types for the user's role
                List<NotificationType> notificationTypes = roleNotificationRepository.findAllByRole(adminRole).stream()
                        .map(RoleNotification::getNotificationType)
                        .toList();

                // Create default notification settings for each type
                for (NotificationType notificationType : notificationTypes) {
                    NotificationSetting setting = new NotificationSetting();
                    setting.setId(new NotificationSettingId(admin.getId(), notificationType.getId()));
                    setting.setUser(admin);
                    setting.setNotificationType(notificationType);
                    setting.setEnabled(true); // Default to enabled
                    notificationSettingRepository.save(setting);
                }
            }

            // Tạo user mặc định nếu chưa có
            String userEmail = "user@toeic.mentor.com";
            if (userRepository.findByEmail(userEmail).isEmpty()) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

                User user = User.builder()
                        .email(userEmail)
                        .password(bCryptPasswordEncoder.encode("Tuantp2004@"))
                        .role(userRole)
                        .fullName("User Toeic Mentor")
                        .gender(EGender.OTHER)
                        .isActive(true)
                        .build();

                userRepository.save(user);

                // Tạo StudyStreak cho người dùng mới
                StudyStreak studyStreak = new StudyStreak();
                studyStreak.setCurrentStreak(1);
                studyStreak.setMaxStreak(1);
                studyStreak.setLastStudyDate(LocalDate.now());
                studyStreak.setUser(user);
                studyStreakRepository.save(studyStreak);

                // Tạo StreakHistory mới cho người dùng
                StreakHistory streakHistory = new StreakHistory();
                streakHistory.setStartStreak(LocalDate.now());
                streakHistory.setUser(user);
                streakHistoryRepository.save(streakHistory);

                // Tạo NotificationSettings cho người dùng mới
                // Fetch all notification types for the user's role
                List<NotificationType> notificationTypes = roleNotificationRepository.findAllByRole(userRole).stream()
                        .map(RoleNotification::getNotificationType)
                        .toList();

                // Create default notification settings for each type
                for (NotificationType notificationType : notificationTypes) {
                    NotificationSetting setting = new NotificationSetting();
                    setting.setId(new NotificationSettingId(user.getId(), notificationType.getId()));
                    setting.setUser(user);
                    setting.setNotificationType(notificationType);
                    setting.setEnabled(true); // Default to enabled
                    notificationSettingRepository.save(setting);
                }

            }
        };
    }
}
