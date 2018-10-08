package com.james.stockparser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.james.stockparser.dataBase.TinyDB;

import java.util.ArrayList;
import java.util.List;

public class ViewPageActivity extends AppCompatActivity {
    private ViewPager mViewPager;//viewpager
    private List<View> viewList;//把需要滑动的页卡添加到这个list中
    private RadioGroup mRadiogroup ;
    private Button btn_enter;
    private CheckBox cb_noShow;
    String TAG = ViewPageActivity.class.getSimpleName();
    TinyDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        final LayoutInflater mInflater = getLayoutInflater().from(this);

        View v1 = mInflater.inflate(R.layout.intro_layout_1, null);
        View v2 = mInflater.inflate(R.layout.intro_layout_2, null);
        View v3 = mInflater.inflate(R.layout.intro_layout_3, null);
        View v4 = mInflater.inflate(R.layout.intro_layout_4, null);
        cb_noShow = (CheckBox)v3.findViewById(R.id.cb_noShow);
        btn_enter = (Button)v3.findViewById(R.id.btn_iKnow);
        db = new TinyDB(this);

        if(db.getBoolean("noShow")){
            cb_noShow.setChecked(true);
            gotoIntent();
        }else{
            cb_noShow.setChecked(false);
        }

        cb_noShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG,"cb_noShow.isChecked() :" + isChecked);
                if(isChecked){
                    db.putBoolean("noShow", true);
                }else{
                    db.putBoolean("noShow", false);
                }
            }
        });


        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"View Page 3");
                gotoIntent();
            }
        });


        viewList = new ArrayList<View>();
        viewList.add(v1);
        viewList.add(v2);
        viewList.add(v3);
        //viewList.add(v4);

        mRadiogroup = (RadioGroup)findViewById(R.id.radiogroup);
        mViewPager.setAdapter(new MyViewPagerAdapter(viewList));
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mRadiogroup.check(R.id.radioButton);
                        break;
                    case 1:
                        mRadiogroup.check(R.id.radioButton2);
                        break;
                    case 2:
                        mRadiogroup.check(R.id.radioButton3);
                        break;
                    case 3:
                        //mRadiogroup.check(R.id.radioButton4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void gotoIntent(){
        Intent intent = new Intent(ViewPageActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
