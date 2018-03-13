package com.example.mind_android.bookingapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.LoanActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.LoanFormActivity;
import com.example.mind_android.bookingapp.activities.dashboard_part.SalesActivity;
import com.example.mind_android.bookingapp.beans.LoanSummary;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

    private List<LoanSummary> summaryList;
    private String intent_name;
    private Activity context;

    public LoanAdapter(List<LoanSummary> summaryList, String intent_name,Activity context) {
        this.summaryList = summaryList;
        this.intent_name = intent_name;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final LoanSummary trans = summaryList.get(position);
        holder.title.setText(trans.getlender());
        holder.remainTV.setText(trans.getTotal_remain_amount());
        holder.monthTV.setText(trans.gettotal_month());
        holder.total_amt.setText(trans.getTotal_amt());
        holder.date.setText(trans.getDate());
        holder.paidLenderTV.setText(trans.getTotal_paid_amount());
        holder.interest.setText(trans.getinterest());


        if (intent_name.equals("loan")) {

            switch (trans.getType()) {
                case "1":
                    holder.type.setText("Paid");
                    holder.payTV.setVisibility(View.INVISIBLE);
                    holder.type.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    break;
                case "2":

                    holder.payTV.setVisibility(View.VISIBLE);
                    holder.type.setText("Unpaid");
                    holder.type.setTextColor(context.getResources().getColor(R.color.Crimson));
                    break;

            }
        }

        holder.payTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context,
                        LoanFormActivity.class)
                        .putExtra("form","pay_form")
                        .putExtra("name",trans.getlender())
                        .putExtra("lender_id",trans.getLender_id())
                        .putExtra("payable_amnt",trans.getTotal_amt())

                );
            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof  LoanActivity)
                {
                    ((LoanActivity) context).resetLoanWarning(trans.getLender_id());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView delete_btn,title, monthTV, remainTV, date, total_amt, type,paidLenderTV,interest,payTV;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            paidLenderTV = (TextView) view.findViewById(R.id.paidLenderTV);
            interest = (TextView) view.findViewById(R.id.interest);
            remainTV = (TextView) view.findViewById(R.id.remainTV);
            monthTV = (TextView) view.findViewById(R.id.monthTV);
            total_amt = (TextView) view.findViewById(R.id.total_amtTv);
            date = (TextView) view.findViewById(R.id.dateTV);
            type = (TextView) view.findViewById(R.id.typeTV);
            payTV = (TextView) view.findViewById(R.id.payTV);
            delete_btn = (TextView) view.findViewById(R.id.delete_btn);

        }
    }
}
