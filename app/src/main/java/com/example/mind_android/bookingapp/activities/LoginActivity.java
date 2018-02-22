package com.example.mind_android.bookingapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.beans.User;
import com.example.mind_android.bookingapp.smsPack.Sms;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
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


    EditText phoneNoET, passEt;
    TextView spin;
    String phone, code, password;
    RelativeLayout login_btn;
    int count=0;
    boolean isForgotClicked =false;
   private TextView forgot_pass,create_account,btn_text,signup_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        phoneNoET = findViewById(R.id.phoneEt);
        passEt = findViewById(R.id.passEt);
        create_account = findViewById(R.id.create_account);
          forgot_pass = findViewById(R.id.forgot_pass);
         login_btn = findViewById(R.id.login_btn);
        btn_text = findViewById(R.id.btn_text);
        signup_header = findViewById(R.id.signup_header);

        signup_header.setText("Sign In");
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextActivity();
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });


        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passEt.setVisibility(View.GONE);
                create_account.setVisibility(View.GONE);
                forgot_pass.setVisibility(View.GONE);
                btn_text.setText("Reset");
                signup_header.setText("Reset Password");

                isForgotClicked=true;
            }
        });
        spin = findViewById(R.id.spinner1);
        spin.setOnClickListener(this);

    }


    private void nextActivity() {

        phone = phoneNoET.getText().toString();
        code = spin.getText().toString();

        if (phone.length() == 0)
            phoneNoET.setError("Field Required");



        if (isForgotClicked)
        {
            forgotPassword(phone,code);
        }
        else {
            password = passEt.getText().toString();
            if (password.length() == 0)
                passEt.setError("Field Required");


            if (count == 0)
                loginUser(phone, password);
        }
    }

    private void forgotPassword(final String phone, final String code)
    {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(LoginActivity.this, "Please wait ...",
                "Processing", true);
        ringProgressDialog.setCancelable(false);

        params.put("business_phone", code+phone);
        System.out.println(params);

client.setConnectTimeout(30000);
        client.post(BASE_URL_NEW + "forgot_password", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* passowrd response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        count++;
                        login_btn.setBackground(getResources().getDrawable(R.drawable.grey_rect_btn));
                        String userid = response.getString("bk_userid");
                        startActivity(new Intent(getApplicationContext(), Sms.class)
                                .putExtra("phone", phone)
                                .putExtra("code", code)
                                .putExtra("password", "")
                                .putExtra("user_id", userid));
                    }
                    else {
                        count=0;
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void loginUser(final String phone, final String password) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(LoginActivity.this, "Please wait ...",
                "Login..", true);
        ringProgressDialog.setCancelable(false);

         @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        params.put("business_phone", code+phone);
        params.put("business_pass", password);
        params.put("user_device_type", "1");
        params.put("user_device_token", "5482746982y");
        params.put("user_device_id", android_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "login", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* login response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        int user_id = Integer.parseInt(response.getString("bk_userid"));
                        String name = response.getString("full_name");
                        String phone = response.getString("business_phone");
                        String bus_name = response.getString("business_name");
                        String bus_loc = response.getString("business_location");
                        String bus_type = response.getString("business_type");
                        String bus_email = response.getString("business_email");

                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();

                        saveData(getApplicationContext(), "login", "1");
                        saveData(getApplicationContext(), "user_id", String.valueOf(user_id));

                        DatabaseHandler db = new DatabaseHandler(LoginActivity.this);

                        Log.d("Insert: ", "Inserting ..");
                        db.addUser(new User(user_id,name,phone
                                ,bus_name,bus_loc,bus_type,bus_email));

                        db.deleteAllStocks();
                        db.deleteAllSales();
                        db.deleteAllexpense();

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }


                    else if (response.getString("status").equals("2")) {
                        count++;
                        login_btn.setBackground(getResources().getDrawable(R.drawable.grey_rect_btn));
                        String userid = response.getString("bk_user_id");
                        startActivity(new Intent(getApplicationContext(), Sms.class)
                                .putExtra("phone", phone)
                                .putExtra("code", code)
                                .putExtra("password", password)
                                .putExtra("user_id", userid));

                        //verify
                    } else if (response.getString("status").equals("3")) {
                        count++;
                        //bussiness
                        String userid = response.getString("bk_user_id");
                        login_btn.setBackground(getResources().getDrawable(R.drawable.grey_rect_btn));


                        startActivity(new Intent(LoginActivity.this, BussinessSignUpActivity.class)
                                .putExtra("phone", phone)
                                .putExtra("code", code)
                                .putExtra("password", phone)
                                .putExtra("user_id", userid));

                        finish();
                    } else {
                        count=0;
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

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

        if(passEt.getVisibility()==View.GONE)
        {
            passEt.setVisibility(View.VISIBLE);
            create_account.setVisibility(View.VISIBLE);
            forgot_pass.setVisibility(View.VISIBLE);
            isForgotClicked=false;
            btn_text.setText("Login");
            signup_header.setText("Sign In");
            count=0;
            login_btn.setBackground(getResources().getDrawable(R.drawable.blue_rect_btn));


        }
        else {
            startActivity(new Intent(LoginActivity.this, EnterLoginActivity.class));
            finishAffinity();
        }
    }

    @Override
    public void onClick(View v) {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {

                System.out.println("========== selected country =========");
                System.out.println(name + " / " + code + "/" + dialCode);
                code = dialCode;
                spin.setText(dialCode);
                picker.dismiss();
            }
        });
    }
}
