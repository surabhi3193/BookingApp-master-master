package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.example.mind_android.bookingapp.activities.LoginActivity;
import com.example.mind_android.bookingapp.adapter.SalesAdapter;
import com.example.mind_android.bookingapp.beans.Sales;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class SalesActivity extends BaseActivity {

    private  TextView total_amtTv,dateTV;
    ListView stocklist;
    DatabaseHandler db;
    List<Sales> sales;
    String user_id;
    LinearLayout income_formLay;
    RelativeLayout bottom_lay;
    Button reset_lay;
    private LinearLayout listLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_sales, frameLayout);
        db = new DatabaseHandler(SalesActivity.this);

        listLayout = findViewById(R.id.listLayout);
        bottom_lay = findViewById(R.id.bottom_lay);
        stocklist = findViewById(R.id.stocklist);
        income_formLay = findViewById(R.id.income_formLay);
        dateTV = findViewById(R.id.dateTV);

        TextView signout_btn = findViewById(R.id.signout_btn);
        TextView addServiceTV = findViewById(R.id.addServiceTV);
        TextView saleTV = findViewById(R.id.saleTV);
        Button submit_btn = findViewById(R.id.submit_btn);

        final EditText nameEt = findViewById(R.id.service_name);
        final EditText priceEt = findViewById(R.id.service_price);
        total_amtTv = findViewById(R.id.total_amtTv);
        income_formLay.setVisibility(View.GONE);
        reset_lay = findViewById(R.id.reset_lay);


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
                new DatePickerDialog(SalesActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        saleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SalesActivity.this, FormActivity.class)
                        .putExtra("Activity", "saleStocks"));


            }
        });
        addServiceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEt.setText("");
                priceEt.setText("");
                income_formLay.requestFocus();
                listLayout.clearFocus();
                income_formLay.setVisibility(View.VISIBLE);
                bottom_lay.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                reset_lay.setVisibility(View.GONE);

