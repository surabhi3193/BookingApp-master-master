package com.example.mind_android.bookingapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.ExpenceActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.ExpenseForm_Activity;
import com.example.mind_android.bookingapp.activities.dashboard_part.FormActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.StockActivity;
import com.example.mind_android.bookingapp.beans.Expense;
import com.example.mind_android.bookingapp.beans.Stock;
import com.example.mind_android.bookingapp.storage.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.deleteExpense;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.deleteStock;


public class StockAdapter extends BaseAdapter {

    private JSONArray jobj;
    private Activity context;
    private String act_name;
    String stock_name,stock_qty,stock_price;


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
        holder.desclay = (LinearLayout) rowView.findViewById(R.id.desclay);
        holder.stock_name = (TextView) rowView.findViewById(R.id.nameTv);
        holder.stock_qty = (TextView) rowView.findViewById(R.id.qtyTv);
        holder.serialno = (TextView) rowView.findViewById(R.id.sr_notv);
        holder.edit_btn = (ImageView) rowView.findViewById(R.id.edit_btn);
        holder.delete_btn = (ImageView) rowView.findViewById(R.id.delete_btn);
        try {
            responseobj = jobj.getJSONObject(position);
            if (act_name.equals("expense")) {
                String stock_id = responseobj.getString("expanse_id");
                 stock_name = responseobj.getString("expanse_name");
                 stock_qty = responseobj.getString("expanse_date");
                 stock_price = responseobj.getString("expanse_amount");

                System.out.println("********** item position *******");
                System.out.println(position);
                System.out.println("** item at position *****");
                System.out.println(stock_id);

                int x = position + 1;
                holder.serialno.setText(String.valueOf(x));
                holder.stock_name.setText(stock_name);
                holder.stock_qty.setVisibility(View.GONE);
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
                NumberFormat format = NumberFormat.getCurrencyInstance();
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
                deleteAlertDialog(context, finalResponseobj1, act_name);

            }
        });

        holder.desclay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (act_name.equalsIgnoreCase("expense"))
                openDescriptionDialogForExpense(finalResponseobj1);
                else
                    openDescriptionDialog(finalResponseobj1);
            }
        });



        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (act_name.equals("expense")) {

                        String stock_id = finalResponseobj1.getString("expanse_id");
                        String stock_name = finalResponseobj1.getString("expanse_name");
                        String expanse_amount = finalResponseobj1.getString("expanse_amount");
                        String expanse_date = finalResponseobj1.getString("expanse_date");
                        String expense_desc = finalResponseobj1.getString("expense_desc");
                        context.startActivity(new Intent(context, ExpenseForm_Activity.class)
                                .putExtra("method_type", "2")
                                .putExtra("expanse_id", stock_id)
                                .putExtra("expanse_name", stock_name)
                                .putExtra("expanse_amount", expanse_amount)
                                .putExtra("expanse_date", expanse_date)
                                .putExtra("expense_desc", expense_desc)

                        );


                    } else {
                        String stock_id = finalResponseobj1.getString("stock_id");
                        String stock_name = finalResponseobj1.getString("stock_name");
                        String stock_qty = finalResponseobj1.getString("stock_qty");
                        String stock_price = finalResponseobj1.getString("stock_price");
                        String stock_per_price = finalResponseobj1.getString("stock_per_price");
                        String stock_date = finalResponseobj1.getString("stock_date");
                        context.startActivity(new Intent(context, FormActivity.class)
                                .putExtra("Activity", "editStocks")
                                .putExtra("stock_id", stock_id)
                                .putExtra("stock_name", stock_name)
                                .putExtra("stock_qty", stock_qty)
                                .putExtra("stock_price", stock_price)
                                .putExtra("stock_per_price", stock_per_price)
                                .putExtra("stock_date", stock_date)

                        );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        return rowView;
    }

    private void openDescriptionDialog(JSONObject finalResponseobj1) {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_popup);
            dialog.setTitle(finalResponseobj1.getString("stock_name"));

            // set the custom dialog components - text, image and button
            TextView title = dialog.findViewById(R.id.title);
            TextView qty = dialog.findViewById(R.id.qty);
            TextView perunit = dialog.findViewById(R.id.perunit);
            TextView value = dialog.findViewById(R.id.value);
            TextView date = dialog.findViewById(R.id.date);

            title.setText(finalResponseobj1.getString("stock_name"));
            qty.setText(finalResponseobj1.getString("stock_qty"));
            perunit.setText(finalResponseobj1.getString("stock_per_price"));
            value.setText(finalResponseobj1.getString("stock_price"));
            date.setText(finalResponseobj1.getString("stock_date"));


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
    private void openDescriptionDialogForExpense(JSONObject finalResponseobj1) {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_popup_exp);
            dialog.setTitle(finalResponseobj1.getString("expanse_name"));

            // set the custom dialog components - text, image and button
            TextView title = dialog.findViewById(R.id.title);
            TextView desc = dialog.findViewById(R.id.desc);
            TextView value = dialog.findViewById(R.id.value);
            TextView date = dialog.findViewById(R.id.date);

            title.setText(finalResponseobj1.getString("expanse_name"));
            desc.setText(finalResponseobj1.getString("expense_desc"));
            value.setText(finalResponseobj1.getString("expanse_amount"));
            date.setText(finalResponseobj1.getString("expanse_date"));


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

    private void deleteAlertDialog(final Activity activity, final JSONObject jobj,
                                   final String act_name) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle1);
        ab.setTitle("Delete");
        ab.setMessage("Are you sure , you want to delete ");
        ab.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (!isNetworkAvailable(activity)) {

                        if (act_name.equals("expense")) {
                            Expense expense = new Expense();
                            int id = Integer.parseInt(jobj.getString("expanse_id"));
                            expense.set_id(id);
                            expense.set_status(2);
                            expense.set_name(jobj.getString("expanse_name"));
                            expense.set_date(jobj.getString("expanse_date"));
                            expense.set_price(jobj.getString("expanse_amount"));


                            DatabaseHandler db = new DatabaseHandler(activity);
                            db.updateExpense(expense, String.valueOf(id));

                            if (activity instanceof ExpenceActivity) {
                                ((ExpenceActivity) activity).showAllExpense();
                            }
                        } else {

                            Stock stock = new Stock();
                            int id = Integer.parseInt(jobj.getString("stock_id"));
                            stock.set_id(id);
                            stock.set_status(2);
                            stock.set_name(jobj.getString("stock_name"));
                            stock.set_qty(jobj.getString("stock_qty"));
                            stock.set_unit_per_price(jobj.getString("stock_per_price"));
                            stock.set_price(jobj.getString("stock_price"));

                            DatabaseHandler db = new DatabaseHandler(activity);
                            db.updateStock(stock, String.valueOf(id));

                            if (activity instanceof StockActivity) {
                                ((StockActivity) activity).showAllStocks();
                            }

                        }


                    } else {

                        if (act_name.equals("expense")) {
                            Expense expense = new Expense();
                            int id = Integer.parseInt(jobj.getString("expanse_id"));
                            expense.set_id(id);
                            expense.set_status(2);
                            expense.set_name(jobj.getString("expanse_name"));
                            expense.set_date(jobj.getString("expanse_date"));
                            expense.set_price(jobj.getString("expanse_amount"));

                            deleteExpense(activity, expense);

                        } else {

                            Stock stock = new Stock();
                            int id = Integer.parseInt(jobj.getString("stock_id"));
                            stock.set_id(id);
                            stock.set_status(2);
                            stock.set_name(jobj.getString("stock_name"));
                            stock.set_qty(jobj.getString("stock_qty"));
                            stock.set_unit_per_price(jobj.getString("stock_per_price"));
                            stock.set_price(jobj.getString("stock_price"));
                            deleteStock(activity, stock);
                        }


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
        LinearLayout desclay;

    }
}
