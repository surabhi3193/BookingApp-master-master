package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.beans.Expense;
import com.example.mind_android.bookingapp.beans.ExpenseCat;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addExpense;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addStock;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class ExpenseForm_Activity extends AppCompatActivity {
    String expense_id = "";
    List<String> categList;
    Spinner spinner;
    EditText otherET, descET, priceEt;
    TextView dateTV;
    private String method_type = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_form_);
        spinner = findViewById(R.id.spinner);

        otherET = findViewById(R.id.otherET);
        descET = findViewById(R.id.descET);
        dateTV = findViewById(R.id.dateTV);
        priceEt = findViewById(R.id.priceEt);

        Button add_btn = findViewById(R.id.add_btn);
        ImageView back_btn = findViewById(R.id.back_btn);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dateTV, myCalendar);
            }

        };

        dateTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ExpenseForm_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            method_type = bundle.getString("method_type");

            assert method_type != null;
            if (method_type.equals("2"))
            {
                expense_id = bundle.getString("expanse_id");
               String expense_desc = bundle.getString("expense_desc");
               String expanse_date = bundle.getString("expanse_date");

                descET.setText(expense_desc);
                dateTV.setText(expanse_date);
                priceEt.setText(bundle.getString("expanse_amount"));
                add_btn.setText(R.string.save);

                if (isNetworkAvailable(ExpenseForm_Activity.this))
                    getExpenseType(2, bundle.getString("expanse_name"));
                else
                    getExpenseTypeFromLocal(2, bundle.getString("expanse_name"));
            }


            else {
                add_btn.setText(R.string.addexpense);

                if (isNetworkAvailable(ExpenseForm_Activity.this)) {
//                    addUnregisterCat();
                    getExpenseType(1, "");
                }
                else {
                    getExpenseTypeFromLocal(1, "");

                }



            }

        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = spinner.getSelectedItem().toString();
                System.out.println("==selected cat =======");
                System.out.println(item);

                if (item.equalsIgnoreCase("OTHER")) {
                    otherET.setVisibility(View.VISIBLE);
                } else {
                    otherET.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expType = "";
                String cat = spinner.getSelectedItem().toString();
                String desc = descET.getText().toString();
                String date = dateTV.getText().toString();
                String price = priceEt.getText().toString();

                if (cat.equalsIgnoreCase("Select Category")) {
                    Toast.makeText(ExpenseForm_Activity.this, " Please select expense category", Toast.LENGTH_SHORT).show();
                }
                if (otherET.getVisibility() == View.VISIBLE) {
                    expType = otherET.getText().toString();
                    System.out.println("======== other category name ===== " + expType);
                    if (expType.length() < 2) {
                        otherET.setError("Field Required");
                    }
                }
                if (desc.length() == 0) {
                    descET.setError("Field Required");
                }
                if (date.length() == 0)
                    dateTV.setError("Field Required");

                if (price.length() == 0)
                    priceEt.setError("Field Required");

                if (otherET.getVisibility() == View.VISIBLE) {
                    if (desc.length() != 0
                            && price.length() != 0
                            && date.length() != 0
                            && expType.length() != 0
                            && !cat.equalsIgnoreCase("Select Category")
                            ) {
                        String user_id = getData(ExpenseForm_Activity.this, "user_id", "");

                        if (isNetworkAvailable(ExpenseForm_Activity.this))
                            addExpense(ExpenseForm_Activity.this, user_id, cat, expType, desc, date,
                                    price, method_type, expense_id, "");

                        else {

                            cat=expType.toUpperCase();
                            cat=cat.toUpperCase();
                            addExpenseinLocal(cat, date, price, method_type, expense_id);

                        }
                    }
                } else {
                    if (desc.length() != 0
                            && price.length() != 0
                            && date.length() != 0
                            && !cat.equalsIgnoreCase("Select Category")
                            ) {
                        dateTV.setError(null);
                        descET.setError(null);
                        priceEt.setError(null);
                        String user_id = getData(ExpenseForm_Activity.this, "user_id", "");

                        if (isNetworkAvailable(ExpenseForm_Activity.this))
                            addExpense(ExpenseForm_Activity.this, user_id, expType, cat, desc, date,
                                    price, method_type, expense_id, "");

                        else {

                            addExpenseinLocal(cat, date, price, method_type, expense_id);

                        }
                    }
                }


            }
        });
    }

