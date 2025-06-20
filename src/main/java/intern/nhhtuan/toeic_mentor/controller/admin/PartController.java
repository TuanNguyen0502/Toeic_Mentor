package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import intern.nhhtuan.toeic_mentor.service.interfaces.IPartService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ITestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/parts")
@RequiredArgsConstructor
public class PartController {
    private final IPartService partService;
    private final ITestService testService;

    @GetMapping("")
    public String showParts(Model model) {
        model.addAttribute("parts", partService.getPartResponse());
        return "admin/part/part-list";
    }

    @GetMapping("/count-tests")
    public String showCountTests(Model model) {
        model.addAttribute("testCountRequest", new TestCountRequest());
        model.addAttribute("ePart", EPart.values());
        model.addAttribute("eStatus", TestCountRequest.EStatus.values());
        model.addAttribute("eType", TestCountRequest.EType.values());
        return "admin/part/count-tests";
    }

    @PostMapping("/count-tests")
    public String countTests(@ModelAttribute("testCountRequest") TestCountRequest testCountRequest,
                             Model model) {
        if (testCountRequest.getType().equals(TestCountRequest.EType.COMBINE)) {
            model.addAttribute("testCountResponseList", testService.countByCombinePartsAndPercent(testCountRequest));
        }
        model.addAttribute("testCountRequest", new TestCountRequest());
        model.addAttribute("ePart", EPart.values());
        model.addAttribute("eStatus", TestCountRequest.EStatus.values());
        model.addAttribute("eType", TestCountRequest.EType.values());
        return "admin/part/count-tests";
    }
}
