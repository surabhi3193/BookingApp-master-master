package com.example.mind_android.bookingapp.activities.dashboard_part;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.BaseActivity;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class ReportActivity extends BaseActivity {

    private String user_id;
    private TextView stock_saletv, other_incometv, proftv,total_Saletv, profitTV, stocktv, expensetv, total_stock;
    private RelativeLayout profile_lay;
    private ImageView plIcon;
    String dirpath;
    ScrollView z;
    Context mContext;
    Button print_btn,summary_btn;
    View u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_report, frameLayout);
         u = findViewById(R.id.rootview);

         z = (ScrollView) findViewById(R.id.scroll);
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
                try {
                    imageToPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

//    private void printPage() {
//
//        // get view group using reference
//        // convert view group to bitmap
//        relativeLayout.setDrawingCacheEnabled(true);
//        relativeLayout.buildDrawingCache();
//        Bitmap bm = relativeLayout.getDrawingCache();
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("image/jpeg");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
//        try {
//            f.createNewFile();
//            FileOutputStream fo = new FileOutputStream(f);
//            fo.write(bytes.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    public void imageToPDF() throws FileNotFoundException {
        try {
            Document document = new Document();
            dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/Bookkeeping_report.pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");

            img.scalePercent(35);
            img.setAlignment(Image.ALIGN_CENTER);
            document.add(img);
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

            document.close();

        } catch (Exception e) {

        }
    }

    private void getReport(final String user_id) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(ReportActivity.this, "Please wait ...",
                "Loading..", true);
        ringProgressDialog.setCancelable(false);

        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

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

                            profitTV.setText(response.getString("total_profit"));
                            proftv.setText(response.getString("total_profit"));
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
        getReport(user_id);
    }


    private void takeScreenShot()
    {
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

        Bitmap b = getBitmapFromView(u,totalHeight,totalWidth);

 File f = new File(Environment.getExternalStorageDirectory() +
         File.separator + "image.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
          b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
        }catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

//       Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(returnedBitmap);
//        Drawable bgDrawable = view.getBackground();
//        if (bgDrawable != null)
//            bgDrawable.draw(canvas);
//        else
//            canvas.drawColor(Color.WHITE);
//        view.draw(canvas);
//        return returnedBitmap;
        Bitmap bitmap = Bitmap.createBitmap(
                totalWidth*2,
                totalHeight*2,
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
