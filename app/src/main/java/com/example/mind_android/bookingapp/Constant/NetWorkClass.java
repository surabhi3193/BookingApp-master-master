package com.example.mind_android.bookingapp.Constant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mind_android.bookingapp.activities.dashboard_part.SalesActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.StockActivity;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

/**
 * Created by abc on 10/02/2018.
 */

public class NetWorkClass extends AppCompatActivity {
//    public  static  String BASE_URL_NEW ="http://mindlbs.com/BookKeeping/index.php/Webservice/";
    public  static  String BASE_URL_NEW ="http://18.218.89.83/BookKeeping/index.php/Webservice/";

    DatabaseHandler db  = new DatabaseHandler(NetWorkClass.this);

    public static void addStock(final Activity context, final String bk_userid, final String stock_name, final String stock_qty, final
    String stock_amount, final String method_type, final String stock_per_price,
                                final String stock_id, final String local)
    {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(context, "Please wait ...",
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

                try {

                    if (response.getString("status").equals("0")) {
                        ringProgressDialog.dismiss();
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();

                    } else
                    {
                        DatabaseHandler db = new DatabaseHandler(context);
                        JSONObject obj = response.getJSONObject("stocks");
                        int id= Integer.parseInt(obj.getString("stock_id"));
                        String name= obj.getString("stock_name");
                        String qty= obj.getString("stock_qty");
                        String unit_price= obj.getString("stock_per_price");
                        String price= obj.getString("stock_price");
                        int status=1;

                        if (method_type.equals("1"))
                        {
                            Log.d("Updating: ", "Updating .. Stock");
                            if (local.equals("local"))
                            {
                                db.updateStock(new Stock(id,name,qty,unit_price,price,status),stock_id);
                                ringProgressDialog.dismiss();
                            }
                            else
                            {
                                ringProgressDialog.dismiss();
                                db.addStock(new Stock(id,name,qty,unit_price,price,status));

                                Toast.makeText(context,"Added Successfully",Toast.LENGTH_SHORT).show();
                                context.onBackPressed();
                            }
                        }

                        else if (method_type.equals("2"))
                        {
                            ringProgressDialog.dismiss();
                            Log.d("Update: ", "Updating .. Stock");
                            db.updateStock(new Stock(id,name,qty,unit_price,price,status), stock_id);
                            context.onBackPressed();
                        }

                    }
                } catch (Exception e) {
                    ringProgressDialog.dismiss();
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




    public static void deleteStock(final Activity activity, final Stock stock) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final String bk_userid = getData(activity, "user_id", "");
        params.put("bk_userid", bk_userid);
        params.put("stock_id", stock.get_id());

        System.out.println(params);

        client.post(BASE_URL_NEW + "delete_stock", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* delete response ***");
                System.out.println(response);
                try {

                    if (response.getString("status").equals("0")) {

                    } else {
                        DatabaseHandler db = new DatabaseHandler(activity);
                        db.deleteStock(stock);
//                        Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        if (activity instanceof StockActivity) {
                            ((StockActivity) activity).showAllStocks();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }

        });
    }



}
