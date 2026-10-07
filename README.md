# SmartBank — Intelligent Banking Management System

A full-stack banking application that combines core banking operations with intelligent personal-finance management features.

SmartBank is a modern banking management system developed using **Java, Spring Boot, MySQL, JDBC, JPA/Hibernate, REST APIs, HTML, CSS, and Vanilla JavaScript**.

The application allows users to manage bank accounts and transactions while providing intelligent financial tools such as **expense categorization, savings goals, spending alerts, suspicious transaction detection, scheduled payments, loan eligibility analysis, and financial health scoring**.

---

## Features

### Core Banking

* Customer registration and account management
* Savings and Current accounts
* Deposit and withdrawal
* Money transfer between accounts
* Balance management
* Complete transaction history
* Transaction search and filtering

### Smart Expense Categorization

Automatically categorizes transactions into:

* Food
* Travel
* Shopping
* Bills
* Entertainment
* Healthcare
* Education
* Other

### Savings Goal Tracker

* Create personalized savings goals
* Set target amount and deadline
* Track savings progress
* Calculate remaining amount
* Display percentage completion

### Suspicious Transaction Detection

A rule-based monitoring system identifies potentially unusual transactions based on:

* Transaction amount
* Spending patterns
* Transaction frequency
* Configured thresholds

Transactions can be classified as Low, Medium, or High risk.

### Spending Limit Alerts

Users can configure monthly spending limits.

| Usage   | Status   |
| ------- | -------- |
| 0–69%   | Normal   |
| 70–89%  | Warning  |
| 90–100% | Critical |
| >100%   | Exceeded |

### Scheduled Payments

Manage recurring payments such as:

* Rent
* EMIs
* Insurance
* Internet bills
* Subscriptions

### Loan Eligibility Calculator

Evaluates loan eligibility using:

* Monthly income
* Monthly expenses
* Existing EMI
* Credit score
* Requested loan amount
* Loan tenure

Provides estimated EMI, debt-to-income ratio, and eligibility status.

### Financial Health Score

Generates a score from **0–100** based on:

* Savings behavior
* Spending patterns
* Debt/EMI burden
* Account balance
* Spending-limit usage
* Savings-goal progress

| Score  | Status            |
| ------ | ----------------- |
| 80–100 | Excellent         |
| 60–79  | Good              |
| 40–59  | Needs Improvement |
| 0–39   | Critical          |

---

## Tech Stack

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* JDBC
* Maven

### Database

* MySQL

### Frontend

* HTML5
* CSS3
* Vanilla JavaScript
* Fetch API

### Development and Testing

* IntelliJ IDEA / Eclipse / VS Code
* MySQL Workbench
* Postman
* Git and GitHub

---

## System Architecture

```text
                 ┌──────────────────────┐
                 │   HTML / CSS / JS    │
                 │      Frontend        │
                 └──────────┬───────────┘
                            │
                         Fetch API
                            │
                            ▼
                 ┌──────────────────────┐
                 │   Spring Boot REST   │
                 │         API          │
                 └──────────┬───────────┘
                            │
                            ▼
                 ┌──────────────────────┐
                 │    Service Layer     │
                 │    Business Logic    │
                 └──────────┬───────────┘
                            │
                            ▼
                 ┌──────────────────────┐
                 │ Repository / JPA     │
                 │      Hibernate       │
                 └──────────┬───────────┘
                            │
                            ▼
                 ┌──────────────────────┐
                 │       MySQL          │
                 │      Database        │
                 └──────────────────────┘
```

---

## OOP Concepts

SmartBank demonstrates the fundamental principles of Object-Oriented Programming.

### Abstraction

An abstract `Account` class defines common account behavior.

```java
public abstract class Account {
    // Common account properties and operations
}
```

### Encapsulation

Account and customer information is kept private and accessed through appropriate methods.

### Inheritance

```text
             Account
             /     \
            /       \
SavingsAccount    CurrentAccount
```

### Polymorphism

The application can operate on an `Account` reference while supporting different account implementations.

```java
Account account;

account = new SavingsAccount();
```

---

## Database Design

The application uses MySQL for persistent data storage.

### Main Tables

```text
customers
accounts
transactions
savings_goals
spending_limits
scheduled_payments
loan_applications
suspicious_transactions
financial_health_scores
```

### Relationship Overview

```text
Customer
   │
   ├── Accounts
   │      │
   │      └── Transactions
   │
   ├── Savings Goals
   ├── Spending Limits
   ├── Scheduled Payments
   ├── Loan Applications
   └── Financial Health Score
```

