package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.SectionUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.request.SectionCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.SectionResponse;
import intern.nhhtuan.toeic_mentor.entity.*;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.ESectionStatus;
import intern.nhhtuan.toeic_mentor.repository.SectionRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements ISectionService {
    private final SectionRepository sectionRepository;
    private final IQuestionService questionService;

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

    @Override
    public boolean create(SectionCreateRequest sectionCreateRequest) {
        Section section = new Section();
        section.setTitle(sectionCreateRequest.getTitle());
        section.setStatus(ESectionStatus.DRAFT);

        // Save the section to the repository
        sectionRepository.save(section);

        // If the save operation was successful, return true
        return true;
    }

    @Override
    public boolean update(Long sectionId, SectionUpdateDTO sectionUpdateDTO) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + sectionId));
        section.setTitle(sectionUpdateDTO.getTitle());
        if (!section.getStatus().equals(sectionUpdateDTO.getStatus())) {
            section.setStatus(sectionUpdateDTO.getStatus());
            if (sectionUpdateDTO.getStatus().equals(ESectionStatus.APPROVED)) {
                for (Question question : section.getQuestions()) {
                    questionService.updateQuestionStatus(question.getId(), EQuestionStatus.APPROVED);
                }
            } else if (sectionUpdateDTO.getStatus().equals(ESectionStatus.REJECTED) ||
                    sectionUpdateDTO.getStatus().equals(ESectionStatus.PENDING_REVIEW)) {
                for (Question question : section.getQuestions()) {
                    questionService.updateQuestionStatus(question.getId(), EQuestionStatus.IN_SECTION);
                }
            }
        }
        // Save the updated section to the repository
        sectionRepository.save(section);
        return true;
    }

    @Override
    public boolean approveSection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + sectionId));
        section.setStatus(ESectionStatus.APPROVED);

        // Update the status of all questions in the section to APPROVED
        for (Question question : section.getQuestions()) {
            questionService.updateQuestionStatus(question.getId(), EQuestionStatus.APPROVED);
        }
        // Save the updated section to the repository
        sectionRepository.save(section);
        return true;
    }

    @Override
    public boolean rejectSection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + sectionId));
        section.setStatus(ESectionStatus.REJECTED);

        // Update the status of all questions in the section to APPROVED
        for (Question question : section.getQuestions()) {
            questionService.updateQuestionStatus(question.getId(), EQuestionStatus.IN_SECTION);
        }
        // Save the updated section to the repository
        sectionRepository.save(section);
        return true;
    }

    @Override
    public SectionUpdateDTO getSectionUpdateById(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + id));

        return SectionUpdateDTO.builder()
                .id(section.getId())
                .title(section.getTitle())
                .status(section.getStatus())
                .parts(getPartNamesBySection(section))
                .build();
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
