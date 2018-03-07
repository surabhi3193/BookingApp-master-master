package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.example.mind_android.bookingapp.activities.EnterLoginActivity;
import com.example.mind_android.bookingapp.activities.LoginActivity;
import com.example.mind_android.bookingapp.adapter.StockAdapter;
import com.example.mind_android.bookingapp.beans.Expense;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addExpense;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addStock;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.deleteExpense;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.deleteStock;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class ExpenceActivity extends BaseActivity {
    String total_amt = "0", price_unit = "0";
    ListView stocklist;
    private int count;
    private LinearLayout listLayout;
    private ScrollView scrollview;
    private static TextView total_amtTv;
    List<Expense> expense;
    DatabaseHandler db;
    private String user_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_expence, frameLayout);

        db = new DatabaseHandler(ExpenceActivity.this);
        user_id =getData(ExpenceActivity.this,"user_id","");

        scrollview = findViewById(R.id.scrollview);
        listLayout = findViewById(R.id.listLayout);
        stocklist = findViewById(R.id.stocklist);
        Button add_stock = findViewById(R.id.add_btn);
        EditText item_nameEt = findViewById(R.id.item_nameTv);
        EditText item_qtEt = findViewById(R.id.itemQuant_TV);
        EditText itemPriceEt = (EditText) findViewById(R.id.item_price_tv);
        EditText itemUnitPriceEt = findViewById(R.id.item_price_unit);
        total_amtTv = findViewById(R.id.total_amtTv);

        TextView addTv = findViewById(R.id.addTv);



        Button reset_lay =findViewById(R.id.reset_lay);

        reset_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetExpensesWarning();

            }
        });

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpenceActivity.this,
                        ExpenseForm_Activity.class).putExtra("method_type","1")
                );

            }
        });

        TextView signout_btn = findViewById(R.id.signout_btn);

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(getApplicationContext(), "login", "0");

                startActivity(new Intent(ExpenceActivity.this,LoginActivity.class));
                finishAffinity();
            }
        });


        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    public void resetExpensesWarning() {
        AlertDialog.Builder ab = new AlertDialog.Builder
                (ExpenceActivity.this, R.style.MyAlertDialogStyle1);
        ab.setTitle("Reset").setIcon(R.drawable.reset);
        ab.setMessage("Are you sure ? ");
        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              resetExpanse();
                dialog.dismiss();
            }
        });

        ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();

    }


    public void showExpense() {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(ExpenceActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);

        String user_id = getData(ExpenceActivity.this, "user_id", "");

        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "expanses_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show stock  response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0"))
                    {
                        scrollview.setVisibility(View.GONE);
                        stocklist.setVisibility(View.GONE);
                        total_amtTv.setText("0.00");
                        Toast.makeText(ExpenceActivity.this,
                                response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        scrollview.setVisibility(View.GONE);
                        listLayout.setVisibility(View.VISIBLE);
                        stocklist.setVisibility(View.VISIBLE);

                        String total_amt = response.getString("total");
                        total_amtTv.setText(total_amt);

                        JSONArray jArray = response.getJSONArray("expanses");
                        StockAdapter stockListAdapter = new StockAdapter(ExpenceActivity.this, jArray,"expense");
                        stocklist.setAdapter(stockListAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }


    private void resetExpanse()
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(ExpenceActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);
        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "clear_expenses", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show stock  response ***");
                System.out.println(response);
                try {

                    if (response.getString("status").equals("0")) {
                        stocklist.setVisibility(View.GONE);
                        total_amtTv.setText("0.00");
//                        Toast.makeText(StockActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        stocklist.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
              ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllExpense();
    }

    @Override
    public void onBackPressed() {
        if (scrollview.getVisibility() == View.VISIBLE) {
            scrollview.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
            finish();
        }
    }



    public void showAllExpense() {
        if (isNetworkAvailable(ExpenceActivity.this)) {

            addUnregisteredExpense();
            deleteExpenseFromServer();
           showExpense();

        } else {
            showExpenseFronLocal();
        }
    }

    private void deleteExpenseFromServer() {
        expense = db.getAllExpenseWith2();
        System.out.println("================ stocks with 2 status ========");
        System.out.println(expense);
        for (Expense cn : expense)

        {
            String log = "Id: " + cn.get_id() +
                    " ,Name: " + cn.get_name() +
                    " ,qty: " + cn.get_date() +
                    " ,status : " + cn.get_status() +
                    " ,price : " + cn.get_price();
            // Writing Contacts to log
            Log.d("Name: ", log);
            deleteExpense(ExpenceActivity.this, cn);
        }

    }

    private void addUnregisteredExpense() {
        expense = db.getAllExpenseWith0();
        System.out.println("================ stocks with 0 status ========");
        System.out.println(expense);
        for (Expense cn : expense)

        {
            String log = "Id: " + cn.get_id() +
                    " ,Name: " + cn.get_name() +
                    " ,qty: " + cn.get_date() +
                    " ,status : " + cn.get_status() +
                    " ,price : " + cn.get_price();
            // Writing Contacts to log
            Log.d("Name: ", log);
//            addExpense(ExpenceActivity.this,user_id,cn.get_name(),cn.get_price(),"1",
//                    String.valueOf(cn.get_id()),"local");
        }
    }

    private void showExpenseFronLocal() {

        DatabaseHandler db = new DatabaseHandler(ExpenceActivity.this);
        JSONArray jArray = new JSONArray();
        double total = 0.00;
        // Reading all contacts
        Log.d("Reading: ", "Reading all Expense..");
        List<Expense> stocks = db.getAllExpenseExcept2();
        try {
            for (Expense cn : stocks) {
                JSONObject jobj = new JSONObject();

                String log = "Id: " + cn.get_id() +
                        " ,Name: " + cn.get_name() +
                        " ,qty: " + cn.get_date() +
                        " ,price : " + cn.get_price();
                // Writing Contacts to log
                Log.d("Name: ", log);


                jobj.put("expanse_id", cn.get_id());
                jobj.put("expanse_name", cn.get_name());
                jobj.put("expanse_date", cn.get_date());
                jobj.put("expanse_amount", cn.get_price());

                jArray.put(jobj);
                double price = Double.parseDouble(cn.get_price());

                total = total + price;
            }

            String amount = String.valueOf(total);
            total_amtTv.setText(amount);

            System.out.println("=============== jArray from local ============");
            System.out.println(jArray);

            StockAdapter stockListAdapter = new StockAdapter(ExpenceActivity.this, jArray,"expense");
            stocklist.setAdapter(stockListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }


}
