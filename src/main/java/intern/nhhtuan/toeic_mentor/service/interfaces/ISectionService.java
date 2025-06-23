package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.SectionResponse;

import java.util.List;

public interface ISectionService {
    List<SectionResponse> getSectionResponses();
}
