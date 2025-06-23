package cory.sakti.commonlibrarycory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final String status;
    private final T data;
    private final ErrorDetails error;
    private final Instant timestamp;

    private ApiResponse(T data) {
        this.status = "success";
        this.data = data;
        this.error = null;
        this.timestamp = Instant.now();
    }

    private ApiResponse(ErrorDetails error) {
        this.status = "error";
        this.data = null;
        this.error = error;
        this.timestamp = Instant.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> error(String message, String details, Object validationErrors) {
        return new ApiResponse<>(new ErrorDetails(message, details, validationErrors));
    }

    public static <T> ApiResponse<T> error(String message, String details) {
        return error(message, details, null);
    }

    public record ErrorDetails(
            String message,
            String details,
            Object validationErrors
    ) {}
}
