USE bus_pass_assignment5;
SELECT COUNT(*) AS bus_pass_count FROM bus_pass;
SELECT * FROM bus_pass ORDER BY pass_id;
SELECT pass_id, register_number, name, route_number, boarding_stop, validity_date, amount_paid
FROM bus_pass
WHERE route_number='R12';
SELECT * FROM users;
