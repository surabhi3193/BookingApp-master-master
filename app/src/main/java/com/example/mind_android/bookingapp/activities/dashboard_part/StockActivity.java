package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.example.mind_android.bookingapp.activities.LoginActivity;
import com.example.mind_android.bookingapp.adapter.StockAdapter;
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

public class StockActivity extends BaseActivity {
    private static ListView stocklist;
    private static LinearLayout listLayout;
    private static TextView total_amtTv;
    DatabaseHandler db;
    List<Stock> stocks;
    String user_id;


    private static void showStock(final Activity context, String user_id) {
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
                        stocklist.setVisibility(View.GONE);
                        total_amtTv.setText("0.00");
//                        Toast.makeText(StockActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {

                        listLayout.setVisibility(View.VISIBLE);
                        stocklist.setVisibility(View.VISIBLE);

                        String total_amt = response.getString("total");
                        total_amtTv.setText(total_amt);
                        JSONArray jArray = response.getJSONArray("stocks");
                        StockAdapter stockListAdapter = new StockAdapter(context, jArray, "stocks");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_stock, frameLayout);
        user_id = getData(StockActivity.this, "user_id", "");

        db = new DatabaseHandler(StockActivity.this);

        listLayout = findViewById(R.id.listLayout);
        stocklist = findViewById(R.id.stocklist);
        total_amtTv = findViewById(R.id.total_amtTv);
        TextView addTv = findViewById(R.id.addTv);

        Button reset_lay =findViewById(R.id.reset_lay);

        reset_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetStock();
            }
        });


        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StockActivity.this, FormActivity.class)
                        .putExtra("Activity", "addStocks")
                        .putExtra("stock_id", "")
                );

            }
        });

        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

    private void resetStock()
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "clear_stocks", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show stock  response ***");
                System.out.println(response);
                try {

                    if (response.getString("status").equals("0")) {
                        stocklist.setVisibility(View.GONE);
                        total_amtTv.setText("0.00");
//                        Toast.makeText(StockActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        stocklist.setVisibility(View.GONE);                    }
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

    public void showAllStocks() {
        if (isNetworkAvailable(StockActivity.this)) {

            addUnregisteredStock();
            deleteStockFromServer();
            showStock(StockActivity.this, user_id);

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
            deleteStock(StockActivity.this, cn);
        }

    }

    private void addUnregisteredStock() {
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
            addStock(StockActivity.this, user_id, cn.get_name(), cn.get_qty(), cn.get_price(), "1", cn.get_unit_per_price(), String.valueOf(cn.get_id()), "local");
        }
    }

    private void showStockFronLocal() {

        DatabaseHandler db = new DatabaseHandler(StockActivity.this);
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
//                double price = Double.parseDouble(cn.get_price());

//                total = total + price;
            }

            String amount = String.valueOf(total);
            total_amtTv.setText(amount);
            System.out.println("=============== jArray from local ============");
            System.out.println(jArray);

            StockAdapter stockListAdapter = new StockAdapter(StockActivity.this, jArray, "stocks");
            stocklist.setAdapter(stockListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllStocks();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
