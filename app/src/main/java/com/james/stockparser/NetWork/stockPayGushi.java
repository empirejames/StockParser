package com.james.stockparser.NetWork;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 101716 on 2017/8/22.
 */

public class stockPayGushi {
    String TAG = stockPayGushi.class.getSimpleName();
    String stockNm;
    final String url = "https://stock.wespai.com/rate107";
    public stockPayGushi() {
    }

    public Map<String,String> getNowGuShi() {
        Map<String,String> temp = new HashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table[id=example]").first();
            Elements rows = table.select("tr");
            Elements td;
            for (int i = 0; i < rows.size(); i++) {
                td = rows.get(i).children();

                for (int j = 0; j < td.size(); j++) {
                    if (j % 22 == 0) {
                        if (isNumeric(td.get(j).text())) {

                            String [] result = td.text().split(" ");
                            //Log.e(TAG, result[0] + "  " + result.length);
                            String stockNm = result[0];
                            String payGu = "";
                            String payShi = "";
                            if(result.length>4){

                                if(result[2].length()>4){
                                    payGu = result[2].substring(0,3);
                                }else{
                                    payGu = result[2];
                                }
                                if(result[4].length()>4){
                                    payShi = result[4].substring(0,3);
                                }else{
                                    payShi = result[4];
                                }

                               //Log.e(TAG, stockNm + " :: " + payGu+ " :: " + payShi);
                                temp.put(stockNm,payGu+ "," + payShi);
                            }

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
