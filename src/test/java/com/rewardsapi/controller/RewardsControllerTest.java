package com.rewardsapi.controller;

import com.rewardsapi.model.CustomerReward;
import com.rewardsapi.model.MonthlyReward;
import com.rewardsapi.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class RewardsControllerTest {

    @Mock
    private RewardsService service;

    @InjectMocks
    private RewardsController controller;

    @Test
    void testGetRewardsSuccess() {

        MonthlyReward mRewards=new MonthlyReward("SEPTEMBER",120);
        List<MonthlyReward> monthlyRewards=new ArrayList<>();
        monthlyRewards.add(mRewards);
        CustomerReward customerRewards=new CustomerReward(1l,monthlyRewards,120);
        when(service.getRewardsForCustomer(1L)).thenReturn(customerRewards);
        ResponseEntity<CustomerReward> rewards = controller.getRewards(1L);
        assertNotNull(rewards);
        assertEquals(1l,rewards.getBody().customerId());
        assertEquals(120l,rewards.getBody().totalPoints());
    }
}