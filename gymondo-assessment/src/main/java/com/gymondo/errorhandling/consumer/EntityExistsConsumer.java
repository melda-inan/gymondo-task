package com.gymondo.errorhandling.consumer;

import org.springframework.http.HttpStatus;

import javax.persistence.EntityExistsException;

public class EntityExistsConsumer implements ExceptionConsumer {

    @Override
    public Result handleException(Exception exception) {
        if (!(exception instanceof EntityExistsException)) {
            return null;
        }

        return new Result(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
