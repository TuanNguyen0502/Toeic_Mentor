package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.service.interfaces.IPartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/parts")
@RequiredArgsConstructor
public class PartController {
    private final IPartService partService;

    @GetMapping("")
    public String showParts(Model model) {
        model.addAttribute("parts", partService.getPartResponse());
        return "admin/part/part-list";
    }
}
