🎁 Customer Rewards Application

    A Spring Boot application to manage customer transactions and calculate monthly reward points based on their spending.
    
📌 Features

    - Add new customers along with their transactions
    - Calculate rewards earned within a specific date range
    - REST API endpoints with proper validation and exception handling
    - Integration tests for the controller and service layers
    - Logs application activity into the console and file (Logback)

🧰 Tech Stack

    Java 17
    Spring Boot 3
    Spring Data JPA
    Hibernate
    MySQL
    JUnit 5
    Mockito
    Lombok

🎯 Reward Calculation Rules

     1. $50–$100 → 1 point per dollar over $50      
     2. $100+ → 2 points per dollar over $100 + 1 point per dollar between $50–$100
     
📦 Project Structure

    Customer-Rewards-App/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── com/infy/
    │   │   │       ├── controller/
    │   │   │       │   └── RewardsController.java
    │   │   │       ├── dto/
    │   │   │       │   ├── CustomerDto.java
    │   │   │       │   └── TransactionDto.java
    │   │   │       ├── exception/
    │   │   │       │   ├── CustomerNotFoundException.java
    │   │   │       │   ├── ErrorDetails.java
    │   │   │       │   ├── GlobalExceptionHandler.java
    │   │   │       │   ├── InvalidDateFormatException.java
    │   │   │       │   ├── InvalidRequestException.java
    │   │   │       │   └── MethodArgumentNotValidException
    │   │   │       │       
    │   │   │       ├── model/
    │   │   │       │   ├── Customer.java
    │   │   │       │   └── Transaction.java
    │   │   │       ├── repository/
    │   │   │       │   ├── CustomerRepository.java
    │   │   │       │   └── TransactionRepository.java
    │   │   │       ├── service/
    │   │   │       │   ├── RewardsService.java
    │   │   │       │   └── RewardsServiceImpl.java
    │   │   │       └── CustomerRewardsApplication.java
    │   │   └── resources/
    │   │       ├── application.properties
    │   │       └── logback-spring.xml
    │   └── test/
    │        └── java/
    │        │   └── com/infy/
    │        │       ├── RewardsControllerIntegrationTest.java
    │        │       └── RewardsServiceIntegrationTest.java
    │        └──resources/
    │             └── application.properties
    │            
    ├── logs/
    │   └── CustomerRewards.log
    ├── pom.xml
    └── README.md

    
🏋️️ Layers Breakdown

    1. Model/Entity Layer
          JPA entities: Customer and Transaction    
          Mapped to relational database tables using Hibernate 
          Relationship: Customer 1 → ∞ Transaction
    
    2. DTO Layer 
          CustomerDto and TransactionDto encapsulate input data 
          Help decouple internal entity structure from API contracts 
          Validated using jakarta.validation annotations
    
    3. Validation Layer
          Enforces constraints using annotations like:@NotNull, @NotBlank, @Positive, @Size    
          Validation errors are automatically handled via @Valid
    
    4. Repository Layer    
          Uses Spring Data JPA to persist entities    
          Interface-based approach with JpaRepository
    
    5. Service Layer    
          Business logic for saving customers and calculating rewards    
          Maps DTOs to JPA entities    
          Delegates data access to the repository layer
    
    6. Controller Layer    
          Handles HTTP requests/responses    
          Accepts validated CustomerDto and TransactionDto via @RequestBody @Valid    
          Routes requests to RewardsService
    
    7. Exception Handling Layer    
          Centralized error handling via @RestControllerAdvice in GlobalExceptionHandler    
          Catches custom exceptions like CustomerNotFoundException, InvalidRequestException, etc.    
          Provides consistent error responses with appropriate HTTP status codes    
          Handles validation errors and unhandled exceptions gracefully

          
📡 REST API Endpoints

1. Add Customer

 POST: /api/rewards/addCustomer

    Request Body:
    
        {
            "customerName":"Merry",
            "transaction":[
                {
                   "amount":500.00,
                   "date": "2025-03-30"
        
                },
                {
                   "amount":70.00,
                   "date": "2025-04-30"
                   
                },
                {
                   "amount":150.00,
                   "date": "2025-05-30"          
                }
            ]
            
        }
        
    Response:
    
    201 Created with saved customer details.
    

2. Calculate Rewards

GET: /api/rewards/calculateRewards/1?startDate=2025-03-01&endDate=2025-05-31

    Response:
    
        {
            "Customer Details": {
                "customerId": 1,
                "customerName": "Merry",
                "transaction": [
                    {
                        "amount": 500.0,
                        "date": "2025-03-30",
                        "transactionId": 1
                    },
                    {
                        "amount": 70.0,
                        "date": "2025-04-30",
                        "transactionId": 2
                    },
                    {
                        "amount": 150.0,
                        "date": "2025-05-30",
                        "transactionId": 3
                    }
                ]
            },
            "Total Rewards": 1020,
            "Rewards Breakdown": [
                {
                    "month": "MARCH",
                    "points": 850
                },
                {
                    "month": "APRIL",
                    "points": 20
                },
                {
                    "month": "MAY",
                    "points": 150
                }
            ]
        }

    
🚀 Setup Instructions

    1. Clone the Repository
    
    git clone https://github.com/priyankapersonal/Customer-Rewards-App.git
    cd Customer-Rewards-App
    
    2. Configure MySQL Database
    
    Create a database named Rewards_Calculation and update src/main/resources/application.properties 
    if needed:
    
          spring.datasource.url=jdbc:mysql://localhost:3306/Rewards_Calculation
          spring.datasource.username=root
          spring.datasource.password=root
    
    3. Build and Run
    
    mvn clean install
    mvn spring-boot:run
    Application will be accessible at: http://localhost:8091


📂 Log Configuration

    Logs are written to both the console and the file: logs/CustomerRewards.log. Only application logs are enabled (others suppressed).
    
🧪 Run Tests

    mvn test
    
    Includes integration tests for:
          - Adding customers
          - Validating transactions
          - Calculating rewards
👩‍💻 Author

         Priyanka Patil
     (Senior Associate Consultant)

