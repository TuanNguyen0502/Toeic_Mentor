package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.SectionUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.request.SectionCreateRequest;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.ESectionStatus;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/sections")
@RequiredArgsConstructor
public class SectionController {
    private final ISectionService sectionService;
    private final IQuestionService questionService;

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

    @GetMapping("/{id}")
    public String getSectionUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("eSectionStatus", ESectionStatus.values());
        model.addAttribute("sectionUpdateDTO", sectionService.getSectionUpdateById(id));
        model.addAttribute("eQuestionStatus", EQuestionStatus.values());
        model.addAttribute("questionResponse", questionService.getQuestionResponseBySectionId(id));
        return "admin/section/update-section";
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

    @PostMapping("/{id}")
    public String updateSection(@PathVariable Long id,
                                @Validated @ModelAttribute SectionUpdateDTO sectionUpdateDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eSectionStatus", ESectionStatus.values());
            model.addAttribute("sectionUpdateDTO", sectionUpdateDTO);
            model.addAttribute("eQuestionStatus", EQuestionStatus.values());
            model.addAttribute("questionResponse", questionService.getQuestionResponseBySectionId(id));
            return "admin/section/update-section";
        }
        try {
            sectionService.update(id, sectionUpdateDTO);
            return "redirect:/admin/sections";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/section/update-section";
        }
    }

}
