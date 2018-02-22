package com.example.mind_android.bookingapp.beans;

public class Expense {

    int _id;
    String _name;
    String _date;
    String _price;
    int _status;

    // Empty constructor
    public Expense() {
    }

    // constructor
    public Expense(int id, String name,String _date, String _price, int _status) {
        this._id = id;
        this._name = name;
        this._date = _date;
        this._price = _price;
        this._status = _status;
    }

    // constructor
    public Expense(String name, String _price) {
        this._name = name;
        this._price = _price;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_price() {
        return _price;
    }

    public void set_price(String _price) {
        this._price = _price;
    }

    public int get_status() {
        return _status;
    }

    public void set_status(int _status) {
        this._status = _status;
    }
}
