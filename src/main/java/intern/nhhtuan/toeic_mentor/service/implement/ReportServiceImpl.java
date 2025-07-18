package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.QuestionUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.dto.response.ReportResponse;
import intern.nhhtuan.toeic_mentor.dto.request.ReportRequest;
import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import intern.nhhtuan.toeic_mentor.repository.QuestionRepository;
import intern.nhhtuan.toeic_mentor.repository.ReportRepository;
import intern.nhhtuan.toeic_mentor.repository.specification.ReportSpecification;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IReportService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final ReportRepository reportRepository;
    private final IQuestionService questionService;
    private final QuestionRepository questionRepository;
    private final NotificationServiceImpl notificationService;
    private final UserRepository userRepository;

    @Override
    public Page<ReportResponse> getReportsByStatus(String status, Pageable pageable) {
        if (status == null || status.isEmpty()) {
//            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            return reportRepository.findAll(pageable)
                    .map(this::mapToResponse);
        } else {
            try {
                EReportStatus reportStatus = EReportStatus.valueOf(status.toUpperCase());
                return reportRepository.findByStatus(reportStatus, pageable)
                        .map(this::mapToResponse);
            } catch (IllegalArgumentException e) {
                return Page.empty(pageable);
            }
        }
    }

    @Override
    public Page<ReportResponse> getReportsWithFilters(Map<String, String> filters, Pageable pageable) {
        return reportRepository.findAll(ReportSpecification.withFilters(filters), pageable)
                .map(this::mapToResponse);
    }

    @Override
    public ReportDetailDTO getReportDetail(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));

        QuestionUpdateDTO questionDTO = questionService.getQuestionUpdateById(report.getQuestion().getId());

        return ReportDetailDTO.builder()
                .id(report.getId())
                .email(report.getUser().getEmail())
                .questionUpdateDTO(questionDTO)
                .category(report.getCategory().name())
                .description(report.getDescription())
                .status(report.getStatus())
                .admin_response("")
                .create_at(String.valueOf(report.getCreatedAt()))
                .build();
    }

    @Override
    @Transactional
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
        Report savedReport = reportRepository.save(report);

        notificationService.createReportNotifications(savedReport);

        return true; // Report saved successfully
    }

    @Override
    @Transactional
    public void updateReport(ReportDetailDTO reportDetailDTO) {
        Report report = reportRepository.findById(reportDetailDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + reportDetailDTO.getId()));

        report.setStatus(reportDetailDTO.getStatus());
            reportRepository.save(report);

        switch (reportDetailDTO.getStatus()) {
            case OPEN:
                break;
            case REJECTED:
                notificationService.createResponseUserNotifications(reportDetailDTO);
                break;
            case RESOLVED:
                boolean success = questionService.update(reportDetailDTO.getQuestionUpdateDTO());
                if (success) {
                    notificationService.createResponseUserNotifications(reportDetailDTO);
                } else {
                    throw new RuntimeException("Failed to update question.");
                }
                break;
        }
    }

    private ReportResponse mapToResponse(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .email(report.getUser().getEmail())
                .question_id(report.getQuestion().getId())
                .category(report.getCategory().name())
                .status(report.getStatus().name())
                .create_at(String.valueOf(report.getCreatedAt()))
                .build();
    }

}
