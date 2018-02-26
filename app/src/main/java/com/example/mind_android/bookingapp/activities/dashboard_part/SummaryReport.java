package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.MainActivity;
import com.example.mind_android.bookingapp.adapter.SummaryAdapter;
import com.example.mind_android.bookingapp.beans.TransectionSummary;
import com.example.mind_android.bookingapp.beans.User;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
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
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class SummaryReport extends AppCompatActivity {

    private List<TransectionSummary> summaryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SummaryAdapter mAdapter;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_report);
        user_id = getData(SummaryReport.this, "user_id", "");

        recyclerView = (RecyclerView) findViewById(R.id.transection_LV);

        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                finish();
            }
        });

        mAdapter = new SummaryAdapter(summaryList,"summary");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(SummaryReport.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        getReport(user_id);

    }

    private void getReport(final String user_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(SummaryReport.this, "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);

        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "transaction_records", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* summary response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        JSONArray jArray = new JSONArray();

                       jArray= response.getJSONArray("result");
                       
                       if (jArray.length()>0)
                       {
                           TransectionSummary summary;
                           for (int i=0;i<jArray.length();i++)
                           {
                               JSONObject obj = jArray.getJSONObject(i);
                               String id = obj.getString("id");
                               String name = obj.getString("name");
                               String qty = obj.getString("qty");
                               String per_price = obj.getString("per_price");
                               String amount = obj.getString("amount");
                               String date = obj.getString("date");
                               String type = obj.getString("type");

                               summary = new TransectionSummary(name,qty,per_price,amount,type,date);
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

}
