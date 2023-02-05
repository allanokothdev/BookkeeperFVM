package com.bookkeeperfvm.android.constants;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Constants {

    public static String CURRENCY = "USD";
    public final static String KEY_STORE_REFERENCE = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    public final static String ADDRESS = "address";
    public final static String PRIVATE_KEY = "privateKey";
    public final static String OBJECT = "object";
    public final static String COUNT = "count";

    public final static String BUSINESSES = "Businesses";
    public final static String STOCK = "Stock";
    public final static String SALES = "Sales";

    public static final String CREATE_WALLET = "createWallet";
    public static final String FETCH_STOCKS = "fetchStocks";
    public static final String FETCH_SALES = "fetchSales";
    public static final String CREATE_STOCK = "createStock";
    public static final String CREATE_SALE = "createSale";
    public static final String FETCH_CREDIT_SCORE = "fetchCreditScore";

}
