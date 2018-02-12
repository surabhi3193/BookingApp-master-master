package com.example.mind_android.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText emailEt, fullnameEt, phoneNoET, passEt, cPasswordEt;
    TextView spin;
    String cpassword,name,phone,email,password,code;

    public static boolean isMatch(String s, String patt) {
        Pattern pat = Pattern.compile(patt);
        Matcher m = pat.matcher(s);
        return m.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        System.out.println("============== onCreate============");
        RelativeLayout next_signup = findViewById(R.id.next_signup);

        emailEt = findViewById(R.id.emailEt);
        fullnameEt = findViewById(R.id.nameET);
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

    private void setData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            name=bundle.getString("name");
            email=bundle.getString("email");
            phone=bundle.getString("phone");
            code=bundle.getString("code");
            password=bundle.getString("password");


            fullnameEt.setText(name);
            phoneNoET.setText(phone);
            emailEt.setText(email);
            passEt.setText(password);
            cPasswordEt.setText(password);
            spin.setText(code);
        }
    }

    private void nextActivity() {

         name = fullnameEt.getText().toString();
         phone = phoneNoET.getText().toString();
         code=spin.getText().toString();
         password = passEt.getText().toString();
         cpassword = cPasswordEt.getText().toString();
        email = emailEt.getText().toString();

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
        if (code.length()==0)
         Toast.makeText(getApplicationContext(),"Select Dial Code",Toast.LENGTH_SHORT).show();

        if (phone.length()==0)
            phoneNoET.setError("Field Required");

        if (password.length()==0)
            passEt.setError("Field Required");

       if (cpassword.length()==0)
            cPasswordEt.setError("Field Required");

     if (password.length()>0 && cpassword.length()>0 && password.equals(cpassword))
    {
        startActivity(new Intent(SignupActivity.this, BussinessSignUpActivity.class)
                .putExtra("name",name)
                .putExtra("phone",phone)
                .putExtra("code",code)
                .putExtra("email",email)
                .putExtra("password",password));
        finish();
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
        startActivity(new Intent(SignupActivity.this,EnterLoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("============== onResume============");
        setData();

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
