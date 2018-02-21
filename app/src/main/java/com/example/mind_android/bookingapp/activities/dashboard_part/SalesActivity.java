package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.EnterLoginActivity;
import com.example.mind_android.bookingapp.activities.LoginActivity;
import com.example.mind_android.bookingapp.activities.MainActivity;
import com.example.mind_android.bookingapp.adapter.SalesAdapter;
import com.example.mind_android.bookingapp.adapter.StockAdapter;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addStock;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.deleteStock;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class SalesActivity extends AppCompatActivity {

    private LinearLayout listLayout;

    ListView stocklist;
    SalesAdapter stockListAdapter;
    DatabaseHandler db;
    List<Stock> stocks;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        db = new DatabaseHandler(SalesActivity.this);

        listLayout =findViewById(R.id.listLayout);
        stocklist =findViewById(R.id.stocklist);

        TextView signout_btn = findViewById(R.id.signout_btn);
        
        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(getApplicationContext(), "login", "0");

                startActivity(new Intent(SalesActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });


showAllStocks();
    }


    private void showStock(final Activity context, String user_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "stock_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show stock  response ***");
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

        showAllStocks();

    }

    private void addUnregisteredStock() {
        stocks = new ArrayList<>();
        stocks = db.getAllStocksWith0();
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
