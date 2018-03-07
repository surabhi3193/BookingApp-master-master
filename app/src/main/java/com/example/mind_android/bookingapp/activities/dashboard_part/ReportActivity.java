package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.example.mind_android.bookingapp.activities.MainActivity;
import com.example.mind_android.bookingapp.beans.User;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class ReportActivity extends BaseActivity {

    private String user_id;
    private TextView saleTV,goodsTv,expenseTV,profitTV,per_unitTV;
    private RelativeLayout profile_lay;
    private ImageView plIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_report, frameLayout);

        Button summary_btn = findViewById(R.id.summary_btn);
        saleTV = findViewById(R.id.saleTV);
        goodsTv = findViewById(R.id.goodsoldTv);
        expenseTV = findViewById(R.id.expenseTv);
        per_unitTV = findViewById(R.id.per_unitTV);
        profitTV = findViewById(R.id.profitTv);
        profile_lay = findViewById(R.id.profile_lay);
        plIcon = findViewById(R.id.plIcon);

        user_id = getData(ReportActivity.this, "user_id", "");
        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        summary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ReportActivity.this, SummaryReport.class));

            }
        });

    }


    private void getReport(final String user_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(ReportActivity.this, "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);

        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "report", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* report response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1"))
                    {
                        if (response.getString("message").equals("Profit"))
                        {
                            profitTV.setText(response.getString("total_profit"));
                            plIcon.setImageResource(R.drawable.profit);
                            profile_lay.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }
                        else
                        {

                            profitTV.setText(response.getString("total_profit"));
                            plIcon.setImageResource(R.drawable.loss);
                            profile_lay.setBackgroundColor(getResources().getColor(R.color.Crimson));
                        }

                        goodsTv.setText(response.getString("stock_profit"));
                        expenseTV.setText(response.getString("expenses"));
                     per_unitTV.setText(response.getString("cost_of_sale"));
                        saleTV.setText(response.getString("total_sale"));

                    }


                } catch (Exception e) {
                    ringProgressDialog.dismiss();
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getReport(user_id);
    }


}
