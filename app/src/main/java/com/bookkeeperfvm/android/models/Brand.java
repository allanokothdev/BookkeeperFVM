package com.bookkeeperfvm.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Brand implements Serializable {

    private String id;
    private String pic;
    private String title;
    private String phone;
    private String walletAddress;
    private String currency;
    private Location location;
    private String publisher;
    private String token;
    private String type;
    private ArrayList<String> tags;
    private ArrayList<String> products;
    private boolean verification;
    private long deadlineDate;

    public Brand() {
    }

    public Brand(String id, String pic, String title, String phone, String walletAddress, String currency, Location location, String publisher, String token, String type, ArrayList<String> tags, ArrayList<String> products, boolean verification, long deadlineDate) {
        this.id = id;
        this.pic = pic;
        this.title = title;
        this.phone = phone;
        this.walletAddress = walletAddress;
        this.currency = currency;
        this.location = location;
        this.publisher = publisher;
        this.token = token;
        this.type = type;
        this.tags = tags;
        this.products = products;
        this.verification = verification;
        this.deadlineDate = deadlineDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public boolean isVerification() {
        return verification;
    }

    public void setVerification(boolean verification) {
        this.verification = verification;
    }

    public long getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(long deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Brand brand = (Brand) obj;
        return id.matches(brand.getId());
    }
}
