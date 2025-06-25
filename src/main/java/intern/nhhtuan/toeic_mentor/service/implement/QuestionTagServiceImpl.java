package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.QuestionTag;
import intern.nhhtuan.toeic_mentor.repository.QuestionTagRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionTagServiceImpl implements IQuestionTagService {
    private final QuestionTagRepository questionTagRepository;

    @Override
    public boolean saveTags(List<String> tags, Question question) {
        if (tags == null || tags.isEmpty()) {
            return false; // No tags to save
        }

        // Clear existing tags for the question
        List<QuestionTag> questionTags = questionTagRepository.findByQuestion_Id(question.getId());
        if (questionTags != null && !questionTags.isEmpty()) {
            questionTagRepository.deleteAll(questionTags);
        }

        // Save new tags
        for (String tag : tags) {
            questionTagRepository.save(new QuestionTag(tag, question));
        }
        return true; // Tags saved successfully
    }

    @Override
    public List<QuestionTag> findByTag(String tag) {
        return questionTagRepository.findByTag(tag);
    }
}
