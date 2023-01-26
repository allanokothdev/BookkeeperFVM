package com.bookkeeperfvm.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final int UNSUBSCRIBED = 1;

    public BrandAdapter(Context mContext, List<Brand> stringList, BrandItemClickListener brandItemClickListener) {
        this.stringList = stringList;
        this.mContext = mContext;
        this.brandItemClickListener = brandItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        Brand brand = stringList.get(position);
        if (brand.getDeadlineDate()>System.currentTimeMillis()){
            return SUBSCRIBED;
        } else {
            return UNSUBSCRIBED;
        }
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
            imageView.setTransitionName(brand.getId());
            Glide.with(mContext.getApplicationContext()).load(brand.getPic()).placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(brand.getTitle());
            subTextView.setText(brand.getPhone());
            subItemTextView.setText(brand.getLocation().getAddress());
            itemView.setOnClickListener(v -> brandItemClickListener.onBrandItemClick(brand,imageView));
        }
    }

    public class UnsubscribedViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;
        private final TextView subTextView;

        private UnsubscribedViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
        }

        void bind(int position) {

            Brand brand = stringList.get(position);
            imageView.setTransitionName(brand.getId());
            Glide.with(mContext.getApplicationContext()).load(brand.getPic()).placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(brand.getTitle());
            subTextView.setOnClickListener(v -> Toast.makeText(mContext,"Send RwF 5,000 to 0790006118 to renew your Records Subscription",Toast.LENGTH_SHORT).show());
        }
    }



}
