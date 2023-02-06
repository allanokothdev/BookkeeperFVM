package com.bookkeeperfvm.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkeeperfvm.android.R;
import com.bookkeeperfvm.android.adapters.StockAdapter;
import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.listeners.RecordItemClickListener;
import com.bookkeeperfvm.android.models.Brand;
import com.bookkeeperfvm.android.models.Record;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StockFragment extends Fragment {

    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private AVLoadingIndicatorView progressBar;
    private final List<Record> objectList = new ArrayList<>();
    private StockAdapter adapter;
    private Brand brand;
    private TextView textView;

    public StockFragment() {
        // Required empty public constructor
    }

    public static StockFragment getInstance(Brand brand){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.OBJECT, brand);
        StockFragment fragment = new StockFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        brand = (Brand) getArguments().getSerializable(Constants.OBJECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);

        textView = view.findViewById(R.id.textView);
        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));
        adapter = new StockAdapter(getContext(), objectList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fetchStock();
        return view;
    }

    private void fetchStock(){
        try {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, Object> data = new HashMap<>();
            data.put("key", brand.getPrivateKey());
            firebaseFunctions.getHttpsCallable(Constants.FETCH_STOCKS).call(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    ArrayList<HashMap> result = (ArrayList<HashMap>) task.getResult().getData();
                    Toast.makeText(requireContext(),result.toString(),Toast.LENGTH_SHORT).show();
                    if (result != null){
                        for (HashMap hashMap: result){

                            int recordId = Integer.parseInt(hashMap.get("recordId").toString());
                            String title = hashMap.get("title").toString();
                            String recordType = hashMap.get("recordType").toString();
                            String timestamp = hashMap.get("timestamp").toString();
                            long recordDate = Long.parseLong(hashMap.get("recordDate").toString());
                            int price = Integer.parseInt(hashMap.get("price").toString());
                            int quantity = Integer.parseInt(hashMap.get("quantity").toString());
                            int salePrice = Integer.parseInt(hashMap.get("salePrice").toString());
                            String brandID = hashMap.get("brandID").toString();

                            Record record = new Record(recordId,title,recordType,timestamp,recordDate,price,quantity,salePrice,brandID);
                            if (!objectList.contains(record)){
                                objectList.add(record);
                                progressBar.setVisibility(View.INVISIBLE);
                                postStockTotal();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Sorry, There are no Stock Record Uploaded", Toast.LENGTH_SHORT).show();

                    }
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(requireActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchStock();
    }

    private int fetchStockTotal(List<Record> transactionList){
        int total = 0;
        try {
            if (transactionList != null){
                for (int i = 0; i< transactionList.size();i++){
                    Record record = transactionList.get(i);
                    total += (record.getPrice() * record.getQuantity());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }

    private void postStockTotal(){
        textView.setText(getString(R.string.currency,Constants.CURRENCY, fetchStockTotal(objectList)));
    }
}

