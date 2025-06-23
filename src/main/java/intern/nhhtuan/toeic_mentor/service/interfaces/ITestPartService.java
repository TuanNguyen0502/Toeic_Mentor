package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.entity.Test;
import intern.nhhtuan.toeic_mentor.entity.TestPart;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;

import java.util.List;

public interface ITestPartService {
    int countByPart_Id(Long partId);

    List<Test> findTestsByPartId(Long partId);

    List<Test> findTestsByCombinePartNames(List<Long> partIds);

    <S extends TestPart> S save(S entity);
}
