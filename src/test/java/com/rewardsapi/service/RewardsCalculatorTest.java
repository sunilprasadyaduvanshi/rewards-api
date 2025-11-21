package com.rewardsapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RewardsCalculatorTest {

    @Autowired
    RewardsCalculator calculator;

    @Test
    public void testRewardCalc() {
        assertEquals(90, calculator.calculatePoints(120));
        assertEquals(0, calculator.calculatePoints(40));
        assertEquals(50, calculator.calculatePoints(100));
    }

}