package com.example.mind_android.bookingapp.beans;

public class Report {

    private String total_sale;
    private String total_stock;
    private String total_expense;
    private String total_other_serice;

    // Empty constructor
    public Report() {
    }

    // constructor
    public Report(String total_sale, String total_stock, String total_expense, String total_other_serice) {
        this.total_sale = total_sale;
        this.total_stock = total_stock;
        this.total_expense = total_expense;
        this.total_other_serice = total_other_serice;

    }

    public String getTotal_sale() {
        return total_sale;
    }

    public void setTotal_sale(String total_sale) {
        this.total_sale = total_sale;
    }

    public String getTotal_stock() {
        return total_stock;
    }

    public void setTotal_stock(String total_stock) {
        this.total_stock = total_stock;
    }

    public String getTotal_expense() {
        return total_expense;
    }

    public void setTotal_expense(String total_expense) {
        this.total_expense = total_expense;
    }

    public String getTotal_other_serice() {
        return total_other_serice;
    }

    public void setTotal_other_serice(String total_other_serice) {
        this.total_other_serice = total_other_serice;
    }
}
