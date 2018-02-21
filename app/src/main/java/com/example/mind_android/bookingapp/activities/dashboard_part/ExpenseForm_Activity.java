package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class ExpenseForm_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_form_);

        final EditText expense_nameEt= findViewById(R.id.expense_nameEt);
        final EditText expense_priceEt= findViewById(R.id.expense_priceEt);
        Button add_btn = findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = expense_nameEt.getText().toString();
                String price = expense_priceEt.getText().toString();

                if (name.length()!= 0 && price.length()!=0)
                {
                    String user_id = getData(ExpenseForm_Activity.this,"user_id","");

                    addExpense(user_id,name,price,"1","");
                }
            }
        });
    }

    private void addExpense(final  String bk_userid, final  String stock_name, final
    String stock_price,final String method_type,final String expense_id)
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(ExpenseForm_Activity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);

        params.put("bk_userid", bk_userid);
        params.put("expanse_name", stock_name);
        params.put("expanse_amout", stock_price);
        params.put("expanse_id", expense_id);
        params.put("method_type", method_type);

        System.out.println(params);

        client.post(BASE_URL_NEW + "add_expanse", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* add response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(ExpenseForm_Activity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else
                    {
                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_SHORT).show();
                        onBackPressed();
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
                ringProgressDialog.dismiss();

                System.err.println(responseString);
            }
        });
    }

}

