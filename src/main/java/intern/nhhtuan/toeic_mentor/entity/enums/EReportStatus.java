package intern.nhhtuan.toeic_mentor.entity.enums;

public enum EReportStatus {
    OPEN("Open"),
    RESOLVED("Resolved"),
    REJECTED("Rejected");

    private final String status;

    EReportStatus(String status) {
        this.status = status;
    }
}
