package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.response.PartResponse;
import intern.nhhtuan.toeic_mentor.entity.Part;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import intern.nhhtuan.toeic_mentor.repository.PartRepository;
import intern.nhhtuan.toeic_mentor.repository.QuestionRepository;
import intern.nhhtuan.toeic_mentor.repository.TestPartRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IPartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements IPartService {
    private final PartRepository partRepository;
    private final QuestionRepository questionRepository;
    private final TestPartRepository testPartRepository;

    @Override
    public List<PartResponse> getPartResponse() {
        return partRepository.findAll().stream()
                .map(part -> PartResponse.builder()
                        .id(part.getId())
                        .name(part.getName().name().replace("_", " "))
                        .questionCount(questionRepository.countByPart_Id(part.getId()))
                        .testCount(testPartRepository.countByPart_Id(part.getId()))
                        .build())
                .toList();
    }

    @Override
    public int getTotalParts() {
        return partRepository.findAll().size();
    }

    @Override
    public Part findByName(Integer name) {
        return partRepository.findByName(getPartName(name));
    }

    public EPart getPartName(Integer part) {
        return Arrays.stream(EPart.values())
                .filter(p -> p.name().contains(part.toString()))
                .findFirst()
                .orElse(null);
    }
}
