#  Rewards Program API

A Spring Boot–based REST API that calculates customer reward points based on recent transactions.  
The system supports transaction storage, reward calculation for the last 90 days, monthly breakdowns, and robust exception handling.

---

##  Features

**Save new customer transactions**
**Calculate reward points**
**Rewards for last 90 days only**
**Monthly reward point breakdown**
**Clean modular design (Controller → Service → Calculator → Repository)**
**JUnit + Mockito + Integration Tests**
 **Global exception handling**
**Clear project structure**

---

##  Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.rewardsapi
│   │       ├── controller
│   │       │   └── RewardsController.java
│   │       │
│   │       ├── service
│   │       │   ├── RewardsService.java
│   │       │   └── RewardsCalculator.java
│   │       │
│   │       ├── repository
│   │       │   └── TransactionRepository.java
│   │       │
│   │       ├── model
│   │       │   ├── Transaction.java
│   │       │   ├── MonthlyReward.java
│   │       │   ├── CustomerReward.java
│   │       │   └── ErrorResponse.java
│   │       │
│   │       ├── exception
│   │       │   ├── CustomException.java
│   │       │   └── GlobalExceptionHandler.java
│   │       │
│   │       └── RewardsProgramApplication.java
│   │
│   └── resources
│       └── application.properties
│       └── rewardsdb_transaction.sql
│
├── test
│   ├── java
│   │    └── com.rewardsapi
│   │       ├── controller
│   │       │   └── RewardsControllerTest.java
│   │       │
│   │       ├── service
│   │       │   ├── RewardsServiceTest.java
│   │       │   └── RewardsCalculatorTest.java
│   │       │               
│   │       └── RewardsIntegrationTest
```

---

##  Technologies Used

 **Java 17**
 **Spring Boot 3.x**
 **Spring Web**
 **Spring Data JPA**
 **H2 / MySQL**
 **Lombok**
 **JUnit 5**
 **Mockito**
 **Maven**

---

###  **Save Transaction**

**Method:** `POST`  
**URL:** `http://localhost:8080/api/rewards/saveData`

####  Request Body

```json
{
  "customerId": 3,
  "amount": 111.0,
  "date": "2025-10-25"
}
```

####  Response

```json
{
  "id": 7,
  "customerId": 3,
  "amount": 111.0,
  "date": "2025-10-25"
}
```

---

###  **Get Rewards for Customer**

**Method:** `GET`  
**URL:** `http://localhost:8080/api/rewards/2`

####  Response

```json
{
  "customerId": 2,
  "monthlyRewards": [
    {
      "month": "OCTOBER",
      "points": 0
    },
    {
      "month": "SEPTEMBER",
      "points": 49
    },
    {
      "month": "NOVEMBER",
      "points": 248
    }
  ],
  "totalPoints": 297
}
```
