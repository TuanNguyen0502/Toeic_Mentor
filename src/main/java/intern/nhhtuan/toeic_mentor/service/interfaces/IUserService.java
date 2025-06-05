package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.ForgotPasswordRequest;
import intern.nhhtuan.toeic_mentor.dto.request.RegisterRequest;

import java.io.IOException;

public interface IUserService {
    boolean register(RegisterRequest registerRequest) throws IOException;

    boolean updatePassword(ForgotPasswordRequest forgotPasswordRequest);
}
