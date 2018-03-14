package com.example.mind_android.bookingapp.beans;

public class BankTransaction {

        private String
                id,
                title,
                total_amt,
                trans_type,
                closing_amount,
                date;

        public BankTransaction() {
        }

        public BankTransaction(String id,String title, String total_amt,
                               String trans_type,String closing_amount
                               ,String date) {
            this.id = id;
            this.title = title;
            this.total_amt = total_amt;
            this.trans_type = trans_type;
            this.closing_amount = closing_amount;
            this.date = date;
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClosing_amount() {
        return closing_amount;
    }

    public void setClosing_amount(String closing_amount) {
        this.closing_amount = closing_amount;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
