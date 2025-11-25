package com.rewardsapi.service;

import com.rewardsapi.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Helper class used to calculate reward points based on purchase amount.
 */

@Slf4j
@Component
public class RewardsCalculator
{

    /**
     * Calculates reward points for a given purchase amount.
     *
     * @param amount the purchase amount
     * @return total reward points earned
     * @throws CustomException if the amount is negative
     */
    public int calculatePoints(double amount){
        log.debug("ENTERED :: RewardsCalculator :: calculatePoints :: Calculating reward points for amount: {}", amount);
        if (amount < 0)
            throw new CustomException("Amount cannot be negative");

        int points = 0;
        if(amount > 100){
            points += ((amount - 100) * 2);
            amount = 100;
        }
        if(amount > 50){
            points += ((amount - 50) * 1);
        }
        log.info("EXITING :: RewardsCalculator :: calculatePoints :: Total reward points calculated for amount {} = {}", amount, points);
        return points;
    }
}