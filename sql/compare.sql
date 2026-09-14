SELECT
    name,
    dining_multiplier * 600 + grocery_multiplier * 400 + travel_multiplier * 200 + gas_multiplier * 150 + other_multiplier * 500 AS cumulative_points
FROM credit_cards
ORDER BY cumulative_points DESC;