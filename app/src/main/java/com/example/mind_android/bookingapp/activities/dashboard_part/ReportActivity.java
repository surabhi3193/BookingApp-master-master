package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.example.mind_android.bookingapp.beans.Expense;
import com.example.mind_android.bookingapp.beans.Report;
import com.example.mind_android.bookingapp.beans.Sales;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class ReportActivity extends BaseActivity {

    double total_stock_price = 0.00;
    double total_sale_price = 0.00;
    double total_expense_price = 0.00;
    double total_other_price = 0.00;
    private ScrollView z;
    private Button print_btn, summary_btn;
    private View u;
    private String user_id;
    private TextView stock_saletv, other_incometv, proftv, total_Saletv, profitTV, stocktv, expensetv, total_stock;
    private RelativeLayout profile_lay;
    private ImageView plIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_report, frameLayout);
        u = findViewById(R.id.rootview);

        z = findViewById(R.id.scroll);
        summary_btn = findViewById(R.id.summary_btn);
        stock_saletv = findViewById(R.id.saleTV);
        other_incometv = findViewById(R.id.other_incometv);
        total_Saletv = findViewById(R.id.total_Saletv);
        proftv = findViewById(R.id.proftv);
        stocktv = findViewById(R.id.stocktv);
        expensetv = findViewById(R.id.expensetv);
        total_stock = findViewById(R.id.total_stock);
        profitTV = findViewById(R.id.profitTv);


        profile_lay = findViewById(R.id.profile_lay);
        plIcon = findViewById(R.id.plIcon);
        print_btn = findViewById(R.id.print_btn);

        user_id = getData(ReportActivity.this, "user_id", "");
        ImageView back_btn = findViewById(R.id.back_btn);
        print_btn.setVisibility(View.VISIBLE);
        summary_btn.setVisibility(View.VISIBLE);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        summary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ReportActivity.this, SummaryReport.class));

            }
        });


        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                print_btn.setVisibility(View.GONE);
                summary_btn.setVisibility(View.GONE);
                takeScreenShot();
                imageToPDF();

            }
        });

    }

    private void imageToPDF() {
        try {
            Document document = new Document();
            String dirpath = Environment.getExternalStorageDirectory().toString();
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/Bookkeeping_report.pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");

            img.scalePercent(30);
            img.setAlignment(Image.ALIGN_CENTER);
            document.add(img);
            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
            System.out.println("=pdf=======");
            System.out.println(dirpath);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile((new File(dirpath + "/Bookkeeping_report.pdf")));
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);

//
//            //PDF file is now ready to be sent to the bluetooth printer using PrintShare
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setPackage("com.dynamixsoftware.printershare");
//            i.setDataAndType(Uri.fromFile(new File(dirpath + "/NewPDF.pdf")),"application/pdf");
//            startActivity(i);


        } catch (Exception e) {
            e.printStackTrace();
            print_btn.setVisibility(View.VISIBLE);
            summary_btn.setVisibility(View.VISIBLE);
            Toast.makeText(ReportActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void getReport(final String user_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(ReportActivity.this, "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);


        params.put("bk_userid", user_id);

        System.out.println(params);
        client.setConnectTimeout(10000);

        client.post(BASE_URL_NEW + "report", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* report response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("1")) {
                        if (response.getString("message").equals("Profit")) {
                            profitTV.setText(response.getString("total_profit"));
                            proftv.setText(response.getString("total_profit"));
                            plIcon.setImageResource(R.drawable.profit);
                            proftv.setTextColor(getResources().getColor(R.color.colorAccent));
                            profile_lay.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        } else {


                            proftv.setText("("+response.getString("total_profit")+")");
                            profitTV.setText(response.getString("total_profit"));
                            plIcon.setImageResource(R.drawable.loss);
                            proftv.setTextColor(getResources().getColor(R.color.Red));

                            profile_lay.setBackgroundColor(getResources().getColor(R.color.Crimson));
                        }

                        stock_saletv.setText(response.getString("stock_per_sale"));
                        other_incometv.setText(response.getString("service_sale"));
                        total_Saletv.setText(response.getString("total_sale"));
                        stocktv.setText(response.getString("stock_purches"));

                        expensetv.setText(response.getString("expenses"));
                        total_stock.setText(response.getString("cost_of_sale"));

                    }


                } catch (Exception e) {
                    ringProgressDialog.dismiss();
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        print_btn.setVisibility(View.VISIBLE);
        summary_btn.setVisibility(View.VISIBLE);

        if (isNetworkAvailable(ReportActivity.this))
            getReport(user_id);

        else
            getReportFromLocal();
    }

    private void getReportFromLocal() {
        DatabaseHandler db = new DatabaseHandler(ReportActivity.this);
        JSONArray jArray = new JSONArray();

        // Reading all stocks
        Log.d("Reading: ", "Reading all Stocks..");
        List<Stock> stocks = db.getAllStocksExcept2();

        try {
            for (Stock cn : stocks) {
                JSONObject jobj = new JSONObject();

                String log = "Id: " + cn.get_id() +
                        " ,Name: " + cn.get_name() +
                        " ,qty: " + cn.get_qty() +
                        " ,unit price : " + cn.get_unit_per_price() +
                        " ,price : " + cn.get_price();
                // Writing Contacts to log
                Log.d("Name: ", log);

                jobj.put("stock_id", cn.get_id());
                jobj.put("stock_name", cn.get_name());
                jobj.put("stock_qty", cn.get_qty());
                jobj.put("stock_per_price", cn.get_unit_per_price());
                jobj.put("stock_price", cn.get_price());
                if (!cn.get_qty().equalsIgnoreCase("0")) {
                    jArray.put(jobj);
                    double price = Double.parseDouble(cn.get_price());

                    total_stock_price = total_stock_price + price;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<Sales> saleslist = db.getAllSalesExcept2();
        try {
            for (Sales cn : saleslist) {
                String log = "Id: " + cn.get_id() +
                        " ,Name: " + cn.get_name() +
                        " ,qty: " + cn.get_qty() +
                        " ,unit price : " + cn.get_unit_per_price() +
                        " ,price : " + cn.get_price();
                // Writing Contacts to log
                Log.d("Name: ", log);
                double price = Double.parseDouble(cn.get_price());

                if (cn.get_sale_type().equalsIgnoreCase("2")) {

                    total_sale_price = total_sale_price + price;

                } else if (cn.get_sale_type().equalsIgnoreCase("1")) {
                    total_other_price = total_other_price + price;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<Expense> expense = db.getAllExpenseExcept2();
        try {
            for (Expense cn : expense) {
                String log = "Id: " + cn.get_id() +
                        " ,Name: " + cn.get_name() +
                        " ,qty: " + cn.get_date() +
                        " ,price : " + cn.get_price();
                // Writing Contacts to log
                Log.d("Name: ", log);


                double price = Double.parseDouble(cn.get_price());

                total_expense_price = total_expense_price + price;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Report report = new Report();
        report.setTotal_sale(String.valueOf(total_sale_price));
        report.setTotal_stock(String.valueOf(total_stock_price));
        report.setTotal_expense(String.valueOf(total_expense_price));
        report.setTotal_other_serice(String.valueOf(total_other_price));

        int count = db.getReportCount();

        if (count == 0)
            db.addReport(report);
        else
            db.updateReport(report);


        stock_saletv.setText(String.valueOf(total_sale_price));//stock sale
        other_incometv.setText(String.valueOf(total_other_price));//other

        double total = total_sale_price + total_other_price;
        total_Saletv.setText(String.valueOf(total)); //total 1

        stocktv.setText(String.valueOf(total_stock_price));

        expensetv.setText(String.valueOf(total_expense_price));
        double total2 = total_expense_price + total_stock_price;
        total_stock.setText(String.valueOf(total2));
    }


    private void takeScreenShot() {
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

        Bitmap b = getBitmapFromView(u, totalHeight, totalWidth);
        try {
        MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");


        File f = new File(Environment.getExternalStorageDirectory() +
                File.separator + "image.jpg");

        FileOutputStream fos;

            fos = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }
        catch (Exception e) {
            Toast.makeText(ReportActivity.this, "Please Allow Storage Permission From Settings ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            print_btn.setVisibility(View.VISIBLE);
            summary_btn.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap bitmap = Bitmap.createBitmap(
                totalWidth * 2,
                totalHeight * 2,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.scale(2.0f, 2.0f);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);

        z.getChildAt(0).draw(c);
        return bitmap;


    }
}
