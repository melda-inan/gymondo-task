package com.gymondo.controller;


import com.gymondo.model.dto.CustomerDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest extends BaseControllerTest {

    private static final String BASE_URL_CUSTOMER = "/gymondo/api/v1/customer/";

    @Autowired
    private CustomerController customerController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        Assertions.assertThat(customerController).isNotNull();
    }


    @Test
    @Sql("/data/initial_data.sql")
    public void getCustomers() {
        final int expectedSize = 3;
        String url = generateFullUrl();
        HttpHeaders headers = getDefaultHeaders();

        HttpEntity entity = new HttpEntity(headers);

        ParameterizedTypeReference<List<CustomerDto>> typeReference = new ParameterizedTypeReference<List<CustomerDto>>() {};

        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(url, HttpMethod.GET, entity, typeReference);

        List<CustomerDto> result = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.size()).isEqualTo(expectedSize);

    }

    @Override
    int getPort() {
        return port;
    }

    @Override
    String getPath() {
        return BASE_URL_CUSTOMER;
    }
}