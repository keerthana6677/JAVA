USE bus_pass_assignment5;

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS bus_pass (
    pass_id VARCHAR(20) PRIMARY KEY,
    register_number VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    route_number VARCHAR(20) NOT NULL,
    boarding_stop VARCHAR(100) NOT NULL,
    validity_date DATE NOT NULL,
    amount_paid DECIMAL(10,2) NOT NULL
) ENGINE=InnoDB;
