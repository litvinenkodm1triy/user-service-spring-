package user_service.exception;

public class UserValidationException extends RuntimeException {

    public UserValidationException(String massage) {
        super(massage);
    }
}
