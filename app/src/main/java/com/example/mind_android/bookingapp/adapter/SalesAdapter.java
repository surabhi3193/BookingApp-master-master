package com.example.mind_android.bookingapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.FormActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SalesAdapter extends BaseAdapter {
    private JSONArray jobj;
    private Activity context;


    public SalesAdapter(Activity context, JSONArray jobj) {
        this.context = context;
        this.jobj = jobj;
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
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.saleslist_item, null, true);
        final ViewHolder holder = new ViewHolder();
        JSONObject responseobj = new JSONObject();
        holder.stock_amount = (TextView) rowView.findViewById(R.id.amtTv);
        holder.stock_name = (TextView) rowView.findViewById(R.id.nameTv);
        holder.stock_qty = (TextView) rowView.findViewById(R.id.qtyTv);
        holder.serialno = (TextView) rowView.findViewById(R.id.sr_notv);
        holder.sell_btn = (Button) rowView.findViewById(R.id.sell_btn);


        try {
            responseobj = jobj.getJSONObject(position);


            String stock_name = responseobj.getString("stock_name");
            String stock_qty = responseobj.getString("stock_qty");
            String stock_price = responseobj.getString("stock_price");

            System.out.println("********** item position *******");
            System.out.println(position);
            System.out.println("** item at position *****");


            int x = position + 1;
            holder.serialno.setText(String.valueOf(x));
            holder.stock_name.setText(stock_name);
            holder.stock_qty.setText(stock_qty);
            holder.stock_amount.setText(stock_price);
            final JSONObject finalResponseobj = responseobj;

            holder.sell_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        String stock_id = finalResponseobj.getString("stock_id");
                        String stock_name = finalResponseobj.getString("stock_name");

                        context.startActivity(new Intent(context, FormActivity.class)
                                .putExtra("Activity", "saleStocks")
                                .putExtra("stock_id", stock_id)
                                .putExtra("stock_name", stock_name));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rowView;
    }


    class ViewHolder {
        TextView serialno, stock_name, stock_qty, stock_amount;
        Button sell_btn;

    }
}
