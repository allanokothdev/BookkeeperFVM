package com.bookkeeperfvm.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class StockFragment extends Fragment implements RecordItemClickListener {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;
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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));
        adapter = new StockAdapter(getContext(), objectList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fetchObjects();
        return view;
    }

    private void fetchObjects(){
        Query query = firebaseFirestore.collection(Constants.RECORDS).orderBy("id", Query.Direction.DESCENDING).whereEqualTo("brandID",brand.getId()).whereArrayContains("tags",Constants.STOCK);
        registration = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null){
                for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        Record object = documentChange.getDocument().toObject(Record.class);
                        if (!objectList.contains(object) && !object.getRecordType().equals(Constants.MENU)){
                            objectList.add(object);
                            adapter.notifyDataSetChanged();
                            postMetrics();
                        }
                    }else if (documentChange.getType()==DocumentChange.Type.MODIFIED){
                        Record object = documentChange.getDocument().toObject(Record.class);
                        if (objectList.contains(object)){
                            objectList.set(objectList.indexOf(object),object);
                            adapter.notifyItemChanged(objectList.indexOf(object));
                            postMetrics();
                        }
                    }else if (documentChange.getType()==DocumentChange.Type.REMOVED){
                        Record object = documentChange.getDocument().toObject(Record.class);
                        if (objectList.contains(object)){
                            objectList.remove(object);
                            adapter.notifyItemRemoved(objectList.indexOf(object));
                            postMetrics();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchObjects();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registration != null){
            registration.remove();
        }
    }

    @Override
    public void onRecordItemClick(Record record, ImageView imageView) {
    }

    private int fetchMetrics(List<Record> transactionList){
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

    private void postMetrics(){
        textView.setText(getString(R.string.currency,Constants.CURRENCY, fetchMetrics(objectList)));
    }
}

