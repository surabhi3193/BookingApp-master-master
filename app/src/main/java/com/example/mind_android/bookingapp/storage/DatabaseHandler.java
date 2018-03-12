package com.example.mind_android.bookingapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mind_android.bookingapp.beans.Expense;
import com.example.mind_android.bookingapp.beans.ExpenseCat;
import com.example.mind_android.bookingapp.beans.Report;
import com.example.mind_android.bookingapp.beans.Sales;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.beans.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 13;

    // Database Name
    private static final String DATABASE_NAME = "usersManager";
    // Users table name
    private static final String TABLE_USERS = "users";
    private static final String TABLE_STOCKS = "stocks";
    private static final String TABLE_SALES = "sales";
    private static final String TABLE_EXPENSE = "expense";
    private static final String TABLE_REPORT = "report";
    private static final String TABLE_EXPENSE_CAT = "TABLE_EXPENSE_CAT";
    private static final String TABLE_LENDER_NAME = "TABLE_LENDER_NAME";
    private static final String TABLE_BANK_NAME = "TABLE_BANK_NAME";

    // Users Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_BUSINESS_NAME = "business_name";
    private static final String KEY_BUSINESS_LOC = "business_loc";
    private static final String KEY_BUSINESS_TYPE = "business_type";
    private static final String KEY_BUSINESS_EMAIL = "business_email";
    private static final String KEY_USER_IMAGE = "user_image";

    // Stocks Table Columns names
    private static final String KEY_STOCK_ID = "stock_id";
    private static final String KEY_STOCK_NAME = "stock_name";
    private static final String KEY_STOCK_QTY = "stock_qty";
    private static final String KEY_STOCK_PER_PRICE = "stock_per_price";
    private static final String KEY_STOCK_PRICE = "stock_price";
    private static final String KEY_STOCK_STATUS = "stock_status";
    private static final String KEY_STOCK_DATE = "stock_date";
    private static final String KEY_STOCK_TOTAL = "stock_total";

    // Sales Table Columns names
    private static final String KEY_SALES_ID = "sales_id";
    private static final String KEY_SALE_TYPE = "sale_type";
    private static final String KEY_SALES_NAME = "sales_name";
    private static final String KEY_SALES_QTY = "sales_qty";
    private static final String KEY_SALES_PER_PRICE = "sales_per_price";
    private static final String KEY_SALES_PRICE = "sale_price";
    private static final String KEY_SALES_STATUS = "sale_status";
    private static final String KEY_SALES_DATE = "sale_date";
    private static final String KEY_SALES_TOTAL = "sale_total";

    // Expense Table Columns names
    private static final String KEY_EXPENSE_ID = "expense_id";
    private static final String KEY_EXPENSE_NAME = "expense_name";
    private static final String KEY_EXPENSE_DATE = "expense_date";
    private static final String KEY_EXPENSE_PRICE = "expense_price";
    private static final String KEY_EXPENSE_STATUS = "expense_status";

    // Report Table Columns names
    private static final String KEY_TOTAL_SALE = "t_sale";
    private static final String KEY_TOTAL_STOCK = "t_stock";
    private static final String KEY_TOTAL_EXPENSE = "t_exp";
    private static final String KEY_TOTAL_OTHER_INCOME = "t_other";

    // Expense Categaory Table Columns names
    private static final String EXPENSE_CAT_NAME = "exp_cat_name";
    private static final String EXPENSE_CAT_STATUS = "exp_cat_status";
    private static final String LENDER_NAME = "lender";
    private static final String LENDER_NAME_STATUS = "lender_status";
    private static final String BANk_NAME = "bank_name";
    private static final String BANk_NAME_STATUS = "bank_name_status";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PH_NO + " TEXT," + KEY_USER_IMAGE + " TEXT," + KEY_BUSINESS_NAME + " TEXT," + KEY_BUSINESS_LOC + " TEXT," + KEY_BUSINESS_TYPE + " TEXT," + KEY_BUSINESS_EMAIL + " TEXT" + ")";
        String CREATE_STOCK_TABLE = "CREATE TABLE " + TABLE_STOCKS + "(" + KEY_STOCK_ID + " INTEGER PRIMARY KEY," + KEY_STOCK_NAME + " TEXT," + KEY_STOCK_QTY + " TEXT," + KEY_STOCK_PER_PRICE + " TEXT," + KEY_STOCK_PRICE + " TEXT," + KEY_STOCK_DATE + " TEXT," + KEY_STOCK_TOTAL + " TEXT," + KEY_STOCK_STATUS + " INTEGER" + ")";
        String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_SALES + "(" + KEY_SALES_ID + " INTEGER PRIMARY KEY," + KEY_SALES_NAME + " TEXT," + KEY_SALES_QTY + " TEXT," + KEY_SALES_PER_PRICE + " TEXT," + KEY_SALES_PRICE + " TEXT," + KEY_SALE_TYPE + " TEXT," + KEY_SALES_DATE + " TEXT," + KEY_SALES_TOTAL + " TEXT," + KEY_SALES_STATUS + " INTEGER" + ")";
        String CREATE_EXPENSE_TABLE = "CREATE TABLE " + TABLE_EXPENSE + "(" + KEY_EXPENSE_ID + " INTEGER PRIMARY KEY," + KEY_EXPENSE_NAME + " TEXT," + KEY_EXPENSE_DATE + " TEXT," + KEY_EXPENSE_PRICE + " TEXT," + KEY_EXPENSE_STATUS + " INTEGER" + ")";
        String CREATE_REPORT_TABLE = "CREATE TABLE " + TABLE_REPORT + "(" + KEY_TOTAL_SALE + " TEXT," + KEY_TOTAL_STOCK + " TEXT," + KEY_TOTAL_EXPENSE + " TEXT," + KEY_TOTAL_OTHER_INCOME + " TEXT" + ")";
        String CREATE_EXPENSE_CAT_TABLE = "CREATE TABLE " + TABLE_EXPENSE_CAT + "(" + EXPENSE_CAT_NAME + " TEXT PRIMARY KEY,"+ EXPENSE_CAT_STATUS + " INTEGER"+ ")";
        String CREATE_LENDER_NAME_TABLE = "CREATE TABLE " + TABLE_LENDER_NAME + "(" + LENDER_NAME + " TEXT PRIMARY KEY"+ ")";
        String CREATE_BANK_NAME_TABLE = "CREATE TABLE " + TABLE_BANK_NAME + "(" + BANk_NAME + " TEXT PRIMARY KEY"+ ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_STOCK_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
        db.execSQL(CREATE_EXPENSE_TABLE);
        db.execSQL(CREATE_REPORT_TABLE);
        db.execSQL(CREATE_EXPENSE_CAT_TABLE);
        db.execSQL(CREATE_LENDER_NAME_TABLE);
        db.execSQL(CREATE_BANK_NAME_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LENDER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK_NAME);

        // Create tables again
        onCreate(db);
    }

    /*
     * All CRUD FOR USER (Create, Read, Update, Delete) Operations
     */
    public void addExpenseCat(ExpenseCat expense_cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EXPENSE_CAT_NAME, expense_cat.get_name()); // User id
        values.put(EXPENSE_CAT_STATUS, expense_cat.get_status()); // User Name


        // Inserting Row
        db.insert(TABLE_EXPENSE_CAT, null, values);

        System.err.println(values);
        System.err.println("============== added cat =========");
        db.close(); // Closing database connection

    }
    public List<ExpenseCat> getAllExpenseCat() {
        List<ExpenseCat> expenseCats = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSE_CAT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ExpenseCat user = new ExpenseCat();
                user.set_name(cursor.getString(0));
                user.set_status(Integer.parseInt(cursor.getString(1)));

                // Adding user to list
                expenseCats.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenseCats;
    }
    public List<ExpenseCat> getAllExpense_CatsWith0() {
        List<ExpenseCat> stocklist = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_STOCKS;

        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE_CAT + " where " + EXPENSE_CAT_STATUS + "='" + 0 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ExpenseCat stock = new ExpenseCat();
                stock.set_name(cursor.getString(0));
                stock.set_status(Integer.parseInt(cursor.getString(1)));

                stocklist.add(stock);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return stocklist;
    }

    public boolean checkExpenseCat(String cat_name) {
        boolean isExist;
        SQLiteDatabase db = this.getWritableDatabase();
        System.err.println("=========== checking  cat_name ========");
        System.err.println(cat_name);
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE_CAT + " WHERE " + EXPENSE_CAT_NAME + "='" + cat_name + "'", null);
            if (c.moveToFirst()) {
                System.err.println("===========   stock exist========");
                isExist = true;
            } else {
                System.err.println("===========   stock does not exist========");
                isExist = false;
            }
            c.close();
            return isExist;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }


    }

