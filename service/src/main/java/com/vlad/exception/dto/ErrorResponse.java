package com.vlad.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String code;
    private String message;

    private int status;
    private String error;

    private String path;
    private OffsetDateTime timestamp;

    private Map<String,Object> details;
}
