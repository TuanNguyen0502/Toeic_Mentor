package intern.nhhtuan.toeic_mentor.exception;

public abstract class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}
