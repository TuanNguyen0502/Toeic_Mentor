package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.ReportRequest;
import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import intern.nhhtuan.toeic_mentor.repository.QuestionRepository;
import intern.nhhtuan.toeic_mentor.repository.ReportRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {
    private final ReportRepository reportRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    public boolean saveReport(String email, ReportRequest request) {
        // Validate the request
        if (request.getQuestionId() == null || request.getCategory() == null || request.getDescription() == null) {
            return false; // Invalid request
        }

        // Create a new report entity
        Report report = Report.builder()
                .category(request.getCategory())
                .description(request.getDescription())
                .status(EReportStatus.OPEN) // Default status
                .user(userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")))
                .question(questionRepository.findById(request.getQuestionId()).orElseThrow(() -> new IllegalArgumentException("Question not found")))
                .build();

        // Save the report to the repository
        reportRepository.save(report);
        return true; // Report saved successfully
    }
}
