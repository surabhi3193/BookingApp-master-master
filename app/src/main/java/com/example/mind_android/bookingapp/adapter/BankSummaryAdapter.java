package com.example.mind_android.bookingapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.BankDetailActivity;
import com.example.mind_android.bookingapp.beans.BankTransaction;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.mind_android.bookingapp.Constant.CheckInternetConnection.isNetworkAvailable;
import static com.example.mind_android.bookingapp.Constant.NetWorkClass.BASE_URL_NEW;
import static com.example.mind_android.bookingapp.storage.MySharedPref.getData;

public class BankSummaryAdapter extends RecyclerView.Adapter<BankSummaryAdapter.MyViewHolder> {

    private List<BankTransaction> summaryList;
    private Activity activity;

    public BankSummaryAdapter(List<BankTransaction> summaryList, Activity activity) {
        this.summaryList = summaryList;
        this.activity = activity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bank_transection_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final BankTransaction trans = summaryList.get(position);

        holder.amntTv.setText(trans.getTotal_amt());
        holder.closingTv.setText(trans.getClosing_amount());
        holder.dateTv.setText(trans.getDate());
        if (trans.getTrans_type().equals("1")) {
            holder.typeTv.setText(R.string.deposit);
        } else if (trans.getTrans_type().equals("2")) {
            holder.typeTv.setText(R.string.withdraw);

        }

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBankWarning(trans);
            }
        });

    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    private void resetBankWarning(final BankTransaction trans) {
        AlertDialog.Builder ab = new AlertDialog.Builder
                (activity, R.style.MyAlertDialogStyle1);
        ab.setTitle("Delete").setIcon(R.drawable.reset);
        ab.setMessage("Are you sure ?");
        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (isNetworkAvailable(activity)) {
                    resetBank(trans.getId(), activity, trans.getTitle());
                }


                dialog.dismiss();
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

    private void resetBank(String tr_id, final Activity activity, final String name) {
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(activity, "Please wait ...",
                "", true);
        ringProgressDialog.setCancelable(false);
        String user_id = getData(activity, "user_id", "");

        params.put("bk_userid", user_id);
        if (tr_id.length() > 0)
            params.put("transaction_id", tr_id);
        System.out.println(params);

        client.post(BASE_URL_NEW + "clear_banks", params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(" ************* show loan clear   response ***");
                System.out.println(response);
                ringProgressDialog.dismiss();
                try {

                    if (response.getString("status").equals("0")) {
//                         recyclerView.setVisibility(View.GONE);

                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (activity instanceof BankDetailActivity)
                            ((BankDetailActivity) activity).getTransections(name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ringProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ringProgressDialog.dismiss();
                System.out.println(responseString);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  closingTv, typeTv, dateTv, amntTv, type;
        ImageView delete_btn;

        public MyViewHolder(View view) {
            super(view);

            amntTv = view.findViewById(R.id.amntTv);
            typeTv = view.findViewById(R.id.typeTv);
            dateTv = view.findViewById(R.id.dateTv);
            closingTv = view.findViewById(R.id.closingTv);
            delete_btn = view.findViewById(R.id.delete_btn);

        }
    }


}
