package com.bookkeeperfvm.android.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.bookkeeperfvm.android.constants.Constants;
import com.bookkeeperfvm.android.models.Wallet;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GetUser extends Application {


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

}
