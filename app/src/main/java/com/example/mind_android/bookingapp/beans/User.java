package com.example.mind_android.bookingapp.beans;

public class User {//private variables
    int _id;
    String _name;
    String _phone_number;
    String _business_name;
    String _business_loc;
    String _business_type;
    String _business_email;
    String _user_image;

    // Empty constructor
    public User() {

    }

    // constructor
    public User(int id, String name, String _phone_number,String _business_name
            ,String _business_loc,String _business_type,String _business_email,String _user_image) {
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._business_name = _business_name;
        this._business_loc = _business_loc;
        this._business_type = _business_type;
        this._business_email = _business_email;
        this._user_image = _user_image;
    }

    // constructor
    public User(String name, String _phone_number) {
        this._name = name;
        this._phone_number = _phone_number;
    }

    public String get_user_image() {
        return _user_image;
    }

    public void set_user_image(String _user_image) {
        this._user_image = _user_image;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting phone number
    public String getPhoneNumber() {
        return this._phone_number;
    }

    // setting phone number
    public void setPhoneNumber(String phone_number) {
        this._phone_number = phone_number;

    }

    public String get_business_name() {
        return _business_name;
    }

    public void set_business_name(String _business_name) {
        this._business_name = _business_name;
    }

    public String get_business_loc() {
        return _business_loc;
    }

    public void set_business_loc(String _business_loc) {
        this._business_loc = _business_loc;
    }

    public String get_business_type() {
        return _business_type;
    }

    public void set_business_type(String _business_type) {
        this._business_type = _business_type;
    }

    public String get_business_email() {
        return _business_email;
    }

    public void set_business_email(String _business_email) {
        this._business_email = _business_email;
    }
}
