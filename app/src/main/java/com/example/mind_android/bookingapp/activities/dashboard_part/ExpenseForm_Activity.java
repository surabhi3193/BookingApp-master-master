package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.beans.Expense;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addExpense;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class ExpenseForm_Activity extends AppCompatActivity {
    private String method_type="1";
String expense_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_form_);

        final EditText expense_nameEt= findViewById(R.id.expense_nameEt);
        final EditText expense_priceEt= findViewById(R.id.expense_priceEt);
        Button add_btn = findViewById(R.id.add_btn);


        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            method_type=bundle.getString("method_type");

            if (method_type.equals("2"))
            {
                expense_id=bundle.getString("expanse_id");

                expense_nameEt.setText(bundle.getString("expanse_name"));
                add_btn.setText("Save");
            }
            else
                add_btn.setText("Add Expense");

        }

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = expense_nameEt.getText().toString();
                String price = expense_priceEt.getText().toString();

                if (name.length()!= 0 && price.length()!=0)
                {
                    String user_id = getData(ExpenseForm_Activity.this,"user_id","");

                    if (isNetworkAvailable(ExpenseForm_Activity.this))
                    addExpense(ExpenseForm_Activity.this,user_id,name,price,method_type,expense_id,"");

                    else
                    {
                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                                format(Calendar.getInstance().getTime());

                        addExpenseinLocal(user_id,name,date,price,method_type,expense_id);

                    }
                }
            }
        });
    }

    private void addExpenseinLocal(final String bk_userid, final String stock_name,
                                 final String date, final
                                 String stock_amount, final String method_type,
                                 final String stock_id) 
    {
        DatabaseHandler db = new DatabaseHandler(ExpenseForm_Activity.this);

        if (method_type.equals("1")) {
            Log.d("Insert: ", "Inserting .. Stock");
            Long tsLong = System.currentTimeMillis() / 1000;
            int id = Integer.parseInt(tsLong.toString());

            db.addExpense(new Expense(id, stock_name, date, stock_amount, 0));
            onBackPressed();
        }

        else if (method_type.equals("2")) {
            int id = Integer.parseInt(stock_id);
            Log.d("Update: ", "Updating .. Stock");
            db.updateExpense(new Expense(id, stock_name, date, stock_amount, 0), stock_id);
            onBackPressed();
        }
    }

}

