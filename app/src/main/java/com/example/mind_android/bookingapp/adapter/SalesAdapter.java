package com.example.mind_android.bookingapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.deleteSale;

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
        holder.dateTV = (TextView) rowView.findViewById(R.id.datetv);
        holder.delete_btn = (ImageView) rowView.findViewById(R.id.delete_btn);
        holder.descLay = rowView.findViewById(R.id.descLay);


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
        holder.descLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDescriptionDialog(finalResponseobj);
            }
        });

        final JSONObject finalResponseobj1 = responseobj;
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlertDialog(context, finalResponseobj1);

            }
        });
        return rowView;
    }

    private void deleteAlertDialog(final Activity activity, final JSONObject jobj) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle1);
        ab.setTitle("Delete");
        ab.setMessage("Are you sure , you want to delete ");
        ab.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (isNetworkAvailable(activity)) {

                        String sale_id = jobj.getString("sale_id");
                        deleteSale(activity, sale_id);

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

    private void openDescriptionDialog(JSONObject finalResponseobj1) {
        System.out.println("========= view details sales=========");
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
            TextView value_head = dialog.findViewById(R.id.value_head);
            RelativeLayout qtylay = dialog.findViewById(R.id.qtylay);
            RelativeLayout perpriceLay = dialog.findViewById(R.id.p_pricelay);
            String type = finalResponseobj1.getString("stock_type");

            if (!type.equalsIgnoreCase("Stocks")) {
                qtylay.setVisibility(View.GONE);
                perpriceLay.setVisibility(View.GONE);
                value_head.setText("Service Value ");
            }
            else
            {
                qtylay.setVisibility(View.VISIBLE);
                perpriceLay.setVisibility(View.VISIBLE);
                value_head.setText("Sale Value ");

            }

            title.setText(finalResponseobj1.getString("sale_stock_name") + "(" + type + ")");
            qty.setText(finalResponseobj1.getString("sale_stock_qty"));
            perunit.setText(finalResponseobj1.getString("sell_unit_price"));
            value.setText(finalResponseobj1.getString("sale_price"));
            date.setText(finalResponseobj1.getString("sale_date"));

            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ViewHolder {
        TextView serialno, stock_name, stock_qty, stock_amount, dateTV;
        LinearLayout descLay;
        ImageView delete_btn;


    }

}
