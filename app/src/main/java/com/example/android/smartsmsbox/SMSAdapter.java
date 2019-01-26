package com.example.android.smartsmsbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.smartsmsbox.R;

import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.String.valueOf;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder> {

    private ArrayList<Chat> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    SMSAdapter(Context context, ArrayList<Chat> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sms_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat c = mData.get(position);
        SMS sms = c.getSmsList().get(0);
        String smsBody;

        if (sms.getBody().length()>15) {
            smsBody = sms.getBody().substring(0,15) + "...";
        } else {
            smsBody = sms.getBody();
        }

        holder.mBody.setText(smsBody.replace("\n", "").replace("\r", ""));
        if (c.getName()!= null) {
            holder.mAddress.setText(c.getName());
        } else {
            holder.mAddress.setText(c.getAddress());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(sms.getTime()));
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMin = calendar.get(Calendar.MINUTE);

        String date = (String.valueOf(mYear) + "-" + valueOf(mMonth)+ "-" +
                valueOf(mDay) + " "+ valueOf(mHour) + ":"+ valueOf(mMin));

        holder.mDate.setText(date);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mBody;
        TextView mAddress;
        TextView mDate;

        ViewHolder(View itemView) {
            super(itemView);
            mBody = itemView.findViewById(R.id.body);
            mAddress = itemView.findViewById(R.id.sender);
            mDate = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Chat getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void swapDataSet(ArrayList<Chat> newData){


//        int i = 0;
//        int size = mData.size();
//
//        while(i<size) {
//            mData.remove(0);
//            notifyItemRemoved(0);
//            i++;
//        }

        this.mData = newData;

        //now, tell the adapter about the update
        notifyDataSetChanged();

    }

}
