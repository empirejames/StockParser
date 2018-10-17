package com.james.stockparser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BaseActivity extends AppCompatActivity {
    String TAG = BaseActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new java.util.Date());
        return date;
    }
    public String getYearORDate(String item){

        String result = "";
        Calendar c = Calendar.getInstance();
        String year = c.get(Calendar.YEAR) + "";
        String month = c.get(Calendar.MONTH)+1 + "";
        String today = c.get(Calendar.DAY_OF_MONTH) + "";
        String yestoday = c.get(Calendar.DAY_OF_MONTH)-1 + "";

        if(item.equals("year")){
            result = year;
        }else if(item.equals("yesterday")){
            result = month + " / " + yestoday;
        }else{
            result = year + month + today;
        }
        return result;
    }

}
