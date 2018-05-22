package com.james.stockparser.NetWork;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 101716 on 2018/5/21.
 */

public class stockDividend {
    String TAG = stockDividend.class.getSimpleName();

    public Map<String,String> getDividend(String date){
        String urlDevidend = "http://www.twse.com.tw/exchangeReport/BWIBBU_d?response=html&date="+date+"&selectType=ALL";
        //Log.e(TAG, "urlDevidend: " + urlDevidend);
        Map<String, String> stockDividend = new HashMap<String, String>();

        try {
            Document doc = Jsoup.connect(urlDevidend).get();
            Element table = doc.select("tbody").first();
            Elements rows = table.select("tr");
            Element td_stock, td_dividend;
            for (int i = 0; i < rows.size(); i++) {
                td_stock = rows.get(i).child(0);
                td_dividend = rows.get(i).child(2);
                stockDividend.put(rows.get(i).child(0).text(),rows.get(i).child(2).text());
                //Log.e(TAG, td_stock.text() + " : "+ td_dividend.text());
            }

        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
        return stockDividend;
    }
}
