package com.bookkeeperfvm.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkeeperfvm.android.R;
import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.listeners.RecordItemClickListener;
import com.bookkeeperfvm.android.models.Metric;
import com.bookkeeperfvm.android.models.Record;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Record> stringList;
    private final String currentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final RecordItemClickListener recordItemClickListener;
    private int finalCost = 0;
    private int buyingCost = 0;
    private int quantity = 0;

    public StockAdapter(Context mContext, List<Record> stringList, RecordItemClickListener recordItemClickListener){
        this.mContext = mContext;
        this.recordItemClickListener = recordItemClickListener;
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
            imageView.setTransitionName(record.getId());
            Glide.with(mContext.getApplicationContext()).load(record.getPic()).placeholder(R.drawable.cover).into(imageView);
            textView.setText(record.getTitle());
            subTextView.setText(mContext.getString(R.string.currency,Constants.CURRENCY, record.getSalePrice()));
            subItemTextView.setText(mContext.getString(R.string.quantity,record.getQuantity()));
            button.setOnClickListener(v -> showBottomSheet(record));
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.OBJECT,record);
            itemView.setOnClickListener(v -> recordItemClickListener.onRecordItemClick(record,imageView));
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
        TextInputEditText summaryTextInputEditText = view.findViewById(R.id.summaryTextInputEditText);
        Button button = view.findViewById(R.id.button);
        Spinner spinner = view.findViewById(R.id.spinner);

        Glide.with(mContext.getApplicationContext()).load(stock.getPic()).placeholder(R.drawable.cover).into(imageView);
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
            String paymentType = spinner.getSelectedItem().toString().trim();
            String summary = summaryTextInputEditText.getText().toString().trim();
            String recordID = firebaseFirestore.collection(Constants.RECORDS).document().getId();
            @SuppressLint("SimpleDateFormat") String today = new SimpleDateFormat("dd MMMM yyy").format(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat") String year = new SimpleDateFormat("yyyy").format(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat") String month = new SimpleDateFormat("MMM yyyy").format(System.currentTimeMillis()).replace(".","");
            @SuppressLint("SimpleDateFormat") String monthly = new SimpleDateFormat("MMMM yyyy").format(System.currentTimeMillis()).replace(".","");

            if (TextUtils.isEmpty(summary)){
                summary = stock.getRecordType();
            }
            ArrayList<String> tags = new ArrayList<>();
            tags.add(currentUserID);
            tags.add(stock.getBrandID());
            tags.add(today);
            tags.add(Constants.SALES);
            tags.add(year);
            tags.add(month);
            tags.add(monthly);
            tags.add(paymentType);
            Record record = new Record(recordID,stock.getPic(),stock.getTitle(),summary,Constants.SALES,getDate(),System.currentTimeMillis(),stock.getSalePrice(),quantity,stock.getPrice(),stock.getBrandID(),tags);
            firebaseFirestore.collection(Constants.RECORDS).document(recordID).set(record).addOnSuccessListener(unused -> {

                reduceQuantityCount(stock.getId(),quantity);
                addProfit(stock.getBrandID(),(quantity*stock.getSalePrice()-(quantity*stock.getPrice())));
                addSales(stock.getBrandID(),(quantity*stock.getSalePrice()));
                reduceStock(stock.getBrandID(),(quantity*stock.getPrice()));
                progressBar.setVisibility(View.GONE);
                bottomSheetDialog.dismiss();
            }).addOnFailureListener(e -> {
                button.setEnabled(true);
                Toast.makeText(mContext,Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            });
        });
    }

    private void reduceStock(final String brandID, int total) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.METRICS).child(brandID);
        databaseReference.runTransaction(new Transaction.Handler() {
            @Override public Transaction.@NotNull Result doTransaction(@NotNull MutableData mutableData) {
                Metric metric = mutableData.getValue(Metric.class);
                assert metric != null;
                metric.setStock(metric.getStock() - total);
                mutableData.setValue(metric);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot currentData) {
                Timber.d("postTransaction:onComplete:%s", databaseError);
            }
        });
    }

    private void addSales(final String brandID, int total) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.METRICS).child(brandID);
        databaseReference.runTransaction(new Transaction.Handler() {
            @Override public Transaction.@NotNull Result doTransaction(@NotNull MutableData mutableData) {
                Metric metric = mutableData.getValue(Metric.class);
                assert metric != null;
                metric.setSales(metric.getSales() + total);
                mutableData.setValue(metric);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot currentData) {
                Timber.d("postTransaction:onComplete:%s", databaseError);
            }
        });
    }

    private void addProfit(final String brandID, int total) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.METRICS).child(brandID);
        databaseReference.runTransaction(new Transaction.Handler() {
            @Override public Transaction.@NotNull Result doTransaction(@NotNull MutableData mutableData) {
                Metric metric = mutableData.getValue(Metric.class);
                assert metric != null;
                metric.setProfit(metric.getProfit() + total);
                mutableData.setValue(metric);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot currentData) {
                Timber.d("postTransaction:onComplete:%s", databaseError);
            }
        });
    }

    private void reduceQuantityCount(final String objectID, int quantity){
        DocumentReference reference = firebaseFirestore.collection(Constants.RECORDS).document(objectID);
        firebaseFirestore.runTransaction(transaction -> {
            DocumentSnapshot documentSnapshot = transaction.get(reference);
            double newCount = documentSnapshot.getDouble("quantity")-quantity;
            transaction.update(reference,"quantity",newCount);
            return null;
        });
    }

    private String getDate(){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(System.currentTimeMillis());
        return DateFormat.format("EEEE, dd MMMM yyyy", cal).toString();
    }

    private String getWeekYear(){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(System.currentTimeMillis());
        return DateFormat.format("MMMM yyyy", cal).toString();
    }
}
