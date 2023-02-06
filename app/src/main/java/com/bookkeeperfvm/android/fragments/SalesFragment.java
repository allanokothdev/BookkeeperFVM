package com.bookkeeperfvm.android.fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SalesFragment extends Fragment {

    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();

    private final List<Record> objectList = new ArrayList<>();
    private RecordAdapter adapter;
    private Brand brand;
    private TextView textView;
    private AVLoadingIndicatorView progressBar;

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

        //Binding UI Elements with App LOgic
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.posts_divider)));
        recyclerView.addItemDecoration(divider);

        //Setting up Adapter to showcase Sales Records
        adapter = new RecordAdapter(getContext(), objectList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fetchSales();
        return view;
    }

    //Fetch Sales Records recorded on the FEVM via Firebase Functions
    private void fetchSales(){
        try {
            progressBar.setVisibility(View.VISIBLE);

            //Parse business private key as argument
            Map<String, Object> data = new HashMap<>();
            data.put("key", brand.getPrivateKey());
            firebaseFunctions.getHttpsCallable(Constants.FETCH_SALES).call(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    //Return data as an array and later loop over it to fetch the Sales Records
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

                            //Map fetched records into Record Object
                            Record record = new Record(recordId,title,recordType,timestamp,recordDate,price,quantity,salePrice,brandID);

                            //Add Record to the list
                            if (!objectList.contains(record)){
                                objectList.add(record);
                                postSalesTotal();

                                //Hide the progress bar
                                progressBar.setVisibility(View.INVISIBLE);

                                //Notify the Adapter to Update the UI
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Sorry, There are no Sales Record Uploaded", Toast.LENGTH_SHORT).show();
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
        fetchSales();
    }


    private String getDate(long time){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        return DateFormat.format("dd MMMM yyy", cal).toString();
    }

    private int fetchSalesTotal(List<Record> transactionList){
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

    private void postSalesTotal(){
        textView.setText(getString(R.string.currency,Constants.CURRENCY, fetchSalesTotal(objectList)));
    }

}

