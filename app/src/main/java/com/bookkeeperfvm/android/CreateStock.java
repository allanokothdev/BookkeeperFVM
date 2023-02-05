package com.bookkeeperfvm.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.models.Brand;
import com.bookkeeperfvm.android.models.Record;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateStock extends AppCompatActivity implements View.OnClickListener {

    private final Context mContext = CreateStock.this;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private Button button;
    private AutoCompleteTextView titleTextInputEditText;
    private TextInputEditText quantityTextInputEditText;
    private TextInputEditText salePriceTextInputEditText;
    private TextInputEditText buyingPriceTextInputEditText;
    private AVLoadingIndicatorView progressBar;

    private Brand brand;
    private final long selectedDate = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stock);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        brand = (Brand) bundle.getSerializable(Constants.OBJECT);

        quantityTextInputEditText = findViewById(R.id.quantityTextInputEditText);
        buyingPriceTextInputEditText = findViewById(R.id.buyingPriceTextInputEditText);
        Button uploadButton = findViewById(R.id.uploadButton);
        ImageView imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        titleTextInputEditText = findViewById(R.id.titleTextInputEditText);
        salePriceTextInputEditText = findViewById(R.id.salePriceTextInputEditText);
        progressBar = findViewById(R.id.progressBar);
        button.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        Glide.with(mContext.getApplicationContext()).load(brand.getPic()).placeholder(R.drawable.placeholder).into(imageView);
        imageView.setOnClickListener(this);
    }


    @TargetApi(23)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {

            String title = titleTextInputEditText.getText().toString().trim();
            String salePrice = salePriceTextInputEditText.getText().toString().trim();
            String buyingPrice = buyingPriceTextInputEditText.getText().toString().trim();
            String quantity = quantityTextInputEditText.getText().toString().trim();
            progressBar.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(title)) {

                titleTextInputEditText.setError(getString(R.string.enter_product_service_title));
                progressBar.setVisibility(View.GONE);
                titleTextInputEditText.requestFocus();

            }else if (TextUtils.isEmpty(salePrice)){
                salePriceTextInputEditText.setError(getString(R.string.enter_sale_price));
                progressBar.setVisibility(View.GONE);
                salePriceTextInputEditText.requestFocus();

            }else if (TextUtils.isEmpty(buyingPrice)){
                buyingPriceTextInputEditText.setError(getString(R.string.enter_unit_buying_price));
                progressBar.setVisibility(View.GONE);
                buyingPriceTextInputEditText.requestFocus();

            }else if (TextUtils.isEmpty(quantity)){
                quantityTextInputEditText.setError(getString(R.string.enter_sales_quantity));
                progressBar.setVisibility(View.GONE);
                quantityTextInputEditText.requestFocus();

            } else {
                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                try {
                    createRecord(title,Integer.parseInt(salePrice),Integer.parseInt(quantity),Integer.parseInt(buyingPrice),selectedDate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void createRecord(String title, int salePrice, int quantity, int buyingPrice, long selectedDate) throws IOException {
        Record record = new Record(0,title,Constants.STOCK,getDate(selectedDate),selectedDate,buyingPrice,quantity,salePrice,brand.getWalletAddress());

        Map<String, Object> data = new HashMap<>();
        data.put("key", brand.getPrivateKey());
        data.put("title", record.getTitle());
        data.put("recordType", record.getRecordType());
        data.put("timestamp", record.getTimestamp());
        data.put("recordDate", record.getRecordDate());
        data.put("price", record.getPrice());
        data.put("quantity", record.getQuantity());
        data.put("salesPrice", record.getSalePrice());
        data.put("brandId", record.getBrandID());
        firebaseFunctions.getHttpsCallable(Constants.CREATE_STOCK).call(record).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    private String getDate(long time){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        return DateFormat.format("EEEE, dd MMMM yyyy", cal).toString();
    }


}