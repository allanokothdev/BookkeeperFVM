package com.bookkeeperfvm.android.fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkeeperfvm.android.R;
import com.bookkeeperfvm.android.adapters.RecordAdapter;
import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.models.Brand;
import com.bookkeeperfvm.android.models.Metric;
import com.bookkeeperfvm.android.models.Record;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SalesFragment extends Fragment {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;
    private final List<Record> objectList = new ArrayList<>();
    private RecordAdapter adapter;
    private Brand brand;
    private TextView textView;
    private TextView subTextView;
    private TextView subItemTextView;
    private final String objectID = getDate(System.currentTimeMillis());

    public SalesFragment() {
        // Required empty public constructor
    }

    public static SalesFragment getInstance(Brand brand){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.OBJECT, brand);
        SalesFragment fragment = new SalesFragment();
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
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.textView);
        subTextView = view.findViewById(R.id.subTextView);
        subItemTextView = view.findViewById(R.id.subItemTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.posts_divider)));
        recyclerView.addItemDecoration(divider);
        adapter = new RecordAdapter(getContext(), objectList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fetchObjects(objectID);
        return view;
    }

    private void fetchObjects(String objectID){
        Query query = firebaseFirestore.collection(Constants.RECORDS).orderBy("id", Query.Direction.DESCENDING).whereEqualTo("recordType",Constants.SALES).whereEqualTo("brandID",brand.getId()).whereArrayContains("tags",objectID);
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
        fetchObjects(objectID);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registration != null){
            registration.remove();
        }
    }

    private String getDate(long time){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        return DateFormat.format("dd MMMM yyy", cal).toString();
    }

    private Metric fetchMetrics(List<Record> transactionList){
        int total = 0;
        int salesTotal = 0;
        int stockTotal = 0;
        try {
            if (transactionList != null){
                for (int i = 0; i< transactionList.size();i++){
                    Record record = transactionList.get(i);
                    salesTotal += (record.getPrice() * record.getQuantity());
                    stockTotal += (record.getSalePrice() * record.getQuantity());
                    total += (salesTotal - stockTotal);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Metric(salesTotal,stockTotal,total);
    }

    private void postMetrics(){
        textView.setText(getString(R.string.currency,Constants.CURRENCY, fetchMetrics(objectList).getSales()));
        subTextView.setText(getString(R.string.currency,Constants.CURRENCY, fetchMetrics(objectList).getStock()));
        subItemTextView.setText(getString(R.string.currency,Constants.CURRENCY, fetchMetrics(objectList).getProfit()));


    }

}

