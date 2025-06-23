package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.SectionCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.SectionResponse;

import java.util.List;

public interface ISectionService {
    List<SectionResponse> getSectionResponses();

    boolean create(SectionCreateRequest sectionCreateRequest);
}
