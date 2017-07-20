package com.james.stockparser.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.james.stockparser.R;
import com.james.stockparser.StockInfoParser;

import java.util.ArrayList;

/**
 * Created by 101716 on 2017/7/20.
 */

public class upNewData extends AppCompatActivity {
    String TAG = upNewData.class.getSimpleName();
    StockInfoParser stockinfo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_main);
        stockinfo = new StockInfoParser();
        stockinfo.start("1110");
    }
}