package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.ReportRequest;
import intern.nhhtuan.toeic_mentor.service.interfaces.IReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;

    @PostMapping("")
    public ResponseEntity<?> createReport(@Valid @ModelAttribute ReportRequest reportRequest,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            // Assuming the email is obtained from the authenticated user context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            reportService.saveReport(email, reportRequest);
            return ResponseEntity.ok("Report submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while submitting the report: " + e.getMessage());
        }
    }
}
