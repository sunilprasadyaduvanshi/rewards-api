package com.rewardsapi.model;

import java.util.List;

public record CustomerReward(Long customerId, List<MonthlyReward> monthlyRewards, int totalPoints) {}