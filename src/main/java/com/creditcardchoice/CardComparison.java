package com.creditcardchoice;

public record CardComparison(String name, double monthlyPoints) {
    public CardComparison {
        monthlyPoints = Math.floor(monthlyPoints);
    }
}
