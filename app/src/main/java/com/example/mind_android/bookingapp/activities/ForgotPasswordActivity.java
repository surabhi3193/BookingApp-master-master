package com.example.mind_android.bookingapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;

public class ForgotPasswordActivity extends AppCompatActivity {

    private String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView submit_btn = findViewById(R.id.submit_btn);
        final TextView passEt = findViewById(R.id.passEt);
        final TextView confirmEt = findViewById(R.id.confirmEt);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_id = bundle.getString("user_id");
        }


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = passEt.getText().toString().trim();
                String cpass = passEt.getText().toString().trim();

                if (pass.length() != 0 && cpass.length() != 0) {
                    passEt.setError(null);
                    confirmEt.setError(null);
                    updatePassword(pass, user_id);
                } else {
                    if (pass.length() == 0) {
                        passEt.setError("Field required");
                    }

                    if (cpass.length() == 0)
                        confirmEt.setError("Field required");
                }

            }
        });
    }

    private void updatePassword(String password, String user_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(ForgotPasswordActivity.this, "Please wait ...",
                "Processing", true);
        ringProgressDialog.setCancelable(false);

        params.put("new_password", password);
        params.put("bk_userid", user_id);
        System.out.println(params);

        client.setConnectTimeout(30000);
        client.post(BASE_URL_NEW + "update_password", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* passowrd update response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    } else {

                        Toast.makeText(ForgotPasswordActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

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


}
