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
import com.bookkeeperfvm.android.utils.GetUser;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements BrandItemClickListener, View.OnClickListener {

    private final Context mContext = MainActivity.this;
    private final String TAG = this.getClass().getSimpleName();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String currentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;
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
        fetchToken();
    }

    private void fetchObjects(String objectID){
        Query query = firebaseFirestore.collection(Constants.BUSINESSES).orderBy("id", Query.Direction.DESCENDING).whereArrayContains("tags",objectID);
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
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registration != null){
            registration.remove();
        }
    }


    @Override
    public void onBrandItemClick(Brand brand, ImageView imageView) {
        Intent intent = new Intent(mContext, BusinessProfile.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",brand);
        intent.putExtras(bundle);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(imageView, brand.getId()));
        startActivity(intent,activityOptionsCompat.toBundle());
    }

    private void fetchToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String token = task.getResult();
                String msg = getString(R.string.msg_token_fmt, token);
                Timber.d(msg);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("token",token);
                FirebaseDatabase.getInstance().getReference(Constants.TOKEN).child(currentUserID).updateChildren(hashMap);
                GetUser.saveObject(mContext,Constants.TOKEN,token);
            }else {
                Timber.tag(TAG).w(task.getException(), "Fetching FCM registration token failed");
            }
        });
    }

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
            startActivity(new Intent(mContext, CreateBusiness.class));
        }
    }
}
