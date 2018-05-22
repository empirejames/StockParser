package com.james.stockparser;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.james.stockparser.NetWork.getRemoteConfig;
import com.james.stockparser.dataBase.TinyDB;

import org.w3c.dom.Text;

public class InstructionActivity extends AppCompatActivity {
    String TAG = InstructionActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView rt_dividend_value, rt_PERatio_value, taishi_count_value, taishi_avg_day_value, suggest_value, suprise, suprise_value;
    TinyDB tinydb;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explan);
        getRemoteConfig grc = new getRemoteConfig(InstructionActivity.this);
        grc.getRemotePara();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right_1, R.anim.slide_in_right_2);
            }
        });
        tinydb = new TinyDB(InstructionActivity.this);
        initView();
        setViewValue();
    }
    public void setViewValue(){
        if(!tinydb.getString("rt_dividend_value").equals("")){
            rt_dividend_value.setText(tinydb.getString("rt_dividend_value"));
            rt_PERatio_value.setText(tinydb.getString("rt_PERatio_value"));
            taishi_count_value.setText(tinydb.getString("taishi_count_value"));
            taishi_avg_day_value.setText(tinydb.getString("taishi_avg_day_value"));
            suggest_value.setText(tinydb.getString("suggest_value"));
            suprise.setText(tinydb.getString("suprise"));
            suprise_value.setText(tinydb.getString("suprise_value"));
        }
    }

    public void initView() {
        rt_dividend_value = (TextView) findViewById(R.id.rt_dividend_value);
        rt_PERatio_value = (TextView) findViewById(R.id.rt_PERatio_value);
        taishi_count_value = (TextView) findViewById(R.id.taishi_count_value);
        taishi_avg_day_value = (TextView) findViewById(R.id.taishi_avg_day_value);
        suggest_value = (TextView) findViewById(R.id.suggest_value);
        suprise = (TextView) findViewById(R.id.suprise);
        suprise_value = (TextView) findViewById(R.id.suprise_value);

    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_search:

                    break;
                case R.id.action_filter:
                    break;
            }
            return true;
        }
    };
}

