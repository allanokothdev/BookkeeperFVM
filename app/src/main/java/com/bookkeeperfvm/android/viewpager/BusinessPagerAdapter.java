package com.bookkeeperfvm.android.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bookkeeperfvm.android.fragments.SalesFragment;
import com.bookkeeperfvm.android.fragments.StockFragment;
import com.bookkeeperfvm.android.models.Brand;

import java.util.ArrayList;
import java.util.List;

public class BusinessPagerAdapter extends FragmentStateAdapter  {

    private final Brand brand;

    public BusinessPagerAdapter(@NonNull FragmentActivity fragmentActivity, Brand brand ) {
        super(fragmentActivity);
        this.brand = brand;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return SalesFragment.getInstance(brand);
            case 1:
                return StockFragment.getInstance(brand);
        }
        return SalesFragment.getInstance(brand);
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
