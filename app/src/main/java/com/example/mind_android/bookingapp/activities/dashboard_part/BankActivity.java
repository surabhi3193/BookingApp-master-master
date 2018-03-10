package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.example.mind_android.bookingapp.adapter.SummaryAdapter;
import com.example.mind_android.bookingapp.beans.TransectionSummary;
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

public class BankActivity extends BaseActivity {

    private List<TransectionSummary> summaryList = new ArrayList<>();
    private SummaryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_bank, frameLayout);

        RecyclerView recyclerView = findViewById(R.id.transection_LV);
        TextView addTv = findViewById(R.id.addTv);


        mAdapter = new SummaryAdapter(summaryList, "bank");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(BankActivity.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BankActivity.this,
                        BankFormActivity.class)
                        .putExtra("form", "bank_form")

                );
            }
        });


    }

    private void getTransections(final String user_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(BankActivity.this,
                "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);


        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "all_bank_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* summary response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        JSONArray jArray;

                        jArray = response.getJSONArray("banks");

                        if (jArray.length() > 0) {
                            TransectionSummary summary;
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject obj = jArray.getJSONObject(i);

                                String name = obj.getString("bank_name");
                                String amount = obj.getString("closing_balance");
                                String date = "";
                                String type = "";

                                summary = new TransectionSummary(name, "", "", amount, type, "", date);
                                summaryList.add(summary);
                            }
                            mAdapter.notifyDataSetChanged();

                        }
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
                System.out.println(responseString);
                ringProgressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String user_id = getData(BankActivity.this, "user_id", "");

        if (summaryList.size() > 0)
            summaryList.clear();

        getTransections(user_id);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
