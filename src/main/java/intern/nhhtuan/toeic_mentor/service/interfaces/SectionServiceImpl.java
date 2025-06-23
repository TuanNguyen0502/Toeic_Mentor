package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.SectionResponse;
import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.Section;
import intern.nhhtuan.toeic_mentor.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements ISectionService {
    private final SectionRepository sectionRepository;

    @Override
    public List<SectionResponse> getSectionResponses() {
        List<SectionResponse> sectionResponses = new ArrayList<>();
        List<Section> sections = sectionRepository.findAll();

        for (Section section : sections) {
            SectionResponse sectionResponse = SectionResponse.builder()
                    .id(section.getId())
                    .title(section.getTitle())
                    .status(section.getStatus().name())
                    .updatedAt(section.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .questionCount(section.getQuestions().size())
                    .parts(getPartNamesBySection(section))
                    .build();
            sectionResponses.add(sectionResponse);
        }
        return sectionResponses;
    }

    private String getPartNamesBySection(Section section) {
        StringBuilder parts = new StringBuilder();
        List<Question> questions = section.getQuestions();
        for (Question question : questions) {
            String partName = question.getPart().getName().name().replace("_", " ");
            if (!parts.toString().contains(partName)) {
                parts.append(partName).append(", ");
            }
        }
        return parts.toString();
    }
}
