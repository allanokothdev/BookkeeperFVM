package com.bookkeeperfvm.android;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.models.Brand;
import com.bookkeeperfvm.android.models.Wallet;
import com.bookkeeperfvm.android.utils.GetUser;
import com.bookkeeperfvm.android.viewpager.BusinessPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class BusinessProfile extends AppCompatActivity implements View.OnClickListener {

    private final Context mContext = BusinessProfile.this;
    private ExtendedFloatingActionButton floatingActionButton;
    private Brand brand;
    private TextView textView;
    private ImageView copyImageView;

    private boolean collapsed = false;
    public static String[] tabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        brand = (Brand) bundle.getSerializable(Constants.OBJECT);
        tabList = getResources().getStringArray(R.array.businessTabs);

        //Save Wallet Details Locally
        GetUser.saveWallet(mContext, new Wallet(brand.getWalletAddress(),brand.getPrivateKey()));

        //Setting up UI Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(brand.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        //Linking UI elements
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        ImageView imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        copyImageView = findViewById(R.id.copyImageView);
        ImageView finishImageView = findViewById(R.id.finishImageView);
        ImageView toolbarImageView = findViewById(R.id.toolbarImageView);
        TextView toolbarTextView = findViewById(R.id.toolbarTextView);
        TextView toolbarSubTextView = findViewById(R.id.toolbarSubTextView);
        AppBarLayout app_bar = findViewById(R.id.app_bar);
        imageView.setTransitionName(brand.getWalletAddress());
        textView.setText(brand.getWalletAddress());

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        finishImageView.setOnClickListener(this);

        app_bar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ( verticalOffset < -26) {
                if (!collapsed) {
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    collapsed = true;
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            } else {
                if (collapsed) {
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    collapsed = false;
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });

        //Setting Business Data to UI elements
        toolbarImageView.setTransitionName(brand.getWalletAddress());
        Glide.with(mContext.getApplicationContext()).load(brand.getPic()).centerCrop().dontAnimate().listener(new RequestListener<Drawable>() {@Override public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) { supportStartPostponedEnterTransition();return false; }@Override public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) { supportStartPostponedEnterTransition();return false; }}).into(toolbarImageView);
        toolbarTextView.setText(brand.getTitle());
        toolbarSubTextView.setText(brand.getPhone());
        setUpViewPager(viewPager, tabLayout);
        floatingActionButton.setOnClickListener(this);
        copyImageView.setOnClickListener(this);
    }

    //Setting up Business Profile Viewpager to showcase fragments
    private void setUpViewPager(ViewPager2 viewPager, TabLayout tabLayout) {
        BusinessPagerAdapter adapter = new BusinessPagerAdapter(this, brand);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabList[position])).attach();
        for (int i = 0; i < tabLayout.getTabCount(); i++){
            TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tv);
        }
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        floatingActionButton.setTag(Constants.SALES);
                        floatingActionButton.setVisibility(View.VISIBLE);
                        floatingActionButton.setText(R.string.add_new_stock);
                        break;
                    case 1:
                        floatingActionButton.setTag(Constants.STOCK);
                        floatingActionButton.setVisibility(View.VISIBLE);
                        floatingActionButton.setText(R.string.add_new_stock);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.copyImageView){

            //Copy to Clipboard
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Wallet Address", textView.getText().toString());
            clipboardManager.setPrimaryClip(clipData);

            //Change Icon Resource
            copyImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));

            //Show Toast
            Toast.makeText(mContext, "Wallet Address copied to Clipboard", Toast.LENGTH_SHORT).show();

        } else if (v.getId()==R.id.floatingActionButton){
            //Create Stock Records
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.OBJECT, brand);
            startActivity(new Intent(mContext, CreateStock.class).putExtras(bundle));
        } else if (v.getId()==R.id.finishImageView){
            //Close Business Profile Page
            finishAfterTransition();
        }
    }

}