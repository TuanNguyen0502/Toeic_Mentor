package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.entity.Test;
import intern.nhhtuan.toeic_mentor.entity.TestPart;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import intern.nhhtuan.toeic_mentor.repository.TestPartRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IPartService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ITestPartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestPartServiceImpl implements ITestPartService {
    private final TestPartRepository testPartRepository;
    private final IPartService partService;

    @Override
    public int countByPart_Id(Long partId) {
        return testPartRepository.countByPart_Id(partId);
    }

    @Override
    public List<Test> findTestsBySeparatePartIds(List<EPart> parts) {
        List<Long> partIds = partService.getIdsByPartName(parts); // Get part ids by EPart names

        return testPartRepository.findAll()
                .stream()
                .filter(testPart -> partIds.contains(testPart.getPart().getId())) // Filter test parts by part ids;
                .map(TestPart::getTest)
                .distinct()
                .toList();
    }

    @Override
    public List<Test> findTestsByCombinePartNames(List<Long> partIds) {
        // Get all test ids that contain all parts in partIds
        return testPartRepository.findAll() // Fetch all test parts
                .stream() // Stream through each test part
                .map(TestPart::getTest) // Filter each test id containing all part ids
                .filter(test -> new HashSet<>(findPartIdsByTestId(test.getId())).containsAll(partIds)) // Map to test
                .distinct()
                .toList();
    }

    public List<Long> findPartIdsByTestId(Long testId) {
        // Get all part ids that belong to a specific test
        return testPartRepository.findByTest_Id(testId) // Fetch all test parts for the given test id
                .stream()
                .map(testPart -> testPart.getPart().getId()) // Map to part ids
                .toList();
    }

    @Override
    public <S extends TestPart> S save(S entity) {
        return testPartRepository.save(entity);
    }
}
