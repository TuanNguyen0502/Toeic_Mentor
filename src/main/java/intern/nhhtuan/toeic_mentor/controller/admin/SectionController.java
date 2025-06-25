package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.QuestionImageUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.QuestionUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.SectionUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.request.SectionCreateRequest;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.ESectionStatus;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionImageService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/sections")
@RequiredArgsConstructor
public class SectionController {
    private final ISectionService sectionService;
    private final IQuestionService questionService;
    private final IQuestionImageService questionImageService;

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

    @GetMapping("/add-questions/{id}")
    public String getAddQuestionPage(@PathVariable Long id, Model model) {
        model.addAttribute("sectionId", id);
        return "admin/section/create-test";
    }

    @GetMapping("{sectionId}/questions/{questionId}")
    public String getQuestionByQuestionId(@PathVariable Long sectionId,
                                          @PathVariable Long questionId,
                                          Model model) {
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("eQuestionStatus", EQuestionStatus.values());
        model.addAttribute("questionUpdate", questionService.getQuestionUpdateById(questionId));
        return "admin/section/question/update-question";
    }

    @GetMapping("{sectionId}/questions/{questionId}/images")
    public String getQuestionImage(@PathVariable Long sectionId,
                                   @PathVariable Long questionId,
                                   Model model) {
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("questionImageUpdateDTO", questionImageService.getQuestionImageUpdateByQuestionId(questionId));
        return "admin/section/question/update-question-images";
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
            return "redirect:/admin/sections/question/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/section/update-section";
        }
    }

    @PostMapping("{sectionId}/questions/{questionId}")
    public String updateQuestion(@PathVariable Long sectionId,
                                 @PathVariable Long questionId,
                                 @Validated @ModelAttribute QuestionUpdateDTO questionUpdateDTO,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eQuestionStatus", EQuestionStatus.values());
            model.addAttribute("questionUpdate", questionUpdateDTO);
            return "admin/section/question/update-question";
        }
        try {
            questionService.update(questionUpdateDTO);
            return "redirect:/admin/sections/" + sectionId + "/questions/" + questionId;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/section/question/update-question";
        }
    }

    @PostMapping("{sectionId}/questions/{questionId}/images")
    public String updateQuestionImages(@PathVariable Long sectionId,
                                       @PathVariable Long questionId,
                                       @ModelAttribute QuestionImageUpdateDTO questionImageUpdateDTO,
                                       RedirectAttributes redirectAttributes) {
        try {
            questionImageService.updateQuestionImage(questionImageUpdateDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật ảnh thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/sections/" + sectionId + "/questions/" + questionId + "/images";
    }
}