//    private void addUnregisterCat() {
//        List<Expense> stocks ;
//        DatabaseHandler db = new DatabaseHandler(ExpenseForm_Activity.this);
//        stocks = db.getAllExpenseWith0();
//        System.out.println("================ cat with 0 status ========");
//        System.out.println(stocks);
//        String user_id = getData(ExpenseForm_Activity.this,"user_id","");
//        for (Expense cn : stocks)
//        {
//            String log = "Name: " + cn.get_name() +
//                    " ,status : " + cn.get_status() ;
//
//            // Writing Contacts to log
//            Log.d("ExpenseCat: ", log);
//
//
//        }
//    }

    private void getExpenseTypeFromLocal(int s, String expanse_name) {
        categList = new ArrayList<>();
        if (s == 2) {
            categList.add(expanse_name);
            otherET.setVisibility(View.GONE);
        }
        else {
            DatabaseHandler db = new DatabaseHandler(ExpenseForm_Activity.this);


            Log.d("Reading: ", "Reading all EXPENSE CAT..");
            List<ExpenseCat> stocks = db.getAllExpenseCat();
            JSONArray jArray = new JSONArray();
            try {
                for (ExpenseCat cn : stocks) {
                    JSONObject jobj = new JSONObject();
                    String log = " ,Name: " + cn.get_name() +
                            " ,status : " + cn.get_status();
                    // Writing Contacts to log
                    Log.d("Name: ", log);

                    jobj.put("expenses_name", cn.get_name());
                    jArray.put(jobj);
                }

                categList.add("Select Category");
                for (int i = 0; i < jArray.length(); i++) {
                    String expenses_name = jArray.getJSONObject(i).getString("expenses_name");

                    categList.add(expenses_name);
                }
                categList.add("OTHER");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                ExpenseForm_Activity.this, R.layout.spinner_item, categList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }

    private void updateLabel(TextView textEdit, Calendar myCalendar) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textEdit.setText(sdf.format(myCalendar.getTime()));
    }

    private void getExpenseType(final int s, final String expanse_name) {

        System.out.println(" ************* expense list api ***");
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final String bk_userid = getData(ExpenseForm_Activity.this, "user_id", "");
        params.put("bk_userid", bk_userid);
        System.out.println(params);

        client.post(BASE_URL_NEW + "expenses_type", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* all stock response ***");
                System.out.println(response);
                try {

                    if (!response.getString("status").equals("0")) {

                        categList = new ArrayList<>();
                        if (s == 2) {
                            categList.add(expanse_name);
                            otherET.setVisibility(View.GONE);
                        }
                        else {
                            JSONArray jArray = response.getJSONArray("expenses");
                            categList.add("Select Category");
                            for (int i = 0; i < jArray.length(); i++) {
                                String expenses_name = jArray.getJSONObject(i).getString("expenses_name");

                                categList.add(expenses_name);
                            }
                            categList.add("OTHER");

                        }

                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                                ExpenseForm_Activity.this, R.layout.spinner_item, categList);

                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner.setAdapter(spinnerArrayAdapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });

    }

    private void addExpenseinLocal(final String stock_name,
                                   final String date, final
                                   String stock_amount, final String method_type,
                                   final String stock_id) {
        DatabaseHandler db = new DatabaseHandler(ExpenseForm_Activity.this);

        if (method_type.equals("1")) {
            Log.d("Insert: ", "Inserting .. Stock");
            Long tsLong = System.currentTimeMillis() / 1000;
            int id = Integer.parseInt(tsLong.toString());

            boolean isExist = db.checkExpenseCat(stock_name);

            if (!isExist)
                db.addExpenseCat(new ExpenseCat(stock_name.toUpperCase(),0));


            db.addExpense(new Expense(id, stock_name, date, stock_amount, 0));
            onBackPressed();

        } else if (method_type.equals("2")) {
            int id = Integer.parseInt(stock_id);
            Log.d("Update: ", "Updating .. Stock");
            db.updateExpense(new Expense(id, stock_name, date, stock_amount, 0), stock_id);
            onBackPressed();
        }
    }

}

