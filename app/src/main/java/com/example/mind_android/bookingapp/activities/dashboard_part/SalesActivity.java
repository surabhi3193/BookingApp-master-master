package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.LoginActivity;
import com.example.mind_android.bookingapp.adapter.SalesAdapter;
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
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addStock;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.deleteStock;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class SalesActivity extends AppCompatActivity {

    ListView stocklist;
    SalesAdapter stockListAdapter;
    DatabaseHandler db;
    List<Stock> stocks;
    String user_id;
    LinearLayout income_formLay;
    private LinearLayout listLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        db = new DatabaseHandler(SalesActivity.this);

        listLayout = findViewById(R.id.listLayout);
        stocklist = findViewById(R.id.stocklist);
        income_formLay = findViewById(R.id.income_formLay);

        TextView signout_btn = findViewById(R.id.signout_btn);
        TextView addServiceTV = findViewById(R.id.addServiceTV);
        Button submit_btn = findViewById(R.id.submit_btn);

        final EditText nameEt = findViewById(R.id.service_name);
        final EditText priceEt = findViewById(R.id.service_price);

        income_formLay.setVisibility(View.GONE);
        addServiceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income_formLay.setVisibility(View.VISIBLE);
            }
        });

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(getApplicationContext(), "login", "0");
                startActivity(new Intent(SalesActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String servicename = nameEt.getText().toString();
                String service_amt = priceEt.getText().toString();

                if (servicename.length() == 0) {
                    nameEt.setError("Field required");
                }

                if (service_amt.length() == 0 || Integer.parseInt(service_amt) == 0) {
                    priceEt.setError("Field required");
                }

                if (servicename.length() > 0 && service_amt.length() > 0
                        && Integer.parseInt(service_amt) != 0) {

                    FormActivity.addsale(SalesActivity.this,user_id, "", "", service_amt,
                            "",servicename,"1");


//                    Toast.makeText(SalesActivity.this, "Under development",
//                            Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });
    }

    private void addService(String servicename, String service_amt) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(SalesActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);
        String user_id = getData(SalesActivity.this, "user_id", "");

        params.put("bk_userid", user_id);
        params.put("service_name", servicename);
        params.put("service_price", service_amt);

        System.out.println(params);

        client.post(BASE_URL_NEW + "add_service", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* add service ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(SalesActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
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

                System.err.println(responseString);
            }
        });
    }

    private void showStock(final Activity context, String user_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("bk_userid", user_id);
        System.out.println("===== params ============");
        System.out.println(params);

        client.post(BASE_URL_NEW + "stock_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show stock  response  in sale ***");
                System.out.println(response);
                try {

                    if (response.getString("status").equals("0")) {
//                        Toast.makeText(StockActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {

                        listLayout.setVisibility(View.VISIBLE);

                        JSONArray jArray = response.getJSONArray("stocks");
                        SalesAdapter stockListAdapter = new SalesAdapter(SalesActivity.this, jArray);
                        stocklist.setAdapter(stockListAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (income_formLay.getVisibility() == View.VISIBLE) {
            income_formLay.setVisibility(View.GONE);
        } else
            finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllStocks();
    }

    public void showAllStocks() {
        if (isNetworkAvailable(SalesActivity.this)) {

            addUnregisteredStock();
            deleteStockFromServer();
            user_id = getData(SalesActivity.this, "user_id", "");
            showStock(SalesActivity.this, user_id);

        } else {
            showStockFronLocal();
        }
    }

    private void deleteStockFromServer() {
        stocks = db.getAllStocksWith2();
        System.out.println("================ stocks with 0 status ========");
        System.out.println(stocks);
        for (Stock cn : stocks)

        {
            String log = "Id: " + cn.get_id() +
                    " ,Name: " + cn.get_name() +
                    " ,qty: " + cn.get_qty() +
                    " ,unit price : " + cn.get_unit_per_price() +
                    " ,status : " + cn.get_status() +
                    " ,price : " + cn.get_price();
            // Writing Contacts to log
            Log.d("Name: ", log);
            deleteStock(SalesActivity.this, cn);


        }
    }

    private void addUnregisteredStock() {
        stocks = db.getAllStocksWith0();
        System.out.println("================ stocks with 0 status ========");
        System.out.println(stocks);
        for (Stock cn : stocks) {
            String log = "Id: " + cn.get_id() +
                    " ,Name: " + cn.get_name() +
                    " ,qty: " + cn.get_qty() +
                    " ,unit price : " + cn.get_unit_per_price() +
                    " ,status : " + cn.get_status() +
                    " ,price : " + cn.get_price();
            // Writing Contacts to log
            Log.d("Name: ", log);
            addStock(SalesActivity.this, user_id, cn.get_name(), cn.get_qty(),
                    cn.get_price(),
                    "1", cn.get_unit_per_price(), String.valueOf(cn.get_id()), "local");
        }
    }

    private void showStockFronLocal() {

        DatabaseHandler db = new DatabaseHandler(SalesActivity.this);
        JSONArray jArray = new JSONArray();
        double total = 0.00;
        // Reading all contacts
        Log.d("Reading: ", "Reading all Stocks..");
        List<Stock> stocks = db.getAllStocksExcept2();
        try {
            for (Stock cn : stocks) {
                JSONObject jobj = new JSONObject();

                String log = "Id: " + cn.get_id() +
                        " ,Name: " + cn.get_name() +
                        " ,qty: " + cn.get_qty() +
                        " ,unit price : " + cn.get_unit_per_price() +
                        " ,price : " + cn.get_price();
                // Writing Contacts to log
                Log.d("Name: ", log);


                jobj.put("stock_id", cn.get_id());
                jobj.put("stock_name", cn.get_name());
                jobj.put("stock_qty", cn.get_qty());
                jobj.put("stock_per_price", cn.get_unit_per_price());
                jobj.put("stock_price", cn.get_price());
                jArray.put(jobj);
                double price = Double.parseDouble(cn.get_price());

                total = total + price;
            }

            String amount = String.valueOf(total);

            System.out.println("=============== jArray from local ============");
            System.out.println(jArray);

            SalesAdapter stockListAdapter = new SalesAdapter(SalesActivity.this, jArray);
            stocklist.setAdapter(stockListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

}
