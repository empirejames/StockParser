package com.james.stockparser;

import android.content.Context;

/**
 * Created by 101716 on 2017/6/27.
 */

public class StockEPS {
    String TAG = StockEPS.class.getSimpleName();
    private String stockNumber, stockName, stockEPS;
    Context context;
    private int _id;

    public StockEPS(String stockNumber, String stockName, String stockEPS) {
        this.stockNumber = stockNumber; //股票代號
        this.stockName = stockName; //股票名稱
        this.stockEPS = stockEPS; //EPS
    }

    public String getStockNumber() {
        return stockNumber;
    }
    public String getStockName() {
        return stockName;
    }
    public String getStockEPS() {
        return stockEPS;
    }

}
