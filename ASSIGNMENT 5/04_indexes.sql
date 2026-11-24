USE bus_pass_assignment5;
CREATE INDEX IF NOT EXISTS idx_bus_pass_route ON bus_pass(route_number);
CREATE INDEX IF NOT EXISTS idx_bus_pass_register ON bus_pass(register_number);
