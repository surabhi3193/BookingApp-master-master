package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.LoginActivity;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addStock;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;
import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class FormActivity extends AppCompatActivity {
    private EditText item_nameEt,item_qtEt,itemUnitPriceEt,sale_unit,sale_item_qty,sale_item_price;
    String total_amt="0",price_unit="0",method_type="0",stock_id="";
    private TextView headTv,itemPriceEt,sale_item_name;
    private LinearLayout stockform,saleForm;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        count=0;
        Button add_stock = findViewById(R.id.add_btn);

        saleForm =findViewById(R.id.saleForm);
        stockform =findViewById(R.id.stockform);
        headTv =findViewById(R.id.headTv);
        item_nameEt =findViewById(R.id.item_nameTv);
        item_qtEt =findViewById(R.id.itemQuant_TV);
        itemPriceEt =findViewById(R.id.item_price_tv);
        itemUnitPriceEt =findViewById(R.id.item_price_unit);

        sale_unit =findViewById(R.id.sale_unit);
        sale_item_name =findViewById(R.id.sale_item_name);
        sale_item_price =findViewById(R.id.sale_item_price);
        sale_item_qty =findViewById(R.id.sale_item_qty);

        TextView signout_btn = findViewById(R.id.signout_btn);

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(getApplicationContext(), "login", "0");

                startActivity(new Intent(FormActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            String act = bundle.getString("Activity");
             stock_id = bundle.getString("stock_id");
          String   stock_name = bundle.getString("stock_name");

            if (act.equals("saleStocks"))
            {
                count=1;
                headTv.setText("Sale");
                add_stock.setText("Sale");
                sale_item_name.setText(stock_name);
                stockform.setVisibility(View.GONE);
                saleForm.setVisibility(View.VISIBLE);

            }
            else if (act.equals("addStocks"))
            {
                count=2;
                method_type="1";
                headTv.setText("Add Stocks");
                add_stock.setText("Add Stocks");
                stockform.setVisibility(View.VISIBLE);
                saleForm.setVisibility(View.GONE);

            }
            else if (act.equals("editStocks"))
            {
                System.out.println("============ in edit stock ========");
                System.out.println(stock_name);
                count=3;
                method_type="2";
                headTv.setText(R.string.edit_stock);
                add_stock.setText("SAVE");
                item_nameEt.setText(stock_name);
                item_nameEt.setCursorVisible(false);
                item_nameEt.setKeyListener(null);
                item_qtEt.requestFocus();
                stockform.setVisibility(View.VISIBLE);
                saleForm.setVisibility(View.GONE);
            }
        }

        item_qtEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String val = itemUnitPriceEt.getText().toString();

                if (val.length()==0)
                    val="0.00";


                String pr = s.toString();
                double total=0.00;
                if (pr.length()==0)
                    pr="0.00";
                try
                {
                    double quant = Double.parseDouble(val);
                    int unit = Integer.parseInt(pr);
                    total = (quant * unit);

                    System.out.println("===== stock details ===== ");
                    System.out.println(quant + " u");
                    System.out.println(unit + " q");
                    System.out.println(total + " t");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),"invalid price",Toast.LENGTH_SHORT).show();
                }

                DecimalFormat decim = new DecimalFormat("0.00");
                Double test = Double.parseDouble(decim.format(total));

                itemPriceEt.setText(String.valueOf(test));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        itemUnitPriceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String val = item_qtEt.getText().toString();
                itemUnitPriceEt.setCursorVisible(true);


                if (val.length()==0)
                    val="0.000";


                String pr = s.toString();
                float total=0;
                if (pr.length()==0)
                    pr="0.00";
                try
                {
                    int quant = Integer.parseInt(val);
                    double unit = Double.parseDouble(pr);
                    total = (float) (quant * unit);

                    System.out.println("===== stock details ===== ");
                    System.out.println(quant + " q");
                    System.out.println(unit + " u");
                    System.out.println(total + " t");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),"invalid price",Toast.LENGTH_SHORT).show();
                }

                DecimalFormat decim = new DecimalFormat("0.00");
                Double test = Double.parseDouble(decim.format(total));
                itemPriceEt.setText(String.valueOf(test));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        itemUnitPriceEt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("=========== action done ==========");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(itemPriceEt.getWindowToken(), 0);
                    itemUnitPriceEt.setCursorVisible(false);
                    return true;
                }
                return false;
            }

        });





        sale_item_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String val = sale_unit.getText().toString();

                if (val.length()==0)
                    val="0.00";


                String pr = s.toString();
                double total=0.00;
                if (pr.length()==0)
                    pr="0.00";
                try
                {
                    double quant = Double.parseDouble(val);
                    int unit = Integer.parseInt(pr);
                    total = (quant * unit);

                    System.out.println("===== stock details ===== ");
                    System.out.println(quant + " u");
                    System.out.println(unit + " q");
                    System.out.println(total + " t");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),"invalid price",Toast.LENGTH_SHORT).show();
                }

                DecimalFormat decim = new DecimalFormat("0.00");
                Double test = Double.parseDouble(decim.format(total));

                sale_item_price.setText(String.valueOf(test));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sale_unit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String val = sale_item_qty.getText().toString();
                itemUnitPriceEt.setCursorVisible(true);


                if (val.length()==0)
                    val="0.000";


                String pr = s.toString();
                float total=0;
                if (pr.length()==0)
                    pr="0.00";
                try
                {
                    int quant = Integer.parseInt(val);
                    double unit = Double.parseDouble(pr);
                    total = (float) (quant * unit);

                    System.out.println("===== stock details ===== ");
                    System.out.println(quant + " q");
                    System.out.println(unit + " u");
                    System.out.println(total + " t");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),"invalid price",Toast.LENGTH_SHORT).show();
                }

                DecimalFormat decim = new DecimalFormat("0.00");
                Double test = Double.parseDouble(decim.format(total));
                sale_item_price.setText(String.valueOf(test));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sale_item_price.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("=========== action done ==========");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sale_item_price.getWindowToken(), 0);
                    sale_unit.setCursorVisible(false);
                    return true;
                }
                return false;
            }

        });


        add_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (count==2 || count==3)
                    stock(method_type);
               else if (count==1)
                   sale();

            }
        });
    }

    private void addStockinLocal(final  String bk_userid, final  String stock_name,
                                 final  String stock_qty, final
    String stock_amount, final  String method_type, final  String stock_per_price,
                                 final  String stock_id)
    {
        DatabaseHandler db = new DatabaseHandler(FormActivity.this);

        if (method_type.equals("1"))
        {
            Log.d("Insert: ", "Inserting .. Stock");
            Long tsLong = System.currentTimeMillis()/1000;
            int id = Integer.parseInt(tsLong.toString());

            db.addStock(new Stock(id,stock_name,stock_qty,stock_per_price,stock_amount,0));
                   onBackPressed();
        }

        else if (method_type.equals("2"))
        {
            int id = Integer.parseInt(stock_id);
            Log.d("Update: ", "Updating .. Stock");
            db.updateStock(new Stock(id,stock_name,stock_qty,stock_per_price,stock_amount,0), stock_id);
             onBackPressed();
        }
    }

    private void sale() {
        String unit = sale_unit.getText().toString();
        String name = sale_item_name.getText().toString();
        String qty = sale_item_qty.getText().toString();
        String price = sale_item_price.getText().toString();

        if (name.length()==0)
            sale_item_name.setError("Stock name required");

        if (qty.length()==0 || qty.equals("0"))
            sale_item_qty.setError("Stock required");

        if (unit.length()==0 || unit.equalsIgnoreCase("0"))
            sale_unit.setError("Stock unit  required");
        if (price.length()==0 || price.equals("0"))
            sale_item_price.setError("Price required");

        else
        {
            if (name.length()>0 && qty.length()>0 && price.length()>0 && unit.length()<0);
            {
                String user_id = getData(FormActivity.this,"user_id","");
                qty= sale_item_qty.getText().toString();
                name= sale_item_name.getText().toString();
                price= sale_item_price.getText().toString();
                unit= sale_unit.getText().toString();

                addsale(user_id,unit,qty,price,stock_id);
            }
        }

    }

    private void stock(String method_type) {
        String name = item_nameEt.getText().toString();
        String qty = item_qtEt.getText().toString();
        String price = itemPriceEt.getText().toString();

        if (name.length()==0)
            item_nameEt.setError("Stock name required");

        if (qty.length()==0 || qty.equals("0"))
            item_qtEt.setError("Stock required");

        if (price.length()==0 || price.equals("0.0"))
            itemUnitPriceEt.setError("Price required");

        else
        {
            if (name.length()>0 && qty.length()>0);
            {
                String user_id = getData(FormActivity.this,"user_id","");
                total_amt= itemPriceEt.getText().toString();
                qty= item_qtEt.getText().toString();
                name= item_nameEt.getText().toString();
                price_unit= itemUnitPriceEt.getText().toString();

                if (!isNetworkAvailable(FormActivity.this))
                {
                    addStockinLocal(user_id,name, qty,total_amt, method_type,price_unit,stock_id);

                }
                else
                {
                    addStock(FormActivity.this,user_id,name, qty,total_amt, method_type,price_unit,stock_id, "");
                }

            }
        }
    }






    private void addsale(final  String bk_userid, final  String unit, final  String stock_qty, final
    String stock_amount, final  String stock_id)
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(FormActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);

        params.put("bk_userid", bk_userid);
        params.put("stock_id", stock_id);
        params.put("sell_quantity", stock_qty);
        params.put("sell_unit_price", unit);
        params.put("sell_price", stock_amount);
        params.put("method_type", 1);

        System.out.println(params);

        client.post(BASE_URL_NEW + "add_sales", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {

                System.out.println(" ************* add response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(FormActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

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
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);            }
        });
    }

}
