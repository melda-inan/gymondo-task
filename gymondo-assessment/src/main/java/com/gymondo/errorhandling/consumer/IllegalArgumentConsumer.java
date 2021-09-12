package com.gymondo.errorhandling.consumer;

import org.springframework.http.HttpStatus;

public class IllegalArgumentConsumer implements ExceptionConsumer {

    @Override
    public Result handleException(Exception exception) {
        if (!(exception instanceof IllegalArgumentException)) {
            return null;
        }

        return new Result(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
