package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.request.SectionCreateRequest;
import intern.nhhtuan.toeic_mentor.service.interfaces.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        model.addAttribute("sectionCreateRequest", new SectionCreateRequest());
        return "admin/section/new-section";
    }

    @PostMapping("")
    public String createSection(@Validated @ModelAttribute SectionCreateRequest sectionCreateRequest,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/section/new-section";
        }
        try {
            sectionService.create(sectionCreateRequest);
            return "redirect:/admin/sections";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/section/new-section";
        }
    }

}
