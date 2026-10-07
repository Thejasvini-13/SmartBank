-- Data Initialization Script for SmartBank
INSERT INTO customers (customer_id, name, email, phone) VALUES
(1, 'Thejasvini M', 'thejaswinimurugalingam@gmail.com', '9876543210'),
(2, 'Priya Patel', 'priya.patel@example.com', '9876543211'),
(3, 'Amit Verma', 'amit.verma@example.com', '9876543212');

INSERT INTO accounts (account_id, account_number, customer_id, account_type, balance, interest_rate, overdraft_limit, status) VALUES
(1, 'SB1001', 1, 'SAVINGS', 124500.00, 4.50, 0.00, 'ACTIVE'),
(2, 'CA1002', 1, 'CURRENT', 45000.00, 0.00, 25000.00, 'ACTIVE'),
(3, 'SB1003', 2, 'SAVINGS', 85000.00, 4.50, 0.00, 'ACTIVE'),
(4, 'SB1004', 3, 'SAVINGS', 15000.00, 4.00, 0.00, 'ACTIVE');

INSERT INTO transactions (transaction_id, account_number, type, amount, category, description, timestamp, is_suspicious, risk_reason) VALUES
(1, 'SB1001', 'CREDIT', 55000.00, 'OTHER', 'Monthly Salary Credit', '2026-10-01 09:00:00', FALSE, NULL),
(2, 'SB1001', 'DEBIT', 450.00, 'FOOD', 'Swiggy Food Order', '2026-10-02 13:15:00', FALSE, NULL),
(3, 'SB1001', 'DEBIT', 2300.00, 'SHOPPING', 'Amazon Fashion Purchase', '2026-10-03 16:45:00', FALSE, NULL),
(4, 'SB1001', 'DEBIT', 1200.00, 'BILLS', 'Electricity Bill Payment', '2026-10-04 10:30:00', FALSE, NULL),
(5, 'SB1001', 'DEBIT', 24500.00, 'TRAVEL', 'International Flight Ticket', '2026-10-05 18:20:00', FALSE, NULL),
(6, 'SB1001', 'DEBIT', 75000.00, 'OTHER', 'High Value Wire Transfer', '2026-10-06 21:00:00', TRUE, 'Transaction amount (₹75000.00) exceeds single transaction limit of ₹50000.00');

INSERT INTO savings_goals (goal_id, customer_id, title, target_amount, current_amount, deadline) VALUES
(1, 1, 'New Laptop', 80000.00, 32000.00, '2027-06-30'),
(2, 1, 'Emergency Fund', 150000.00, 65000.00, '2027-12-31'),
(3, 2, 'Home Renovation', 200000.00, 90000.00, '2027-09-15');

INSERT INTO spending_limits (limit_id, account_number, monthly_limit, current_spending) VALUES
(1, 'SB1001', 30000.00, 28450.00),
(2, 'SB1003', 40000.00, 12000.00);

INSERT INTO scheduled_payments (payment_id, account_number, payee_name, amount, frequency, next_payment_date, status) VALUES
(1, 'SB1001', 'House Rent', 15000.00, 'MONTHLY', '2026-11-05', 'ACTIVE'),
(2, 'SB1001', 'Airtel Broadband', 1199.00, 'MONTHLY', '2026-10-15', 'ACTIVE'),
(3, 'SB1001', 'Car EMI', 12500.00, 'MONTHLY', '2026-10-10', 'ACTIVE');
