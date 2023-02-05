package com.bookkeeperfvm.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Record implements Serializable {

    private int id;
    private String title;
    private String recordType;
    private String timestamp;
    private long recordDate;
    private int price;
    private int quantity;
    private int salePrice;
    private String brandID;

    public Record() {
    }

    public Record(int id, String title, String recordType, String timestamp, long recordDate, int price, int quantity, int salePrice, String brandID) {
        this.id = id;
        this.title = title;
        this.recordType = recordType;
        this.timestamp = timestamp;
        this.recordDate = recordDate;
        this.price = price;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.brandID = brandID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Record record = (Record) obj;
        return id==record.getId();
    }
}
