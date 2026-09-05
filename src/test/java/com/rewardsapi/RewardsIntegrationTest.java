package com.rewardsapi;

import com.rewardsapi.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** End-to-end HTTP tests using the real controller, service, calculator, and H2 database. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RewardsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void clearDatabase() {
        transactionRepository.deleteAll();
    }

    @Test
    void savesTransactionsAndCalculatesRewardsThroughTheApi() throws Exception {
        long customerId = 101L;
        LocalDate transactionDate = LocalDate.now().minusDays(1);

        String token = obtainAccessToken("user","password");

        saveTransaction(customerId, 120.0, transactionDate, token)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.customerId").value(customerId));
        saveTransaction(customerId, 75.0, transactionDate, token)
                .andExpect(jsonPath("$.id").isNumber());

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.monthlyRewards.length()").value(1))
                .andExpect(jsonPath("$.monthlyRewards[0].points").value(115))
                .andExpect(jsonPath("$.totalPoints").value(115));
    }

    @Test
    void returnsNotFoundWhenCustomerHasNoRecentTransactions() throws Exception {
        long customerId = 202L;
        String token = obtainAccessToken("user","password");
        saveTransaction(customerId, 120.0, LocalDate.now().minusDays(91), token);

        mockMvc.perform(get("/api/rewards/{customerId}", customerId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer ID 202 not found."))
                .andExpect(jsonPath("$.status").value(404));
    }

    private org.springframework.test.web.servlet.ResultActions saveTransaction(
            long customerId, double amount, LocalDate date, String token) throws Exception {
        String requestBody = """
                {"customerId": %d, "amount": %.2f, "date": "%s"}
                """.formatted(customerId, amount, date);

        return mockMvc.perform(post("/api/rewards/saveData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String resp = result.getResponse().getContentAsString();
        Map<String, Object> map = new ObjectMapper().readValue(resp, Map.class);
        return (String) map.get("token");
    }
}

