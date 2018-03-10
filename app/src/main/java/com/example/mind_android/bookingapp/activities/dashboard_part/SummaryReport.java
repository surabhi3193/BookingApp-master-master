package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.adapter.SummaryAdapter;
import com.example.mind_android.bookingapp.beans.TransectionSummary;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class SummaryReport extends AppCompatActivity {

    private List<TransectionSummary> summaryList = new ArrayList<>();
    private SummaryAdapter mAdapter;
    private String user_id;
    private Spinner spinner;
    private TextView fromTV, toTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_report);
        user_id = getData(SummaryReport.this, "user_id", "");
        spinner = findViewById(R.id.spinner);
        TextView filter_btn = findViewById(R.id.filter_btn);
        fromTV = findViewById(R.id.fromTV);
        toTV = findViewById(R.id.toTv);

        RecyclerView recyclerView = findViewById(R.id.transection_LV);

        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        List<String> categList = new ArrayList<>();
        categList.add("All");
        categList.add("Stock");//3
        categList.add("Sale");//1
        categList.add("Expense");//4
        categList.add("Loan");//5
        categList.add("Bank");//6

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                SummaryReport.this, R.layout.spinner_item, categList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        mAdapter = new SummaryAdapter(summaryList, "summary");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(SummaryReport.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        getReport(user_id, "", "", "");
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(fromTV, myCalendar);
            }

        };
        final DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(toTV, myCalendar);
            }

        };

        fromTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(SummaryReport.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(SummaryReport.this, todate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start, end, type;

                start = fromTV.getText().toString();
                end = toTV.getText().toString();
                type = spinner.getSelectedItem().toString();

                switch (type) {

                    case "Sale":
                        type = "1";
                        break;
                    case "Stock":
                        type = "3";
                        break;
                    case "Expense":
                        type = "4";
                        break;
                    case "Loan":
                        type = "5";
                        break;
                    case "Bank":
                        type = "6";
                        break;
                    default:
                        type = "";
                        break;
                }

                getReport(user_id, start, end, type);
            }
        });


    }

    private void getReport(final String user_id, String start, String end, String type) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(SummaryReport.this, "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);


        params.put("bk_userid", user_id);
        params.put("filter_value", type);
        params.put("start_date", start);
        params.put("end_date", end);

        System.out.println(params);

        client.post(BASE_URL_NEW + "transaction_records", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* summary response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        JSONArray jArray;

                        jArray = response.getJSONArray("result");

                        if (jArray.length() > 0) {
                            summaryList.clear();
                            TransectionSummary summary;
                            for (int i = 0; i < jArray.length(); i++)

                            {
                                JSONObject obj = jArray.getJSONObject(i);
                                String name = obj.getString("name");
                                String qty = obj.getString("qty");
                                String per_price = obj.getString("per_price");
                                String amount = obj.getString("amount");
                                String date = obj.getString("date");
                                String type = obj.getString("type");
                                String trans_type = obj.getString("transaction_type");

                                summary = new TransectionSummary(name, qty, per_price, amount, type, trans_type, date);
                                summaryList.add(summary);
                            }

                            mAdapter.notifyDataSetChanged();

                        } else {
                            summaryList.clear();
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

    private void updateLabel(TextView textEdit, Calendar myCalendar) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textEdit.setText(sdf.format(myCalendar.getTime()));
    }

}