/*
     * All CRUD FOR USER (Create, Read, Update, Delete) Operations
     */

    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getID()); // User id
        values.put(KEY_NAME, user.getName()); // User Name
        values.put(KEY_PH_NO, user.getPhoneNumber()); // User Phone
        values.put(KEY_BUSINESS_NAME, user.get_business_name()); // User business_name
        values.put(KEY_BUSINESS_LOC, user.get_business_loc()); // User business_loc
        values.put(KEY_BUSINESS_TYPE, user.get_business_type()); // User business  type
        values.put(KEY_BUSINESS_EMAIL, user.get_business_email()); // User business_email
        values.put(KEY_USER_IMAGE, user.get_user_image()); // User business_email

        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single user
//    public User getUser(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        User user = new User();
//        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID, KEY_NAME, KEY_PH_NO, KEY_USER_IMAGE, KEY_BUSINESS_NAME, KEY_BUSINESS_LOC,
//                        KEY_BUSINESS_TYPE, KEY_BUSINESS_EMAIL}, KEY_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//        if (cursor != null){
//            cursor.moveToFirst();
//         user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2)
//                , cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
//
//            cursor.close();
//    }
//        return user;
//    }

    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setPhoneNumber(cursor.getString(2));

                user.set_business_name(cursor.getString(3));
                user.set_business_loc(cursor.getString(4));
                user.set_business_type(cursor.getString(5));
                user.set_business_email(cursor.getString(6));
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

       cursor.close();
        return userList;
    }

    // Updating single user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getID());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PH_NO, user.getPhoneNumber());
        values.put(KEY_BUSINESS_NAME, user.get_business_name());
        values.put(KEY_BUSINESS_LOC, user.get_business_loc());
        values.put(KEY_BUSINESS_TYPE, user.get_business_type());
        values.put(KEY_BUSINESS_EMAIL, user.get_business_email());
        values.put(KEY_USER_IMAGE, user.get_user_image());

        // updating row
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getID())});
    }

    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getID())});
        db.close();
    }

    // Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    /**
     * All CRUD FOR STOCK (Create, Read, Update, Delete) Operations
     */


    public Stock getStock(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STOCKS, new String[]{KEY_STOCK_ID, KEY_STOCK_NAME, KEY_STOCK_QTY,
                        KEY_STOCK_PER_PRICE, KEY_STOCK_PRICE, KEY_STOCK_DATE, KEY_STOCK_TOTAL,
                        KEY_STOCK_STATUS}, KEY_STOCK_NAME + "=?",
                new String[]{String.valueOf(name)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Stock user = new Stock(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2)
                , cursor.getString(3), cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                Integer.parseInt(cursor.getString(7)));
        // return us
cursor.close();
return user;
    }

    public int getStocksCount() {
        System.err.println("===== stock count=========");
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_STOCKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        System.err.println(count);
        // return count
        return count;
    }

    public boolean checkStock(String stock_name) {
        boolean isExist;
        SQLiteDatabase db = this.getWritableDatabase();
        System.err.println("=========== checking  stock ========");
        System.err.println(stock_name);
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_STOCKS + " WHERE " + KEY_STOCK_NAME + "='" + stock_name + "'", null);
            if (c.moveToFirst()) {
                System.err.println("===========   stock exist========");
                isExist = true;
            } else {
                System.err.println("===========   stock does not exist========");
                isExist = false;
            }
            c.close();
            return isExist;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }


    }

    public void addStock(Stock stock) {
        System.err.println("=========== adding stock ========");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STOCK_ID, stock.get_id()); // ID
        values.put(KEY_STOCK_NAME, stock.get_name()); // NAME
        values.put(KEY_STOCK_QTY, stock.get_qty()); //QTY
        values.put(KEY_STOCK_PER_PRICE, stock.get_unit_per_price()); // PER UNIT PRICE
        values.put(KEY_STOCK_PRICE, stock.get_price()); // STOCK PRICE
        values.put(KEY_STOCK_DATE, stock.get_date()); // STOCK PRICE
        values.put(KEY_STOCK_TOTAL, stock.get_total_stock()); // STOCK PRICE
        values.put(KEY_STOCK_STATUS, stock.get_status()); // STOCK PRICE


        // Inserting Row
        db.insert(TABLE_STOCKS, null, values);
        db.close(); // Closing database connection
        System.err.println(values);
        System.err.println("===========  stock added  ========");
    }

    public List<Stock> getAllStocksExcept2() {
        List<Stock> stocklist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STOCKS + " where "
                + KEY_STOCK_STATUS + "!='" + 2 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                System.err.println("======cursor=====");
                System.err.println(cursor.getString(0));
                System.err.println(cursor.getString(1));
                System.err.println(cursor.getString(2));
                System.err.println(cursor.getString(3));
                System.err.println(cursor.getString(4));
                System.err.println(cursor.getString(5));
                System.err.println(cursor.getString(6));
                System.err.println(cursor.getString(7));

                Stock stock = new Stock();
                stock.set_id(Integer.parseInt(cursor.getString(0)));
                stock.set_name(cursor.getString(1));
                stock.set_qty(cursor.getString(2));
                stock.set_unit_per_price(cursor.getString(3));
                stock.set_price(cursor.getString(4));
                stock.set_date(cursor.getString(5));
                stock.set_total_stock(cursor.getString(6));
                stock.set_status(Integer.parseInt(cursor.getString(7)));
                // Adding user to list
                stocklist.add(stock);
            } while (cursor.moveToNext());
        }
