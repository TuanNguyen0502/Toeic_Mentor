package intern.nhhtuan.toeic_mentor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ToeicMentorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToeicMentorApplication.class, args);
    }

//    @Bean
//    CommandLineRunner initData(RoleRepository roleRepository,
//                               UserRepository userRepository) {
//        return args -> {
//            // Tạo role nếu chưa có
//            for (ERole roleName : ERole.values()) {
//                roleRepository.findByName(roleName).orElseGet(() -> {
//                    Role role = new Role();
//                    role.setName(roleName);
//                    return roleRepository.save(role);
//                });
//            }
//
//            // Tạo user admin mặc định nếu chưa có
//            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//            String adminEmail = "admin@toeic.mentor.com";
//            if (userRepository.findByEmail(adminEmail).isEmpty()) {
//                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));
//
//                User admin = User.builder()
//                        .email(adminEmail)
//                        .password(bCryptPasswordEncoder.encode("Tuantp2004@"))
//                        .role(adminRole)
//                        .fullName("Admin Toeic Mentor")
//                        .gender(EGender.OTHER)
//                        .isActive(true)
//                        .build();
//
//                userRepository.save(admin);
//            }
//
//            // Tạo user mặc định nếu chưa có
//            String userEmail = "user@toeic.mentor.com";
//            if (userRepository.findByEmail(userEmail).isEmpty()) {
//                Role adminRole = roleRepository.findByName(ERole.ROLE_USER)
//                        .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
//
//                User user = User.builder()
//                        .email(userEmail)
//                        .password(bCryptPasswordEncoder.encode("Tuantp2004@"))
//                        .role(adminRole)
//                        .fullName("User Toeic Mentor")
//                        .gender(EGender.OTHER)
//                        .isActive(true)
//                        .build();
//
//                userRepository.save(user);
//            }
//        };
//    }
}
