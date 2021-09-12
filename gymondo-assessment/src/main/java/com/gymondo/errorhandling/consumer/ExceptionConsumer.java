package com.gymondo.errorhandling.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public interface ExceptionConsumer {

    Result handleException(Exception exception);

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class Result {
        private HttpStatus status;
        private String message;
    }

}
