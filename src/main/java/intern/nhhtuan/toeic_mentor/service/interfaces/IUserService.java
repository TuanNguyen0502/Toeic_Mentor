package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.ProfileDTO;
import intern.nhhtuan.toeic_mentor.dto.request.ForgotPasswordRequest;
import intern.nhhtuan.toeic_mentor.dto.request.RegisterRequest;
import intern.nhhtuan.toeic_mentor.entity.User;

import java.io.IOException;

public interface IUserService {
    User findByEmail(String email);

    int getTotalUsers();

    boolean register(RegisterRequest registerRequest) throws IOException;

    boolean updatePassword(ForgotPasswordRequest forgotPasswordRequest);

    ProfileDTO getProfile(String email);

    boolean updateProfile(ProfileDTO profileDTO);
}