---

## REST API

### Customer APIs

```http
POST   /api/customers
GET    /api/customers/{id}
PUT    /api/customers/{id}
DELETE /api/customers/{id}
```

### Account APIs

```http
POST   /api/accounts
GET    /api/accounts
GET    /api/accounts/{id}
PUT    /api/accounts/{id}
```

### Banking Operations

```http
POST /api/accounts/{id}/deposit
POST /api/accounts/{id}/withdraw
POST /api/transfers
GET  /api/accounts/{id}/balance
```

### Transaction APIs

```http
GET /api/transactions
GET /api/transactions/{id}
GET /api/accounts/{id}/transactions
```

### Financial APIs

```http
POST /api/savings-goals
GET  /api/savings-goals

POST /api/spending-limits
GET  /api/spending-limits

POST /api/scheduled-payments
GET  /api/scheduled-payments

POST /api/loans/eligibility

GET /api/financial-health/{customerId}

GET /api/security/suspicious-transactions
```

---

## Data and Error Handling

The application follows backend development best practices including:

* Prepared statements for JDBC operations
* Input validation
* Proper HTTP status codes
* Centralized exception handling
* Database constraints
* Transaction validation
* Prevention of negative deposits and withdrawals
* Insufficient-balance validation
* Duplicate account validation

Example HTTP responses:

```text
200 OK
201 CREATED
400 BAD REQUEST
404 NOT FOUND
409 CONFLICT
500 INTERNAL SERVER ERROR
```

---

## Project Structure

```text
SmartBank/
│
├── backend/
│   ├── src/main/java/com/smartbank/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── exception/
│   │   └── SmartBankApplication.java
│   │
│   └── src/main/resources/
│       └── application.properties
│
├── frontend/
│   ├── index.html
│   ├── dashboard.html
│   ├── accounts.html
│   ├── transactions.html
│   ├── savings.html
│   ├── spending.html
│   ├── payments.html
│   ├── loan.html
│   ├── health.html
│   ├── security.html
│   │
│   ├── css/
│   │   └── style.css
│   │
│   └── js/
│       ├── api.js
│       ├── dashboard.js
│       ├── accounts.js
│       ├── transactions.js
│       ├── savings.js
│       ├── spending.js
│       ├── payments.js
│       ├── loan.js
│       └── health.js
│
├── database/
│   └── schema.sql
│
└── README.md
```

---

## Getting Started

### Prerequisites

Make sure you have installed:

* Java 17+
* Maven
* MySQL
* Git

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/smartbank.git
cd smartbank
```

### 2. Create the Database

Open MySQL and create the database:

```sql
CREATE DATABASE smartbank;
```

Then execute the provided:

```text
database/schema.sql
```

### 3. Configure MySQL

Update the Spring Boot configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smartbank
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

For security, avoid committing real database credentials to GitHub.

### 4. Run the Backend

Navigate to the backend directory:

```bash
cd backend
```

Run:

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

### 5. Run the Frontend

Open the frontend using a local development server such as VS Code Live Server.

The frontend communicates with the Spring Boot REST API through the Fetch API.

---

## API Testing

The REST APIs can be tested using Postman.

Example:

### Deposit

```http
POST /api/accounts/1/deposit
Content-Type: application/json
```

```json
{
  "amount": 5000
}
```

### Response

```json
{
  "message": "Deposit successful",
  "balance": 29500
}
```

---

## Screenshots

Add application screenshots here:

```text
screenshots/
├── dashboard.png
├── accounts.png
├── transactions.png
├── savings-goals.png
├── loan-eligibility.png
└── financial-health.png
```

Example:

```markdown
![Dashboard](screenshots/dashboard.png)
```

---

## Project Objectives

The main objectives of SmartBank are to:

* Apply Java OOP concepts to a real-world domain
* Build a relational database-driven application
* Implement CRUD operations using JDBC and JPA
* Develop RESTful APIs using Spring Boot
* Connect frontend and backend using JavaScript
* Implement practical financial-management features
* Practice layered backend architecture
* Build a full-stack portfolio application

---

## Future Enhancements

Potential future improvements include:

* JWT-based authentication
* Role-based access control
* Email and SMS notifications
* Real-time transaction notifications
* Advanced fraud detection using Machine Learning
* AI-powered financial recommendations
* Investment portfolio management
* UPI/payment gateway integration
* Docker containerization
* Cloud deployment

---

## Author

**Thejasvini Murugalingam**

A full-stack Java project demonstrating **Object-Oriented Programming, database management, backend development, REST API development, and frontend-backend integration**.
