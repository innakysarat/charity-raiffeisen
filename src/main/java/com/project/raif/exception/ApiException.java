package com.project.raif.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
public class ApiException extends RuntimeException {
    private ErrorCode code;

    public ApiException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}

