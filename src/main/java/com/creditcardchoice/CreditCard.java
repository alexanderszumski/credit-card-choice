package com.creditcardchoice;
public class CreditCard {
    private String name;
    private double dining;
    private double grocery;
    private double travel;
    private double gas;
    private double other;

    public CreditCard(String name, double dining, double grocery, double travel, double gas, double other){
        this.name = name;
        this.dining = dining;
        this.grocery = grocery;
        this.travel = travel;
        this.gas = gas;
        this.other = other;
    }
    public String getName() {
        return name;
    }

    public double getDining() {
        return dining;
    }

    public double getGrocery() {
        return grocery;
    }

    public double getTravel() {
        return travel;
    }

    public double getGas() {
        return gas;
    }

    public double getOther() {
        return other;
    }
    public double calculateMonthlyPoints(double dining, double grocery, double travel, double gas, double other){
        return (dining * getDining()) + (grocery * getGrocery()) + (travel * getTravel()) + (gas * getGas()) + (other * getOther());
    }
}
