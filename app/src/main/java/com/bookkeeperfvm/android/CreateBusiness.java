package com.bookkeeperfvm.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.models.Brand;
import com.bookkeeperfvm.android.models.Wallet;
import com.bookkeeperfvm.android.utils.GetUser;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CreateBusiness extends AppCompatActivity implements View.OnClickListener {

    private Wallet wallet;
    private RelativeLayout container;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private final Context mContext = CreateBusiness.this;
    private final String currentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Button button;

    private ImageView imageView;
    private TextInputEditText titleTextInputEditText;
    private AVLoadingIndicatorView progressBar;
    private Uri mainImageUri = null;
    private String downloadUrlString = null;
    public final static int PICK_IMAGE = 1;
    private CountryCodePicker countryCodePicker;
    private String walletAddress;

    //CREATE NEW WALLET ADDRESS AND PRIVATE KEY FOR THE BUSINESS
    @Override
    protected void onStart() {
        super.onStart();
        createWallet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_business);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        EditText editText = findViewById(R.id.editText);
        Button uploadButton = findViewById(R.id.uploadButton);
        container = findViewById(R.id.container);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        titleTextInputEditText = findViewById(R.id.titleTextInputEditText);
        progressBar = findViewById(R.id.progressBar);
        button.setOnClickListener(this);
        countryCodePicker.detectLocaleCountry(true);
        countryCodePicker.detectSIMCountry(true);
        countryCodePicker.detectNetworkCountry(true);
        countryCodePicker.registerCarrierNumberEditText(editText);
        uploadButton.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @TargetApi(23)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {

            String title = titleTextInputEditText.getText().toString().trim();
            String phone = countryCodePicker.getFullNumberWithPlus().trim();
            progressBar.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(title)) {

                titleTextInputEditText.setError(getString(R.string.enter_brand_title));
                titleTextInputEditText.requestFocus();
                progressBar.setVisibility(View.GONE);

            }else if (TextUtils.isEmpty(phone)){

                countryCodePicker.requestFocus();
                Snackbar snackbar = Snackbar.make(container,"Enter phone Number", Snackbar.LENGTH_SHORT);snackbar.show();
                progressBar.setVisibility(View.GONE);


            } else if (mainImageUri==null){

                imageView.requestFocus();
                Toast.makeText(mContext, "Add Brand Profile Photo",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            } else {
                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                createBusiness(title,phone);
            }
        } else if (v.getId()==R.id.uploadButton){

            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Business Photo"),PICK_IMAGE);
        }
    }

    private void createBusiness(String title, String phone){
        try {
            ArrayList<String> tags = new ArrayList<>();
            tags.add(currentUserID);
            if (wallet != null){
                tags.add(walletAddress);
            }
            if (downloadUrlString != null){
                assert wallet != null;
                Brand brand = new Brand(wallet.getAddress(),wallet.getPrivateKey(),downloadUrlString,title,phone,tags);
                publishObject(brand);

            } else {

                //STORE BUSINESS IMAGE IN FIREBASE STORAGE
                final StorageReference ref = FirebaseStorage.getInstance().getReference().child(Constants.BUSINESSES).child(System.currentTimeMillis() + ".png");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mainImageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] data = byteArrayOutputStream.toByteArray();
                UploadTask uploadTask = ref.putBytes(data);
                uploadTask.addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        Uri downloadUri = task.getResult();
                        Brand brand = new Brand(wallet.getAddress(),wallet.getPrivateKey(),downloadUri.toString(),title,phone,tags);
                        publishObject(brand);
                    }
                })).addOnFailureListener(e -> {
                    button.setEnabled(true);
                    Snackbar snackbar = Snackbar.make(container, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT);snackbar.show();
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
        } catch (IOException e){e.printStackTrace(); }
    }

    //PUBLISH BUSINESS DETAILS IN FIREBASE FIRESTORE STORAGE
    private void publishObject(Brand brand){
        firebaseFirestore.collection(Constants.BUSINESSES).document(brand.getWalletAddress()).set(brand).addOnSuccessListener(unused -> {
            progressBar.setVisibility(View.GONE);
            finishAfterTransition();
        });
    }


    //FETCHES SELECTED IMAGE FROM USERS PHONE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode ==PICK_IMAGE && resultCode == RESULT_OK && data != null){
                mainImageUri = data.getData();
                imageView.setImageURI(mainImageUri);
                postImage(mainImageUri);
            }
        }catch (Exception e){e.printStackTrace(); }
    }

    //STORES BUSINESS IMAGE IN FIREBASE STORAGE
    private void postImage(Uri mainImageUri){
        try {
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child(Constants.BUSINESSES).child(System.currentTimeMillis() + ".png");
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mainImageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    downloadUrlString = downloadUri.toString();
                }
            })).addOnFailureListener(e -> {
                Snackbar snackbar = Snackbar.make(container, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT);snackbar.show();
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //CREATES NEW WALLET ADDRESS & PRIVATE KET FOR THE BUSINESS
    private void createWallet() {
        firebaseFunctions.getHttpsCallable(Constants.CREATE_WALLET).call().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                assert result != null;
                String privateKey = Objects.requireNonNull(result.get("privateKey")).toString();
                String address = Objects.requireNonNull(result.get("address")).toString();
                walletAddress = address;
                wallet = new Wallet(address,privateKey);
                GetUser.saveWallet(mContext,wallet);
            } else {
                Snackbar snackbar = Snackbar.make(container, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }
}