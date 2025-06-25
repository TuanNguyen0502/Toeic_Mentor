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
        model.addAttribute("ePercentChoice", TestCountRequest.EPercentChoice.values());
        model.addAttribute("eStatus", TestCountRequest.EStatus.values());
        model.addAttribute("eType", TestCountRequest.EType.values());
        return "admin/part/count-tests";
    }

    @GetMapping("/count")
    public String countTests(@ModelAttribute("testCountRequest") TestCountRequest testCountRequest,
                             Model model) {
        model.addAttribute("testCountResponseList", testService.countByPartsAndPercent(testCountRequest));
        model.addAttribute("ePercentChoice", TestCountRequest.EPercentChoice.values());
        model.addAttribute("ePart", EPart.values());
        model.addAttribute("eStatus", TestCountRequest.EStatus.values());
        model.addAttribute("eType", TestCountRequest.EType.values());
        model.addAttribute("testCountRequest", new TestCountRequest());
        return "admin/part/count-tests";
    }
}
