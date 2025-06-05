package intern.nhhtuan.toeic_mentor;

import intern.nhhtuan.toeic_mentor.entity.ERole;
import intern.nhhtuan.toeic_mentor.entity.Role;
import intern.nhhtuan.toeic_mentor.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ToeicMentorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToeicMentorApplication.class, args);
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (ERole roleName : ERole.values()) {
                roleRepository.findByName(roleName).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
            }
        };
    }
}
