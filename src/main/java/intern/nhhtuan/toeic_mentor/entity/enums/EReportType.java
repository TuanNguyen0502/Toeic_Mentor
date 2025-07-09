package intern.nhhtuan.toeic_mentor.entity.enums;

public enum EReportType {
    WRONG_QUESTION("Wrong Question"),
    TYPING_ERROR("Typing Error"),
    UNCLEAR_ANSWER("Unclear Answer"),
    OTHER("Other");

    private final String description;

    EReportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
