package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.adapter.BankSummaryAdapter;
import com.example.mind_android.bookingapp.beans.BankTransaction;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class BankDetailActivity extends AppCompatActivity {

    private List<BankTransaction> summaryList = new ArrayList<>();
    private BankSummaryAdapter mAdapter;
    private RecyclerView recyclerView;
    private String bank_name;
    private TextView headtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail);
        recyclerView = findViewById(R.id.transection_LV);
        headtv = findViewById(R.id.headtv);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bank_name = bundle.getString("bank_name");
        }

        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        mAdapter = new BankSummaryAdapter(summaryList, BankDetailActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(BankDetailActivity.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    public void getTransections(final String bank_name) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(BankDetailActivity.this,
                "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);
        String user_id = getData(BankDetailActivity.this, "user_id", "");


        params.put("bk_userid", user_id);
        params.put("bank_name", bank_name);

        System.out.println(params);

        client.post(BASE_URL_NEW + "bank_transactions", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* bank per trans response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {
                    if (summaryList.size()>0)
                        summaryList.clear();

                    if (response.getString("status").equals("1")) {
                        JSONArray jArray;

                        jArray = response.getJSONArray("transactions");

                        if (jArray.length() > 0) {
                            BankTransaction summary;
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject obj = jArray.getJSONObject(i);

                                String id = obj.getString("transaction_id");
                                String name = obj.getString("bank_name");
                                String amount = obj.getString("amount");
                                String type = obj.getString("transaction_type");
                                String transaction_date = obj.getString("transaction_date");
                                String trans_closing = obj.getString("trans_closing");
                                headtv.setText(name);
                                summary = new BankTransaction(id, name, amount, type, trans_closing, transaction_date);
                                summaryList.add(summary);
                            }


                        }
                    }

                    mAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
                ringProgressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bank_name != null) {
            if (summaryList.size() > 0)
                summaryList.clear();
            getTransections(bank_name);
        }
    }
}
