package com.rewardsapi.service;

import com.rewardsapi.exception.CustomException;
import com.rewardsapi.model.CustomerReward;
import com.rewardsapi.model.MonthlyReward;
import com.rewardsapi.model.Transaction;
import com.rewardsapi.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class responsible for reward calculation logic.
 * This class processes transactions, calculates points,
 * groups them by customer and month, and prepares the final response.
 */

@Slf4j
@Service
public class RewardsService {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RewardsCalculator calculator;

    /**
     * Calculates and returns reward points for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return the customer's monthly and total reward points
     * @throws CustomException if the customer has no transactions
     */
    public CustomerReward getRewardsForCustomer(Long customerId) {

        log.info("ENTERED :: RewardsService :: getRewardsForCustomer :: Fetching transactions for customerId: {}", customerId);
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusDays(90); // last 90 days
        List<Transaction> userTransactions = repository.findAll().stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(now))
                .toList();

        if (userTransactions.isEmpty()) {
            log.error("No transactions found for customerId: {}", customerId);
            throw new CustomException("Customer ID " + customerId + " not found.");
        }
        log.info("Found {} transactions for customerId: {}", userTransactions.size(), customerId);
        Map<String, Integer> monthlyPoints = new HashMap<>();

        for (Transaction transaction : userTransactions) {
            int points = calculator.calculatePoints(transaction.getAmount());
            String month = transaction.getDate().getMonth().toString();
            log.info("Transaction: amount={}, month={}, calculatedPoints={}",
                    transaction.getAmount(), month, points);
            monthlyPoints.merge(month, points, Integer::sum);
        }
        List<MonthlyReward> rewards = monthlyPoints.entrySet().stream()
                .map(e -> new MonthlyReward(e.getKey(), e.getValue()))
                .toList();

        int total = rewards.stream().mapToInt(MonthlyReward::points).sum();
        log.info("EXITING :: RewardsService :: getRewardsForCustomer :: Total reward points for customerId {} = {}", customerId, total);
        return new CustomerReward(customerId, rewards, total);
    }

    public Transaction saveTransaction(Transaction request) {
        log.info("ENTERED :: RewardsService :: saveTransaction :: Saving the data for : {}", request);
        return repository.save(request);
    }
}