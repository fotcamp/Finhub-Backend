package fotcamp.finhub.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseWrapper(
        ApiStatus status,
        String errorMsg,
        Object data
) {
    public static ApiResponseWrapper success() {
        return new ApiResponseWrapper(ApiStatus.SUCCESS, null, null);
    }

    public static ApiResponseWrapper success(Object data) {
        return new ApiResponseWrapper(ApiStatus.SUCCESS, null, data);
    }

    public static ApiResponseWrapper fail(String errorMsg) {
        return new ApiResponseWrapper(ApiStatus.FAIL, errorMsg, null);
    }

    public static ApiResponseWrapper fail(String errorMsg, Object data) {
        return new ApiResponseWrapper(ApiStatus.FAIL, errorMsg, data);
    }
}
