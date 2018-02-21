package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.example.mind_android.bookingapp.adapter.StockAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class ExpenceActivity extends AppCompatActivity {
    ScrollView scrollview;
    String total_amt = "0", price_unit = "0";
    ListView stocklist;
    private int count;
    private LinearLayout addLayout, listLayout;
    private Button add_stock;
    private EditText item_nameEt, item_qtEt, itemPriceEt, itemUnitPriceEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expence);
        addLayout = findViewById(R.id.addLayout);
        listLayout = findViewById(R.id.listLayout);
        stocklist = findViewById(R.id.stocklist);
        add_stock = findViewById(R.id.add_btn);
        item_nameEt = findViewById(R.id.item_nameTv);
        item_qtEt = findViewById(R.id.itemQuant_TV);
        itemPriceEt = (EditText) findViewById(R.id.item_price_tv);
        itemUnitPriceEt = findViewById(R.id.item_price_unit);
        showStock();
        TextView addTv = findViewById(R.id.addTv);

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpenceActivity.this, ExpenseForm_Activity.class)
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
    }


    private void showStock() {
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

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(ExpenceActivity.this,
                                response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        addLayout.setVisibility(View.GONE);
                        listLayout.setVisibility(View.VISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();
        showStock();
    }

    @Override
    public void onBackPressed() {
        if (addLayout.getVisibility() == View.VISIBLE) {
            addLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
            finish();
        }
    }


}
