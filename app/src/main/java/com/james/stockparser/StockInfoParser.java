package com.james.stockparser;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockInfoParser {
    String TAG = StockInfoParser.class.getSimpleName();
    String url = "http://goodinfo.tw/StockInfo/StockDividendScheduleList.asp?MARKET_CAT=%E5%85%A8%E9%83%A8&YEAR=%E5%8D%B3%E5%B0%87%E9%99%A4%E6%AC%8A%E6%81%AF&INDUSTRY_CAT=%E5%85%A8%E9%83%A8";
    String urlForGuHi = "http://stock.wespai.com/p/5625";
    String urlForEPS = "http://stock.wespai.com/p/7733";
    String urlForPresent = "http://stock.wespai.com/stock107";
    String urlForGuli = "http://stock.wespai.com/tenrate#";

    private HandlerThread mThread;
    private Handler mThreadHandler;
    DatabaseReference ref;
    ArrayList<String> stockNumberList = new ArrayList<String>();
    ArrayList<String> historyGuHi = new ArrayList<String>();
    ArrayList<String> historyEPS = new ArrayList<String>();
    ArrayList<String> historyPresent = new ArrayList<String>();
    ArrayList<String> historyGuli = new ArrayList<String>();

    public void start() {
        mThread = new HandlerThread("jsoup");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
//                historyGuHi = getUrlInfo(urlForGuHi);
//                updateHistoryData("guShi");
//                Log.e(TAG,"updateHistoryData");
//                historyEPS = getUrlInfo(urlForEPS);
//                updateHistoryData("eps");
//                Log.e(TAG,"updateHistoryData :: EPS");
//                historyPresent = getUrlInfo(urlForPresent);
//                updateHistoryData("present");
//                historyGuli = getUrlInfo(urlForGuli);
//                updateHistoryData("guli");
                  getDateTaiXiDay();
                  updateStockData();
            }
        });

    }

    private void updateHistoryData(final String stuff) {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (stuff.equals("guShi")){
                    for (int i = 0; i < historyGuHi.size(); i++) {
                        for (int j = 0; j < historyGuHi.get(i).split(" ").length; j++) {
                            ref.child("history").child(historyGuHi.get(i).split(" ")[0]).child(stuff).child(j+"").setValue(historyGuHi.get(i).split(" ")[j]);
                            //ref.child("history").child(i+"").child(stuff).child(j + "").setValue(historyGuHi.get(0).split(" ")[j]);
                        }
                    }
                }else if(stuff.equals("eps")){
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp.getKey().equals("history")){
                            for (DataSnapshot stockNm : dsp.getChildren()) {
                                for(int i =0; i<historyEPS.size();i++){
                                    if (historyEPS.get(i).split(" ")[0].equals(stockNm.getKey().toString())){
                                        //Log.e(TAG, historyEPS.get(i).split(" ")[0] +" :: " + stockNm.getKey());
                                        for(int j=0;j<historyEPS.get(i).split(" ").length;j++){
                                            if(historyEPS.get(i).split(" ")[j].equals("")) {
                                                ref.child("history").child(historyEPS.get(i).split(" ")[0]).child(stuff).child(j + "").setValue(historyEPS.get(i).split(" ")[j]);
                                            }
                                        }
                                    }
                                }


                            }
                        }
                    }
                }else if (stuff.equals("present")){
                    DatabaseReference fbDb = null;
                    if (fbDb == null) {
                        fbDb = FirebaseDatabase.getInstance().getReference();
                    }
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {

                        if (dsp.getKey().equals("history")){
                            for (DataSnapshot stockNm : dsp.getChildren()) {
                                for(int i =0; i<historyPresent.size();i++){
                                    if (historyPresent.get(i).split(" ")[1].equals(stockNm.getKey().toString())){
                                        UpdateNewPresent(historyPresent.get(i).split(" ")[1],historyPresent.get(i).split(" ")[5], true);
                                    }else{
                                        //UpdateNewPresent(historyPresent.get(i).split(" ")[1],historyPresent.get(i).split(" ")[5],false);
                                    }
                                }
                            }
                        }
                    }
                }else if (stuff.equals("guli")){
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp.getKey().equals("history")){
                            for (DataSnapshot stockNm : dsp.getChildren()) {
                                for(int i =0; i<historyGuli.size();i++){
                                    if (historyGuli.get(i).split(" ")[0].equals(stockNm.getKey().toString())){
                                        for(int j=0;j<historyGuli.get(i).split(" ").length;j++){
                                            ref.child("history").child(historyGuli.get(i).split(" ")[0]).child(stuff).child(j+"").setValue(historyGuli.get(i).split(" ")[j]);
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
    private void UpdateNewPresent(final String stockNum, final String present,final boolean isHavePresent) {

        ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = ref.child("history").child(stockNum).child("present");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String size = dataSnapshot.getChildrenCount()+"";
                Log.e(TAG, "UpdateNewPresent:: " + stockNum + " : " + present +" : "+ size);
                if(isHavePresent){
                    ref.child("history").child(stockNum).child("present").child(size).setValue(present);
                }else{
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

    public ArrayList<String> getUrlInfo(String url) {
        ArrayList<String> temp = new ArrayList<>();
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
                            Log.e(TAG, td.text() + " :: " + td.size());
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
