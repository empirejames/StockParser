package com.james.stockparser;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

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

public class StockInfoParser {
    String TAG = StockInfoParser.class.getSimpleName();
    String url = "http://goodinfo.tw/StockInfo/StockDividendScheduleList.asp?MARKET_CAT=%E5%85%A8%E9%83%A8&YEAR=%E5%8D%B3%E5%B0%87%E9%99%A4%E6%AC%8A%E6%81%AF&INDUSTRY_CAT=%E5%85%A8%E9%83%A8";
    private HandlerThread mThread;
    private Handler mThreadHandler;
    ArrayList stock_name = new ArrayList();
    ArrayList stock_date = new ArrayList();
    Iterator iterator, iterator1, iteratorTR;
    DatabaseReference ref;
    ArrayList<String> stockNumberList = new ArrayList<String>();

    public void start(final String stockNumber) {
        mThread = new HandlerThread("jsoup");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                updateTaiXiDay();
                getStockData();
                // getCompanyInfo(stockNumber);
            }
        });

    }


    private void getStockData() {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if (dsp.getKey().equals("stocks")) {
                        for (DataSnapshot stock : dsp.getChildren()) {
                            for(int i=0 ; i<stockNumberList.size();i++){
                               // Log.e(TAG,stockNumberList.get(i).toString() + " V.S. " + stock.child("stockNumber").getValue().toString());
                                if (stockNumberList.get(i).toString().contains(stock.child("stockNumber").getValue().toString())){
                                    //Log.e(TAG, "Update Date  " + stockNumberList.get(i).toString().substring(stockNumberList.get(i).toString().indexOf(":")+1,stockNumberList.get(i).toString().length()));
                                    ref.child("stocks").child(stock.getKey()).child("thisYear").setValue(stockNumberList.get(i).toString().substring(stockNumberList.get(i).toString().indexOf(":")+1,stockNumberList.get(i).toString().length()));
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


    public ArrayList<String> updateTaiXiDay() {
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
               // Log.e(TAG, i + " : " + transferDate(temp[0]) + " :: " + temp[1]);
                stockNumberList.add(temp[1]+":"+transferDate(temp[0]));
                //transferDate(temp[0]);
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


    public String[] getCompanyInfo(String number) {
        String te01, te02, te03;
        String[] companyInfo = null;
        Connection.Response response = null;
        try {
            while (response == null || response.statusCode() != 200) {
                response = Jsoup.connect(url).execute();
                //Thread.sleep(1000);
            }
            Document doc = response.parse();
            Elements table = doc.getElementsByTag("table");
            Elements title_td = doc.select("td");
            Log.e(TAG, table.size() + "");
            for (int i = 0; i < table.size(); i++) {
                //Elements title_select = title.get(i).select("tr").select("td");
                te01 = table.get(i).text();
                String stockNumber = table.get(i).select("td").get(0).text();
                if (stockNumber.equals(number)) {
                    Log.e(TAG, te01);
                }
                //te02=title_select.get(2).text();
                //te03=title_select.get(3).text();
                //Log.e(TAG, te01 +" : "+ te02 + " : "+te03);
            }
            Elements notice = doc.getElementsByTag("tbody");
            notice = notice.get(1).getElementsByTag("tr");
            for (Element e : notice) {
                String t = e.child(0).text();
                String time = e.child(1).text();
                //Log.e(TAG, "...." + t + time);
            }
            return companyInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

}
