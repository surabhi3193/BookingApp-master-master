package com.example.mind_android.bookingapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    EditText  phoneNoET, passEt;
    TextView spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNoET = findViewById(R.id.phoneEt);
        passEt = findViewById(R.id.passEt);
        TextView create_account = findViewById(R.id.create_account);


        RelativeLayout login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextActivity();
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          startActivity(new Intent(LoginActivity.this,SignupActivity.class));
          finish();
            }
        });


         spin = findViewById(R.id.spinner1);
       spin.setOnClickListener(this);

    }


    private void nextActivity() {

        String phone = spin.getText().toString()+phoneNoET.getText().toString();
        String password = passEt.getText().toString();
       if (phone.length()!=0 && password.length()!=0)
       {
           loginUser(phone,password);
       }
        else
        {
           if (phone.length()==0)
               phoneNoET.setError("Field Required");
           else if(password.length()==0)
                   passEt.setError("Field Required");
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    private void loginUser(final  String phone,final  String password) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(LoginActivity.this, "Please wait ...",
                "Login..", true);
        ringProgressDialog.setCancelable(false);

        params.put("business_phone", phone);
        params.put("business_pass", password);
        params.put("user_device_type", "1");
        params.put("user_device_token", "5482746982y");
        params.put("user_device_id", "3466135346");

        System.out.println(params);

        client.post(BASE_URL_NEW + "login", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* signup response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();

                    } else
                        {
                            String user_id =response.getString("bk_userid");
                        Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_SHORT).show();
                        saveData(getApplicationContext(),"login","1");
                        saveData(getApplicationContext(),"user_id",user_id);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this,EnterLoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {

                System.out.println("========== selected country =========");
                System.out.println(name + " / " + code + "/" +dialCode);
                spin.setText(dialCode);
                picker.dismiss();
            }
        });
    }
}