cursor.close();
        return stocklist;
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocklist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STOCKS;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stock stock = new Stock();
                stock.set_id(Integer.parseInt(cursor.getString(0)));
                stock.set_name(cursor.getString(1));
                stock.set_qty(cursor.getString(2));
                stock.set_unit_per_price(cursor.getString(3));
                stock.set_price(cursor.getString(4));
                stock.set_date(cursor.getString(5));
                // Adding user to list
                stocklist.add(stock);
            } while (cursor.moveToNext());
        }

cursor.close();
        return stocklist;
    }

    public List<Stock> getAllStocksWith0() {
        List<Stock> stocklist = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_STOCKS;

        String selectQuery = "SELECT * FROM " + TABLE_STOCKS + " where " + KEY_STOCK_STATUS + "='" + 0 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stock stock = new Stock();
                stock.set_id(Integer.parseInt(cursor.getString(0)));
                stock.set_name(cursor.getString(1));
                stock.set_qty(cursor.getString(2));
                stock.set_unit_per_price(cursor.getString(3));
                stock.set_price(cursor.getString(4));
                stock.set_date(cursor.getString(5));
                stock.set_total_stock(cursor.getString(6));
                stock.set_status(Integer.parseInt(cursor.getString(7)));
                stocklist.add(stock);
            } while (cursor.moveToNext());
        }

