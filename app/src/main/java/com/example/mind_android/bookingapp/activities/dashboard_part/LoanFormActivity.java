package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.mind_android.bookingapp.activities.BanktemFragment;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.example.mind_android.bookingapp.activities.LoantemFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class LoanFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText loanamountEt,intrestET,monthET,paid_loan_amountET;
    public  EditText lendernameEt;
    private TextView dateTv,paid_dateTv,total_amountTV,paidtotal_amountTV,paidlender_nameET;
    private LinearLayout addlenderLAy;
    private LinearLayout loanForm,payForm;
    private String trans_type = "0";
    public static String lender_name= "",lender_id = "";
    
    private Spinner spinner ;
    private Button addbankName_Btn;
    List<String> categList;
    static String[]loanArr = {};
    static String[]loanidArr = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loan_form);
        addbankName_Btn =findViewById(R.id.addbank_btn);
        addlenderLAy =findViewById(R.id.addbank_lay);
        loanForm = findViewById(R.id.loan_form);
        payForm = findViewById(R.id.pay_form);
        spinner = findViewById(R.id.spinner);
        dateTv = findViewById(R.id.dateTV);
        paid_dateTv = findViewById(R.id.paid_dateTV);
        paidtotal_amountTV = findViewById(R.id.paidtotal_amountTV);
        paidlender_nameET = findViewById(R.id.paidlender_nameET);
        paid_loan_amountET = findViewById(R.id.paid_loan_amountET);
        lendernameEt = findViewById(R.id.bank_nameEt);
        loanamountEt = findViewById(R.id.loan_amountET);
        intrestET = findViewById(R.id.intrestET);
        monthET = findViewById(R.id.monthET);
        total_amountTV = findViewById(R.id.total_amountTV);
        Button add_btn = findViewById(R.id.add_btn);
        Button pay_btn = findViewById(R.id.pay_btn);



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

        final DatePickerDialog.OnDateSetListener pdate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(paid_dateTv, myCalendar);


            }

        };

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String type = "";
            type = bundle.getString("form");

            switch (type) {
                case "loan_form":
                    trans_type="1";

                    loanForm.setVisibility(View.VISIBLE);
                    payForm.setVisibility(View.GONE);
                   

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            lender_name=spinner.getSelectedItem().toString();

                            System.out.println("= lender name =======");
                            System.out.println(lender_name);
                            if (lender_name.equalsIgnoreCase("Add Lender"))
                            {
                                addlenderLAy.setVisibility(View.VISIBLE);
                                loanForm.setVisibility(View.GONE);
                                performaddBankAction();
                            }
                            else
                            {
                                System.out.println("======== lender id 1 ====== " + loanidArr[position]);
//                                System.out.println("======== lender id 2 ====== " + );
                                lender_id=loanidArr[position];
                                addlenderLAy.setVisibility(View.GONE);
                                loanForm.setVisibility(View.VISIBLE);

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            addlenderLAy.setVisibility(View.GONE);
                            loanForm.setVisibility(View.VISIBLE);

                        }
                    });
                   
                   
                   
                    break;

                    case "pay_form":
                        trans_type="2";
                    payForm.setVisibility(View.VISIBLE);
                    loanForm.setVisibility(View.GONE);
                    paidlender_nameET.setText(bundle.getString("name"));
                    paidtotal_amountTV.setText(bundle.getString("payable_amnt"));
                    lender_id=bundle.getString("lender_id");

                    break;
                    default:
                        loanForm.setVisibility(View.VISIBLE);
            }
        }


        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        monthET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String amnt = loanamountEt.getText().toString();
                String rate = intrestET.getText().toString();
                String time =s.toString();
                if (time.length()>0){
                    int t = (Integer.parseInt(time));
                    double p = Double.parseDouble(amnt);
                    double r = Double.parseDouble(rate);

                    int total= (int) ((p*r*t)/100);
                    System.out.println("===  p =======");
                    System.out.println(p);

                    System.out.println("=== r=======");
                    System.out.println(r);

                    System.out.println("=== t=======");
                    System.out.println(t);

                    System.out.println("=== total SI =======");
                    System.out.println(total);

                    total= (int) (total+p);

                    DecimalFormat decim = new DecimalFormat("0.00");
                    Double test = Double.parseDouble(decim.format(total));

                    total_amountTV.setText(String.valueOf(test));


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dateTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LoanFormActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        paid_dateTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LoanFormActivity.this, pdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name =spinner.getSelectedItem().toString();
                String amount = loanamountEt.getText().toString();
                String rate = intrestET.getText().toString();
                String total_amt = total_amountTV.getText().toString();
                String month = monthET.getText().toString();

                String cdate = dateTv.getText().toString();

                if (name.length() == 0) {
                    Toast.makeText(LoanFormActivity.this,"Please select lender",Toast.LENGTH_SHORT).show();
                }
                if (amount.length() == 0) {
                    loanamountEt.setError("Amount required");
                }
                if (cdate.length() == 0) {
                    dateTv.setError("Date required");
                }


                else {
                    addTransection(lender_id,name, amount,rate,month,total_amt, cdate,trans_type,"");

                }

            }
        });



        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lendername = paidlender_nameET.getText().toString();
                String paid_amount = paid_loan_amountET.getText().toString();
                String total_amount = paidtotal_amountTV.getText().toString();
                String date = paid_dateTv.getText().toString();

                if (paid_amount.length()==0)
                {
                    paid_loan_amountET.setError("Field required");
                }

                if (date.length()==0)
                {
                    paid_dateTv.setError("Field required");
                }

                else
                {
                    paid_loan_amountET.setError(null);
                    paid_dateTv.setError(null);
                    addTransection(lender_id,lendername, "","","","", date,trans_type,paid_amount);

                }
            }
        });
    }

    private void addTransection(String lender_id,String name, String loan_amount, String rate,
                                String month,String total_amount,String date,String type,
                                String paid_amount) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(LoanFormActivity.this, "Please wait ...",
                "", true);


        ringProgressDialog.setCancelable(false);

        String user_id = getData(LoanFormActivity.this, "user_id",
                "");

        params.put("bk_userid", user_id);
        params.put("loan_id", lender_id);
        params.put("lender_name", name);
        params.put("loan_amount", loan_amount);
        params.put("interest_rate", rate);
        params.put("interest_month", month);
        params.put("loan_total_amount", total_amount);
        params.put("method_type", trans_type);
        params.put("loan_date", date);
        params.put("loan_paid_amount", paid_amount);

        System.err.println(params);

        client.setTimeout(20 * 1000);

        client.post(BASE_URL_NEW + "add_loan", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* add loan transection response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(LoanFormActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Loan Updated successfully", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void performaddBankAction() {
        addbankName_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =lendernameEt.getText().toString();

                if (name.length()==0)
                    lendernameEt.setError("Field Required");
                else
                {
                    
                    addBank(name);
                }
            }
        });
    }


    private void addBank(String name)
    {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(LoanFormActivity.this,
                "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);

        String user_id = getData(LoanFormActivity.this,"user_id","");
        params.put("bk_userid", user_id);
        params.put("lender_name", name);

        System.out.println(params);

        client.post(BASE_URL_NEW + "add_lender", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* summary response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1"))
                    {
                        Toast.makeText(LoanFormActivity.this,"Lender added in your list",
                                Toast.LENGTH_SHORT).show();
                        addlenderLAy.setVisibility(View.GONE);
                        loanForm.setVisibility(View.VISIBLE);
                        getAllLenders();
                    }
                    else
                    {
                        Toast.makeText(LoanFormActivity.this,response.getString("message"),
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


    public  void getAllLenders() {
        final List<String> banklist = new ArrayList<String>();
        final List<String> idlist = new ArrayList<String>();



        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final String bk_userid = getData(LoanFormActivity.this, "user_id", "");
        params.put("bk_userid", bk_userid);
        System.out.println(params);

        client.post(BASE_URL_NEW + "lender_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* all stock response ***");
                System.out.println(response);

                categList = new ArrayList<>();

                try {

                    if (response.getString("status").equals("0")) {
                        categList.add("Add Lender");
                        categList.add("Select Lender");
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                LoanFormActivity.this, R.layout.spinner_item, categList);

                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner.setAdapter(spinnerArrayAdapter);
                    }

                    else {

                        JSONArray jArray = response.getJSONArray("lenders");
                        categList.add("Select Lender");
                        banklist.add("");
                        idlist.add("yy");


                        if (jArray.length()>0)
                        {
                            for (int i =0;i<jArray.length();i++)
                            {
                                JSONObject obj = jArray.getJSONObject(i);

                                String id = obj.getString("lender_id");
                                String name = obj.getString("lender_name");
                                banklist.add(name);
                                idlist.add(id);
                                categList.add(name);


                            }
                            categList.add("Add Lender");

                            loanArr = banklist.toArray(new String[banklist.size()]);
                            loanidArr = idlist.toArray(new String[idlist.size()]);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    LoanFormActivity.this, R.layout.spinner_item, categList);

                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                            spinner.setAdapter(spinnerArrayAdapter);

                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                System.out.println(responseString);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllLenders();
    }
}
