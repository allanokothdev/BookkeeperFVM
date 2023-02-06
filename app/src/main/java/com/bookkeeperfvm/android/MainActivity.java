package com.bookkeeperfvm.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookkeeperfvm.android.adapters.BrandAdapter;
import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.listeners.BrandItemClickListener;
import com.bookkeeperfvm.android.models.Brand;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BrandItemClickListener, View.OnClickListener {

    private final Context mContext = MainActivity.this;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;

    private final String currentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final List<Brand> objectList = new ArrayList<>();
    private BrandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.posts_divider)));
        recyclerView.addItemDecoration(divider);
        adapter = new BrandAdapter(mContext, objectList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fetchObjects(currentUserID);
    }

    //Fetch user's Businesses from Firebase Firestore using their Firebase User ID as a filter
    private void fetchObjects(String objectID){
        Query query = firebaseFirestore.collection(Constants.BUSINESSES).orderBy("walletAddress", Query.Direction.DESCENDING).whereArrayContains("tags",objectID);
        registration = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null){
                for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        Brand object = documentChange.getDocument().toObject(Brand.class);
                        if (!objectList.contains(object)){
                            objectList.add(object);
                            adapter.notifyItemInserted(objectList.size()-1);
                        }
                    }else if (documentChange.getType()==DocumentChange.Type.MODIFIED){
                        Brand object = documentChange.getDocument().toObject(Brand.class);
                        if (objectList.contains(object)){
                            objectList.set(objectList.indexOf(object),object);
                            adapter.notifyItemChanged(objectList.indexOf(object));
                        }
                    }else if (documentChange.getType()==DocumentChange.Type.REMOVED){
                        Brand object = documentChange.getDocument().toObject(Brand.class);
                        if (objectList.contains(object)){
                            objectList.remove(object);
                            adapter.notifyItemRemoved(objectList.indexOf(object));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchObjects(currentUserID);
        adapter.notifyDataSetChanged();
    }

    //Stop Firebase from listening to new database events, (Saves Database Cost)
    @Override
    public void onStop() {
        super.onStop();
        if (registration != null){
            registration.remove();
        }
    }


    //Proceed to respective Business Profile
    @Override
    public void onBrandItemClick(Brand brand, ImageView imageView) {
        Intent intent = new Intent(mContext, BusinessProfile.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",brand);
        intent.putExtras(bundle);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(imageView, brand.getWalletAddress()));
        startActivity(intent,activityOptionsCompat.toBundle());
    }

    //Sign Out
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageView) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Sign Out");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", (dialog, which) -> { firebaseAuth.signOut();
                finishAfterTransition();
            });
            builder.setNegativeButton("Hell No", (dialog, which) -> { });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (view.getId() == R.id.floatingActionButton){

            //Create new Business on Bookkeeper
            startActivity(new Intent(mContext, CreateBusiness.class));
        }
    }


}
