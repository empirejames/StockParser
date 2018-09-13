package com.james.stockparser.NetWork;

import android.os.AsyncTask;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by 101716 on 2018/9/13.
 */

public class VersionChecker  extends AsyncTask<String, String, String> {
    String newVersion;
    String pkgName;
    @Override
    protected String doInBackground(String... strings) {
        try {
            pkgName = strings[0];
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + pkgName + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;
    }
}
