package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.SaletemFragment;
import com.example.mind_android.bookingapp.beans.Sales;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.addStock;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class FormActivity extends AppCompatActivity {
    public static TextView sale_item_name;
    public static String sale_stock = "", sale_item_id = "";
    String price_unit = "0", method_type = "0", stock_id = "";
    private EditText item_nameEt, item_qtEt, itemUnitPriceEt, sale_unit, sale_item_qty;
    private TextView itemPriceEt,dateTV;
    private TextView sale_item_price;
    private int count;

    public static void addsale(final Activity context, final String bk_userid,
                               final String unit, final String stock_qty, final
                               String stock_amount, final String stock_id, final String
                                       stock_name, final String sale_type, final String local) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);

        params.put("bk_userid", bk_userid);
        params.put("sell_quantity", stock_qty);
        params.put("sell_unit_price", unit);
        params.put("sell_price", stock_amount);
        params.put("method_type", 1);
        params.put("sale_type", sale_type);
        if (sale_type.equals("1"))
            params.put("stock_name", stock_name);

        else if (sale_type.equals("2"))
            params.put("stock_id", stock_id);

        System.out.println(params);

        client.post(BASE_URL_NEW + "add_sales", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println(" ************* add response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
                        String currentDateandTime = sdf.format(new Date());
                        if (sale_type.equals("2")) {
                            String id = response.getString("stock_id");
                            String name = response.getString("stock_name");
                            String qty = response.getString("sell_quantity");
                            String per_price = response.getString("sell_unit_price");
                            String price = response.getString("sell_price");

                            Sales sales = new Sales();
                            sales.set_id(Integer.parseInt(id));
                            sales.set_name(name);
                            sales.set_qty(qty);
                            sales.set_unit_per_price(per_price);
                            sales.set_price(price);
                            sales.set_sale_type("2");
                            sales.set_date(currentDateandTime);
                            sales.set_price(price);
                            sales.set_status(1);
                            DatabaseHandler db = new DatabaseHandler(context);

                            if (local.equalsIgnoreCase("local")) {
                                db.updateSales(sales, stock_id);
                                db.updateStock(new Stock((Integer.parseInt(id)), name, qty,
                                        per_price, price, currentDateandTime, "", 1), id);
                            } else {
                                db.addSales(sales);
                                db.updateStock(new Stock((Integer.parseInt(id)), name, qty,
                                        per_price, price, currentDateandTime, "", 1), id);
                            }

                        } else if (sale_type.equalsIgnoreCase("1")) {
                            String id = response.getString("stock_id");
                            String name = response.getString("stock_name");
                            String qty = "";
                            String per_price = "";
                            String price = response.getString("sell_price");

                            Sales sales = new Sales();
                            sales.set_id(Integer.parseInt(id));
                            sales.set_name(name);
                            sales.set_qty(qty);
                            sales.set_unit_per_price(per_price);
                            sales.set_price(price);
                            sales.set_sale_type("1");
                            sales.set_date(currentDateandTime);
                            sales.set_price(price);
                            sales.set_status(1);
                            DatabaseHandler db = new DatabaseHandler(context);

                            if (local.equalsIgnoreCase("local")) {
                                db.updateSales(sales, stock_id);
                            } else
                                db.addSales(sales);
                        }

                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
//                      if (!local.equalsIgnoreCase("local"));
                        context.onBackPressed();
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
                System.out.println(responseString);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        count = 0;
        Button add_stock = findViewById(R.id.add_btn);

        LinearLayout saleForm = findViewById(R.id.saleForm);
        LinearLayout stockform = findViewById(R.id.stockform);
        TextView headTv = findViewById(R.id.headTv);
        item_nameEt = findViewById(R.id.item_nameTv);
        item_qtEt = findViewById(R.id.itemQuant_TV);
        itemPriceEt = findViewById(R.id.item_price_tv);
        itemUnitPriceEt = findViewById(R.id.item_price_unit);
        dateTV = findViewById(R.id.dateTV);


        sale_unit = findViewById(R.id.sale_unit);
        sale_item_name = findViewById(R.id.sale_item_name);
        sale_item_price = findViewById(R.id.sale_item_price);
        sale_item_qty = findViewById(R.id.sale_item_qty);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dateTV, myCalendar);
            }

        };

        dateTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FormActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        final FragmentManager fm = getFragmentManager();
        final SaletemFragment p = new SaletemFragment();

        sale_item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.show(fm, "sale Item");
            }
        });

        ImageView back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            String act = bundle.getString("Activity");
            stock_id = bundle.getString("stock_id");
            String stock_name = bundle.getString("stock_name");
            String stock_price = bundle.getString("stock_price");
            String stock_qty = bundle.getString("stock_qty");
            String stock_per_price = bundle.getString("stock_per_price");

            assert act != null;
            switch (act) {
                case "saleStocks":
                    count = 1;
                    headTv.setText(R.string.sale);
                    add_stock.setText(R.string.sale);
                    stockform.setVisibility(View.GONE);
                    saleForm.setVisibility(View.VISIBLE);
                    break;

                case "addStocks":
                    count = 2;
                    method_type = "1";
                    headTv.setText(R.string.addstock);
                    add_stock.setText(R.string.addstock);
                    stockform.setVisibility(View.VISIBLE);
                    saleForm.setVisibility(View.GONE);
                    break;

                case "editStocks":
                    System.out.println("============ in edit stock ========");
                    System.out.println(stock_name);

                    System.out.println(stock_per_price);
                    count = 3;
                    method_type = "2";
                    headTv.setText(R.string.edit_stock);
                    add_stock.setText(R.string.save);
                    item_nameEt.setText(stock_name);
                    itemPriceEt.setText(stock_price);
                    itemUnitPriceEt.setText(stock_per_price);
                    item_qtEt.setText(stock_qty);


                    item_nameEt.setCursorVisible(false);
                    item_nameEt.setKeyListener(null);
                    item_qtEt.requestFocus();
                    stockform.setVisibility(View.VISIBLE);
                    saleForm.setVisibility(View.GONE);
                    break;
            }
        }

        item_qtEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String val = itemUnitPriceEt.getText().toString();

                if (val.length() == 0)
                    val = "0.00";


                String pr = s.toString();
                double total = 0.00;
                if (pr.length() == 0)
                    pr = "0.00";
                try {
                    double quant = Double.parseDouble(val);
                    int unit = Integer.parseInt(pr);
                    total = (quant * unit);

                    System.out.println("===== stock details ===== ");
                    System.out.println(quant + " u");
                    System.out.println(unit + " q");
                    System.out.println(total + " t");
                } catch (Exception e) {
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


                if (val.length() == 0)
                    val = "0.000";


                String pr = s.toString();
                float total = 0;
                if (pr.length() == 0)
                    pr = "0.00";
                try {
                    int quant = Integer.parseInt(val);
                    double unit = Double.parseDouble(pr);
                    total = (float) (quant * unit);

                    System.out.println("===== stock details ===== ");
                    System.out.println(quant + " q");
                    System.out.println(unit + " u");
                    System.out.println(total + " t");
                } catch (Exception e) {
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
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

                if (val.length() == 0)
                    val = "0.00";


                String pr = s.toString();
                double total = 0.00;
                if (pr.length() == 0)
                    pr = "0.00";
                try {
                    double quant = Double.parseDouble(val);
                    int unit = Integer.parseInt(pr);
                    total = (quant * unit);

                    System.out.println("===== stock details ===== ");
                    System.out.println(quant + " u");
                    System.out.println(unit + " q");
                    System.out.println(total + " t");
                } catch (Exception e) {
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


                if (val.length() == 0)
                    val = "0.000";


                String pr = s.toString();
                float total = 0;
                if (pr.length() == 0)
                    pr = "0.00";
                try {
                    int quant = Integer.parseInt(val);
                    double unit = Double.parseDouble(pr);
                    total = (float) (quant * unit);

                    System.out.println("===== stock details ===== ");
                    System.out.println(quant + " q");
                    System.out.println(unit + " u");
                    System.out.println(total + " t");
                } catch (Exception e) {
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
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

                if (count == 2 || count == 3)
                    stock(method_type);
                else if (count == 1)
                    sale();

            }
        });
    }

    private void addStockinLocal(final String stock_name,
                                 final String stock_qty, final
                                 String stock_amount, final String method_type, final String stock_per_price,
                                 final String stock_id) {
        DatabaseHandler db = new DatabaseHandler(FormActivity.this);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        if (method_type.equals("1")) {
            Log.d("Insert: ", "Inserting .. Stock");
            Long tsLong = System.currentTimeMillis() / 1000;
            int id = Integer.parseInt(tsLong.toString());

            boolean isExist = db.checkStock(stock_name);
            if (isExist)
            {

                Stock stock = db.getStock(stock_name);

                id = stock.get_id();
                String name = stock.get_name();
                String qty = String.valueOf(Integer.parseInt(stock_qty) + Integer.parseInt(stock.get_qty()));
                String per_price = String.valueOf((Double.parseDouble(stock_per_price) + Double.parseDouble(stock.get_unit_per_price())) / 2);
                String total_amount = String.valueOf(Double.parseDouble(stock_amount) + Double.parseDouble(stock.get_price()));

                db.updateStock(new Stock(id, name, qty, per_price, total_amount, currentDateandTime, "", 3), stock_id);

            }
            else
                db.addStock(new Stock(id, stock_name, stock_qty, stock_per_price, stock_amount, currentDateandTime, "", 0));

            onBackPressed();
        }

        else if (method_type.equals("2")) {
            int id = Integer.parseInt(stock_id);
            Stock stock = db.getStock(stock_name);
            int status = stock.get_status();

            Log.d("Update: ", "Updating .. Stock");
            if (status==0)
            db.updateStock(new Stock(id, stock_name, stock_qty, stock_per_price, stock_amount, currentDateandTime, "", 0), stock_id);


            if (status==0)
                db.updateStock(new Stock(id, stock_name, stock_qty, stock_per_price, stock_amount, currentDateandTime, "", 3), stock_id);
            onBackPressed();
        }
    }

    private void sale() {
        String unit = sale_unit.getText().toString();
        String name = sale_item_name.getText().toString();
        String qty = sale_item_qty.getText().toString();
        String price = sale_item_price.getText().toString();
        String date = dateTV.getText().toString();

        if (name.length() == 0)
            sale_item_name.setError("Stock name required");

        if (qty.length() == 0 || qty.equals("0"))
            sale_item_qty.setError("Stock required");

        if (unit.length() == 0 || unit.equalsIgnoreCase("0"))
            sale_unit.setError("Stock unit  required");
        if (price.length() == 0 || price.equalsIgnoreCase("0.0"))
            sale_item_price.setError("Price required");
        if (date.length() == 0)
            Toast.makeText(FormActivity.this,"Select Date",Toast.LENGTH_SHORT).show();


        System.out.println(" ========= params  for enter sale =========");
        System.out.println(name);
        System.out.println(unit + " unit");
        System.out.println(qty + " qty");
        System.out.println(price + " price");


        if (!price.equalsIgnoreCase("0.0"))
        {
            if (name.length() > 0 && qty.length() > 0
                    && price.length() > 0
                    && unit.length() > 0
                    && date.length() > 0
                    && !qty.equalsIgnoreCase("0")
                    && !unit.equalsIgnoreCase("0")
                    ) {
                String user_id = getData(FormActivity.this, "user_id", "");

                if (isNetworkAvailable(FormActivity.this))
                    addsale(FormActivity.this, user_id, unit, qty, price, sale_item_id, "", "2", "");

                else {
                    Toast.makeText(FormActivity.this,"Internet Connection Unavailable, Try Again ",Toast.LENGTH_SHORT).show();

//                    addsaleToLocal(FormActivity.this, name, qty, price, unit, "2");
                }
            }
        }
    }

    private void stock(String method_type) {
        String name = item_nameEt.getText().toString().toUpperCase();
        String qty = item_qtEt.getText().toString();
        String price = itemPriceEt.getText().toString();

        if (name.length() == 0)
            item_nameEt.setError("Stock name required");

        if (qty.length() == 0 || qty.equals("0"))
            item_qtEt.setError("Stock required");

        if (price.length() == 0 || price.equals("0.0"))
            itemUnitPriceEt.setError("Price required");


        if (name.length() > 0 && qty.length() > 0 && price.length() > 0 && !price.equalsIgnoreCase("0.0")) {
            String user_id = getData(FormActivity.this, "user_id", "");
            price_unit = itemUnitPriceEt.getText().toString();

            if (!isNetworkAvailable(FormActivity.this)) {
                addStockinLocal(name, qty, price, method_type, price_unit, stock_id);

            } else {
                addStock(FormActivity.this, user_id, name, qty, price, method_type, price_unit, stock_id, "");
            }

        }
    }

    private void updateLabel(TextView textEdit, Calendar myCalendar) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textEdit.setText(sdf.format(myCalendar.getTime()));
    }

}