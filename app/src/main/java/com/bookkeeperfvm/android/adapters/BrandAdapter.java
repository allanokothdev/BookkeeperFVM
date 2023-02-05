package com.bookkeeperfvm.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkeeperfvm.android.R;
import com.bookkeeperfvm.android.listeners.BrandItemClickListener;
import com.bookkeeperfvm.android.models.Brand;
import com.bumptech.glide.Glide;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter{

    private final Context mContext;
    private final List<Brand> stringList;
    private final BrandItemClickListener brandItemClickListener;
    public static final int SUBSCRIBED = 0;

    public BrandAdapter(Context mContext, List<Brand> stringList, BrandItemClickListener brandItemClickListener) {
        this.stringList = stringList;
        this.mContext = mContext;
        this.brandItemClickListener = brandItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return SUBSCRIBED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubscribedViewHolderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.subscribed_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        try {
            ((SubscribedViewHolderViewHolder) holder).bind(position);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
    public class SubscribedViewHolderViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;
        private final TextView subTextView;
        private final TextView subItemTextView;

        private SubscribedViewHolderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
            subItemTextView = itemView.findViewById(R.id.subItemTextView);
        }

        void bind(int position) {

            Brand brand = stringList.get(position);
            imageView.setTransitionName(brand.getWalletAddress());
            Glide.with(mContext.getApplicationContext()).load(brand.getPic()).placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(brand.getTitle());
            subTextView.setText(brand.getPhone());
            subItemTextView.setText(brand.getWalletAddress());
            itemView.setOnClickListener(v -> brandItemClickListener.onBrandItemClick(brand,imageView));
        }
    }


}
