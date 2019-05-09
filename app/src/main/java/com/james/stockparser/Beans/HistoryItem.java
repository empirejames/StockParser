package com.james.stockparser.Beans;

import android.content.Context;

/**
 * Created by 101716 on 2017/6/27.
 */

public class HistoryItem {
    String TAG = HistoryItem.class.getSimpleName();
    private String hisStockNumber, hisStockName, hisProduct;
    private String hisEPS;
    Context context;
    private int _id;

    public HistoryItem(String hisStockNumber, String hisStockName, String hisProduct, String hisEPS) {
        this.hisStockNumber = hisStockNumber;
        this.hisStockName = hisStockName;
        this.hisProduct = hisProduct;
        this.hisEPS = hisEPS;
    }

    public String getHisStockNumber() {
        return hisStockNumber;
    }

    public String getHisStockName() {
        return hisStockName;
    }

    public String getHisProduct() {
        return hisProduct;
    }
    public String getHisEPS() {
        return hisEPS;
    }

}
