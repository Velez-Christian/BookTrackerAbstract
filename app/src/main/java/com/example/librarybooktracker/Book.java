package com.example.librarybooktracker;

abstract class Book {

    private int numberOfDays;
    private int totalPayment;
    private  int rate = 20;

    public Book() {
        this.numberOfDays = 0;
    }

    //public Book(int numberOfDays) {
    //    this.numberOfDays = 0;
    //}

    public Book(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    abstract public int computePayment();

    public int getNumberOfDays() {
        return numberOfDays;
    }

    //public void setTotalPayment(int totalPayment){
        //this.totalPayment = totalPayment;
    //}



    //public int computePayment() {
       // this.totalPayment = numberOfDays * rate;
       // return 0;
    //}

    //public int returnPayment() {
    //    return totalPayment;
   //}
}
