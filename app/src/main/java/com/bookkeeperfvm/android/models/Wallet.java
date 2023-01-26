package com.bookkeeperfvm.android.models;

import java.io.Serializable;

public class Wallet implements Serializable {

    private String address;
    private String privateKey;
    private String seedPhrase;
    private String keyStore;

    public Wallet(){ }

    public Wallet(String address, String privateKey, String seedPhrase, String keyStore) {
        this.address = address;
        this.privateKey = privateKey;
        this.seedPhrase = seedPhrase;
        this.keyStore = keyStore;
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

    public String getSeedPhrase() {
        return seedPhrase;
    }

    public void setSeedPhrase(String seedPhrase) {
        this.seedPhrase = seedPhrase;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Wallet wallet = (Wallet)obj;
        return address.matches(wallet.getAddress());
    }
}
