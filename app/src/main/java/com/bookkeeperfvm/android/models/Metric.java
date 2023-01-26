package com.bookkeeperfvm.android.models;

import java.io.Serializable;

public class Metric implements Serializable {

    private int sales;
    private int stock;
    private int profit;

    public Metric(){ }

    public Metric(int sales, int stock, int profit) {
        this.sales = sales;
        this.stock = stock;
        this.profit = profit;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }
}
