DROP DATABASE MedplusCarePharmacy;

CREATE DATABASE MedplusCarePharmacy;

USE MedplusCarePharmacy;

CREATE TABLE Item (
                      item_id INT PRIMARY KEY AUTO_INCREMENT,
                      description VARCHAR(255),
                      qty INT,
                      whole_sale_price DECIMAL(10, 2),
                      retail_price DECIMAL(10, 2),
                      discount DECIMAL(5, 2),
                      exp_date DATE
);

CREATE TABLE Supplier (
                          sp_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255),
                          contact INT,
                          email VARCHAR(100)
);

CREATE TABLE item_supplier_detail (
                                      sp_id INT,
                                      item_id INT,
                                      FOREIGN KEY (sp_id) REFERENCES Supplier(sp_id) ON DELETE SET NULL,
                                      FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE SET NULL
);

CREATE TABLE `Order` (
                        o_id INT PRIMARY KEY AUTO_INCREMENT,
                        qty INT,
                        total DECIMAL(10, 2)
);

CREATE TABLE order_item_detail (
                                   o_id INT,
                                   item_id INT,
                                   FOREIGN KEY (o_id) REFERENCES `Order`(o_id) ON DELETE SET NULL,
                                   FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE SET NULL
);

CREATE TABLE User (
                      usr_id INT PRIMARY KEY AUTO_INCREMENT,
                      usr_name VARCHAR(255),
                      password VARCHAR(255)
);

CREATE TABLE Employee (
                          emp_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255),
                          position VARCHAR(20),
                          address VARCHAR(255),
                          contact_no VARCHAR(20),
                          salary DECIMAL(10, 2),
                          usr_id INT,
                          FOREIGN KEY (usr_id) REFERENCES `User`(usr_id) ON DELETE SET NULL
);

CREATE TABLE Payment (
                         pay_id INT PRIMARY KEY AUTO_INCREMENT,
                         cash DECIMAL(10, 2),
                         balance DECIMAL(10, 2),
                         date DATE,
                         usr_id INT,
                         o_id INT,
                         sp_id INT,
                         FOREIGN KEY (usr_id) REFERENCES User(usr_id) ON DELETE SET NULL,
                         FOREIGN KEY (o_id) REFERENCES `Order`(o_id) ON DELETE SET NULL,
                         FOREIGN KEY (sp_id) REFERENCES Supplier(sp_id) ON DELETE SET NULL
);

CREATE TABLE Test (
                      test_id INT AUTO_INCREMENT PRIMARY KEY,
                      description VARCHAR(255) NOT NULL,
                      lab VARCHAR(40) NOT NULL,
                      sample_type VARCHAR(10) NOT NULL,
                      test_type VARCHAR(20) NOT NULL,
                      price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Report (
                        r_id INT AUTO_INCREMENT PRIMARY KEY,
                        type VARCHAR(255) NOT NULL,
                        issue_date DATE NOT NULL,
                        pickup_date DATE,
                        result VARCHAR(255),
                        test_id INT,
                        FOREIGN KEY (test_id) REFERENCES Test(test_id) ON DELETE SET NULL
);

CREATE TABLE Customer (
                          cust_id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          contact_no INT,
                          email VARCHAR(255)

);

CREATE TABLE Prescription (
                              presc_id INT AUTO_INCREMENT PRIMARY KEY,
                              patient_name VARCHAR(255) NOT NULL,
                              age INT NOT NULL,
                              medical_officer_name VARCHAR(255) NOT NULL,
                              context VARCHAR(255),
                              duration VARCHAR(50),
                              date DATE NOT NULL,
                              cust_id INT,
                              FOREIGN KEY (cust_id) REFERENCES Customer(cust_id) ON DELETE SET NULL
);

CREATE TABLE presc_test_detail (
                                   presc_id INT,
                                   test_id INT,
                                   FOREIGN KEY (presc_id) REFERENCES Prescription(presc_id) ON DELETE SET NULL,
                                   FOREIGN KEY (test_id) REFERENCES Test(test_id) ON DELETE SET NULL
);

CREATE TABLE test_pay_detail (
                                 pay_id INT,
                                 test_id INT,
                                 FOREIGN KEY (pay_id) REFERENCES Payment(pay_id) ON DELETE SET NULL,
                                 FOREIGN KEY (test_id) REFERENCES Test(test_id) ON DELETE SET NULL
);



