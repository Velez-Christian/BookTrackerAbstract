package com.example.librarybooktracker;

public class Regular extends Book {
    public Regular(int numberOfDays) {
        super(numberOfDays);
    }

    public int computePayment() {
        int totalRegularPayment;
        totalRegularPayment = 20 * getNumberOfDays();

        //setTotalPayment(totalRegularPayment);
        return totalRegularPayment;
    }
}