//                Toast.makeText(SalesActivity.this,"Under development ",Toast.LENGTH_SHORT).show();
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
                String date = dateTV.getText().toString();

                if (servicename.length() == 0) {
                    nameEt.setError("Field required");
                }

                if (service_amt.length() == 0) {
                    priceEt.setError("Field required");
                }
                if (date.length() == 0) {
             Toast.makeText(SalesActivity.this,"Select Date",Toast.LENGTH_SHORT).show();                }

                if (servicename.length() > 0 && service_amt.length() > 0 && date.length()>0) {

                    if (isNetworkAvailable(SalesActivity.this)) {
                        FormActivity.addsale(SalesActivity.this, user_id, "", "", service_amt,
                                "", servicename, "1", "",date);
                    } else {
                        Toast.makeText(SalesActivity.this,"Internet Connection Unavailable, Try Again ",Toast.LENGTH_SHORT).show();
//                        FormActivity.addsaleToLocal(SalesActivity.this, servicename, "", service_amt, "", "1");
                    }
                    showAllSales();
                }
            }
        });


        reset_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSaleWarning();
            }
        });


        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        ImageView back_btn_sales = findViewById(R.id.back_btn_sales);

        back_btn_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }


    public void resetSaleWarning() {
        AlertDialog.Builder ab = new AlertDialog.Builder
                (SalesActivity.this, R.style.MyAlertDialogStyle1);
        ab.setTitle("Reset").setIcon(R.drawable.reset);
        ab.setMessage("Are you sure ? ");
        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (isNetworkAvailable(SalesActivity.this))
                    resetSale();
                else
                    db.deleteAllSales();
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

    private void resetSale() {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(SalesActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);

        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "clear_sales", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show stock  response ***");
                System.out.println(response);
                stocklist.setVisibility(View.GONE);
                db.deleteAllSales();
                ringProgressDialog.dismiss();
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

    private void showSale(final Activity context, String user_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.put("bk_userid", user_id);
        System.out.println("===== params ============");
        System.out.println(params);

        client.post(BASE_URL_NEW + "sales_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show stock  response  in sale ***");
                System.out.println(response);
                try {

                    if (response.getString("status").equals("0")) {
                        total_amtTv.setText("0");
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {

                        listLayout.setVisibility(View.VISIBLE);
                        stocklist.setVisibility(View.VISIBLE);

                        String total_amt = response.getString("total");
                        total_amtTv.setText(total_amt);

                        JSONArray jArray = response.getJSONArray("sales");

                        SalesAdapter stockListAdapter = new SalesAdapter(SalesActivity.this, jArray);
                        stocklist.setAdapter(stockListAdapter);
                        stockListAdapter.notifyDataSetChanged();
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
            bottom_lay.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            reset_lay.setVisibility(View.VISIBLE);
            showAllSales();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllSales();
    }

    public void showAllSales() {
        user_id = getData(SalesActivity.this, "user_id", "");

        if (isNetworkAvailable(SalesActivity.this)) {

            int count = db.getSalesCount();
            if (count == 0) {
                resetSale();
            } else {
                addUnregisteredSale();
                deleteStockFromServer();
            }
            showSale(SalesActivity.this, user_id);

        } else {
            showSalesFromLocal();
        }
    }

    private void deleteStockFromServer() {
        sales = db.getAllSalesWith2();
        System.out.println("================ stocks with 0 status ========");
        System.out.println(sales);
        for (Sales cn : sales)

        {
            String log = "Id: " + cn.get_id() +
                    " ,Name: " + cn.get_name() +
                    " ,qty: " + cn.get_qty() +
                    " ,unit price : " + cn.get_unit_per_price() +
                    " ,status : " + cn.get_status() +
                    " ,price : " + cn.get_price();
            // Writing Contacts to log
            Log.d("Name: ", log);
//            deleteStock(SalesActivity.this, cn);


        }
    }

    private void addUnregisteredSale() {
        sales = db.getAllSalesWith0();
        System.out.println("================ stocks with 0 status ========");
        System.out.println(sales);
        for (Sales cn : sales) {
            String log = "Id: " + cn.get_id() +
                    " ,Name: " + cn.get_name() +
                    " ,qty: " + cn.get_qty() +
                    " ,unit price : " + cn.get_unit_per_price() +
                    " ,status : " + cn.get_status() +
                    " ,price : " + cn.get_price();
            // Writing Contacts to log
            Log.d("Name: ", log);
            FormActivity.addsale(SalesActivity.this, user_id, cn.get_unit_per_price(),
                    cn.get_qty(), cn.get_price(),
                    String.valueOf(cn.get_id()), cn.get_name(), cn.get_sale_type(), "local",cn.get_date());

        }
    }

    private void showSalesFromLocal() {

        DatabaseHandler db = new DatabaseHandler(SalesActivity.this);
        JSONArray jArray = new JSONArray();
        double total = 0.00;
        // Reading all contacts
        Log.d("Reading: ", "Reading all Stocks..");
        List<Sales> stocks = db.getAllSalesExcept2();
        try {
            for (Sales cn : stocks) {
                JSONObject jobj = new JSONObject();

                String log = "Id: " + cn.get_id() +
                        " ,Name: " + cn.get_name() +
                        " ,qty: " + cn.get_qty() +
                        " ,unit price : " + cn.get_unit_per_price() +
                        " ,price : " + cn.get_price();
                // Writing Contacts to log
                Log.d("Name: ", log);


                jobj.put("stock_id", cn.get_id());
                jobj.put("sale_stock_name", cn.get_name());
                jobj.put("sale_stock_qty", cn.get_qty());
                jobj.put("sale_date", cn.get_date());
                jobj.put("stock_type", cn.get_sale_type());

                jobj.put("sale_price", cn.get_price());
                jArray.put(jobj);
                double price = Double.parseDouble(cn.get_price());

                total = total + price;
            }

            String amount = String.valueOf(total);
            total_amtTv.setText(amount);

            System.out.println("=============== jArray from local ============");
            System.out.println(jArray);

            SalesAdapter stockListAdapter = new SalesAdapter(SalesActivity.this, jArray);
            stocklist.setAdapter(stockListAdapter);
            stockListAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void updateLabel(TextView textEdit, Calendar myCalendar) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textEdit.setText(sdf.format(myCalendar.getTime()));
    }

}
