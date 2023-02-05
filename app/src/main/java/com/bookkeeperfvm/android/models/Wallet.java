package com.bookkeeperfvm.android.models;

import java.io.Serializable;

public class Wallet implements Serializable {

    private String address;
    private String privateKey;

    public Wallet(){ }

    public Wallet(String address, String privateKey) {
        this.address = address;
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Wallet wallet = (Wallet)obj;
        return address.matches(wallet.getAddress());
    }
}
