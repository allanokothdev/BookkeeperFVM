package com.bookkeeperfvm.android.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.models.Category;
import com.bookkeeperfvm.android.models.User;
import com.bookkeeperfvm.android.models.Wallet;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GetUser extends Application {

    public static User getUser(Context context) {
        User user = new User();
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences prefs = EncryptedSharedPreferences.create(Constants.KEY_STORE_REFERENCE, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            return new User(Constants.KEY_STORE_REFERENCE, prefs.getString(Constants.NAME, Constants.NAME), prefs.getString(Constants.PIC, Constants.PIC), prefs.getString(Constants.EMAIL, Constants.EMAIL),prefs.getString(Constants.PHONE_NUMBER, Constants.PHONE_NUMBER),  prefs.getString(Constants.GENDER,Constants.GENDER), prefs.getString(Constants.TOKEN,Constants.TOKEN),prefs.getString(Constants.ADDRESS,Constants.ADDRESS),null);
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
        return user;
    }

    public static Wallet getWallet(Context context) {
        Wallet wallet = new Wallet();
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.KEY_STORE_REFERENCE, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            wallet = new Wallet(sharedPreferences.getString(Constants.ADDRESS, Constants.ADDRESS), sharedPreferences.getString(Constants.PRIVATE_KEY, Constants.PRIVATE_KEY),sharedPreferences.getString(Constants.SEED_PHRASE, Constants.SEED_PHRASE), sharedPreferences.getString(Constants.KEY_STORE,Constants.KEY_STORE));
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
        return wallet;
    }

    public static void saveObject(Context context, String key, String value) {

        try{
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.KEY_STORE_REFERENCE, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
    }

    public static void saveWallet(Context context, Wallet wallet) {
        try{
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.KEY_STORE_REFERENCE, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.ADDRESS, wallet.getAddress());
            editor.putString(Constants.PRIVATE_KEY, wallet.getPrivateKey());
            editor.putString(Constants.SEED_PHRASE, wallet.getSeedPhrase());
            editor.putString(Constants.KEY_STORE, wallet.getKeyStore());
            editor.apply();
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
    }

    public static void saveUser(Context context, User user) {
        try{
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.KEY_STORE_REFERENCE, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.PIC, user.getPic());
            editor.putString(Constants.NAME, user.getName());
            editor.putString(Constants.EMAIL, user.getEmail());
            editor.putString(Constants.GENDER, user.getGender());
            editor.putString(Constants.PHONE_NUMBER, user.getPhone());
            editor.putString(Constants.TOKEN, user.getToken());
            editor.putString(Constants.ADDRESS, user.getAddress());
            editor.apply();
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
    }

    public static String fetchObject(Context context, String key) {
        String object = "0";
        try{
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.KEY_STORE_REFERENCE, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            object = sharedPreferences.getString(key, "0");
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
        return object;
    }

    public static String fetchPasscode(Context context) {
        String passcode = Constants.DEFAULT_PASSCODE;
        try{
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.KEY_STORE_REFERENCE, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            passcode = sharedPreferences.getString(Constants.PASSCODE, Constants.DEFAULT_PASSCODE);
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
        return passcode;
    }

    public static void savePasscode(Context context,String value) {
        try{
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.KEY_STORE_REFERENCE, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.PASSCODE, value);
            editor.apply();
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
    }

    public static Category calculateProfitOrLoss(int sales, int expenses){
        String type = "Profit";
        int balance = 0;

        if (sales > expenses){
            type = "Profit";
            balance = sales - expenses;
        } else if (expenses > sales){
            type = "Loss";
            balance = expenses - sales;
        }
        return new Category(type,balance);
    }
}
