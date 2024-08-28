package fotcamp.finhub.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiCommonResponse<T>(
        ApiStatus status,
        String errorMsg,
        T data
) {
    public static <T> ApiCommonResponse<T> success() {
        return new ApiCommonResponse<>(ApiStatus.SUCCESS, null, null);
    }

    public static <T> ApiCommonResponse<T> success(T data) {
        return new <T>  ApiCommonResponse<T> (ApiStatus.SUCCESS, null, data);
    }

    public static <T> ApiCommonResponse<T>  fail(String errorMsg) {
        return new ApiCommonResponse<>(ApiStatus.FAIL, errorMsg, null);
    }
}
