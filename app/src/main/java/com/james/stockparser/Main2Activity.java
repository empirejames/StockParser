package com.james.stockparser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.james.stockparser.Beans.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Bean> list  = new ArrayList<Bean>();
        list.add(new Bean("c", (double)3.4));
        list.add(new Bean("b", (double)3.3));
        list.add(new Bean("a", (double)5.2));
        Collections.sort(list, new Comparator<Bean>() {
            @Override
            public int compare(Bean a, Bean b) {
                return b.getOrder().compareTo(a.getOrder());
            }
        });
        for (Bean bean : list) {
            Log.i("dt", bean.getData());
        }
    }

}
