package com.bookkeeperfvm.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Record implements Serializable {

    private String id;
    private String pic;
    private String title;
    private String summary;
    private String recordType;
    private String timestamp;
    private long recordDate;
    private int price;
    private int quantity;
    private int salePrice;
    private String brandID;
    private ArrayList<String> tags;

    public Record() {
    }

    public Record(String id, String pic, String title, String summary, String recordType, String timestamp, long recordDate, int price, int quantity, int salePrice, String brandID, ArrayList<String> tags) {
        this.id = id;
        this.pic = pic;
        this.title = title;
        this.summary = summary;
        this.recordType = recordType;
        this.timestamp = timestamp;
        this.recordDate = recordDate;
        this.price = price;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.brandID = brandID;
        this.tags = tags;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(long recordDate) {
        this.recordDate = recordDate;
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

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
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
