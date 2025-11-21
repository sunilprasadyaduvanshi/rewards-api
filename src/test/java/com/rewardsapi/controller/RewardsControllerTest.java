package com.rewardsapi.controller;

import com.rewardsapi.model.CustomerReward;
import com.rewardsapi.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsService service;

    @Test
    void testGetRewardsSuccess() throws Exception {

        CustomerReward mockReward =
                new CustomerReward(1L, List.of(), 100);

        Mockito.when(service.getRewardsForCustomer(1L))
                .thenReturn(mockReward);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalPoints").value(100));
    }
}