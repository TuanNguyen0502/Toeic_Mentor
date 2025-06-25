package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.entity.Question;

import java.util.Map;

public interface IQuestionOptionService {
    boolean saveOptions(Map<String, String> options, Question question);
}
