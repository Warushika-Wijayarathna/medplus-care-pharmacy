DROP DATABASE MedplusCarePharmacy;

CREATE DATABASE MedplusCarePharmacy;

USE MedplusCarePharmacy;

CREATE TABLE Customer (
                          cust_id VARCHAR(10) PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          contact_no INT unique ,
                          email VARCHAR(255) unique
);

CREATE TABLE AutoIncrement_Customer (
                                        next_id INT
);

INSERT INTO AutoIncrement_Customer (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_customer_id
    BEFORE INSERT ON Customer
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_Customer SET next_id = next_id + 1;
    SET NEW.cust_id = CONCAT('C', LPAD((SELECT next_id FROM AutoIncrement_Customer), 4, '0'));
END;
//

DELIMITER ;

CREATE TABLE User (
                      usr_id VARCHAR(10) PRIMARY KEY,
                      usr_name VARCHAR(255),
                      password VARCHAR(255)
);

CREATE TABLE AutoIncrement_User (
                                    next_id INT
);

INSERT INTO AutoIncrement_User (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_user_id
    BEFORE INSERT ON User
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_User SET next_id = next_id + 1;
    SET NEW.usr_id = CONCAT('U', LPAD((SELECT next_id FROM AutoIncrement_User), 4, '0'));
END;
//

DELIMITER ;

CREATE TABLE Item (
                      item_id VARCHAR(10) PRIMARY KEY,
                      description VARCHAR(255),
                      qty INT,
                      whole_sale_price DECIMAL(10, 2),
                      retail_price DECIMAL(10, 2),
                      discount DECIMAL(5, 2),
                      exp_date DATE
);

CREATE TABLE AutoIncrement_Item (
                                    next_id INT
);

INSERT INTO AutoIncrement_Item (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_item_id
    BEFORE INSERT ON Item
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_Item SET next_id = next_id + 1;
    SET NEW.item_id = CONCAT('I', LPAD((SELECT next_id FROM AutoIncrement_Item), 5, '0'));
END;
//

DELIMITER ;

CREATE TABLE Supplier (
                          sp_id VARCHAR(10) PRIMARY KEY,
                          name VARCHAR(255),
                          contact INT unique,
                          email VARCHAR(100) unique
);

CREATE TABLE AutoIncrement_Supplier (
                                        next_id INT
);

INSERT INTO AutoIncrement_Supplier (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_supplier_id
    BEFORE INSERT ON Supplier
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_Supplier SET next_id = next_id + 1;
    SET NEW.sp_id = CONCAT('S', LPAD((SELECT next_id FROM AutoIncrement_Supplier), 4, '0'));
END;
//

DELIMITER ;

CREATE TABLE `Order` (
                         o_id VARCHAR(10) PRIMARY KEY,
                         total DECIMAL(10, 2),
                         cust_id VARCHAR(10),
                         user_id VARCHAR(10),
                         date DATE,
                         FOREIGN KEY (cust_id) REFERENCES Customer(cust_id) ON DELETE SET NULL,
                         FOREIGN KEY (user_id) REFERENCES User(usr_id) ON DELETE SET NULL

);

CREATE TABLE AutoIncrement_Order (
                                     next_id INT
);

INSERT INTO AutoIncrement_Order (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_order_id
    BEFORE INSERT ON `Order`
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_Order SET next_id = next_id + 1;
    SET NEW.o_id = CONCAT('O', LPAD((SELECT next_id FROM AutoIncrement_Order), 4, '0'));
END;
//

DELIMITER ;


CREATE TABLE Employee (
                          emp_id VARCHAR(10) PRIMARY KEY,
                          name VARCHAR(255),
                          position VARCHAR(20),
                          address VARCHAR(255),
                          contact_no VARCHAR(20),
                          salary DECIMAL(10, 2),
                          usr_id VARCHAR(10),
                          FOREIGN KEY (usr_id) REFERENCES User(usr_id) ON DELETE SET NULL
);

CREATE TABLE AutoIncrement_Employee (
                                        next_id INT
);

INSERT INTO AutoIncrement_Employee (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_employee_id
    BEFORE INSERT ON Employee
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_Employee SET next_id = next_id + 1;
    SET NEW.emp_id = CONCAT('E', LPAD((SELECT next_id FROM AutoIncrement_Employee), 4, '0'));
END;
//

DELIMITER ;

CREATE TABLE Test (
                      test_id VARCHAR(10) PRIMARY KEY,
                      description VARCHAR(255) NOT NULL,
                      lab VARCHAR(40) ,
                      sample_type VARCHAR(10) ,
                      test_type VARCHAR(20) NOT NULL,
                      price DECIMAL(10, 2) NOT NULL,
                      discount DECIMAL(5, 2) NULL
);

CREATE TABLE AutoIncrement_Test (
                                    next_id INT
);

INSERT INTO AutoIncrement_Test (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_test_id
    BEFORE INSERT ON Test
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_Test SET next_id = next_id + 1;
    SET NEW.test_id = CONCAT('T', LPAD((SELECT next_id FROM AutoIncrement_Test), 4, '0'));
END;
//

DELIMITER ;

CREATE TABLE Report (
                        r_id VARCHAR(10) PRIMARY KEY,
                        cust_id VARCHAR(10),
                        test_id VARCHAR(10),
                        result VARCHAR(255),
                        issue_date DATE NOT NULL,
                        pickup_date DATE,
                        FOREIGN KEY (test_id) REFERENCES Test(test_id) ON DELETE SET NULL,
                        FOREIGN KEY (cust_id) REFERENCES Customer(cust_id) ON DELETE SET NULL
);

CREATE TABLE AutoIncrement_Report (
                                      next_id INT
);

INSERT INTO AutoIncrement_Report (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_report_id
    BEFORE INSERT ON Report
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_Report SET next_id = next_id + 1;
    SET NEW.r_id = CONCAT('R', LPAD((SELECT next_id FROM AutoIncrement_Report), 4, '0'));
END;
//

DELIMITER ;

CREATE TABLE Prescription (
                              presc_id VARCHAR(10) PRIMARY KEY,
                              cust_id VARCHAR(10),
                              patient_name VARCHAR(255) NOT NULL,
                              age INT NOT NULL,
                              medical_officer_name VARCHAR(255) NOT NULL,
                              context VARCHAR(255),
                              duration VARCHAR(50),
                              date DATE NOT NULL,

                              FOREIGN KEY (cust_id) REFERENCES Customer(cust_id) ON DELETE SET NULL
);

CREATE TABLE AutoIncrement_Prescription (
                                            next_id INT
);

INSERT INTO AutoIncrement_Prescription (next_id) VALUES (0);

DELIMITER //

CREATE TRIGGER increment_prescription_id
    BEFORE INSERT ON Prescription
    FOR EACH ROW
BEGIN
    UPDATE AutoIncrement_Prescription SET next_id = next_id + 1;
    SET NEW.presc_id = CONCAT('PR', LPAD((SELECT next_id FROM AutoIncrement_Prescription), 3, '0'));
END;
//

DELIMITER ;

CREATE TABLE presc_test_detail (
                                   presc_id VARCHAR(10),
                                   test_id VARCHAR(10),
                                   FOREIGN KEY (presc_id) REFERENCES Prescription(presc_id) ON DELETE SET NULL,
                                   FOREIGN KEY (test_id) REFERENCES Test(test_id) ON DELETE SET NULL
);

CREATE TABLE order_test_detail (
                                 o_id VARCHAR(10),
                                 test_id VARCHAR(10),
                                 qty INT,
                                 FOREIGN KEY (o_id) REFERENCES `Order`(o_id) ON DELETE SET NULL,
                                 FOREIGN KEY (test_id) REFERENCES Test(test_id) ON DELETE SET NULL
);

CREATE TABLE order_item_detail (
                                   o_id VARCHAR(10),
                                   item_id VARCHAR(10),
                                   qty INT,
                                   FOREIGN KEY (o_id) REFERENCES `Order`(o_id) ON DELETE SET NULL,
                                   FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE SET NULL
);

CREATE TABLE item_supplier_detail (
                                      sp_id VARCHAR(10),
                                      item_id VARCHAR(10),
                                      FOREIGN KEY (sp_id) REFERENCES Supplier(sp_id) ON DELETE SET NULL,
                                      FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE SET NULL
);




INSERT INTO Item (description, qty, whole_sale_price, retail_price, discount, exp_date)
VALUES
    ('Paracetamol Tablets', 100, 5.99, 7.99, 0.1, '2024-12-31'),
    ('Amoxicillin Capsules', 50, 10.50, 15.99, 0.05, '2024-10-15'),
    ('Ibuprofen Tablets', 80, 8.25, 11.49, 0.08, '2025-02-28'),
    ('Aspirin Tablets', 120, 4.75, 6.99, 0.15, '2024-09-30'),
    ('Cough Syrup', 60, 12.99, 18.75, 0.12, '2024-11-20');

INSERT INTO Supplier (name, contact, email)
VALUES
    ('Pharma Suppliers Inc.', 124345799, 'USA'),
    ('MediCare Pharmaceuticals', 124345789, 'UK'),
    ('Global Pharma Solutions', 124345779, 'Canada'),
    ('HealthyLife Pharma', 1243457869, 'Australia'),
    ('MediWorld Enterprises', 124345759, 'Germany');

INSERT INTO User (usr_name, password)
VALUES
    ('john_doe', 'password123'),
    ('jane_smith', 'secure456'),
    ('admin', '1234'),
    ('manager', 'manager123'),
    ('pharmacist', 'pharma789');

INSERT INTO Employee (name, position, address, contact_no, salary, usr_id)
VALUES
    ('John Doe', '123456789V', '123 Main Street, City, Country', '+1234567890', 2500.00, 'U0001'),
    ('Jane Smith', '987654321V', '456 Elm Street, City, Country', '+0987654321', 2800.00, 'U0002'),
    ('Admin User', '555555555V', '789 Oak Street, City, Country', '+5555555555', 3500.00, 'U0003'),
    ('Manager User', '666666666V', '101 Pine Street, City, Country', '+6666666666', 4000.00, 'U0004'),
    ('Pharmacist User', '777777777V', '202 Cedar Street, City, Country', '+7777777777', 3800.00, 'U0005');



INSERT INTO Customer (name, contact_no, email)
VALUES
    ('Alice Johnson', '+123456789', 'alice@example.com'),
    ('Bob Smith', '+198765432', 'bob@example.com'),
    ('Charlie Brown', '+111111111', 'charlie@example.com'),
    ('David Lee', '+222222222', 'david@example.com'),
    ('Emma Wilson', '+333333333', 'emma@example.com');

INSERT INTO Test (description, lab, sample_type, test_type, price)
VALUES
    ('Blood Test', 'medihelp', 'blood', 'instant', 75.00),
    ('Urine Test', 'medihelp', 'blood', 'instant', 50.00),
    ('X-Ray', 'medihelp', 'blood', 'instant', 150.00),
    ('MRI Scan', 'medihelp', 'blood', 'delayed', 300.00),
    ('ECG', 'medihelp', 'blood', 'delayed', 100.00);

INSERT INTO Report (cust_id, test_id,issue_date, pickup_date, result)
VALUES
    ('C0001','T0001', '2024-04-08', '2024-04-10', 'Normal'),
    ('C0002','T0003', '2024-04-09', '2024-04-11', 'Abnormal'),
    ('C0003','T0004', '2024-04-07', '2024-04-09', 'Normal'),
    ('C0004','T0005', '2024-04-08', '2024-04-10', 'Abnormal'),
    ('C0005','T0002', '2024-04-09', '2024-04-11', 'Normal');

INSERT INTO Prescription (patient_name, age, medical_officer_name, context, duration, date, cust_id)
VALUES
    ('Alice Johnson', 35, 'Dr. Smith', 'Fever', '5 days', '2024-04-08', 'C0001'),
    ('Bob Smith', 45, 'Dr. Williams', 'Pain', '7 days', '2024-04-09', 'C0002'),
    ('Charlie Brown', 25, 'Dr. Martin', 'Cold', '3 days', '2024-04-07', 'C0003'),
    ('David Lee', 50, 'Dr. Davis', 'Allergy', '10 days', '2024-04-08', 'C0004'),
    ('Emma Wilson', 40, 'Dr. Miller', 'Headache', '2 days', '2024-04-09', 'C0005');

INSERT INTO presc_test_detail (presc_id, test_id)
VALUES
    ('PR001', 'T0001'),
    ('PR002', 'T0002'),
    ('PR003', 'T0003'),
    ('PR004', 'T0004'),
    ('PR005', 'T0005');



TRUNCATE TABLE item_supplier_detail;

SELECT test_id, description,price, discount  FROM Test
UNION ALL
SELECT item_id, description,retail_price, discount FROM Item;

CREATE TABLE Temperature (
                             temp_id int PRIMARY KEY AUTO_INCREMENT,
                             date DATE NOT NULL,
                             time TIME NOT NULL,
                             temperature DECIMAL(5,2) NOT NULL
);


SELECT *
FROM order_test_detail
         JOIN order_item_detail ON order_test_detail.o_id = order_item_detail.o_id;

SELECT
    product_id,
    description,
    price,
    quantity,
    COALESCE(discount, 0.0) AS discount,
    discounted_price
FROM (
         SELECT
             order_item_detail.item_id AS product_id,
             Item.description AS description,
             Item.retail_price AS price,
             order_item_detail.qty AS quantity,
             COALESCE(Item.discount, 0.0) AS discount,
             COALESCE((order_item_detail.qty * Item.retail_price * (1 - COALESCE(Item.discount, 0.0) / 100)),
                      (order_item_detail.qty * Item.retail_price)) AS discounted_price
         FROM
             order_item_detail
                 JOIN Item ON order_item_detail.item_id = Item.item_id
         WHERE order_item_detail.o_id = 'O0001'

         UNION ALL

         SELECT
             order_test_detail.test_id AS product_id,
             Test.description AS description,
             Test.price AS price,
             order_test_detail.qty AS quantity,
             COALESCE(Test.discount, 0.0) AS discount,
             COALESCE((order_test_detail.qty * Test.price * (1 - COALESCE(Test.discount, 0.0) / 100)),
                      (order_test_detail.qty * Test.price)) AS discounted_price
         FROM
             order_test_detail
                 JOIN Test ON order_test_detail.test_id = Test.test_id
         WHERE order_test_detail.o_id = 'O0001'
     ) AS combined_data;

CREATE VIEW DailyProfitView AS
SELECT
        DATE(o.date) AS sale_date,
        SUM(oid.qty * (i.retail_price-i.whole_sale_price)) AS daily_profit
        FROM
        order_item_detail oid
        JOIN `Order` o ON oid.o_id = o.o_id
        JOIN Item i ON oid.item_id = i.item_id
        GROUP BY
        DATE(o.date);
