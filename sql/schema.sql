CREATE TABLE credit_cards (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    dining_multiplier NUMERIC(5,2) NOT NULL,
    grocery_multiplier NUMERIC(5,2) NOT NULL,
    travel_multiplier NUMERIC(5,2) NOT NULL,
    gas_multiplier NUMERIC(5,2) NOT NULL,
    other_multiplier NUMERIC(5,2) NOT NULL
);