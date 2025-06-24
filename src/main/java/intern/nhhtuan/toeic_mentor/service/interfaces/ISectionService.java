package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.SectionUpdateDTO;
import intern.nhhtuan.toeic_mentor.dto.request.SectionCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.SectionResponse;

import java.util.List;

public interface ISectionService {
    List<SectionResponse> getSectionResponses();

    boolean create(SectionCreateRequest sectionCreateRequest);

    boolean update(Long sectionId, SectionUpdateDTO sectionUpdateDTO);

    boolean approveSection(Long sectionId);

    boolean rejectSection(Long sectionId);

    SectionUpdateDTO getSectionUpdateById(Long id);
}
