package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.QuestionTag;

import java.util.List;

public interface IQuestionTagService {
    boolean saveTags(List<String> tags, Question question);

    List<QuestionTag> findByTag(String tag);
}
