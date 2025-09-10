package com.dev.kamran.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int code;
    private T data;
    private String error;
    private Pagination pagination;

    public static <T> ApiResponse<T> success(T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .code(status.value())
                .data(data)
                .error(null)
                .pagination(null)
                .build();
    }

    public static <T> ApiResponse<List<T>> success(List<T> data, HttpStatus status) {
        return ApiResponse.<List<T>>builder()
                .code(status.value())
                .data(data)
                .error(null)
                .pagination(null)
                .build();
    }

    public static <T> ApiResponse<List<T>> paginated(Page<T> page, HttpStatus status) {
        Pagination pagination = new Pagination(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiResponse.<List<T>>builder()
                .code(status.value())
                .data(page.getContent())
                .error(null)
                .pagination(pagination)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .code(status.value())
                .data(null)
                .error(message)
                .pagination(null)
                .build();
    }

    @Data
    @AllArgsConstructor
    public static class Pagination {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }
}
