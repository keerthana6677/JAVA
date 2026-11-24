USE bus_pass_assignment5;

DELETE FROM bus_pass;
INSERT INTO bus_pass VALUES
('P1001','192411101','Arun Kumar','R12','Main Gate','2027-06-30',450.00),
('P1003','192411103','Bhargavi N.','R12','Engineering Block','2027-12-31',500.00),
('P1004','192411104','Divya','R09','Main Gate','2027-08-15',450.00);

INSERT INTO users(username,password)
VALUES ('bhargavi','pass123'), ('admin','admin123')
ON DUPLICATE KEY UPDATE password=VALUES(password);
