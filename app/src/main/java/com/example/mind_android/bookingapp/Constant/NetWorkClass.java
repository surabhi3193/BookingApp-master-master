package com.example.mind_android.bookingapp.Constant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mind_android.bookingapp.activities.LoginActivity;
import com.example.mind_android.bookingapp.activities.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

/**
 * Created by abc on 10/02/2018.
 */

public class NetWorkClass extends AppCompatActivity {
    public  static  String BASE_URL_NEW ="http://mindlbs.com/BookKeeping/index.php/Webservice/";




    private void addStock(final Activity activity,final  String bk_userid, final  String stock_name, final  String stock_qty, final
    String stock_amount, final  String method_type, final  String stock_per_price, final  String stock_id)
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(activity, "Please wait ...",
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
                System.out.println(" ************* signup response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_LONG).show();

                    } else
                    {
                        Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_SHORT).show();
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
