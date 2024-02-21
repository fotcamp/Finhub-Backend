package fotcamp.finhub.common.exception;

public class TokenNotValidateException extends RuntimeException{

    public TokenNotValidateException() {
    }

    public TokenNotValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenNotValidateException(String message) {
        super(message);
    }
}
