package com.james.stockparser;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.james.stockparser.NetWork.stockHotCount;
import com.james.stockparser.NetWork.stockPayGushi;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockInfoParser {
    Context mContext;
    String TAG = StockInfoParser.class.getSimpleName();
    String url = "http://goodinfo.tw/StockInfo/StockDividendScheduleList.asp?MARKET_CAT=%E5%85%A8%E9%83%A8&YEAR=%E5%8D%B3%E5%B0%87%E9%99%A4%E6%AC%8A%E6%81%AF&INDUSTRY_CAT=%E5%85%A8%E9%83%A8";
    String urlForGuHi = "http://stock.wespai.com/p/5625";
    String urlForEPS = "http://stock.wespai.com/p/7733";
    String urlForPresent = "http://stock.wespai.com/stock107";
    String urlForGuli = "http://stock.wespai.com/tenrate#";
    String urlChoMa = "https://stock.wespai.com/lists";
    Map<String, String> stockDividend;
    private HandlerThread mThread;
    private Handler mThreadHandler;
    DatabaseReference ref;
    ArrayList<String> stockNumberList = new ArrayList<String>();
    ArrayList<String> historyGuHi = new ArrayList<String>();
    ArrayList<String> historyEPS = new ArrayList<String>();
    ArrayList<String> historyPresent = new ArrayList<String>();
    ArrayList<String> historyGuli = new ArrayList<String>();
    ArrayList<String> choMaValue = new ArrayList<String>();

    public void start(final Context mContext) {
        Log.e(TAG, " Thread . start ... ");
        mThread = new HandlerThread("jsoup");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {

//                getDividend();
//                getValue();
                //historyGuHi = getUrlInfo(urlForGuHi, mContext);
                //updateHistoryData("guShi");
                //Log.e(TAG, "updateHistoryData :: guShi");
                //historyEPS = getUrlInfo(urlForEPS , mContext);
                //updateHistoryData("eps");
//                Log.e(TAG,"updateHistoryData :: EPS");
//               historyPresent = getUrlInfo(urlForPresent, mContext);
                //               updateHistoryData("present");
//                Log.e(TAG,"updateHistoryData :: present");
//                historyGuli = getUrlInfo(urlForGuli);
//                updateHistoryData("guli");
//                Log.e(TAG,"updateHistoryData :: guli");
                getDateTaiXiDay();
                updateStockData();
//                getChoma();
//                  Log.e(TAG,"updateStockData :: TaiXiDay");
//                  getRemoteConfig();
//                Map<String, String> getPaygushi = new HashMap<String, String>();
//                stockPayGushi stp = new stockPayGushi();
//                getPaygushi = stp.getNowGuShi();
//                Log.e(TAG,getPaygushi.get("2640"));
            }
        });

    }

    private void getRemoteConfig() {

        final FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();
        long cacheExpiration = 3600;
        mRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Make the values available to your app
                            mRemoteConfig.activateFetched();
                            //get value from remote config
                            String testString = mRemoteConfig.getString("test");
                            Log.e(TAG, "Test " + testString);
                        }
                    }
                });
    }

    private void updateHistoryData(final String stuff) {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (stuff.equals("guShi")) {
                    for (int i = 0; i < historyGuHi.size(); i++) {
                        for (int j = 0; j < historyGuHi.get(i).split(" ").length; j++) {
                            ref.child("history").child(historyGuHi.get(i).split(" ")[0]).child(stuff).child(j + "").setValue(historyGuHi.get(i).split(" ")[j]);
                            Log.e(TAG, "Set Gu hi : " + historyGuHi.get(i).split(" ")[j]);
                            //ref.child("history").child(i+"").child(stuff).child(j + "").setValue(historyGuHi.get(0).split(" ")[j]);
                        }
                    }
                } else if (stuff.equals("eps")) {
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp.getKey().equals("history")) {
                            for (DataSnapshot stockNm : dsp.getChildren()) {
                                for (int i = 0; i < historyEPS.size(); i++) {
                                    if (historyEPS.get(i).split(" ")[0].equals(stockNm.getKey().toString())) {

                                        for (int j = 0; j < historyEPS.get(i).split(" ").length; j++) {
                                            ref.child("history").child(historyEPS.get(i).split(" ")[0]).child(stuff).child(j + "").setValue(historyEPS.get(i).split(" ")[j]);
                                            Log.e(TAG, " set Value : " + historyEPS.get(i).split(" ")[j]);

                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (stuff.equals("present")) {
                    DatabaseReference fbDb = null;
                    if (fbDb == null) {
                        fbDb = FirebaseDatabase.getInstance().getReference();
                    }
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {

                        if (dsp.getKey().equals("history")) {
                            for (DataSnapshot stockNm : dsp.getChildren()) {
                                for (int i = 0; i < historyPresent.size(); i++) {
                                    if (historyPresent.get(i).split(" ")[1].equals(stockNm.getKey().toString())) {
                                        UpdateNewPresent(historyPresent.get(i).split(" ")[1], historyPresent.get(i).split(" ")[5], true);
                                    } else {
                                        //UpdateNewPresent(historyPresent.get(i).split(" ")[1],historyPresent.get(i).split(" ")[5],false);
                                    }
                                }
                            }
                        }
                    }
                } else if (stuff.equals("guli")) {
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp.getKey().equals("history")) {
                            for (DataSnapshot stockNm : dsp.getChildren()) {
                                for (int i = 0; i < historyGuli.size(); i++) {
                                    if (historyGuli.get(i).split(" ")[0].equals(stockNm.getKey().toString())) {
                                        for (int j = 0; j < historyGuli.get(i).split(" ").length; j++) {
                                            ref.child("history").child(historyGuli.get(i).split(" ")[0]).child(stuff).child(j + "").setValue(historyGuli.get(i).split(" ")[j]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void UpdateNewPresent(final String stockNum, final String present, final boolean isHavePresent) {

        ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = ref.child("history").child(stockNum).child("present");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String size = dataSnapshot.getChildrenCount() + "";
                Log.e(TAG, "UpdateNewPresent:: " + stockNum + " : " + present + " : " + size);
                if (isHavePresent) {
                    ref.child("history").child(stockNum).child("present").child(size).setValue(present);
                } else {
                    ref.child("history").child(stockNum).child("present").child(size).setValue("無發放");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateStockData() {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if (dsp.getKey().equals("stocks")) {
                        for (DataSnapshot stock : dsp.getChildren()) {
                            for (int i = 0; i < stockNumberList.size(); i++) {
                                // Log.e(TAG,stockNumberList.get(i).toString() + " V.S. " + stock.child("stockNumber").getValue().toString());
                                if (stockNumberList.get(i).toString().contains(stock.child("stockNumber").getValue().toString())) {
                                    //Log.e(TAG, "Update Date  " + stockNumberList.get(i).toString().substring(stockNumberList.get(i).toString().indexOf(":")+1,stockNumberList.get(i).toString().length()));
                                    ref.child("stocks").child(stock.getKey()).child("thisYear").setValue(stockNumberList.get(i).toString().substring(stockNumberList.get(i).toString().indexOf(":") + 1, stockNumberList.get(i).toString().length()));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getValue() {
        String s = stockDividend.get("1101").toString();
        Log.e(TAG, "Stock 1101 " + s);
    }


    public void getDividend() {
        Log.e(TAG, "getDividend start");
        String urlDevidend = "http://www.twse.com.tw/exchangeReport/BWIBBU_d?response=html&date=20180518&selectType=ALL";
        stockDividend = new HashMap<String, String>();

        try {
            Document doc = Jsoup.connect(urlDevidend).get();
            Element table = doc.select("tbody").first();
            Elements rows = table.select("tr");
            Element td_stock, td_dividend;
            for (int i = 0; i < rows.size(); i++) {
                td_stock = rows.get(i).child(0);
                td_dividend = rows.get(i).child(2);
                stockDividend.put(rows.get(i).child(0).text(), rows.get(i).child(2).text());
                Log.e(TAG, td_stock.text() + " : " + td_dividend.text());
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public Map<String, String> getChoma() {
        String url = urlChoMa;
        String[] temp;
        String date = "";
        Map<String, String> stockChoMa = new HashMap<String, String>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table").first();
            Elements rows = table.select("tr");
            for (int i = 0; i < rows.size(); i++) {

                temp = rows.get(i).text().split(" ");
                //Log.e(TAG,rows.get(i).text() + "");
                if (temp.length > 19) {
//                    Log.e(TAG, temp[0] + ":" + temp[7]  + ":"+ temp[8]+ ":"+ temp[9]
//                            + ":" + temp[10]  + ":"+ temp[11]+ ":"+ temp[16]
//                            + ":" + temp[17]  + ":"+ temp[18]+ ":"+ temp[19]);
                    stockChoMa.put(temp[0], temp[7] + ":" + temp[8] + ":" + temp[9]
                            + ":" + temp[10] + ":" + temp[11] + ":" + temp[16]
                            + ":" + temp[17] + ":" + temp[18] + ":" + temp[19]);
                }

                //stockNumberList.add(temp[1] + ":" + transferDate(temp[0]));
                //transferDate(temp[0]);
            }
            // Log.e(TAG, stock_name.size() + " V.S " + stock_date.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockChoMa;
    }

    public ArrayList<String> getDateTaiXiDay() {
        String url = "http://www.twse.com.tw/exchangeReport/TWT48U?response=html";
        String[] temp;
        String date = "";
        try {
            Document doc = Jsoup.connect(url).get();
            Log.e(TAG, doc.title());
            Element table = doc.select("table").first();
            Elements rows = table.select("tr");
            for (int i = 0; i < rows.size(); i++) {
                temp = rows.get(i).text().split(" ");
                if (i % 2 == 0)
                    Log.e(TAG, transferDate(temp[0]) + " :: " + temp[1] + ": " + temp[7]);
                stockNumberList.add(temp[1] + ":" + transferDate(temp[0]));
                transferDate(temp[0]);
            }
            // Log.e(TAG, stock_name.size() + " V.S " + stock_date.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockNumberList;
    }

    public String transferDate(String date) {
        String result = "";
        int y = date.indexOf("年");
        int m = date.indexOf("月");
        int d = date.indexOf("日");
        if (y == 3 && m == 6 && d == 9) {
            String year = date.substring(0, y);
            int a = Integer.parseInt(year) + 1911;
            String mouth = date.substring(y + 1, m);
            String day = date.substring(m + 1, d);
            result = a + mouth + day;
        }
        return result;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public ArrayList<String> getUrlInfo(String url, Context mContext) {
        ArrayList<String> temp = new ArrayList<>();
        try {

            //2019 01 03 if could not get real data from internet , could using local in asset
//            InputStream is=null;
//            is = mContext.getAssets().open("stockEPS.html");
//            Document doc = Jsoup.parse(is, "UTF-8", "");

            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table[id=example]").first();
            Elements rows = table.select("tr");
            Elements td;

            Log.e(TAG, "Start to run");
            for (int i = 0; i < rows.size(); i++) {
                td = rows.get(i).children();
                for (int j = 0; j < td.size(); j++) {
                    if (j % 22 == 0) {
                        if (isNumeric(td.get(j).text())) {
                            Log.e(TAG, td.text() + " :: " + td.size() + " :: " + rows.size());
                            temp.add(td.text());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
