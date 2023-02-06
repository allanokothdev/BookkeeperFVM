package com.bookkeeperfvm.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkeeperfvm.android.R;
import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.models.Record;
import com.bookkeeperfvm.android.utils.GetUser;
import com.bookkeeperfvm.android.utils.ScreenUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Record> stringList;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private int finalCost = 0;
    private int buyingCost = 0;
    private int quantity = 0;

    public StockAdapter(Context mContext, List<Record> stringList){
        this.mContext = mContext;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.stock_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override public int getItemCount() { return stringList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;
        private final TextView subTextView;
        private final TextView subItemTextView;
        private final Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.button);
            subTextView = itemView.findViewById(R.id.subTextView);
            subItemTextView = itemView.findViewById(R.id.subItemTextView);
        }

        void bind(int position) {

            Record record = stringList.get(position);
            Glide.with(mContext.getApplicationContext()).load(R.drawable.logo).placeholder(R.drawable.cover).into(imageView);
            textView.setText(record.getTitle());
            subTextView.setText(mContext.getString(R.string.currency,Constants.CURRENCY, record.getSalePrice()));
            subItemTextView.setText(mContext.getString(R.string.quantity,record.getQuantity()));
            button.setOnClickListener(v -> showBottomSheet(record));
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.OBJECT,record);
        }
    }

    private void showBottomSheet(Record stock){

        finalCost = stock.getSalePrice();
        buyingCost = stock.getPrice();
        quantity = 1;

        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.bottom_sheet_stock, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView nameTextView = view.findViewById(R.id.textView);
        TextView priceTextView = view.findViewById(R.id.subTextView);
        TextView totalPriceTextView = view.findViewById(R.id.subItemTextView);
        ImageView decrementImageView = view.findViewById(R.id.decrementImageView);
        TextView quantityTextView = view.findViewById(R.id.quantityTextView);
        ImageView incrementImageView = view.findViewById(R.id.incrementImageView);
        AVLoadingIndicatorView progressBar = view.findViewById(R.id.progressBar);
        Button button = view.findViewById(R.id.button);

        Glide.with(mContext.getApplicationContext()).load(R.drawable.logo).placeholder(R.drawable.cover).into(imageView);
        nameTextView.setText(stock.getTitle());
        quantityTextView.setText(String.valueOf(quantity));
        priceTextView.setText(mContext.getString(R.string.currency, Constants.CURRENCY, stock.getSalePrice()));
        totalPriceTextView.setText(mContext.getString(R.string.currency,Constants.CURRENCY, finalCost));

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        incrementImageView.setOnClickListener(v -> {
            if (quantity < stock.getQuantity()){

                finalCost = finalCost + stock.getSalePrice();
                buyingCost = buyingCost + stock.getPrice();
                quantity++;
                totalPriceTextView.setText(mContext.getString(R.string.price, finalCost));
                quantityTextView.setText(String.valueOf(quantity));

            }else {

                Toast.makeText(mContext,"We don't have stock for this much order",Toast.LENGTH_SHORT).show();
            }
        });

        decrementImageView.setOnClickListener(v -> {
            if (quantity>1){
                finalCost = finalCost - stock.getSalePrice();
                buyingCost = buyingCost - stock.getPrice();
                quantity--;
                totalPriceTextView.setText(mContext.getString(R.string.currency,Constants.CURRENCY, finalCost));
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.OBJECT,stock);

        button.setOnClickListener(v -> {

            //Records new SALE REcord on the FEVM data storage
            Record record = new Record(0,stock.getTitle(),Constants.SALES,getDate(),System.currentTimeMillis(),stock.getSalePrice(),quantity,stock.getPrice(),stock.getBrandID());
            Map<String, Object> data = new HashMap<>();

            //Fetches Private Key from Secured Local Storage
            data.put("key", GetUser.getWallet(mContext, record.getBrandID()).getPrivateKey());
            data.put("title", record.getTitle());
            data.put("recordType", record.getRecordType());
            data.put("timestamp", record.getTimestamp());
            data.put("recordDate", record.getRecordDate());
            data.put("price", record.getPrice());
            data.put("quantity", record.getQuantity());
            data.put("salesPrice", record.getSalePrice());
            data.put("brandId", record.getBrandID());
            firebaseFunctions.getHttpsCallable(Constants.CREATE_SALE).call(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                    assert result != null;
                    String link = Objects.requireNonNull(result.get("link")).toString();
                    confirmTransaction(link);

                } else {
                    Toast.makeText(mContext, "Try Again", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            });
        });
    }


    private String getDate(){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(System.currentTimeMillis());
        return DateFormat.format("EEEE, dd MMMM yyyy", cal).toString();
    }

    private void confirmTransaction(String url){
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_confirm, null);
        TextView subTextView = view.findViewById(R.id.subTextView);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        ScreenUtils screenUtils = new ScreenUtils(mContext);
        behavior.setPeekHeight(screenUtils.getHeight());

        subTextView.setOnClickListener(view12 -> {
            try {
                Intent indie = new Intent(Intent.ACTION_VIEW);
                indie.setData(Uri.parse(url));
                mContext.startActivity(indie);
                bottomSheetDialog.dismiss();
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
