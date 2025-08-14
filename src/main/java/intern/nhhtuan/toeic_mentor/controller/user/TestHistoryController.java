package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.response.TestHistoryDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.TestHistoryResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.ITestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/test-histories")
@RequiredArgsConstructor
public class TestHistoryController {
    private final ITestService testService;

    @GetMapping("")
    public String getTestHistories(
            Model model,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtEnd,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Page<TestHistoryResponse> testHistoryResponses;
        try {
            Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            testHistoryResponses = testService.getTestHistoryResponses(
                    email, createdAtStart, createdAtEnd, completed, page, size, sortBy, direction
            );
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            testHistoryResponses = Page.empty();
        }
        model.addAttribute("testHistories", testHistoryResponses);
        return "user/test/test-history-list";
    }

    @GetMapping("/{id}")
    public String getTestHistoryDetail(@PathVariable("id") Long id, Model model) {
        TestHistoryDetailResponse testHistoryDetailResponse = testService.getTestHistoryDetailResponseById(id);
        model.addAttribute("testHistoryDetailResponse", testHistoryDetailResponse);
        return "user/test/test-history-detail";
    }
}
