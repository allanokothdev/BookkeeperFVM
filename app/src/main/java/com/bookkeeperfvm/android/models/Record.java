package com.bookkeeperfvm.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Record implements Serializable {

    private String id;
    private String productID;
    private String pic;
    private String title;
    private String summary;
    private String recordType;
    private String paymentType;
    private String timestamp;
    private String monthView;
    private String yearView;
    private long recordDate;
    private long expiryDate;
    private int price;
    private int quantity;
    private int salePrice;
    private String token;
    private String brandID;
    private String publisher;
    private ArrayList<String> tags;

    public Record() {
    }

    public Record(String id, String productID, String pic, String title, String summary, String recordType, String paymentType, String timestamp, String monthView, String yearView, long recordDate, long expiryDate, int price, int quantity, int salePrice, String token, String brandID, String publisher, ArrayList<String> tags) {
        this.id = id;
        this.productID = productID;
        this.pic = pic;
        this.title = title;
        this.summary = summary;
        this.recordType = recordType;
        this.paymentType = paymentType;
        this.timestamp = timestamp;
        this.monthView = monthView;
        this.yearView = yearView;
        this.recordDate = recordDate;
        this.expiryDate = expiryDate;
        this.price = price;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.token = token;
        this.brandID = brandID;
        this.publisher = publisher;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMonthView() {
        return monthView;
    }

    public void setMonthView(String monthView) {
        this.monthView = monthView;
    }

    public String getYearView() {
        return yearView;
    }

    public void setYearView(String yearView) {
        this.yearView = yearView;
    }

    public long getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(long recordDate) {
        this.recordDate = recordDate;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Record record = (Record) obj;
        return id.matches(record.getId());
    }
}
