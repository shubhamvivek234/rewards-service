INSERT INTO customers (id, name, email, phone, created_at) VALUES
(1, 'Alice Johnson', 'alice.johnson@example.com', '+1-555-0101', CURRENT_TIMESTAMP),
(2, 'Bob Smith', 'bob.smith@example.com', '+1-555-0102', CURRENT_TIMESTAMP),
(3, 'Carol Davis', 'carol.davis@example.com', '+1-555-0103', CURRENT_TIMESTAMP),
(4, 'David Wilson', 'david.wilson@example.com', '+1-555-0104', CURRENT_TIMESTAMP);

INSERT INTO transactions (customer_id, amount, transaction_date, description, reward_points, created_at) VALUES
(1, 120.00, '2026-07-12', 'Electronics Store Purchase', 90, CURRENT_TIMESTAMP),
(1, 45.00, '2026-07-25', 'Coffee Shop Order', 0, CURRENT_TIMESTAMP),
(1, 200.00, '2026-08-05', 'Home Appliance Purchase', 250, CURRENT_TIMESTAMP),
(1, 75.00, '2026-08-20', 'Clothing Store Purchase', 25, CURRENT_TIMESTAMP),
(1, 100.00, '2026-09-02', 'Grocery Supermarket Purchase', 50, CURRENT_TIMESTAMP),
(1, 50.00, '2026-09-19', 'Bookstore Purchase', 0, CURRENT_TIMESTAMP);

INSERT INTO transactions (customer_id, amount, transaction_date, description, reward_points, created_at) VALUES
(2, 90.00, '2026-07-08', 'Sporting Goods Purchase', 40, CURRENT_TIMESTAMP),
(2, 150.00, '2026-07-22', 'Department Store Order', 150, CURRENT_TIMESTAMP),
(2, 130.00, '2026-08-11', 'Hardware Tools Purchase', 110, CURRENT_TIMESTAMP),
(2, 30.00, '2026-08-28', 'Convenience Store Items', 0, CURRENT_TIMESTAMP),
(2, 60.00, '2026-09-14', 'Pharmacy Prescription', 10, CURRENT_TIMESTAMP),
(2, 220.00, '2026-09-27', 'Furniture Accent Piece', 290, CURRENT_TIMESTAMP);

INSERT INTO transactions (customer_id, amount, transaction_date, description, reward_points, created_at) VALUES
(3, 300.00, '2026-07-15', 'Laptop and Accessories', 450, CURRENT_TIMESTAMP),
(3, 50.00, '2026-08-18', 'Office Supplies', 0, CURRENT_TIMESTAMP),
(3, 110.00, '2026-09-22', 'Audio Headphones', 70, CURRENT_TIMESTAMP);
