package com.gymondo.controller;

import com.gymondo.model.dto.CustomerDto;
import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.dto.ProductDto;
import com.gymondo.model.dto.VoucherDto;
import com.gymondo.model.enums.CustomerSubscriptionStatus;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubscriptionControllerTest extends BaseControllerTest {

    private static final String BASE_URL_SUBSCRIPTION = "/gymondo/api/v1/subscription/";

    @Autowired
    private SubscriptionController subscriptionController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql({"/data/initial_data.sql", "/data/initial_subscription_data.sql"})
    public void getCustomerSubscriptionsByCustomer() {
        List<CustomerSubscriptionDto> expectedResult = Arrays.asList(getMockCustomerSubscriptionActive(), getMockCustomerSubscriptionPaused());
        final Long customerId = 1L;

        String url = generateFullUrl() + "?customerId=" + customerId;
        HttpHeaders headers = getDefaultHeaders();

        ParameterizedTypeReference<List<CustomerSubscriptionDto>> typeReference = new ParameterizedTypeReference<List<CustomerSubscriptionDto>>() {};
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<List<CustomerSubscriptionDto>> response = restTemplate.exchange(url, HttpMethod.GET, entity, typeReference);

        List<CustomerSubscriptionDto> actualResult = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResult.size()).isEqualTo(expectedResult.size());
    }

    @Test
    @Sql({"/data/initial_data.sql", "/data/initial_subscription_data.sql"})
    public void getCustomerSubscriptionsByCustomerAndStatus() {
        List<CustomerSubscriptionDto> expectedResult = Arrays.asList(getMockCustomerSubscriptionActive());
        final String status = CustomerSubscriptionStatus.ACTIVE.name();
        final Long customerId = 1L;

        String url = generateFullUrl() + "?customerId=" + customerId + "&status=" + status;
        HttpHeaders headers = getDefaultHeaders();

        ParameterizedTypeReference<List<CustomerSubscriptionDto>> typeReference = new ParameterizedTypeReference<List<CustomerSubscriptionDto>>() {};
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<List<CustomerSubscriptionDto>> response = restTemplate.exchange(url, HttpMethod.GET, entity, typeReference);

        List<CustomerSubscriptionDto> actualResult = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResult.size()).isEqualTo(expectedResult.size());
        Assertions.assertThat(actualResult).allMatch(it -> it.getStatus().equals(CustomerSubscriptionStatus.ACTIVE));
    }

    @Test
    @Sql({"/data/initial_data.sql", "/data/initial_subscription_data.sql"})
    public void getCustomerSubscriptionById() {
        CustomerSubscriptionDto expectedResult = getMockCustomerSubscriptionActive();
        final Long id = expectedResult.getId();

        String url = generateFullUrl() + id;
        HttpHeaders headers = getDefaultHeaders();

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<CustomerSubscriptionDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, CustomerSubscriptionDto.class);

        CustomerSubscriptionDto actualResult = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        Assertions.assertThat(actualResult.getProduct().getId()).isEqualTo(expectedResult.getProduct().getId());
        Assertions.assertThat(actualResult.getCustomer().getId()).isEqualTo(expectedResult.getCustomer().getId());
        Assertions.assertThat(actualResult.getStatus()).isEqualTo(expectedResult.getStatus());
    }

    @Test
    @Sql({"/data/initial_data.sql", "/data/initial_subscription_data.sql"})
    public void pauseSubscription() {
        CustomerSubscriptionDto activeSubscription = getMockCustomerSubscriptionActive();
        final Long id = activeSubscription.getId();

        String url = generateFullUrl() + id + "/pause";
        HttpHeaders headers = getDefaultHeaders();

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<CustomerSubscriptionDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, CustomerSubscriptionDto.class);

        CustomerSubscriptionDto actualResult = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.getId()).isEqualTo(id);
        Assertions.assertThat(actualResult.getStatus()).isEqualTo(CustomerSubscriptionStatus.PAUSED);
    }

    @Test
    @Sql({"/data/initial_data.sql", "/data/initial_subscription_data.sql"})
    public void unpauseSubscription() {
        CustomerSubscriptionDto pausedSubscription = getMockCustomerSubscriptionPaused();
        final Long id = pausedSubscription.getId();

        String url = generateFullUrl() + id + "/unpause";
        HttpHeaders headers = getDefaultHeaders();

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<CustomerSubscriptionDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, CustomerSubscriptionDto.class);

        CustomerSubscriptionDto actualResult = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.getId()).isEqualTo(id);
        Assertions.assertThat(actualResult.getStatus()).isEqualTo(CustomerSubscriptionStatus.ACTIVE);
    }

    @Test
    @Sql({"/data/initial_data.sql", "/data/initial_subscription_data.sql"})
    public void cancelSubscription() {
        CustomerSubscriptionDto activeSubscription = getMockCustomerSubscriptionActive();
        final Long id = activeSubscription.getId();

        String url = generateFullUrl() + id + "/cancel";
        HttpHeaders headers = getDefaultHeaders();

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<CustomerSubscriptionDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, CustomerSubscriptionDto.class);

        CustomerSubscriptionDto actualResult = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.getId()).isEqualTo(id);
        Assertions.assertThat(actualResult.getStatus()).isEqualTo(CustomerSubscriptionStatus.CANCELED);
    }

    private CustomerSubscriptionDto getMockCustomerSubscriptionActive() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        VoucherDto voucherDto = new VoucherDto();
        voucherDto.setCode("WELCOMENEWBY");

        CustomerSubscriptionDto customerSubscription = new CustomerSubscriptionDto();
        customerSubscription.setId(1L);
        customerSubscription.setStatus(CustomerSubscriptionStatus.ACTIVE);
        customerSubscription.setCustomer(customerDto);
        customerSubscription.setProduct(productDto);
        customerSubscription.setVoucher(voucherDto);

        customerSubscription.setDurationMonth(3);
        customerSubscription.setPrice(48.0);
        customerSubscription.setTax(4.8);
        customerSubscription.setStartDate(LocalDate.of(2021, 9, 12));
        customerSubscription.setEndDate(LocalDate.of(2021, 12, 12));

        return customerSubscription;
    }

    private CustomerSubscriptionDto getMockCustomerSubscriptionPaused() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(2L);


        CustomerSubscriptionDto customerSubscription = new CustomerSubscriptionDto();
        customerSubscription.setId(2L);
        customerSubscription.setStatus(CustomerSubscriptionStatus.PAUSED);
        customerSubscription.setCustomer(customerDto);
        customerSubscription.setProduct(productDto);

        customerSubscription.setDurationMonth(6);
        customerSubscription.setPrice(90.0);
        customerSubscription.setTax(9.0);
        customerSubscription.setStartDate(LocalDate.of(2021, 9, 12));
        customerSubscription.setEndDate(LocalDate.of(2022, 3, 12));

        return customerSubscription;
    }

    @Override
    int getPort() {
        return port;
    }

    @Override
    String getPath() {
        return BASE_URL_SUBSCRIPTION;
    }
}