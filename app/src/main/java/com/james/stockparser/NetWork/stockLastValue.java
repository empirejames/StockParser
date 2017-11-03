package com.james.stockparser.NetWork;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.james.stockparser.Fragment.FragmentAbout;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 101716 on 2017/8/22.
 */

public class stockLastValue {
    String TAG = stockLastValue.class.getSimpleName();
    String stockNm;
    String result4;
    JSONObject jsonObjectM;
    JSONArray jsonArray;
    private String value;
    Context context;
    final String tseURL = "http://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
    final String otcURL = "http://isin.twse.com.tw/isin/C_public.jsp?strMode=4";
    public stockLastValue(String stockNm, Context context) {
        this.stockNm = stockNm;
        this.context = context;
    }

    public String getData() {
        final String url = "https://www.google.com/finance/getprices?q=" + stockNm + "&x=TPE&i=7200&p=2d";
        final String mainUrl = "http://163.29.17.179/stock/fibest.jsp?lang=zh_tw";

        final String twUrl_tse = "http://163.29.17.179/stock/api/getStockInfo.jsp?ex_ch=tse_"+stockNm+".tw&json=1&delay=0&_=";
        final String twUrl_otc = "http://163.29.17.179/stock/api/getStockInfo.jsp?ex_ch=otc_"+stockNm+".tw&json=1&delay=0&_=";
        String[] temp;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = timestamp.getTime() + "";
        connectTSE(mainUrl,twUrl_tse,ts);
        try {
            if(jsonArray.length()==0){ //上櫃股票
                connectTSE(mainUrl,twUrl_otc,ts);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    Log.e(TAG, i + " : " + jsonItem.getString("z"));
                    value = jsonItem.getString("z");
                }
            }else{ //上市股票
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    Log.e(TAG, i + " : " + jsonItem.getString("z"));
                    value = jsonItem.getString("z");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//            String json = Jsoup.connect(twUrl+ts).ignoreContentType(true)
//                    .cookie("JSESSIONID", sessionId)
//                    .execute()
//                    .body();
//            temp = json.split(",");
//            Log.e(TAG,json);
            //value = temp[temp.length - 4]; // close value
        return value;
    }
    public boolean connectTSE(String mainUrl, String twUrl_tse, String ts){
        try {
            Connection.Response res = Jsoup.connect(mainUrl)
                    .method(Connection.Method.GET)
                    .execute();
            Log.e(TAG, "cookies " + res.cookies() + "");
            String sessionId = res.cookie("JSESSIONID");
            Document document = Jsoup.connect(twUrl_tse + ts)
                    .cookie("JSESSIONID", sessionId)
                    .get();
            String htmlString = document.toString();
            String result = htmlString.replace("<html>", "");
            String result1 = result.replace("<head></head>", "");
            String result2 = result1.replace("<body>", "");
            String result3 = result2.replace("</body>", "");
            result4 = result3.replace("</html>", "");
            jsonObjectM = new JSONObject(result4);
            jsonArray = jsonObjectM.getJSONArray("msgArray");
            Log.e(TAG, result4.trim());
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return true;
    }


//    protected void onPostExecute(String result) {
//        Log.e(TAG, "++ : " + value);
//        FragmentAbout mainactivity = (FragmentAbout) context;
//        mainactivity.returnString = result;
//        super.onPostExecute(result);
//    }
}
