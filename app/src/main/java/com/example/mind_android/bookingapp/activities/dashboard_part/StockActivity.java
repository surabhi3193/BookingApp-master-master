package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class StockActivity extends AppCompatActivity {
   private TableLayout tableLayout ;
   private int count;
   private LinearLayout addLayout,listLayout;

   private Button add_stock;
   private EditText item_nameEt,item_qtEt,itemPriceEt,itemUnitPriceEt;
   ScrollView scrollview;
   String total_amt="0",price_unit="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        tableLayout =findViewById(R.id.tableLay);
        addLayout =findViewById(R.id.addLayout);
        listLayout =findViewById(R.id.listLayout);

        add_stock=findViewById(R.id.add_btn);
        item_nameEt =findViewById(R.id.item_nameTv);
        item_qtEt =findViewById(R.id.itemQuant_TV);
        itemPriceEt =(EditText) findViewById(R.id.item_price_tv);
        itemUnitPriceEt =findViewById(R.id.item_price_unit);


       count= tableLayout.getChildCount();
       showStock();

       TextView addTv = findViewById(R.id.addTv);

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listLayout.setVisibility(View.GONE);
                addLayout.setVisibility(View.VISIBLE);
            }
        });



        itemUnitPriceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            String val = item_qtEt.getText().toString();

            if (val.length()==0)
                item_qtEt.setError("Enter stock quantity");
            else {

                String pr = s.toString();
                if (pr.length()==0)
                    pr="0";

                int quant = Integer.parseInt(val);
                int unit = Integer.parseInt(pr);
                int total = (quant * unit);
                System.out.println("===== stock details ===== ");
                System.out.println(quant + " q");
                System.out.println(unit + " u");
                System.out.println(total + " t");

                itemPriceEt.setText(String.valueOf(total));
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        add_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = item_nameEt.getText().toString();
                String qty = item_qtEt.getText().toString();

                if (name.length()==0)
                    item_nameEt.setError("Stock name required");

                if (qty.length()==0)
                    item_nameEt.setError("Stock name required");

                else
                {
                    if (name.length()>0 && qty.length()>0);
                    {
                        String user_id = getData(StockActivity.this,"user_id","");
                       total_amt= itemPriceEt.getText().toString();
                       qty= item_qtEt.getText().toString();
                       name= item_nameEt.getText().toString();
                       price_unit= itemUnitPriceEt.getText().toString();


                        addStock(user_id,name, qty,total_amt,"1",price_unit,"");
                    }
                }


            }
        });


    }

    private void addStock(final  String bk_userid, final  String stock_name, final  String stock_qty, final
    String stock_amount, final  String method_type, final  String stock_per_price,
                          final  String stock_id)
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(StockActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);

        params.put("bk_userid", bk_userid);
        params.put("stock_name", stock_name);
        params.put("stock_qty", stock_qty);
        params.put("stock_amount", stock_amount);
        params.put("method_type", method_type);
        params.put("stock_per_price", stock_per_price);
        params.put("stock_id", stock_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "add_stock", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* add response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(StockActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();

                    } else
                    {
                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_SHORT).show();
                   addLayout.setVisibility(View.GONE);
                   listLayout.setVisibility(View.VISIBLE);
                   showStock();

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


    private void showStock()
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(StockActivity.this, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);

        String user_id = getData(StockActivity.this,"user_id","");

        params.put("bk_userid", user_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "stock_list", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                System.out.println(" ************* show stock  response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(StockActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();

                    } else
                    {
                   addLayout.setVisibility(View.GONE);
                   listLayout.setVisibility(View.VISIBLE);

                   JSONArray jArray = response.getJSONArray("stocks");

                   addRow(jArray);
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

    private void addRow(JSONArray jsonArray) {

        for (int i =0;i<jsonArray.length();i++)

        {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name =obj.getString("stock_name");
                String qty =obj.getString("stock_qty");
                String price =obj.getString("stock_price");

                TableRow row= new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);


                TextView   srno = new TextView(this);
                srno.setLayoutParams(new TableLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
                TextView nameTv = new TextView(this);
                TextView qtyTv = new TextView(this);
                TextView amntTv = new TextView(this);


                LinearLayout linearLayout = new LinearLayout(StockActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                ImageView edit = new ImageView(StockActivity.this);
                edit.setBackgroundResource(R.drawable.edit);
                linearLayout.addView(edit);

                ImageView dlt = new ImageView(StockActivity.this);
                dlt.setBackgroundResource(R.drawable.delete);
                linearLayout.addView(dlt);
                 String q = String.valueOf(i+1);
                qtyTv.setText(qty);
                srno.setText(String.valueOf(q));
                nameTv.setText(name);
                amntTv.setText(price);

                row.addView(qtyTv);
                row.addView(srno);
                row.addView(amntTv);
                row.addView(nameTv);
                row.addView(linearLayout);

                tableLayout.addView(row,(i+1));
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }





    }


//    private void addStock() {
//        for (int i = 0; i <2; i++) {
//
//            TableRow row= new TableRow(this);
//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//            row.setLayoutParams(lp);
//            checkBox = new CheckBox(this);
//            tv = new TextView(this);
//            addBtn = new ImageButton(this);
//            addBtn.setImageResource(R.drawable.add);
//            minusBtn = new ImageButton(this);
//            minusBtn.setImageResource(R.drawable.minus);
//            qty = new TextView(this);
//            checkBox.setText("hello");
//            qty.setText("10");
//            row.addView(checkBox);
//            tableLayout.addView(row,i);
//        }
//
//    }


}
