package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.service.interfaces.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/sections")
@RequiredArgsConstructor
public class SectionController {
    private final ISectionService sectionService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("sectionResponses", sectionService.getSectionResponses());
        return "admin/section/section-list";
    }
}
