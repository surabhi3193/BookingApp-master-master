package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.example.mind_android.bookingapp.adapter.LoanAdapter;
import com.example.mind_android.bookingapp.beans.LoanSummary;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class LoanActivity extends BaseActivity {

    private List<LoanSummary> summaryList = new ArrayList<>();
    private LoanAdapter mAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_loan, frameLayout);

         recyclerView = findViewById(R.id.transection_LV);
        TextView addTv = findViewById(R.id.addTv);

        mAdapter = new LoanAdapter(summaryList, "loan", LoanActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(LoanActivity.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ImageView back_btn = findViewById(R.id.back_btn);

        Button reset_lay = findViewById(R.id.reset_lay);

        reset_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetLoanWarning("");
            }
        });


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

                startActivity(new Intent(LoanActivity.this,
                        LoanFormActivity.class)
                        .putExtra("form", "loan_form")

                );
            }
        });
    }

    public void resetLoanWarning(final String loan_id) {
        AlertDialog.Builder ab = new AlertDialog.Builder
                (LoanActivity.this, R.style.MyAlertDialogStyle1);
        ab.setTitle("Reset").setIcon(R.drawable.reset);
        ab.setMessage("Are you sure ? ");
        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (isNetworkAvailable(LoanActivity.this))
                resetLoan(loan_id);


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
    private void getLoan(final String user_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(LoanActivity.this,
                "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);


        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "loan_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* loan list  response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        JSONArray jArray;
                        jArray = response.getJSONArray("loans");

                        if (jArray.length() > 0) {
                            LoanSummary summary;
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject obj = jArray.getJSONObject(i);


                                String id = obj.getString("loan_id");
                                String name = obj.getString("lender_name");
                                String taken_amount = obj.getString("loan_amount");
                                String interest_rate = obj.getString("interest_rate");
                                String interest_month = obj.getString("interest_month");
                                String loan_total_amount = obj.getString("loan_total_amount");
                                String borrowed_paid_amount = obj.getString("borrowed_paid_amount");
                                String total_remains_amount = obj.getString("total_remains_amount");
                                String loan_date = obj.getString("loan_date");
                                String loan_status = obj.getString("loan_status");


                                String rate = interest_rate + "%";

                                taken_amount = "Total Taken Amount : " + taken_amount;

                                summary = new LoanSummary(id, name, taken_amount, rate, interest_month
                                        , total_remains_amount, borrowed_paid_amount, loan_total_amount,
                                        loan_status, loan_date);
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
    public void resetLoan(String lender_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(LoanActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);

        String user_id= getData(LoanActivity.this,"user_id","");
        params.put("bk_userid", user_id);

        if (lender_id.length()>0)
            params.put("loan_id", lender_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "clear_loans", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show loan clear   response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
//                         recyclerView.setVisibility(View.GONE);

                    Toast.makeText(LoanActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
//                        db.deleteAllStocks();
//                        recyclerView.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
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
        String user_id = getData(LoanActivity.this, "user_id", "");
        if (summaryList.size() > 0)
            summaryList.clear();
        getLoan(user_id);
    }
    @Override
    public void onBackPressed() {
        finish();

    }
}
