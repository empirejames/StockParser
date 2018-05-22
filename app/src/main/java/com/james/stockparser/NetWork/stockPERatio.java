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

public class stockPERatio {
    String TAG = stockPERatio.class.getSimpleName();

    public Map<String,String> getPERatio(String date){
        String urlPERatio = "http://www.twse.com.tw/exchangeReport/BWIBBU_d?response=html&date="+date+"&selectType=ALL";
        //Log.e(TAG, "urlPERatio: " + urlPERatio);
        Map<String, String> stockPERatio = new HashMap<String, String>();

        try {
            Document doc = Jsoup.connect(urlPERatio).get();
            Element table = doc.select("tbody").first();
            Elements rows = table.select("tr");
            Element td_stock, td_dividend;
            for (int i = 0; i < rows.size(); i++) {
                stockPERatio.put(rows.get(i).child(0).text(),rows.get(i).child(4).text());
                //Log.e(TAG, rows.get(i).child(0).text() + " : "+ rows.get(i).child(4).text());
            }

        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
        return stockPERatio;
    }
}
