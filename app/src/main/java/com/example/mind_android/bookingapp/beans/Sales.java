package com.example.mind_android.bookingapp.beans;

public class Sales {

    int _id;
    String _name;
    String _qty;
    String _unit_per_price;
    String _price;
    int _status;

    // Empty constructor
    public Sales() {
    }

    // constructor
    public Sales(int id, String name, String _qty, String _unit_per_price
            , String _price, int _status) {
        this._id = id;
        this._name = name;
        this._qty = _qty;
        this._unit_per_price = _unit_per_price;
        this._price = _price;
        this._status = _status;
    }

    // constructor
    public Sales(String name, String _price) {
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

    public String get_qty() {
        return _qty;
    }

    public void set_qty(String _qty) {
        this._qty = _qty;
    }

    public String get_unit_per_price() {
        return _unit_per_price;
    }

    public void set_unit_per_price(String _unit_per_price) {
        this._unit_per_price = _unit_per_price;
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
