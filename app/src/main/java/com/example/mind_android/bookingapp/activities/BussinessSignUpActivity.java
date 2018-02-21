package com.example.mind_android.bookingapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.activities.SignupActivity.isMatch;
import static com.example.mind_android.bookingapp.storage.MySharedPref.NullData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class BussinessSignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    String[] bussiness = { "Business Type","Services","Retail" , };
    EditText buss_nameET,cityEt,emailEt,fullnameEt;
    Spinner spin;
    TextView countryEt;
    ScrollView scrollview;
    String password,name,email,phone,buss_name="",city="",country="",buss_type,code,user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_sign_up);

        scrollview = findViewById(R.id.scrollview);
        emailEt = findViewById(R.id.emailEt);
        fullnameEt = findViewById(R.id.nameET);
         spin =  findViewById(R.id.spinner2);
         buss_nameET =  findViewById(R.id.buss_nameET);
         cityEt =  findViewById(R.id.cityEt);
         countryEt =  findViewById(R.id.countryEt);
         TextView login_link =  findViewById(R.id.login_link);
        RelativeLayout signup_btn= findViewById(R.id.signup_btn);

          countryEt.setOnClickListener(this);
        setData();


        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_items,bussiness);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        Bundle bundle = getIntent().getExtras();

        if (bundle!=null) {

            phone = bundle.getString("phone");
            password = bundle.getString("password");
            code = bundle.getString("code");
            user_id = bundle.getString("user_id");

        }


        cityEt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("=========== action done ==========");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(countryEt.getWindowToken(), 0);
                    scrollview.fullScroll(View.FOCUS_DOWN);
                    cityEt.setCursorVisible(false);
                    return true;
                }
                return false;
            }

        });



        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BussinessSignUpActivity.this,LoginActivity.class));
                finish();
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                nextActivity();
            }
        });
    }

    private void setData() {
        buss_name = getData(getApplicationContext(),"buss_name",null);
        buss_type = getData(getApplicationContext(),"buss_type",null);
        city = getData(getApplicationContext(),"city",null);
        country = getData(getApplicationContext(),"country",null);

        if (buss_name!=null && buss_name.length()>0)
            buss_nameET.setText(buss_name);

        if (buss_type!=null && buss_type.length()>0)
        {
            if (buss_type.equals("Services"))
                spin.setSelection(1);
            else if (buss_type.equals("Retail"))
                spin.setSelection(2);
            else
                spin.setSelection(0);
        }

        if (city!=null && city.length()>0)
            cityEt.setText(buss_name);

        if (country!=null && country.length()>0)
            countryEt.setText(country);
    }

    private void nextActivity() {


        email = emailEt.getText().toString();
        name = fullnameEt.getText().toString();
         buss_name = buss_nameET.getText().toString();
         city = cityEt.getText().toString();
         country = countryEt.getText().toString();
         buss_type = spin.getSelectedItem().toString();


        String empatt = "^[a-zA-Z0-9_.]+@[a-zA-Z]+\\.[a-zA-Z]+$";

        if (name.length()==0)
            fullnameEt.setError("Field Required");

        if (email.length()>0) {
            boolean b4 = isMatch(email, empatt);
            if (!b4) {
                emailEt.setError("Invalid Email ID");
                return;
            }
        }

         if (buss_name.length()==0)
             buss_nameET.setError("Field Required");

         if (city.length()==0)
             cityEt.setError("Field Required");

         if (country.length()==0)
             countryEt.setError("Field Required");


        if (buss_type.equals("Services"))
            buss_type="1";

        else if (buss_type.equals("Retails"))
            buss_type="2";
        else {
            buss_type="0";
            Toast.makeText(getApplicationContext(), "Select Bussiness Type", Toast.LENGTH_SHORT).show();
        }

        if (buss_name.length()>0&&city.length()>0&&country.length()>0&& !buss_type.equals("0"))
            registerUser(buss_name,city,country,buss_type,name,phone,code,password,email,user_id);
    }

    private void registerUser(String buss_name, String city, String country, String buss_type, String name,
                              String phone, String code, String password, String email, String user_id) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(BussinessSignUpActivity.this, "Please wait ...",
                "Signup", true);
        ringProgressDialog.setCancelable(false);
        params.put("bk_userid", user_id);
        params.put("business_name", buss_name);
        params.put("full_name", name);
        params.put("business_location", city);
        params.put("business_country",  country);
        params.put("business_pass", password);
        params.put("business_phone", code+phone);
        params.put("business_type", buss_type);
        params.put("user_device_type", "1");
        params.put("user_device_token", "5482746982y");
        params.put("user_device_id", "3466135346");
        params.put("business_email", email);

        System.err.println(params);

        client.setTimeout(20*1000);

        client.post(BASE_URL_NEW + "signup", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* signup response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(BussinessSignUpActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),"Signup Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {

        buss_name = buss_nameET.getText().toString();
        city = cityEt.getText().toString();
        country = countryEt.getText().toString();
        buss_type = spin.getSelectedItem().toString();


        saveData(getApplicationContext(),"buss_name",buss_name);
        saveData(getApplicationContext(),"buss_type",buss_type);
        saveData(getApplicationContext(),"city",city);
        saveData(getApplicationContext(),"country",country);

      startActivity(new Intent(BussinessSignUpActivity.this,SignupActivity.class)
              .putExtra("name",name)
              .putExtra("phone",phone)
              .putExtra("email",email)
              .putExtra("password",password)
              .putExtra("code",code)

      );
      finish();
    }

    @Override
    public void onClick(View v) {

        System.out.println("============ clicked country ============");

       final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {

                System.out.println("========== selected country =========");
                System.out.println(name);
              countryEt.setText(name);
              picker.dismiss();

            }
        });
    }
}