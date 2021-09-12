package com.gymondo.controller;

import org.springframework.http.HttpHeaders;

public abstract class BaseControllerTest {

    abstract int getPort();
    abstract String getPath();

    String getBaseUrl() {
        return "http://localhost";
    }

    String generateFullUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(getBaseUrl())
                .append(":")
                .append(getPort())
                .append(getPath());

        return builder.toString();
    }

    HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("API-KEY", "11111");
        return headers;
    }
}
