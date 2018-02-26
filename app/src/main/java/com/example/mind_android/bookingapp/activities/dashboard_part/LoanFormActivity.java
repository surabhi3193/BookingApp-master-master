package com.example.mind_android.bookingapp.activities.dashboard_part;

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

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class LoanFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText loanamountEt,intrestET,monthET,paid_loan_amountET;
    public static TextView lendernameEt;
    private TextView dateTv,paid_dateTv,total_amountTV,paidtotal_amountTV,paidlender_nameET;

    private LinearLayout loanForm,payForm;
    private String trans_type = "0";
    public static String lender_name= "",lender_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loan_form);
        loanForm = findViewById(R.id.loan_form);
        payForm = findViewById(R.id.pay_form);

        dateTv = findViewById(R.id.dateTV);
        paid_dateTv = findViewById(R.id.paid_dateTV);
        paidtotal_amountTV = findViewById(R.id.paidtotal_amountTV);
        paidlender_nameET = findViewById(R.id.paidlender_nameET);
        paid_loan_amountET = findViewById(R.id.paid_loan_amountET);
        lendernameEt = findViewById(R.id.lender_nameET);
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
                    final FragmentManager fm=getFragmentManager();
                    final LoantemFragment p=new LoantemFragment();
                    loanForm.setVisibility(View.VISIBLE);
                    payForm.setVisibility(View.GONE);
                    lendernameEt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            p.show(fm, "loan list");
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

                String name = lendernameEt.getText().toString();
                String amount = loanamountEt.getText().toString();
                String rate = intrestET.getText().toString();
                String total_amt = total_amountTV.getText().toString();
                String month = monthET.getText().toString();

                String cdate = dateTv.getText().toString();

                if (name.length() == 0) {
                    lendernameEt.setError("Name required");
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
                                String month,String total_amount,String date,String type,String paid_amount) {
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
}
