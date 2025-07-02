package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.QuestionUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.dto.response.ReportResponse;
import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import intern.nhhtuan.toeic_mentor.repository.ReportRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IReportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final ReportRepository reportRepository;
    private final IQuestionService questionService;

    @Override
    public Page<ReportResponse> getReportsByStatus(String status, Pageable pageable) {
        if (status == null || status.isEmpty()) {
            return reportRepository.findAll(pageable)
                    .map(this::mapToResponse);
        } else {
            try {
                EReportStatus reportStatus = EReportStatus.valueOf(status.toUpperCase());
                return reportRepository.findByStatusOrderByCreatedAtDesc(reportStatus, pageable)
                        .map(this::mapToResponse);
            } catch (IllegalArgumentException e) {
                return Page.empty(pageable);
            }
        }
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
                .create_at(report.getCreatedAt().toString())
                .build();
    }

    private ReportResponse mapToResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setEmail(report.getUser().getEmail());
        response.setQuestion_id(report.getQuestion().getId());
        response.setCategory(report.getCategory().name());
        response.setStatus(report.getStatus().name());
        response.setCreate_at(report.getCreatedAt().toString());
        return response;
    }



}
