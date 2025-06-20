package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.entity.Test;
import intern.nhhtuan.toeic_mentor.entity.TestPart;

import java.util.List;

public interface ITestPartService {
    int countByPart_Id(Long partId);

    List<Long> findTestIdsByPartId(Long partId);

    List<Test> findTestsByCombinePartIds(List<Long> partIds);

    <S extends TestPart> S save(S entity);
}
