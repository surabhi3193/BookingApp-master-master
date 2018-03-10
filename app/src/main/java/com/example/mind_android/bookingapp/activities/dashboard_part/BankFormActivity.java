package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
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

public class BankFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static String bank_name = "", bank_id = "";
    static String[] bankArr = {};
    static String[] bankidArr = {};
    private String[] bussiness = {"Transaction Type", "Deposit", "Withdrawal",};
    private EditText bankamountEt;
    private TextView dateTv;
    private Spinner spin, spinner;
    private LinearLayout bank_form;
    private String trans_type = "0";
    private List<String> categList;
    private LinearLayout addbank_lay;
    private EditText bank_nameEt;
    private Button addbankName_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bank_form);
        bank_form = findViewById(R.id.bank_form);
        spinner = findViewById(R.id.spinner);
        addbank_lay = findViewById(R.id.addbank_lay);
        addbankName_Btn = findViewById(R.id.addbank_btn);
        bank_nameEt = findViewById(R.id.bank_nameEt);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String type;
            type = bundle.getString("form");

            assert type != null;
            switch (type) {
                case "bank_form":

                    bank_form.setVisibility(View.VISIBLE);
                    break;

                case "loan_form":

                    break;
            }
        }

        dateTv = findViewById(R.id.dateTV);

        bankamountEt = findViewById(R.id.bank_amountEt);
        spin = findViewById(R.id.spinner2);
        Button add_btn = findViewById(R.id.add_btn);

//        final FragmentManager fm=getFragmentManager();
//        final BanktemFragment p=new BanktemFragment();

        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        getBankList();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bank_name = spinner.getSelectedItem().toString();

                System.out.println("= bank name =======");
                System.out.println(bank_name);
                if (bank_name.equalsIgnoreCase("Add Bank")) {
                    addbank_lay.setVisibility(View.VISIBLE);
                    bank_form.setVisibility(View.GONE);
                    performaddBankAction();
                } else {
                    bank_id = bankidArr[position];
                    addbank_lay.setVisibility(View.GONE);
                    bank_form.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                addbank_lay.setVisibility(View.GONE);
                bank_form.setVisibility(View.VISIBLE);

            }
        });
//        banknameEt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                p.show(fm, "bank list");
//            }
//        });

        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(BankFormActivity.this, R.layout.spinner_items, bussiness);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dateTv, myCalendar);
            }

        };

        dateTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(BankFormActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = spinner.getSelectedItem().toString();
                String amount = bankamountEt.getText().toString();
                String type = spin.getSelectedItem().toString();
                String cdate = dateTv.getText().toString();

                switch (type) {
                    case "Transection Type":
                        trans_type = "0";
                        break;

                    case "Deposit":
                        trans_type = "1";
                        break;

                    case "Withdrawal":
                        trans_type = "2";
                        break;

                }


                if (name.length() == 0 || name.equalsIgnoreCase("Select Bank")) {
//                    banknameEt.setError("Name required");
                    Toast.makeText(BankFormActivity.this,
                            "Select Bank", Toast.LENGTH_SHORT).show();

                }
                if (amount.length() == 0) {
                    bankamountEt.setError("Amount required");
                }
                if (cdate.length() == 0) {
                    dateTv.setError("Date required");
                }

                if (trans_type.equals("0")) {
                    Toast.makeText(BankFormActivity.this, "Select transaction type ", Toast.LENGTH_SHORT
                    ).show();
                } else {
                    addTransection(bank_name, amount, cdate, trans_type);
                }

            }
        });
    }

    private void addTransection(String name, String amount, String cdate, String trans_type) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(BankFormActivity.this, "Please wait ...",
                "Adding", true);


        ringProgressDialog.setCancelable(false);

        String user_id = getData(BankFormActivity.this, "user_id", "");

        params.put("bk_userid", user_id);
        params.put("bank_name", name);
        params.put("amount", amount);
        params.put("transaction_type", trans_type);
        params.put("transaction_date", cdate);
        params.put("method_type", "1");
        params.put("transaction_id", "");
        params.put("new_bank_name", "");

        System.err.println(params);

        client.setTimeout(20 * 1000);

        client.post(BASE_URL_NEW + "add_bank_transaction", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* add bank transection response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(BankFormActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Transection added successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateLabel(TextView textEdit, Calendar myCalendar) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textEdit.setText(sdf.format(myCalendar.getTime()));
    }

    private void getBankList() {

        final List<String> banklist = new ArrayList<>();
        final List<String> idlist = new ArrayList<>();
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final String bk_userid = getData(BankFormActivity.this, "user_id", "");
        params.put("bk_userid", bk_userid);
        System.out.println(params);

        client.post(BASE_URL_NEW + "bank_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* all stock response ***");
                categList = new ArrayList<>();
                System.out.println(response);
                try {

                    if (response.getString("status").equals("0")) {

                        categList.add("Add Bank");
                        categList.add("Select Bank");
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                                BankFormActivity.this, R.layout.spinner_item, categList);

                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner.setAdapter(spinnerArrayAdapter);

                    } else {


                        JSONArray jArray = response.getJSONArray("banks");
                        categList.add("Select Bank");

                        for (int i = 0; i < jArray.length(); i++) {
                            String expenses_name = jArray.getJSONObject(i)
                                    .getString("bank_name");

                            String id = jArray.getJSONObject(i)
                                    .getString("bank_id");


                            banklist.add(expenses_name);
                            idlist.add(id);
                            categList.add(expenses_name);
                        }
                        categList.add("Add Bank");

                        idlist.add("xx");

                        bankArr = banklist.toArray(new String[banklist.size()]);
                        bankidArr = idlist.toArray(new String[idlist.size()]);
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                                BankFormActivity.this, R.layout.spinner_item, categList);

                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner.setAdapter(spinnerArrayAdapter);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });

    }

    private void performaddBankAction() {
        addbankName_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = bank_nameEt.getText().toString();

                if (name.length() == 0)
                    bank_nameEt.setError("Name Required");

                else {
                    bank_nameEt.setError(null);
                    addBank(name);
                }
            }
        });
    }

    private void addBank(String name) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(BankFormActivity.this,
                "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);

        String user_id = getData(BankFormActivity.this, "user_id", "");
        params.put("bk_userid", user_id);
        params.put("bank_name", name);

        System.out.println(params);

        client.post(BASE_URL_NEW + "add_bank", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* summary response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        Toast.makeText(BankFormActivity.this, "Bank added in your list",
                                Toast.LENGTH_SHORT).show();
                        addbank_lay.setVisibility(View.GONE);
                        bank_form.setVisibility(View.VISIBLE);
                        getBankList();
                    } else {
                        Toast.makeText(BankFormActivity.this, response.getString("message"),
                                Toast.LENGTH_SHORT).show();
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
