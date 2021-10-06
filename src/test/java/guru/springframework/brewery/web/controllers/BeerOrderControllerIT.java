package guru.springframework.brewery.web.controllers;

import guru.springframework.brewery.domain.Customer;
import guru.springframework.brewery.repositories.CustomerRepository;
import guru.springframework.brewery.web.model.BeerOrderPagedList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BeerOrderControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void listOrders() {

        List<Customer> customers = customerRepository.findAll();

        UUID customerId = customers.stream().findAny().get().getId();

        BeerOrderPagedList beerOrderPagedList = restTemplate.getForObject(
                "/api/v1/customers/{customerId}/orders",
                BeerOrderPagedList.class,
                customerId);

        assertEquals(1, beerOrderPagedList.getTotalElements());
        assertEquals("testOrder1", beerOrderPagedList.getContent().get(0).getCustomerRef());

    }
}