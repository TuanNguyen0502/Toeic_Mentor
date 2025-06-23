package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.PartResponse;
import intern.nhhtuan.toeic_mentor.entity.Part;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;

import java.util.List;

public interface IPartService {
    List<PartResponse> getPartResponse();

    int getTotalParts();

    Part findByName(Integer name);

    Part findByName(EPart ePart);

    List<Long> getIdsByPartName(List<EPart> parts);
}
