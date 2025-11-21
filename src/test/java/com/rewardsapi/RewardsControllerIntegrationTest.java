package com.rewardsapi;

import com.rewardsapi.controller.RewardsController;
import com.rewardsapi.model.CustomerReward;
import com.rewardsapi.model.MonthlyReward;
import com.rewardsapi.model.Transaction;
import com.rewardsapi.repository.TransactionRepository;
import com.rewardsapi.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class RewardsControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl="http://localhost";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private RewardsController controller;

    @Mock
    private RewardsService service;

    @Mock
    private TransactionRepository repository;

    @BeforeEach
    public void setUp()
    {
        baseUrl=baseUrl.concat(":").concat(port+"").concat("/api/rewards/saveData");
    }

    @Test
    void testAddTransaction() throws Exception {
        String request = """
            {
              "id":3,
              "customerId": 3,
              "amount": 111,
              "date": "2025-10-25"
            }
            """;
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(3))
                .andExpect(jsonPath("$.amount").value(111));
    }

    @Test
    void testGetRewards()
    {
        Transaction transaction=new Transaction();
        transaction.setId(1L);
        transaction.setCustomerId(1L);
        transaction.setAmount(120);
        transaction.setDate(LocalDate.now());
        List<Transaction> result=new ArrayList<>();
        result.add(transaction);
        Mockito.when(repository.findAll()).thenReturn(result);

        MonthlyReward mr=new MonthlyReward("SEPTEMBER",120);
        List<MonthlyReward> reward=new ArrayList<>();
        reward.add(mr);
        CustomerReward cr=new CustomerReward(1l,reward,120);
        Mockito.when(service.getRewardsForCustomer(1L)).thenReturn(cr);

        ResponseEntity<CustomerReward> response = controller.getRewards(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().customerId());
        assertEquals(1, response.getBody().monthlyRewards().size());
    }
}