package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.dto.response.ReportResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportType;
import intern.nhhtuan.toeic_mentor.service.interfaces.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller("AdminReportController")
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;

    @GetMapping("")
    public String showReports(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sort,
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        // Build filters map
        Map<String, String> filters = new HashMap<>();
        if (status != null && !status.isEmpty()) {
            filters.put("status", status);
        }
        if (category != null && !category.isEmpty()) {
            filters.put("category", category);
        }
        if (email != null && !email.isEmpty()) {
            filters.put("email", email);
        }

        // Handle sort
        Pageable effectivePageable = pageable;
        if (sort != null && !sort.isEmpty()) {
            String[] sortParts = sort.split(",");
            if (sortParts.length == 2) {
                String sortField = sortParts[0];
                String sortDir = sortParts[1];
                effectivePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                        sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
            }
        }

        Page<ReportResponse> reports;
        if (filters.isEmpty()) {
            reports = reportService.getReportsByStatus(status, effectivePageable);
        } else {
            reports = reportService.getReportsWithFilters(filters, effectivePageable);
        }

        model.addAttribute("reports", reports.getContent());
        model.addAttribute("totalPages", reports.getTotalPages());
        model.addAttribute("currentPage", reports.getNumber());
        model.addAttribute("status", status);
        model.addAttribute("category", category);
        model.addAttribute("email", email);
        model.addAttribute("sort", sort);
        model.addAttribute("reportStatuses", EReportStatus.values());
        model.addAttribute("reportTypes", EReportType.values());

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

    @PostMapping("/{id}")
    public String updateReportDetail(@PathVariable Long id,
                                     @Validated @ModelAttribute("report") ReportDetailDTO reportDetailDTO,
                                     BindingResult bindingResult,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eQuestionStatus", EQuestionStatus.values());
            return "admin/report/report-detail";
        }

        reportService.updateReport(reportDetailDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Question updated and notification sent successfully!");
        return "redirect:/admin/reports/" + id;
    }
}
