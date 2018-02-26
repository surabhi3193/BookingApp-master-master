package com.example.mind_android.bookingapp.beans;

public class TransectionSummary  {
    
        private String
                title,
                qty,
                per_unit,
                total_amt,
                type,
                date;

        public TransectionSummary() {
        }

        public TransectionSummary(String title, String qty, String per_unit, String total_amt,
                                  String type, String date) {
            this.title = title;
            this.qty = qty;
            this.per_unit = per_unit;
            this.total_amt = total_amt;
            this.type = type;
            this.date = date;
        }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPer_unit() {
        return per_unit;
    }

    public void setPer_unit(String per_unit) {
        this.per_unit = per_unit;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
