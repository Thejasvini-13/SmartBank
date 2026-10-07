-- =========================================================
-- SmartBank Database Schema & Sample Data
-- Relational database design for MySQL & H2 Compatibility
-- =========================================================

CREATE DATABASE IF NOT EXISTS smartbank;
USE smartbank;

-- 1. Customers Table
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INT NOT NULL,
    account_type VARCHAR(20) NOT NULL, -- SAVINGS, CURRENT
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    interest_rate DECIMAL(5, 2) DEFAULT 0.00,
    overdraft_limit DECIMAL(15, 2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- 3. Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    type VARCHAR(10) NOT NULL, -- CREDIT, DEBIT
    amount DECIMAL(15, 2) NOT NULL,
    category VARCHAR(50) DEFAULT 'OTHER', -- FOOD, TRAVEL, SHOPPING, BILLS, ENTERTAINMENT, HEALTH, EDUCATION, OTHER
    description VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_suspicious BOOLEAN DEFAULT FALSE,
    risk_reason VARCHAR(255)
);

-- 4. Savings Goals Table
CREATE TABLE IF NOT EXISTS savings_goals (
    goal_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    target_amount DECIMAL(15, 2) NOT NULL,
    current_amount DECIMAL(15, 2) DEFAULT 0.00,
    deadline DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- 5. Spending Limits Table
CREATE TABLE IF NOT EXISTS spending_limits (
    limit_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    monthly_limit DECIMAL(15, 2) NOT NULL,
    current_spending DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Scheduled Payments Table
CREATE TABLE IF NOT EXISTS scheduled_payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    payee_name VARCHAR(100) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    frequency VARCHAR(20) NOT NULL, -- MONTHLY, WEEKLY, YEARLY
    next_payment_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Loan Applications Table
CREATE TABLE IF NOT EXISTS loan_applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    monthly_income DECIMAL(15, 2) NOT NULL,
    monthly_expenses DECIMAL(15, 2) NOT NULL,
    existing_emi DECIMAL(15, 2) NOT NULL,
    credit_score INT NOT NULL,
    requested_amount DECIMAL(15, 2) NOT NULL,
    tenure_months INT NOT NULL,
    status VARCHAR(20) NOT NULL, -- ELIGIBLE, NOT_ELIGIBLE
    estimated_emi DECIMAL(15, 2),
    debt_to_income_ratio DECIMAL(5, 2),
    explanation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- 8. Financial Health Scores Table
CREATE TABLE IF NOT EXISTS financial_health_scores (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    overall_score INT NOT NULL,
    category VARCHAR(30) NOT NULL, -- EXCELLENT, GOOD, NEEDS_IMPROVEMENT, CRITICAL
    savings_score INT NOT NULL,
    spending_score INT NOT NULL,
    debt_score INT NOT NULL,
    goal_score INT NOT NULL,
    recommendations TEXT,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- =========================================================
-- SAMPLE DATA INSERTIONS
-- =========================================================

INSERT INTO customers (customer_id, name, email, phone) VALUES
(1, 'Thejasvini M', 'thejaswinimurugalingam@gmail.com', '9876543210'),
(2, 'Priya Patel', 'priya.patel@example.com', '9876543211'),
(3, 'Amit Verma', 'amit.verma@example.com', '9876543212');

INSERT INTO accounts (account_id, account_number, customer_id, account_type, balance, interest_rate, overdraft_limit, status) VALUES
(1, 'SB1001', 1, 'SAVINGS', 124500.00, 4.50, 0.00, 'ACTIVE'),
(2, 'CA1002', 1, 'CURRENT', 45000.00, 0.00, 25000.00, 'ACTIVE'),
(3, 'SB1003', 2, 'SAVINGS', 85000.00, 4.50, 0.00, 'ACTIVE'),
(4, 'SB1004', 3, 'SAVINGS', 15000.00, 4.00, 0.00, 'ACTIVE');

INSERT INTO transactions (account_number, type, amount, category, description, timestamp, is_suspicious, risk_reason) VALUES
('SB1001', 'CREDIT', 55000.00, 'OTHER', 'Monthly Salary Credit', '2026-10-01 09:00:00', FALSE, NULL),
('SB1001', 'DEBIT', 450.00, 'FOOD', 'Swiggy Food Order', '2026-10-02 13:15:00', FALSE, NULL),
('SB1001', 'DEBIT', 2300.00, 'SHOPPING', 'Amazon Fashion Purchase', '2026-10-03 16:45:00', FALSE, NULL),
('SB1001', 'DEBIT', 1200.00, 'BILLS', 'Electricity Bill Payment', '2026-10-04 10:30:00', FALSE, NULL),
('SB1001', 'DEBIT', 24500.00, 'TRAVEL', 'International Flight Ticket', '2026-10-05 18:20:00', FALSE, NULL),
('SB1001', 'DEBIT', 75000.00, 'OTHER', 'High Value Wire Transfer', '2026-10-06 21:00:00', TRUE, 'Transaction amount exceeds safety threshold (> ₹50,000)');

INSERT INTO savings_goals (customer_id, title, target_amount, current_amount, deadline) VALUES
(1, 'New Laptop', 80000.00, 32000.00, '2027-06-30'),
(1, 'Emergency Fund', 150000.00, 65000.00, '2027-12-31'),
(2, 'Home Renovation', 200000.00, 90000.00, '2027-09-15');

INSERT INTO spending_limits (account_number, monthly_limit, current_spending) VALUES
('SB1001', 30000.00, 28450.00),
('SB1003', 40000.00, 12000.00);

INSERT INTO scheduled_payments (account_number, payee_name, amount, frequency, next_payment_date, status) VALUES
('SB1001', 'House Rent', 15000.00, 'MONTHLY', '2026-11-05', 'ACTIVE'),
('SB1001', 'Airtel Broadband', 1199.00, 'MONTHLY', '2026-10-15', 'ACTIVE'),
('SB1001', 'Car EMI', 12500.00, 'MONTHLY', '2026-10-10', 'ACTIVE');