cursor.close();
        return stocklist;
    }

    public List<Stock> getAllStocksWith3() {
        List<Stock> stocklist = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_STOCKS;

        String selectQuery = "SELECT * FROM " + TABLE_STOCKS + " where " + KEY_STOCK_STATUS + "='" + 3 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stock stock = new Stock();
                stock.set_id(Integer.parseInt(cursor.getString(0)));
                stock.set_name(cursor.getString(1));
                stock.set_qty(cursor.getString(2));
                stock.set_unit_per_price(cursor.getString(3));
                stock.set_price(cursor.getString(4));
                stock.set_date(cursor.getString(5));
                stock.set_total_stock(cursor.getString(6));
                stock.set_status(Integer.parseInt(cursor.getString(7)));
                stocklist.add(stock);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return stocklist;
    }

    public List<Stock> getAllStocksWith2() {
        List<Stock> stocklist = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STOCKS + " where " + KEY_STOCK_STATUS + "='" + 2 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stock stock = new Stock();
                stock.set_id(Integer.parseInt(cursor.getString(0)));
                stock.set_name(cursor.getString(1));
                stock.set_qty(cursor.getString(2));
                stock.set_unit_per_price(cursor.getString(3));
                stock.set_price(cursor.getString(4));
                stock.set_date(cursor.getString(5));
                stock.set_total_stock(cursor.getString(6));
                stock.set_status(Integer.parseInt(cursor.getString(7)));
                stocklist.add(stock);
            } while (cursor.moveToNext());
        }
