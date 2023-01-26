package com.bookkeeperfvm.android.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bookkeeperke.android.fragments.ActivityFragment;
import com.bookkeeperke.android.fragments.DebtFragment;
import com.bookkeeperke.android.fragments.ExpenseFragment;
import com.bookkeeperke.android.fragments.LoanFragment;
import com.bookkeeperke.android.fragments.SalesFragment;
import com.bookkeeperke.android.fragments.StockFragment;
import com.bookkeeperke.android.models.Brand;
import com.bookkeeperke.android.models.Record;

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
            case 2:
                return ExpenseFragment.getInstance(brand);
            case 3:
                return DebtFragment.getInstance(brand);
            case 4:
                return LoanFragment.getInstance(brand);
            case 5:
                return ActivityFragment.getInstance(brand);
        }
        return SalesFragment.getInstance(brand);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    private List<Record> filterList(List<Record> objectList, String objectID){
        List<Record> filteredList = new ArrayList<>();
        for (Record record:objectList){
            if (record.getRecordType().equals(objectID)){
                filteredList.add(record);
            }
        }
        return filteredList;
    }
}
