package com.gymondo.errorhandling.consumer;

import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;

public class EntityNotFoundConsumer implements ExceptionConsumer {

    @Override
    public Result handleException(Exception exception) {
        if (!(exception instanceof EntityNotFoundException)) {
            return null;
        }

        return new Result(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