cursor.close();
        return stocklist;
    }

    //    // Updating single user
    public void updateStock(Stock stock, String stock_id) {

        System.err.println("=========== updating stock ========" + stock.get_status());
        System.out.println("========== temp id ========== " + stock_id);
        System.out.println("========== server id ========== " + stock.get_id());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STOCK_ID, stock.get_id());
        values.put(KEY_STOCK_NAME, stock.get_name());
        values.put(KEY_STOCK_QTY, stock.get_qty());
        values.put(KEY_STOCK_PER_PRICE, stock.get_unit_per_price());
        values.put(KEY_STOCK_PRICE, stock.get_price());
        values.put(KEY_STOCK_DATE, stock.get_date()); // STOCK PRICE
        values.put(KEY_STOCK_TOTAL, stock.get_total_stock()); // STOCK PRICE
        values.put(KEY_STOCK_STATUS, stock.get_status());

        System.out.println("======= values ======");
        System.out.println(values);
        System.err.println("===========  stock updated========");
        // updating row
        if (stock_id.length() == 0) {
            System.out.println("======== tempo id is unavailable======" + stock_id);
            stock_id = String.valueOf(stock.get_id());
        }
        db.update(TABLE_STOCKS, values, KEY_STOCK_ID + " = ?",
                new String[]{stock_id});


    }

    // Deleting single user
    public void deleteStock(Stock stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STOCKS, KEY_STOCK_ID + " = ?",
                new String[]{String.valueOf(stock.get_id())});
        db.close();
    }

    public void deleteAllStocks() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE_STOCKS);

    }


    /**
     * All CRUD FOR SALES (Create, Read, Update, Delete) Operations
     */

    // Adding new sale
    public boolean checkSale(String stock_name) {
        boolean isExist;
        SQLiteDatabase db = this.getWritableDatabase();
        System.err.println("=========== checking  stock ========");
        System.err.println(stock_name);
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_STOCKS + " WHERE " + KEY_STOCK_NAME + "='" + stock_name + "'", null);
            if (c.moveToFirst()) {
                System.err.println("===========   stock exist========");
                isExist = true;
            } else {
                System.err.println("===========   stock does not exist========");
                isExist = false;
            }
            c.close();
            return isExist;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }


    }

    public int getSalesCount() {
        System.err.println("===== stock count=========");
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_SALES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        System.err.println(count);
        // return count
        return count;
    }


    public void addSales(Sales sales) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SALES_ID, sales.get_id()); // ID
        values.put(KEY_SALES_NAME, sales.get_name()); // NAME
        values.put(KEY_SALES_QTY, sales.get_qty()); //QTY
        values.put(KEY_SALES_PER_PRICE, sales.get_unit_per_price()); // PER UNIT PRICE
        values.put(KEY_SALES_PRICE, sales.get_price()); // STOCK PRICE
        values.put(KEY_SALE_TYPE, sales.get_sale_type()); // STOCK PRICE
        values.put(KEY_SALES_DATE, sales.get_date()); // STOCK PRICE
        values.put(KEY_SALES_TOTAL, sales.get_sale_total()); // STOCK PRICE
        values.put(KEY_SALES_STATUS, sales.get_status()); // STOCK PRICE

        // Inserting Row
        db.insert(TABLE_SALES, null, values);
        db.close(); // Closing database connection
    }

    public List<Sales> getAllSalesExcept2() {
        List<Sales> stocklist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SALES + " where "
                + KEY_SALES_STATUS + "!='" + 2 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales sales = new Sales();
                sales.set_id(Integer.parseInt(cursor.getString(0)));
                sales.set_name(cursor.getString(1));
                sales.set_qty(cursor.getString(2));
                sales.set_unit_per_price(cursor.getString(3));
                sales.set_price(cursor.getString(4));
                sales.set_sale_type(cursor.getString(5));
                sales.set_date(cursor.getString(6));
                sales.set_sale_total(cursor.getString(7));
                sales.set_status(Integer.parseInt(cursor.getString(8)));
                stocklist.add(sales);
            } while (cursor.moveToNext());
        }

