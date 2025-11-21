package com.rewardsapi.controller;

import com.rewardsapi.model.CustomerReward;
import com.rewardsapi.model.Transaction;
import com.rewardsapi.service.RewardsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/**
 * REST controller for handling reward-related API requests.
 *
 * <p>This controller exposes an endpoint that returns reward points calculated
 * for all customers based on their transactions.</p>
 */

@Slf4j
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    @Autowired
    private RewardsService service;

    /**
     * Gets reward details for a single customer.
     *
     * @param customerId the customer's ID
     * @return reward information for that customer
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerReward> getRewards(@PathVariable Long customerId) {
        log.info("ENTERED :: RewardsController :: getRewards :: CustomerId: {}", customerId);
        CustomerReward reward=service.getRewardsForCustomer(customerId);
        log.info("EXITING :: RewardsController :: getRewards :: Rewards fetched successfully for customerId: {}  and reward:{}", customerId,reward);
        return ResponseEntity.ok(reward);
    }

    /**
     * Saves a new transaction into the system.
     *
     * @param request the transaction details sent in the request body
     * @return the saved transaction with generated ID
     */
    @PostMapping("/saveData")
    public ResponseEntity<Transaction> saveData(@RequestBody Transaction request) {
        Transaction saved = service.saveTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
