package grow.a.garden.dto.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
public class BaseResponse<T> {

    @NonNull
    private int status;

    @NonNull
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String additionalInfo = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object errors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Metadata metadata;

    public BaseResponse() {
    }

    @Builder
    public BaseResponse(@NonNull int status, @NonNull String message, T data, String additionalInfo, Object errors, Metadata metadata) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.additionalInfo = additionalInfo;
        this.errors = errors;
        this.metadata = metadata;
    }

    public BaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResponse(int status, String message, String additionalInfo) {
        this.status = status;
        this.message = message;
        this.additionalInfo = additionalInfo;
    }

    public BaseResponse(int status, String message, String additionalInfo, Object errors) {
        this.status = status;
        this.message = message;
        this.additionalInfo = additionalInfo;
        this.errors = errors;
    }

    public BaseResponse(int status, String message, T data, String additionalInfo) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.additionalInfo = additionalInfo;
    }

    public BaseResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int status, String message, T data, Metadata metadata) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.metadata = metadata;
    }

}
