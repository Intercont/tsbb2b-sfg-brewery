package guru.springframework.brewery.web.controllers;

import guru.springframework.brewery.services.BeerOrderService;
import guru.springframework.brewery.web.model.BeerOrderDto;
import guru.springframework.brewery.web.model.BeerOrderPagedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerOrderController.class)
class BeerOrderControllerWebMVCTest {

    @MockBean
    private BeerOrderService beerOrderService;

    @Autowired
    MockMvc mockMvc;

    UUID customerId;
    UUID orderId;
    BeerOrderDto beerOrderDto;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        beerOrderDto = BeerOrderDto.builder().customerId(customerId).build();
    }

    @AfterEach
    void tearDown() {
        reset(beerOrderService);
    }

    @Test
    void listOrders() throws Exception {

        List<BeerOrderDto> beerOrderDtoList = new ArrayList<>();
        beerOrderDtoList.add(beerOrderDto);

        BeerOrderPagedList beerOrderPagedList =
                new BeerOrderPagedList(beerOrderDtoList, PageRequest.of(1, 1), 1L);

        given(beerOrderService.listOrders(any(), any()))
                .willReturn(beerOrderPagedList);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/{customerId}/orders", customerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].customerId", is(customerId.toString())))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getOrder() throws Exception {

        given(beerOrderService.getOrderById(any(), any()))
                .willReturn(beerOrderDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/{customerId}/orders/{orderId}", customerId, orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.customerId", is(customerId.toString())))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}