package com.james.stockparser;

import android.content.Context;
import android.util.Log;

/**
 * Created by 101716 on 2017/6/27.
 */

public class StockItem {
    String TAG = StockItem.class.getSimpleName();
    private String stockNumber, stockName, tianxiCount, releaseCount, tianxiPercent, tianxiDay, thisYear;
    Context context;
    private int _id;

    public StockItem(String stockNumber, String stockName, String tianxiCount, String releaseCount, String tianxiPercent, String tianxiDay, String thisYear) {
        this.stockNumber = stockNumber; //股票代號
        this.stockName = stockName; //股票名稱
        this.tianxiCount = tianxiCount; //填息次數
        this.releaseCount = releaseCount; //發放次數
        this.tianxiPercent = tianxiPercent; //填息比例
        this.tianxiDay = tianxiDay; //填息平均天數
        this.thisYear = thisYear; //本年除息日
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public String getStockName() {
        return stockName;
    }

    public String getTianxiCount() {
        return tianxiCount;
    }
    public String getReleaseCount() {
        return releaseCount;
    }

    public String getTianxiPercent() {
        return tianxiPercent;
    }

    public String getTianxiDay() {
        return tianxiDay;
    }

    public String getThisYear() {
        return thisYear;
    }

}
