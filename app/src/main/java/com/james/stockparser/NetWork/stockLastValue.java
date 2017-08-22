package com.james.stockparser.NetWork;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.james.stockparser.Fragment.FragmentAbout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by 101716 on 2017/8/22.
 */

public class stockLastValue {
    String TAG = stockLastValue.class.getSimpleName();
    String stockNm;
    private String value;
    Context context;

    public stockLastValue(String stockNm, Context context) {
        this.stockNm = stockNm;
        this.context = context;
    }

    public String getData(){
        final String url = "https://www.google.com/finance/getprices?q=" + stockNm + "&x=TPE&i=7200&p=1d";
        String[] temp;
        try {
            String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
            temp = json.split(",");
            value = temp[temp.length - 4]; // close value
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

//    protected void onPostExecute(String result) {
//        Log.e(TAG, "++ : " + value);
//        FragmentAbout mainactivity = (FragmentAbout) context;
//        mainactivity.returnString = result;
//        super.onPostExecute(result);
//    }
}
