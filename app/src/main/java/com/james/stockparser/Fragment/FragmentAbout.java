package com.james.stockparser.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.james.stockparser.R;

public class FragmentAbout extends AppCompatActivity {
    private static Toolbar mToolbar;
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    String TAG = FragmentAbout.class.getSimpleName();
    TextView tvContener;
    Button btn_evalution, btn_attention, btn_share, btn_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);
        btn_evalution = (Button)findViewById(R.id.btn_eva);
        btn_attention = (Button)findViewById(R.id.btn_attent);
        btn_share = (Button)findViewById(R.id.btn_share);
        btn_feedback = (Button)findViewById(R.id.btn_feedback);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvContener = (TextView)findViewById(R.id.tvContent);
        tvContener.setMovementMethod(new ScrollingMovementMethod());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvContener.setText(getString(R.string.stockFYI));
            }
        });




    }
}
