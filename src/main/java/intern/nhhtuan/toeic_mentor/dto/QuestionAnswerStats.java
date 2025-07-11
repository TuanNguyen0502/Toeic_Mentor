package intern.nhhtuan.toeic_mentor.dto;

public interface QuestionAnswerStats {
    Long getQuestionId();
    Long getCorrectCount();
    Long getWrongCount();
    Long getTotalCount();
}
