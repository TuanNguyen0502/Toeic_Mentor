package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.dto.response.ReportResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import intern.nhhtuan.toeic_mentor.service.interfaces.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller("AdminReportController")
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;

    @GetMapping("")
    public String showReports(
            @RequestParam(required = false) String status,
            Pageable pageable,
            Model model) {

        Page<ReportResponse> reports = reportService.getReportsByStatus(status, pageable);

        model.addAttribute("reports", reports.getContent());
        model.addAttribute("totalPages", reports.getTotalPages());
        model.addAttribute("currentPage", reports.getNumber());
        model.addAttribute("status", status);

        return "admin/report/report-list";
    }

    @GetMapping("/{id}")
    public String showReportDetail(@PathVariable Long id, Model model) {
        ReportDetailDTO report = reportService.getReportDetail(id);
        if (report != null) {
            model.addAttribute("report", report);
            model.addAttribute("eQuestionStatus", EQuestionStatus.values());
            return "admin/report/report-detail";
        } else {
            model.addAttribute("error", "Report not found");
            return "admin/report/report-list";
        }
    }
}
