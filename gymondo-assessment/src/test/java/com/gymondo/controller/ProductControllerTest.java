package com.gymondo.controller;

import com.gymondo.model.dto.*;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest extends BaseControllerTest {

    private static final String BASE_URL_PRODUCT = "/gymondo/api/v1/product/";

    @Autowired
    private ProductController productController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        Assertions.assertThat(productController).isNotNull();
    }

    @Test
    public void shouldGetErrorWithUnauthorizedApiKey() {
        String url = generateFullUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.add("API-KEY", "test");

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Sql("/data/initial_data.sql")
    public void getProductById() {
        ProductDto expectedResult = getMockProductDto();

        final Long id = expectedResult.getId();

        String url = generateFullUrl() + id;
        HttpHeaders headers = getDefaultHeaders();

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<ProductDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, ProductDto.class);

        ProductDto actualResult = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        Assertions.assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
        Assertions.assertThat(actualResult.getDescription()).isEqualTo(expectedResult.getDescription());
        Assertions.assertThat(actualResult.getSubscriptions().size()).isEqualTo(expectedResult.getSubscriptions().size());
        Assertions.assertThat(actualResult.getVouchers().size()).isEqualTo(expectedResult.getVouchers().size());

    }

    @Test
    @Sql("/data/initial_data.sql")
    public void listProducts() {
        final int expectedSize = 3;
        String url = generateFullUrl();
        HttpHeaders headers = getDefaultHeaders();

        HttpEntity entity = new HttpEntity(headers);

        ParameterizedTypeReference<List<ProductDto>> typeReference = new ParameterizedTypeReference<List<ProductDto>>() {};

        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(url, HttpMethod.GET, entity, typeReference);

        List<ProductDto> result = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.size()).isEqualTo(expectedSize);
    }

    @Test
    @Sql("/data/initial_data.sql")
    public void subscribe() {
        final Long id = 1L;
        String url = generateFullUrl() + id + "/subscribe";

        HttpHeaders headers = getDefaultHeaders();

        SubscriptionRequestDto subscriptionRequest = getMockSubscriptionRequest();

        HttpEntity entity = new HttpEntity(subscriptionRequest, headers);

        ResponseEntity<CustomerSubscriptionDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, CustomerSubscriptionDto.class);

        CustomerSubscriptionDto customerSubscription = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(customerSubscription).isNotNull();
        Assertions.assertThat(customerSubscription.getProduct().getId()).isEqualTo(id);
        Assertions.assertThat(customerSubscription.getVoucher().getCode()).isEqualTo(subscriptionRequest.getVoucherCode());

    }

    private ProductDto getMockProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Yoga Power");
        productDto.setDescription("Unleash Your Power Within Through Athletic-Based Yoga");
        productDto.setSubscriptions(Collections.singletonList(new SubscriptionDto()));
        productDto.setVouchers(Arrays.asList(new VoucherDto(), new VoucherDto()));
        return productDto;
    }

    private SubscriptionRequestDto getMockSubscriptionRequest() {
        SubscriptionRequestDto subscriptionRequest = new SubscriptionRequestDto();
        subscriptionRequest.setCustomerId(1L);
        subscriptionRequest.setSubscriptionId(1L);
        subscriptionRequest.setVoucherCode("WELCOMENEWBY");
        return subscriptionRequest;
    }

    @Override
    int getPort() {
        return port;
    }

    @Override
    String getPath() {
        return BASE_URL_PRODUCT;
    }
}