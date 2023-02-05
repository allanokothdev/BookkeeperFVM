package com.bookkeeperfvm.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Brand implements Serializable {

    private String walletAddress;
    private String privateKey;
    private String pic;
    private String title;
    private String phone;
    private ArrayList<String> tags;

    public Brand() {
    }

    public Brand(String walletAddress, String privateKey, String pic, String title, String phone, ArrayList<String> tags) {
        this.walletAddress = walletAddress;
        this.privateKey = privateKey;
        this.pic = pic;
        this.title = title;
        this.phone = phone;
        this.tags = tags;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Brand brand = (Brand) obj;
        return walletAddress.matches(brand.getWalletAddress());
    }
}
