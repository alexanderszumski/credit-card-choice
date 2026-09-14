ALTER TABLE credit_cards
ADD CONSTRAINT nonnegative_multipliers
CHECK (
    dining_multiplier >= 0
    AND grocery_multiplier >= 0
    AND travel_multiplier >= 0
    AND gas_multiplier >= 0
    AND other_multiplier >= 0
);