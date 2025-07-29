package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.repository.StudyStreakRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IStudyStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyStreakServiceImpl implements IStudyStreakService {
    private final StudyStreakRepository studyStreakRepository;

    @Override
    public int getCurrentStreak(String email) {
        return studyStreakRepository.getCurrentStreak(email);
    }
}
