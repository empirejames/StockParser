package com.james.stockparser.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.james.stockparser.R;

import java.util.ArrayList;

public class FragmentMain extends AppCompatActivity {
    private static Toolbar mToolbar;
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    String TAG = FragmentMain.class.getSimpleName();
    ArrayList<String> hisEPS = new ArrayList<>();
    ArrayList<String> hisGuLi = new ArrayList<>();
    ArrayList<String> hisGuShi = new ArrayList<>();
    ArrayList<String> hisPresent = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("stockName")!= null) {
            String name  = bundle.getString("stockName");
            String number  = bundle.getString("stockNumber");
            hisEPS = bundle.getStringArrayList("stockEps");
            Log.e(TAG,"Fragment EPS: " + hisEPS);
            hisGuLi = bundle.getStringArrayList("stockGuLi");
            hisGuShi = bundle.getStringArrayList("stockGuShi");
            hisPresent = bundle.getStringArrayList("stockPresent");
            getSupportActionBar().setTitle(number + " " + name );
            Log.e(TAG,number + " " + name );
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager

        //Implementing tab selected listener over tablayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                DummyFragment DF = new DummyFragment();
                switch (tab.getPosition()) {
                    case 0:
                        Log.e(TAG, "TAG1");
                        break;
                    case 1:
                        Log.e(TAG, "TAG2");
                        break;
                    case 2:
                        Log.e(TAG, "TAG3");
                        break;
                    case 3:
                        Log.e(TAG, "TAG4");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment(2017,"eps",hisEPS,hisGuLi,hisGuShi,hisPresent), "歷年EPS");
        adapter.addFrag(new DummyFragment(2017,"guli",hisEPS,hisGuLi,hisGuShi,hisPresent), "歷年配股息發放");
        adapter.addFrag(new DummyFragment(2017,"gushi",hisEPS,hisGuLi,hisGuShi,hisPresent), "歷年配股息時間");
        adapter.addFrag(new DummyFragment(2017,"present",hisEPS,hisGuLi,hisGuShi,hisPresent), "歷年股東會禮品");
        viewPager.setAdapter(adapter);
    }
}
