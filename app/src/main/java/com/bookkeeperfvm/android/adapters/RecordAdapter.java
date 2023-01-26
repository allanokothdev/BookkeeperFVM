package com.bookkeeperfvm.android.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkeeperfvm.android.R;
import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.models.Record;
import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Record> stringList;

    public RecordAdapter(Context mContext, List<Record> stringList) {
        this.stringList = stringList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return stringList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;
        private final TextView subTextView;
        private final TextView subItemTextView;
        private final TextView dateTextView;
        private final TextView timeTextView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
            subItemTextView = itemView.findViewById(R.id.subItemTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        void bind(int position) {

            Record record = stringList.get(position);
            imageView.setTransitionName(record.getId());
            Glide.with(mContext.getApplicationContext()).load(record.getPic()).placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(record.getTitle());
            subTextView.setText(record.getRecordType());
            subItemTextView.setText(mContext.getString(R.string.price_quantity,record.getQuantity(), Constants.CURRENCY,record.getPrice()));
            timeTextView.setText(mContext.getString(R.string.currency, Constants.CURRENCY,(record.getQuantity()*record.getPrice())));
            dateTextView.setText(getTime(record.getRecordDate()));

        }
    }

    private String getTime(long time){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        return DateFormat.format("MMM\ndd", cal).toString();
    }

    private String getNow(long time){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        return DateFormat.format("EEEE, dd MMMM yyy - HH:mm:aa", cal).toString();
    }
}
