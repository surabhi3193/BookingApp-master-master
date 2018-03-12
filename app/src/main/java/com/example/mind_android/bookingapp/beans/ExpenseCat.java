package com.example.mind_android.bookingapp.beans;

public class ExpenseCat {


    String _name;
    int _status;

    // Empty constructor
    public ExpenseCat() {
    }

    // constructor
    public ExpenseCat( String name,int _status) {

        this._name = name;
        this._status = _status;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_status() {
        return _status;
    }

    public void set_status(int _status) {
        this._status = _status;
    }
}