cursor.close();
        return stocklist;
    }

    // Getting All Users
    public List<Sales> getAllSales() {
        List<Sales> stocklist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SALES;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales sales = new Sales();
                sales.set_id(Integer.parseInt(cursor.getString(0)));
                sales.set_name(cursor.getString(1));
                sales.set_qty(cursor.getString(2));
                sales.set_unit_per_price(cursor.getString(3));
                sales.set_price(cursor.getString(4));
                sales.set_sale_type(cursor.getString(5));
                sales.set_date(cursor.getString(6));
                sales.set_sale_total(cursor.getString(7));
                sales.set_status(Integer.parseInt(cursor.getString(8)));
                stocklist.add(sales);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stocklist;
    }

    // Getting All Stocks with 0

    public List<Sales> getAllSalesWith0() {
        List<Sales> stocklist = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_STOCKS;

        String selectQuery = "SELECT * FROM " + TABLE_SALES + " where " + KEY_SALES_STATUS + "='" + 0 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales sales = new Sales();
                sales.set_id(Integer.parseInt(cursor.getString(0)));
                sales.set_name(cursor.getString(1));
                sales.set_qty(cursor.getString(2));
                sales.set_unit_per_price(cursor.getString(3));
                sales.set_price(cursor.getString(4));
                sales.set_sale_type(cursor.getString(5));
                sales.set_date(cursor.getString(6));
                sales.set_sale_total(cursor.getString(7));
                sales.set_status(Integer.parseInt(cursor.getString(8)));
                stocklist.add(sales);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return stocklist;
    }

    public List<Sales> getAllSalesWith2() {
        List<Sales> stocklist = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_STOCKS;

        String selectQuery = "SELECT * FROM " + TABLE_SALES + " where " + KEY_SALES_STATUS + "='" + 2 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Sales sales = new Sales();
                sales.set_id(Integer.parseInt(cursor.getString(0)));
                sales.set_name(cursor.getString(1));
                sales.set_qty(cursor.getString(2));
                sales.set_unit_per_price(cursor.getString(3));
                sales.set_price(cursor.getString(4));
                sales.set_sale_type(cursor.getString(5));
                sales.set_date(cursor.getString(6));
                sales.set_sale_total(cursor.getString(7));
                sales.set_status(Integer.parseInt(cursor.getString(8)));
                stocklist.add(sales);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stocklist;
    }

    //    // Updating single user
    public void updateSales(Sales sales, String sales_id) {

        System.out.println("========== temp id ========== " + sales_id);
        System.out.println("========== server id ========== " + sales.get_id());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SALES_ID, sales.get_id());
        values.put(KEY_SALES_NAME, sales.get_name());
        values.put(KEY_SALES_QTY, sales.get_qty());
        values.put(KEY_SALES_PER_PRICE, sales.get_unit_per_price());
        values.put(KEY_SALES_PRICE, sales.get_price());
        values.put(KEY_SALE_TYPE, sales.get_sale_type());
        values.put(KEY_SALES_DATE, sales.get_date());
        values.put(KEY_SALES_TOTAL, sales.get_sale_total());
        values.put(KEY_SALES_STATUS, sales.get_status());

        System.out.println("======= values ======");
        System.out.println(values);
        // updating row
        db.update(TABLE_SALES, values, KEY_SALES_ID + " = ?",
                new String[]{sales_id});


    }

    // Deleting single user
    public void deleteStock(Sales sales) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SALES, KEY_SALES_ID + " = ?",
                new String[]{String.valueOf(sales.get_id())});
        db.close();
    }

    public void deleteAllSales() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE_SALES);

    }


    /**
     * All CRUD FOR EXPENSE (Create, Read, Update, Delete) Operations
     */

    // Adding new user
    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_ID, expense.get_id()); // ID
        values.put(KEY_EXPENSE_NAME, expense.get_name()); // NAME
        values.put(KEY_EXPENSE_DATE, expense.get_date()); //QTY
        values.put(KEY_EXPENSE_PRICE, expense.get_price()); // STOCK PRICE
        values.put(KEY_EXPENSE_STATUS, expense.get_status()); // STOCK PRICE

        // Inserting Row
        db.insert(TABLE_EXPENSE, null, values);
        db.close(); // Closing database connection
    }

    public List<Expense> getAllExpenseExcept2() {
        List<Expense> expenseList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE + " where "
                + KEY_EXPENSE_STATUS + "!='" + 2 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.set_id(Integer.parseInt(cursor.getString(0)));
                expense.set_name(cursor.getString(1));
                expense.set_date(cursor.getString(2));
                expense.set_price(cursor.getString(3));
                expense.set_status(Integer.parseInt(cursor.getString(4)));
                // Adding user to list
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }
    public int getExpensesCount() {
        System.err.println("===== stock count=========");
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_EXPENSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        System.err.println(count);
        // return count
        return count;
    }
    // Getting All Users
    public List<Expense> getAllExpense() {
        List<Expense> expenseList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.set_id(Integer.parseInt(cursor.getString(0)));
                expense.set_name(cursor.getString(1));
                expense.set_date(cursor.getString(2));

                expense.set_price(cursor.getString(3));
                expense.set_status(Integer.parseInt(cursor.getString(4)));
                expenseList.add(expense);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenseList;
    }

    // Getting All Stocks with 0

    public List<Expense> getAllExpenseWith0() {
        List<Expense> expenseList = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_STOCKS;

        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE + " where " + KEY_EXPENSE_STATUS + "='" + 0 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.set_id(Integer.parseInt(cursor.getString(0)));
                expense.set_name(cursor.getString(1));
                expense.set_date(cursor.getString(2));

                expense.set_price(cursor.getString(3));

                expense.set_status(Integer.parseInt(cursor.getString(4)));


                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenseList;
    }

    public List<Expense> getAllExpenseWith2() {
        List<Expense> expenseList = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_STOCKS;

        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE + " where " + KEY_EXPENSE_ID + "='" + 2 + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expense sales = new Expense();
                sales.set_id(Integer.parseInt(cursor.getString(0)));
                sales.set_name(cursor.getString(1));
                sales.set_date(cursor.getString(2));

                sales.set_price(cursor.getString(3));
                sales.set_status(Integer.parseInt(cursor.getString(4)));

                // Adding user to list
                expenseList.add(sales);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }

    //    // Updating single user
    public void updateExpense(Expense sales, String sales_id) {

        System.out.println("========== temp id ========== " + sales_id);
        System.out.println("========== server id ========== " + sales.get_id());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_ID, sales.get_id());
        values.put(KEY_EXPENSE_NAME, sales.get_name());
        values.put(KEY_EXPENSE_DATE, sales.get_date());
        values.put(KEY_EXPENSE_PRICE, sales.get_price());
        values.put(KEY_EXPENSE_STATUS, sales.get_status());

        System.out.println("======= values ======");
        System.out.println(values);
        // updating row
        db.update(TABLE_EXPENSE, values, KEY_EXPENSE_ID + " = ?",
                new String[]{sales_id});


    }

    // Deleting single user
    public void deleteExpense(Expense sales) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSE, KEY_EXPENSE_ID + " = ?",
                new String[]{String.valueOf(sales.get_id())});
        db.close();
    }

    public void deleteAllexpense() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + TABLE_EXPENSE);

    }


    public int getReportCount() {
        System.err.println("===== stock count=========");
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_REPORT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        System.err.println(count);
        // return count
        return count;
    }


    public void addReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL_SALE, report.getTotal_sale()); // ID
        values.put(KEY_TOTAL_STOCK, report.getTotal_stock()); // ID
        values.put(KEY_TOTAL_EXPENSE, report.getTotal_expense()); // ID
        values.put(KEY_TOTAL_OTHER_INCOME, report.getTotal_other_serice()); // ID


        // Inserting Row
        db.insert(TABLE_REPORT, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Report
    public List<Report> getAllReports() {
        List<Report> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REPORT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Report report = new Report();

                report.setTotal_sale(cursor.getString(0));
                report.setTotal_stock(cursor.getString(1));
                report.setTotal_expense(cursor.getString(2));
                report.setTotal_other_serice(cursor.getString(3));
                // Adding user to list
                userList.add(report);
            } while (cursor.moveToNext());
        }

cursor.close();
        return userList;
    }

    //    // Updating single user
    public void updateReport(Report report) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL_SALE, report.getTotal_sale());
        values.put(KEY_TOTAL_STOCK, report.getTotal_stock());
        values.put(KEY_TOTAL_EXPENSE, report.getTotal_expense());
        values.put(KEY_TOTAL_OTHER_INCOME, report.getTotal_other_serice());

        System.out.println("======= values ======");
        System.out.println(values);
        // updating row

        String where = "rowid=(SELECT MIN(rowid) FROM " + TABLE_REPORT + ")";

        db.update(TABLE_REPORT, values, where, null);


    }

}