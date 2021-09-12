package com.gymondo.errorhandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymondo.errorhandling.consumer.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    private static final List<ExceptionConsumer> consumers = Arrays.asList(new EntityNotFoundConsumer(),
                                                                           new IllegalArgumentConsumer(),
                                                                           new EntityExistsConsumer(),
                                                                           new HttpStatusCodeExceptionConsumer());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception exception) {
        try {
            ExceptionConsumer.Result result = consumeException(exception);

            ObjectMapper objectMapper = new ObjectMapper();
            byte[] payload = objectMapper.writeValueAsBytes(result);
            response.setStatus(result.getStatus().value());
            response.getOutputStream().write(payload);
            response.addHeader("Content-Type", "application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionConsumer.Result result = getDefaultResult(exception);
            response.setStatus(result.getStatus().value());
        }

        return new ModelAndView();
    }

    private ExceptionConsumer.Result consumeException(Exception exception) {
        return consumers.stream()
                .map(it -> {
                    return it.handleException(exception);
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> getDefaultResult(exception));
    }

    private ExceptionConsumer.Result getDefaultResult(Exception exception) {
        return new ExceptionConsumer.Result(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
