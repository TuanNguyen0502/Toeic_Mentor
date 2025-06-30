package intern.nhhtuan.toeic_mentor.entity.enums;

public enum ENotificationType {
    ERROR_REPORT("Error Report"),
    USER("User Notification");

    private final String description;

    ENotificationType(String description) {
        this.description = description;
    }
}
