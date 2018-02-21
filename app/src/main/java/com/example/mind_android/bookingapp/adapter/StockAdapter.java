package com.example.mind_android.bookingapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.FormActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.StockActivity;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.deleteStock;


public class StockAdapter extends BaseAdapter {

    private JSONArray jobj;
    private Activity context;
    private String act_name;


    public StockAdapter(Activity context, JSONArray jobj, String act_name) {
        this.context = context;
        this.jobj = jobj;
        this.act_name = act_name;
    }

    @Override
    public int getCount() {
        return jobj.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.stocklist_item, null, true);
        final ViewHolder holder = new ViewHolder();
        JSONObject responseobj = new JSONObject();
        holder.stock_amount = (TextView) rowView.findViewById(R.id.amtTv);
        holder.stock_name = (TextView) rowView.findViewById(R.id.nameTv);
        holder.stock_qty = (TextView) rowView.findViewById(R.id.qtyTv);
        holder.serialno = (TextView) rowView.findViewById(R.id.sr_notv);
        holder.edit_btn = (ImageView) rowView.findViewById(R.id.edit_btn);
        holder.delete_btn = (ImageView) rowView.findViewById(R.id.delete_btn);
        try {
            responseobj = jobj.getJSONObject(position);
            if (act_name.equals("expense")) {
                String stock_id = responseobj.getString("expanse_id");
                String stock_name = responseobj.getString("expanse_name");
                String stock_qty = responseobj.getString("expanse_date");
                String stock_price = responseobj.getString("expanse_amount");

                System.out.println("********** item position *******");
                System.out.println(position);
                System.out.println("** item at position *****");
                System.out.println(stock_id);

                int x = position + 1;
                holder.serialno.setText(String.valueOf(x));
                holder.stock_name.setText(stock_name);
                holder.stock_qty.setText(stock_qty);
                holder.stock_amount.setText(stock_price);

            } else {
                String stock_id = responseobj.getString("stock_id");
                String stock_name = responseobj.getString("stock_name");
                String stock_qty = responseobj.getString("stock_qty");
                String stock_price = responseobj.getString("stock_price");

                System.out.println("********** item position *******");
                System.out.println(position);
                System.out.println("** item at position *****");
                System.out.println(stock_id);

                int x = position + 1;
                holder.serialno.setText(String.valueOf(x));
                holder.stock_name.setText(stock_name);
                holder.stock_qty.setText(stock_qty);
                holder.stock_amount.setText(stock_price);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject finalResponseobj1 = responseobj;
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (act_name.equals("expense")) {
                    } else {
                        String stock_id = finalResponseobj1.getString("stock_id");
                        String stock_name = finalResponseobj1.getString("stock_name");
                        deleteAlertDialog(context, finalResponseobj1);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });


        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String stock_id = finalResponseobj1.getString("stock_id");
                    String stock_name = finalResponseobj1.getString("stock_name");


                    context.startActivity(new Intent(context, FormActivity.class)
                            .putExtra("Activity", "editStocks")
                            .putExtra("stock_id", stock_id)
                            .putExtra("stock_name", stock_name)

                    );

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        return rowView;
    }

    private void deleteAlertDialog(final Activity activity, final JSONObject jobj) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle1);
        ab.setTitle("Delete Stock ");
        ab.setMessage("Are you sure to delete");
        ab.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Stock stock = new Stock();
                    int id = Integer.parseInt(jobj.getString("stock_id"));

                    stock.set_id(id);
                    stock.set_status(2);
                    stock.set_name(jobj.getString("stock_name"));
                    stock.set_qty(jobj.getString("stock_qty"));
                    stock.set_unit_per_price(jobj.getString("stock_per_price"));
                    stock.set_price(jobj.getString("stock_price"));

                    if (!isNetworkAvailable(activity)) {

                        DatabaseHandler db = new DatabaseHandler(activity);
                        db.updateStock(stock, String.valueOf(id));

                        if (activity instanceof StockActivity) {
                            ((StockActivity) activity).showAllStocks();
                        }
                    } else {

                        deleteStock(activity, stock);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        });

        ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.show();
    }

    class ViewHolder {
        public TextView serialno, stock_name, stock_qty, stock_amount;
        public ImageView edit_btn, delete_btn;

    }
}
