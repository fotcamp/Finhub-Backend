package fotcamp.finhub.common.exception;

public enum ErrorMessage {
    UNKNOWN_ERROR("알 수 없는 오류가 발생했습니다.", 500),
    WRONG_TYPE_TOKEN("잘못된 타입의 토큰입니다.", 400),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", 403),
    UNSUPPORTED_TOKEN("지원되지 않는 토큰입니다.", 400),
    ACCESS_DENIED("접근이 거부되었습니다.", 403),
    DUPLICATED_EMAIL("중복되는 이메일입니다.", 400),
    EMPTY_HEADER("헤더 필수값이 비어있습니다.", 400),
    NOT_CORRECT_HEADER("헤더 필수값이 틀렸습니다.", 403),
    NOT_FOUND("해당 요청 데이터가 존재하지 않습니다.", 404);


    private final String msg;
    private final int code;

    ErrorMessage(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}

