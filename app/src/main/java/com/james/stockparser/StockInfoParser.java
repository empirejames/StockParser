package com.james.stockparser;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class StockInfoParser {
    String TAG = StockInfoParser.class.getSimpleName();
    String url = "http://www.cnyes.com/twstock/financial4.aspx";
    private HandlerThread mThread;
    private Handler mThreadHandler;


    public void start(final String stockNumber) {
        mThread = new HandlerThread("jsoup");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                getCompanyInfo(stockNumber);
            }
        });

    }

    public String[] getCompanyInfo(String number) {
        String te01,te02,te03;
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
            Log.e(TAG, table.size() + "" );
            for (int i = 0; i < table.size(); i++) {
                //Elements title_select = title.get(i).select("tr").select("td");
                te01=table.get(i).text();
                String stockNumber = table.get(i).select("td").get(0).text();
                if (stockNumber.equals(number)){
                    Log.e(TAG, te01 );
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
                Log.e(TAG, "...." + t + time);
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
