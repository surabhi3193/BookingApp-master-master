package com.example.mind_android.bookingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.beans.TransectionSummary;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {

    private List<TransectionSummary> summaryList;
    private String intent_name;

    public SummaryAdapter(List<TransectionSummary> summaryList, String intent_name) {
        this.summaryList = summaryList;
        this.intent_name = intent_name;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transection_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TransectionSummary trans = summaryList.get(position);
        holder.title.setText(trans.getTitle());
        holder.per_unit.setText(trans.getPer_unit());
        holder.qty.setText(trans.getQty());
        holder.total_amt.setText(trans.getTotal_amt());
        holder.date.setText(trans.getDate());

        if (trans.getQty().length()==0)
            holder.multiply.setVisibility(View.INVISIBLE);

        if (intent_name.equals("summary")) {

            switch (trans.getType())
            {
                case "1":
                    holder.type.setText(R.string.srvices);
                    holder.image.setImageResource(R.drawable.service_trans);
                    break;
                case "2":

                    holder.type.setText(R.string.sale);
                    holder.image.setImageResource(R.drawable.sale_trans);
                    break;
                case "3":
                    holder.type.setText(R.string.stock);
                    holder.image.setImageResource(R.drawable.stock_trans);
                    break;
                case "4":
                    holder.type.setText(R.string.expense);
                    holder.image.setImageResource(R.drawable.expense_trans);
                    break;

                    case "5":
                    holder.type.setText(R.string.loan);
                    holder.image.setImageResource(R.drawable.loans);
                    break;

                    case "6":

                        if (trans.getTrans_type().equals("1")) {
                            holder.type.setText(R.string.deposit);
                            holder.image.setImageResource(R.drawable.deposit);
                        }
                        else if (trans.getTrans_type().equals("2")) {
                            holder.type.setText(R.string.withdraw);
                            holder.image.setImageResource(R.drawable.withdraw);
                        }
                    break;
            }
        }

        else if (intent_name.equals("bank")) {

            holder.type.setText("Closing Balance");
            holder.image.setVisibility(View.GONE);
            holder.qty.setVisibility(View.GONE);
            holder.per_unit.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
            holder.multiply.setVisibility(View.GONE);
            holder.type.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, qty, per_unit, date, total_amt, type,multiply;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            multiply = (TextView) view.findViewById(R.id.multiply);
            per_unit = (TextView) view.findViewById(R.id.per_unitTV);
            qty = (TextView) view.findViewById(R.id.qtyTv);
            total_amt = (TextView) view.findViewById(R.id.total_amtTv);
            date = (TextView) view.findViewById(R.id.dateTV);
            type = (TextView) view.findViewById(R.id.typeTV);
            type = (TextView) view.findViewById(R.id.typeTV);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

}
