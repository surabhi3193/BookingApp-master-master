package com.example.mind_android.bookingapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
        holder.dateTV = (TextView) rowView.findViewById(R.id.datetv);


        try {
            responseobj = jobj.getJSONObject(position);

            String stock_name = responseobj.getString("sale_stock_name");
            String stock_qty = responseobj.getString("sale_stock_qty");
            String stock_price = responseobj.getString("sale_price");
         String sale_type = responseobj.getString("stock_type");
            String cdate = responseobj.getString("sale_date");

            System.out.println("********** item position *******");
            System.out.println(sale_type);
            System.out.println("** item at position *****");

            int x = position + 1;
            holder.serialno.setText(String.valueOf(x));
            holder.stock_name.setText(stock_name);
            holder.stock_qty.setText(stock_qty);
            holder.stock_amount.setText(stock_price);
            holder.dateTV.setText(cdate);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject finalResponseobj = responseobj;
        holder.serialno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDescriptionDialog(finalResponseobj);
            }
        });
        return rowView;
    }


    class ViewHolder {
        TextView serialno, stock_name, stock_qty, stock_amount,dateTV;


    }
    private void openDescriptionDialog(JSONObject finalResponseobj1) {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_pop_up_sales);
            dialog.setTitle(finalResponseobj1.getString("sale_stock_name"));

            // set the custom dialog components - text, image and button
            TextView title = dialog.findViewById(R.id.title);
            TextView qty = dialog.findViewById(R.id.qty);
            TextView perunit = dialog.findViewById(R.id.perunit);
            TextView value = dialog.findViewById(R.id.value);
            TextView date = dialog.findViewById(R.id.date);

            title.setText(finalResponseobj1.getString("sale_stock_name") + "(" +finalResponseobj1.getString("stock_type")+")");
            qty.setText(finalResponseobj1.getString("sale_stock_qty"));
            perunit.setText(finalResponseobj1.getString("sell_unit_price"));
            value.setText(finalResponseobj1.getString("sale_price"));
            date.setText(finalResponseobj1.getString("sale_date"));

            Button dialogButton =dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
