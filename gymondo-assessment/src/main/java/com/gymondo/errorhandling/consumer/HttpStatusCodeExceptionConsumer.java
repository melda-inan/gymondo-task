package com.gymondo.errorhandling.consumer;

import org.springframework.web.client.HttpStatusCodeException;

public class HttpStatusCodeExceptionConsumer implements ExceptionConsumer {

    @Override
    public Result handleException(Exception exception) {
        if (!(exception instanceof HttpStatusCodeException)) {
            return null;
        }

        HttpStatusCodeException clientErrorException = (HttpStatusCodeException) exception;
        return new Result(clientErrorException.getStatusCode(), clientErrorException.getStatusText() + " " + clientErrorException.getResponseBodyAsString());
    }
}
