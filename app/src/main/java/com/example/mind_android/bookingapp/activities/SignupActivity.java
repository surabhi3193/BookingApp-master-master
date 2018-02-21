package com.example.mind_android.bookingapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.smsPack.Sms;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;


public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText  phoneNoET, passEt, cPasswordEt;
    TextView spin;
    String cpassword,phone,password,code;
    int count =0;
    private RelativeLayout next_signup;

    public static boolean isMatch(String s, String patt) {
        Pattern pat = Pattern.compile(patt);
        Matcher m = pat.matcher(s);
        return m.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        System.out.println("============== onCreate============");
         next_signup = findViewById(R.id.next_signup);


        phoneNoET = findViewById(R.id.phoneEt);
        passEt = findViewById(R.id.passEt);
        cPasswordEt = findViewById(R.id.cPassEt);



        next_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextActivity();
            }
        });


         spin = findViewById(R.id.spinner1);
        spin.setOnClickListener(this);

    }

    private void nextActivity() {


         phone = phoneNoET.getText().toString();
         code=spin.getText().toString();
         password = passEt.getText().toString();
         cpassword = cPasswordEt.getText().toString();



        if (code.length()==0)
         Toast.makeText(getApplicationContext(),"Select Dial Code",Toast.LENGTH_SHORT).show();

        if (phone.length()==0)
            phoneNoET.setError("Field Required");

        if (password.length()==0)
            passEt.setError("Field Required");

       if (cpassword.length()==0)
            cPasswordEt.setError("Field Required");

     if (password.length()>0 && cpassword.length()>0 && password.equals(cpassword)) {
         if (count == 0)
         {

             sendOtp(phone, code, password);
         }
     }
        else
        {
            cPasswordEt.setError("Password Mismatch");
        }

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
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("============== onResume============");

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



    public  void sendOtp( final  String phone, final  String code, final  String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params  = new RequestParams();

        params.put("business_phone",code+phone);
        params.put("business_pass", password);

        System.out.println(params);

        client.post(BASE_URL_NEW + "send_otp", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* signup response ***");
                System.out.println(response);
//                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(),
                                response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else
                    {
                        System.out.println("======= otp sent successfully ============");

                        count++;
                        next_signup.setBackground(getResources().getDrawable(R.drawable.grey_rect_btn));
                        String userid = response.getString("bk_userid");
                                startActivity(new Intent(SignupActivity.this, Sms.class)
                                .putExtra("phone",phone)
                                .putExtra("code",code)
                                .putExtra("password",password)
                                .putExtra("user_id",userid));

                        finish();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                ringProgressDialog.dismiss();
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
            }
        });
    }

}
