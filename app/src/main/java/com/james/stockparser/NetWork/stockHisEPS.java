package com.james.stockparser.NetWork;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class stockHisEPS {

    String TAG = stockHisEPS.class.getSimpleName();
    ArrayList<String> historyEPS = new ArrayList<String>();
    DatabaseReference ref;
    String urlForEPS = "http://stock.wespai.com/p/7733";


    public void start(){
        new GetStart().execute();
    }


    public class GetStart extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            historyEPS = getUrlInfo(urlForEPS);
            updateHistoryData("eps");
            Log.e(TAG,"updateHistoryData :: EPS End");
            return null;
        }
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
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    private void updateHistoryData(final String stuff) {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (stuff.equals("eps")) {
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp.getKey().equals("history")) {
                            for (DataSnapshot stockNm : dsp.getChildren()) {
                                for (int i = 0; i < historyEPS.size(); i++) {

                                    int number = Integer.parseInt(historyEPS.get(i).split(" ")[0]);
                                    if(number > 6000){
                                        if (historyEPS.get(i).split(" ")[0].equals(stockNm.getKey().toString())) {
                                            Log.e(TAG, historyEPS.get(i).split(" ")[0] + " :: " + stockNm.getKey());
                                            for (int j = 0; j < historyEPS.get(i).split(" ").length; j++) {

                                                ref.child("history").child(historyEPS.get(i).split(" ")[0]).child(stuff).child(j + "").setValue(historyEPS.get(i).split(" ")[j]);
                                                Log.e(TAG, "Set EPS value : " + historyEPS.get(i).split(" ")[j]);

                                            }
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

}

