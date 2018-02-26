package com.example.mind_android.bookingapp.beans;

public class LoanSummary {
    
        private String
                lender_id,
                lender,
                interest,
                total_month,
                total_amt,
                total_remain_amount,
                total_paid_amount,
                taken_amount,
                type,
                date;

        public LoanSummary() {
        }

        public LoanSummary(String lender_id,String lender, String taken_amount,String interest, String total_month,String total_remain_amount,String total_paid_amount, String total_amt,
                           String type, String date) {
            this.lender = lender;
            this.interest = interest;
            this.total_month = total_month;
            this.total_amt = total_amt;
            this.type = type;
            this.date = date;
            this.total_remain_amount = total_remain_amount;
            this.total_paid_amount = total_paid_amount;
            this.taken_amount = taken_amount;
            this.lender_id = lender_id;
        }

    public String getLender_id() {
        return lender_id;
    }

    public void setLender_id(String lender_id) {
        this.lender_id = lender_id;
    }

    public String getTaken_amount() {
        return taken_amount;
    }

    public void setTaken_amount(String taken_amount) {
        this.taken_amount = taken_amount;
    }

    public String getlender() {
        return lender;
    }

    public void setlender(String lender) {
        this.lender = lender;
    }

    public String getinterest() {
        return interest;
    }

    public void setinterest(String interest) {
        this.interest = interest;
    }

    public String gettotal_month() {
        return total_month;
    }

    public void settotal_month(String total_month) {
        this.total_month = total_month;
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


    public String getTotal_remain_amount() {
        return total_remain_amount;
    }

    public void setTotal_remain_amount(String total_remain_amount) {
        this.total_remain_amount = total_remain_amount;
    }

    public String getTotal_paid_amount() {
        return total_paid_amount;
    }

    public void setTotal_paid_amount(String total_paid_amount) {
        this.total_paid_amount = total_paid_amount;
    }
}
