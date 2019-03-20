package com.james.stockparser;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.james.stockparser.Fragment.FragmentAbout;
import com.james.stockparser.Fragment.FragmentMain;
import com.james.stockparser.NetWork.getRemoteConfig;
import com.james.stockparser.NetWork.stockDividend;
import com.james.stockparser.NetWork.stockHotCount;
import com.james.stockparser.NetWork.stockLastValue;
import com.james.stockparser.NetWork.stockPERatio;
import com.james.stockparser.NetWork.stockPayGushi;
import com.james.stockparser.Unit.User;
import com.james.stockparser.dataBase.TinyDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MainActivity extends BaseActivity {
    String TAG = MainActivity.class.getSimpleName();
    private ListView listV;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public MyAdapter adapter;
    public String returnString;
    static boolean callAlready = false;


    Map<String, String> stockMap = new HashMap<String, String>();
    Map<String, String> stockChoMa = new HashMap<String, String>();
    Map<String, String> stockPEMap = new HashMap<String, String>();
    Map<String, String> hotClick = new HashMap<String, String>();
    Map<String, String> getPaygushi = new HashMap<String, String>();
    ArrayList<StockItem> myDataset = new ArrayList<StockItem>();
    ArrayList<StockEPS> myEPS = new ArrayList<StockEPS>();
    ArrayList<HistoryItem> myHisEPS = new ArrayList<HistoryItem>();
    ArrayList<String> stockNumbers = new ArrayList<String>();
    ArrayList<StockItem> myDataFilter = new ArrayList<StockItem>();
    ArrayList<StockItem> nearlyStock = new ArrayList<StockItem>();
    ArrayList<StockItem> myFavorite = new ArrayList<StockItem>();
    ArrayList<StockItem> myHistory = new ArrayList<StockItem>();
    ArrayList<StockItem> myFilterResult = new ArrayList<StockItem>();


    ArrayList a = new ArrayList();
    ArrayList<String> addT = new ArrayList<String>();

    ArrayList<String> hstEPS = new ArrayList<String>();
    ArrayList<String> hstGuLi = new ArrayList<String>();
    ArrayList<String> hstGuShi = new ArrayList<String>();
    ArrayList<String> hstPresent = new ArrayList<String>();

    ArrayList<String> favList = new ArrayList<String>();
    ArrayAdapter<String> countNumAdapter;
    ArrayAdapter<String> avgNumAdapter;
    ArrayAdapter<String> epsNumberAdapter;
    String[] countList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
    String[] avgList = {"10", "20", "30", "40"};
    String[] avgEPSList = {"0.0", "1.0", "2.0", "4.0", "6.0", "8.0", "10.0", "12.0", "14.0", "16.0", "18.0", "20.0", "22.0", "24.0"};
    private String[] nextLine;
    private Toolbar mToolbar;
    SearchView searchView;
    String dividend, peRatio, hotclickCount, payGu, payShi;
    String releaseCount, stockName, stockNumber, thisYear, tianxiCount, tianxiDay, tianxiPercent;
    String hisName, hisNumber, hisProduct, hisEPS;
    String stockInfoName, stockInfoNumber, stockInfoEPS;
    DatabaseReference ref;
    ProgressDialog mProgressDialog;
    Bundle bundle;
    TinyDB tinydb;
    SharedPreferences prefs;
    RatingBar ratingbarStart;
    Button btnDiglog;
    Spinner countNumber, avgNumber, epsNumber;
    String userId;
    boolean isVistor;
    boolean mDisPlayFav = false;
    boolean selectAll = false;
    boolean containStock = false;
    int PageNumber = 0;
    private Menu menuItem;
    Boolean countHigh = false;
    Boolean avgLow = false;
    Boolean avgEPS = false;
    float percentTaixi;
    String countSelectNumber, avgSelectNumber, epsSelectNumber;
    boolean scrollFlag;
    BottomNavigationView navigation;
    Animation mShowAction, mShowToolbar;
    Animation mHiddenAction, mHiddenToolbar;
    Animation mShowFabBtn, mHiddenFabBtn;
    FloatingActionButton mFab;
    String userStatus = "home";
    AdView mAdView;
    int CountNum = 0;
    String alreadyGj = "false";
    String countClick;
    boolean alreadyGood;
    private InterstitialAd interstitial;
    private AdRequest adRequestAA;
    //IabHelper mHelper;
    private int countAD = 4;
    private ImageView right_select;

    MenuInflater inflater;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menuItem = menu;

        SearchManager searchManager = (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("股票代號/名稱");
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorGray));

        searchView.setOnQueryTextFocusChangeListener(new SearchView.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                Log.e(TAG, "adapter.getFavorite()" + recycleViewAdapter.getFavorite());
//                if (recycleViewAdapter.getFavorite().size() > 0) {
//                    saveUserData(compareNewData(favList, recycleViewAdapter.getFavorite(), true));
//                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myDataFilter.clear();
                if (!s.equals("")) {
                    userStatus = "";
                    //Log.e(TAG, "onQueryTextChange : " + s);
                    myDataFilter = filterResult(s, true, 0);
                    myFilterResult = filterResult(s, true, 0);
                    listAdaperr(myDataFilter, isVistor, selectAll);
                } else {
                    listAdaperr(myDataset, isVistor, selectAll);
                }
                return false;
            }
        });
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!callAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            callAlready = true;
        }
        mShowAction = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        mShowToolbar = AnimationUtils.loadAnimation(this, R.anim.alpha_in_toolbar);
        mShowFabBtn = AnimationUtils.loadAnimation(this, R.anim.alpha_fab_in);
        mHiddenAction = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
        mHiddenToolbar = AnimationUtils.loadAnimation(this, R.anim.alpha_out_toolbar);
        mHiddenFabBtn = AnimationUtils.loadAnimation(this, R.anim.alpha_fab_out);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F618803C89E1614E3394A55D5E7A756B").build(); //Nexus 5
        mAdView.loadAd(adRequest);
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-1659435325076970/8847307592");
        adRequestAA = new AdRequest.Builder().addTestDevice("F618803C89E1614E3394A55D5E7A756B").build(); //Nexus 5
        interstitial.loadAd(adRequestAA);
        displayInterstitial();
        tinydb = new TinyDB(MainActivity.this);
        alreadyGj = tinydb.getString("GJ");


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        bundle = getIntent().getExtras();
        isVistor = isVistor();
        Log.e(TAG, "isVistor : " + isVistor);
        initFab();
        if (!isVistor) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e("FCN TOKEN GET", "Refreshed token: " + refreshedToken);
            writeNewUserIfNeeded();
        } else {
            alertDialog("目前會員數約8000人，為持續服務優質會員，一萬人後將關閉訪客註冊及登入，趕快搶先註冊會員唷 !");
        }
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();
                Log.e(TAG, "lastVisibleItem :" + lastVisibleItem);
                if (lastVisibleItem == (totalItemCount - 1)) {
                    navigation.setVisibility(View.VISIBLE);
                    navigation.startAnimation(mShowAction);
                }

                if (dy > 0 && navigation.isShown()) {
                    navigation.setVisibility(View.GONE);
                    navigation.startAnimation(mHiddenAction);
                    mFab.setVisibility(View.GONE);
                    mFab.startAnimation(mHiddenFabBtn);
                    mAdView.setVisibility(View.GONE);
                } else if (dy < 0) {
                    navigation.setVisibility(View.VISIBLE);
                    navigation.startAnimation(mShowAction);
                    mFab.setVisibility(View.VISIBLE);
                    mFab.startAnimation(mShowFabBtn);
                    mAdView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        BottomNavigationViewHelper.disableShiftMode(navigation);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        new GetData().execute();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("要離開「權息大師」嗎?")
                .setCancelable(false)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();

                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //readFav();
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if (!isVistor() && adapter.getToDelete() != null) {
//            saveUserData(compareNewData(favList, adapter.getToDelete(), false));
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            interstitial.loadAd(adRequestAA);
        }
    }

    private void initFab() {
        mFab = findViewById(R.id.fab);


        if (tinydb.getString("show_floating_button").equals("false")) {
            //fab.setVisibility(View.GONE);
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InstructionActivity.class);
                startActivity(i);
                //Toast.makeText(MainActivity.this, "FAB Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isVistor() {
        if (bundle != null) {
            if (bundle.getString("isVistor") != null) {
                String vistor = bundle.getString("isVistor");
                if (vistor.toString().equals("Y")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }


    public void listAdaperr(ArrayList<StockItem> item, boolean isVistor, boolean selectAll) {
        mAdapter = new RecycleViewAdapter(MainActivity.this, item, stockChoMa, favList, userId, isVistor, selectAll, userStatus);
        RecyclerView.ItemDecoration itemDecoration = mRecyclerView.getItemDecorationAt(0);
        if (itemDecoration == null) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutAnimation(getListAnim());
        mAdapter.notifyDataSetChanged();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_nearly:
                    PageNumber = 2;
                    checkUsing();
                    invalidateOptionsMenu(); //update toolbar
                    userStatus = "nearly";
                    nearlyStock = nearly_Taixi();
                    countAD += 1;
                    if (countAD == 5) {
                        displayInterstitial();
                        countAD = 0;
                    }
                    listAdaperr(nearlyStock, isVistor, selectAll);
                    return true;
                case R.id.navigation_home:
                    PageNumber = 0;
                    invalidateOptionsMenu(); //update toolbar
                    userStatus = "home";
                    listAdaperr(myDataset, isVistor, selectAll);
                    return true;
                case R.id.navigation_dashboard:
                    PageNumber = 1;
                    if (isVistor()) {
                        alertDialog("訪客身分無法使用我的最愛功能");
                    } else {
                        invalidateOptionsMenu();//update toolbar
                        userStatus = "favorite";
                        getFav();
                        //myFavorite = myFavovResult(compareNewData(favList, myFavorite, true)); //summary main item
                    }
                    return true;
                case R.id.navigation_history:
                    PageNumber = 2;
                    invalidateOptionsMenu();//update toolbar
                    userStatus = "history";
                    listAdaperr(myHistory, isVistor, true);
                    return true;
                case R.id.navigation_notifications:
                    Intent i = new Intent(MainActivity.this, FragmentAbout.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }

    };


    private void getFav() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = ref.child("users").child(userId).child("favorite");
        addT.clear();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Log.e(TAG, dsp.getValue() + "");
                    addT.add(dsp.getValue() + "");
                }
                myFavorite = myFavovResult(addT);
                listAdaperr(myFavorite, isVistor, true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private LayoutAnimationController getListAnim() {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        return controller;
    }

    public void showDialog() {
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(this);
        FrameLayout frameLayout = new FrameLayout(optionDialog.getContext());
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        optionDialog.setView(frameLayout);
        final AlertDialog alert = optionDialog.create();
        View myView = inflater.inflate(R.layout.my_dialog, frameLayout);
        btnDiglog = (Button) myView.findViewById(R.id.dialog_btn);
        ratingbarStart = (RatingBar) myView.findViewById(R.id.ratingBarSelect);
        ratingbarStart.setRating(4);
        ratingbarStart.setEnabled(true);
        ratingbarStart.setClickable(true);
        countNumber = (Spinner) myView.findViewById(R.id.spinner_countNumber);
        avgNumber = (Spinner) myView.findViewById(R.id.spinner_avgNumber);
        epsNumber = (Spinner) myView.findViewById(R.id.spinner_avgEPS);
        countNumAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, countList);
        avgNumAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, avgList);
        epsNumberAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, avgEPSList);
        countNumber.setAdapter(countNumAdapter);
        avgNumber.setAdapter(avgNumAdapter);
        epsNumber.setAdapter(epsNumberAdapter);

        countNumber.setSelection(10); //default
        avgNumber.setSelection(2); //default
        epsNumber.setSelection(3); //default

        countNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countSelectNumber = countList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        avgNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                avgSelectNumber = avgList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        epsNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                epsSelectNumber = avgEPSList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ratingbarStart.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                percentTaixi = rating / 5;
            }
        });
        SwitchCompat swCount = (SwitchCompat) myView.findViewById(R.id.switchCountBtn);
        SwitchCompat swAvgDay = (SwitchCompat) myView.findViewById(R.id.switchAvgDayBtn);
        SwitchCompat swAvgEPS = (SwitchCompat) myView.findViewById(R.id.advanceSwitch);
        swAvgEPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    avgEPS = true;
                } else {
                    avgEPS = false;
                }
            }
        });
        swCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    countHigh = true;
                } else {
                    countHigh = false;
                }
            }
        });
        swAvgDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    avgLow = true;
                } else {
                    avgLow = false;
                }
            }
        });
        btnDiglog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userStatus = "filting";
                myDataFilter = filterResult("null", true, percentTaixi);
                alert.dismiss();
            }
        });
        alert.show();
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    //navigation.setVisibility(View.GONE);
                    break;

                case R.id.action_filter:
                    showDialog();
                    break;
            }

            if (!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    public ArrayList<String> compareNewData(ArrayList oldData, ArrayList newData, boolean isCombine) {
        ArrayList<String> listWithoutDuplicateElements = new ArrayList<String>();
        if (isCombine) {
            oldData.addAll(newData);
            HashSet<String> set = new HashSet<String>(oldData);
            listWithoutDuplicateElements = new ArrayList<String>(set);
        } else {
            oldData.removeAll(newData);
            listWithoutDuplicateElements.addAll(oldData);
        }
        return listWithoutDuplicateElements;
    }

    public void writeFav() {
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(adapter.getFavorite());
        editor.putStringSet("myFav", set);
        Log.e(TAG, editor.putStringSet("myFavSet", set) + ":: SAVE");
    }

    public void readFav() {
        SharedPreferences spref = getPreferences(MODE_PRIVATE);
        spref.getStringSet("myFav", null);
        Log.e(TAG, spref.getStringSet("myFavSet", null) + ":: LOAD");
    }

    public ArrayList<StockItem> nearly_Taixi() {
        ArrayList<StockItem> item = new ArrayList<StockItem>();
        item.clear();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < myDataset.size(); i++) {
            //long day=(beginDate.getTime()-endDate.getTime())/(24*60*60*1000);
            //Log.e(TAG,"day..." + day);
            try {
                if (!myDataset.get(i).getThisYear().toString().equals("")) {
                    //Log.e(TAG, "today" + getDate() + " - stockDay : " + myDataset.get(i).getThisYear().toString());
                    Date beginDate = format.parse(getYearORDate(""));
                    Date endDate = format.parse(myDataset.get(i).getThisYear().toString());
                    long day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
                    //Log.e(TAG, "day: " + day);
                    if (day > 0 && day <= 15) {
                        //Log.e(TAG, "today" + getDate() + " - stockDay : " + myDataset.get(i).getThisYear().toString());
                        addingArrList(item, i);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    public ArrayList<StockItem> myFavovResult(ArrayList list) {
        ArrayList<StockItem> item = new ArrayList<StockItem>();
        item.clear();
        for (int i = 0; i < myDataset.size(); i++) {
            if (list.contains(myDataset.get(i).getStockNumber())) {
                addingArrList(item, i);
            }
        }
        return item;
    }

    public ArrayList<StockItem> filterResult(String input, boolean hasPattern, float taixiPercent) {
        ArrayList<StockItem> item = new ArrayList<StockItem>();
        item.clear();
        float percent = 0;
        if (hasPattern && !input.equals("null")) {
            for (int i = 0; i < myDataset.size(); i++) {
                if (myDataset.get(i).getStockNumber().contains(input) || myDataset.get(i).getStockName().contains(input)) {
                    addingArrList(item, i);
                }
            }
        } else {
            float taixiAvgdayTOint = Float.parseFloat("0.8");
            float count;
            float avgdayCount;
            int avgthread;
            int countthread;
            float epsthread;
            float hisOfEPS;
            String sotckNumber;
            if (taixiPercent > 0) {
                taixiAvgdayTOint = taixiPercent;
            }
            for (int i = 0; i < myDataset.size(); i++) {
                sotckNumber = myDataset.get(i).getStockNumber();
                //hisOfEPS = Float.valueOf(searchForHisEPS(sotckNumber));
                Log.e(TAG, "search " + searchForHisEPS(sotckNumber));
                hisOfEPS = Float.parseFloat(searchForHisEPS(sotckNumber));
                percent = Float.parseFloat(myDataset.get(i).getTianxiPercent());
                count = Float.parseFloat(myDataset.get(i).getTianxiCount());
                avgdayCount = Float.parseFloat(myDataset.get(i).getTianxiDay());
                countthread = Integer.parseInt(countSelectNumber);
                avgthread = Integer.parseInt(avgSelectNumber);
                epsthread = Float.parseFloat(epsSelectNumber);

                if (countHigh) {
                    if (avgLow) {
                        if (avgEPS) {
                            //Log.e(TAG,"true | true | true");
                            if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && count > countthread && avgdayCount < avgthread && hisOfEPS < epsthread + 0.5 && hisOfEPS > epsthread + 0.5) {
                                addingArrList(item, i);
                            }
                        } else {
                            //Log.e(TAG,"true | true | false");
                            if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && count > countthread && avgdayCount < avgthread) {
                                addingArrList(item, i);
                            }
                        }
                    } else {
                        if (avgEPS) {
                            //Log.e(TAG,"true | false | true");
                            if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && count > countthread && hisOfEPS < epsthread + 0.5 && hisOfEPS > epsthread + 0.5) {
                                addingArrList(item, i);
                            }
                        } else {
                            //Log.e(TAG,"true | false | false");
                            if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && count > countthread) {
                                addingArrList(item, i);
                            }
                        }
                    }
                } else {
                    if (avgLow) {
                        if (avgEPS) {
                            //Log.e(TAG,"false | true | true");
                            if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && avgdayCount < avgthread && hisOfEPS < epsthread + 0.5 && hisOfEPS > epsthread + 0.5) {
                                addingArrList(item, i);
                            }
                        } else {
                            //Log.e(TAG,"false | true | false");
                            if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && avgdayCount < avgthread) {
                                addingArrList(item, i);
                            }
                        }

                    } else {
                        if (avgEPS) {
                            //Log.e(TAG,"false | false | true");
                            if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && hisOfEPS > epsthread) {
                                Log.e(TAG, hisOfEPS + " >" + epsthread);
                                addingArrList(item, i);
                            }
                            //Log.e(TAG, "EEEEE .. " + sotckNumber);
                        } else {
                            //Log.e(TAG,"false | false | false");
                            if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1) {
                                addingArrList(item, i);
                            }
                        }

                    }
                }
            }
            listAdaperr(item, isVistor, selectAll);
            avgLow = false;
            countHigh = false;
            avgEPS = false;
            if (item.size() > 0) {
                Toast.makeText(getApplicationContext(), "搜尋到 : " + item.size() + "筆", Toast.LENGTH_LONG).show();
                checkUsing();
            } else {
                Toast.makeText(getApplicationContext(), "搜索不到資料! 請更改一下篩選條件", Toast.LENGTH_LONG).show();
                checkUsing();
            }

        }
        return item;
    }

    public String searchForHisEPS(String stockNm) {
        String stEPS = "";
        for (int i = 0; i < myHisEPS.size(); i++) {
            if (myHisEPS.get(i).getHisStockNumber().equals(stockNm)) {
                if (isNumeric(myHisEPS.get(i).getHisEPS().toString())) {
                    stEPS = myHisEPS.get(i).getHisEPS();
                    return stEPS;
                } else {
                    stEPS = "0.0";
                }
            } else {
                stEPS = "0.0";
            }
        }
        return stEPS;
    }

    public ArrayList<StockItem> addingArrList(ArrayList<StockItem> item, int i) {
        item.add(new StockItem(
                myDataset.get(i).getPayGu(),
                myDataset.get(i).getPayShi(),
                myDataset.get(i).getHotclickCount(),
                myDataset.get(i).getPeRatio(),
                myDataset.get(i).getNowDividend(),
                myDataset.get(i).getStockNumber(),
                myDataset.get(i).getStockName(),
                myDataset.get(i).getTianxiCount(),
                myDataset.get(i).getReleaseCount(),
                myDataset.get(i).getTianxiPercent(),
                myDataset.get(i).getTianxiDay(),
                myDataset.get(i).getThisYear())
        );
        return item;
    }

    public ArrayList<StockItem> addingSeearch(ArrayList<StockItem> item, int i) {
        item.add(new StockItem(
                myDataset.get(i).getPayGu(),
                myDataset.get(i).getPayShi(),
                myDataset.get(i).getHotclickCount(),
                myDataset.get(i).getPeRatio(),
                myDataFilter.get(i).getNowDividend(),
                myDataFilter.get(i).getStockNumber(),
                myDataFilter.get(i).getStockName(),
                myDataFilter.get(i).getTianxiCount(),
                myDataFilter.get(i).getReleaseCount(),
                myDataFilter.get(i).getTianxiPercent(),
                myDataFilter.get(i).getTianxiDay(),
                myDataFilter.get(i).getThisYear())
        );
        return item;
    }

    public ArrayList<StockItem> addingNear(ArrayList<StockItem> item, int i) {
        item.add(new StockItem(
                nearlyStock.get(i).getPayGu(),
                nearlyStock.get(i).getPayShi(),
                nearlyStock.get(i).getHotclickCount(),
                nearlyStock.get(i).getPeRatio(),
                nearlyStock.get(i).getNowDividend(),
                nearlyStock.get(i).getStockNumber(),
                nearlyStock.get(i).getStockName(),
                nearlyStock.get(i).getTianxiCount(),
                nearlyStock.get(i).getReleaseCount(),
                nearlyStock.get(i).getTianxiPercent(),
                nearlyStock.get(i).getTianxiDay(),
                nearlyStock.get(i).getThisYear())
        );
        return item;
    }

    public boolean isNumeric(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String getYesterDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        String date = sdf.format(c.getTime());
        return date;
    }


    private void saveUserData(final ArrayList favorite) {
        Log.e(TAG, "saveUserData:: " + favorite);
        ref = FirebaseDatabase.getInstance().getReference();
        userId = bundle.getString("uid");
        final DatabaseReference usersRef = ref.child("users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && favorite.size() != 0) {
                    Log.e(TAG, "dataSnapshot.exists() && favorite.size() != 0");
                    usersRef.child("favorite").setValue(favorite);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void countStocks(final String stockNumber) {
        final DatabaseReference ref;

        ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = ref.child("stockCount").child(stockNumber);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    usersRef.child("count").setValue(1);
                } else {
                    for (final DataSnapshot count : dataSnapshot.getChildren()) {
                        String countNumber = count.getValue().toString();
                        int a = Integer.parseInt(countNumber) + 1;
                        usersRef.child("count").setValue(a);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void writeNewUserIfNeeded() {
        ref = FirebaseDatabase.getInstance().getReference();
        String userId = bundle.getString("uid");
        final String username = bundle.getString("name");
        final String email = bundle.getString("email");
        final DatabaseReference usersRef = ref.child("users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    usersRef.setValue(new User(username, email));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alertDialog(String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("系統提示")
                .setMessage(message)
                .setPositiveButton("確認", null)
                .show();
    }

    public void multipleDelete(boolean isMultipleDelete) {
        if (isMultipleDelete) {
            adapter.showCheckBox(false);
            mDisPlayFav = false;
        } else {
            adapter.showCheckBox(true);
            mDisPlayFav = true;
        }
    }

    public class GetStockInfo extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(final String... params) {
            stockLastValue sv = new stockLastValue(params[0], MainActivity.this);
            returnString = sv.getData();

            Log.e(TAG, "returnString: " + returnString);
            hstEPS.clear();
            hstGuShi.clear();
            hstGuLi.clear();
            hstPresent.clear();
            ref = FirebaseDatabase.getInstance().getReference();

            ref.keepSynced(true);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp.getKey().equals("history")) {
                            for (DataSnapshot stockNum : dsp.getChildren()) {
                                if (stockNum.getKey().toString().equals(params[0])) {
                                    for (DataSnapshot stockItem : stockNum.getChildren()) {
                                        if (stockItem.getKey().toString().equals("eps")) {
                                            hstEPS.add(stockItem.getValue().toString());
                                            //Log.e(TAG, "Main Eps" + stockItem.getValue() + " ...");
                                        } else if (stockItem.getKey().toString().equals("guli")) {
                                            hstGuLi.add(stockItem.getValue().toString());
                                            //Log.e(TAG,"guli" +stockItem.getValue() + " ...");
                                        } else if (stockItem.getKey().toString().equals("guShi")) {
                                            hstGuShi.add(stockItem.getValue().toString());
                                            //Log.e(TAG,"guShi" +stockItem.getValue() + " ...");
                                        } else if (stockItem.getKey().toString().equals("present")) {
                                            hstPresent.add(stockItem.getValue().toString());
                                            //Log.e(TAG,"present" +stockItem.getValue() + " ...");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    startFragment();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        public void startFragment() {
            Log.e(TAG, "startFragment EPS: " + hstEPS);
            Intent in = new Intent(getApplicationContext(), FragmentMain.class);
            in.putExtra("stockNumber", stockNumber);
            if (returnString != null) {
                if (returnString.equals("HIGH")) {
                    in.putExtra("stockName", stockName);
                } else {
                    in.putExtra("stockName", stockName + "    現價 : " + returnString);
                }
            } else {
                in.putExtra("stockName", stockName + "    現價 : " + "無");
            }

            in.putExtra("stockEps", hstEPS);
            in.putExtra("stockGuLi", hstGuLi);
            in.putExtra("stockGuShi", hstGuShi);
            in.putExtra("stockPresent", hstPresent);
            startActivity(in);
            overridePendingTransition(R.anim.slide_in_left_1, R.anim.slide_int_left_2);
        }
    }

    private void checkUsing() {
        CountNum += 1;
        if (CountNum == 10 && !alreadyGj.equals("true")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("◎ 給個 5 星好評，讓我們永續經營")
                    .setTitle("感恩您的愛用")
                    .setCancelable(false)
                    .setPositiveButton("讚", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intentDL = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.james.stockparser"));
                            startActivity(intentDL);
                        }
                    })
                    .setNeutralButton("已經讚囉", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            tinydb.putString("GJ", "true");
                            alreadyGood = true;
                        }
                    })
                    .setNegativeButton("下次", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            tinydb.putString("GJ", "false");
                            alreadyGood = false;
                            CountNum = 0;
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void addSome(int position) {
        countAD += 1;
        if (countAD == 5) {
            displayInterstitial();
            countAD = 0;
        }
        if (!searchView.getQuery().toString().equals("")) {
            addingSeearch(myHistory, position);
            // Log.e(TAG, "stockName " + stockName + "position" + position);
            stockName = myDataFilter.get(position).getStockName();
            stockNumber = myDataFilter.get(position).getStockNumber();
            //Log.e(TAG, "stockName " + stockName + "stockNumber" + stockNumber);
        } else if (userStatus.equals("filting")) {
            stockName = myDataFilter.get(position).getStockName();
            stockNumber = myDataFilter.get(position).getStockNumber();
        } else if (userStatus.equals("home")) {
            addingArrList(myHistory, position);
            stockName = myDataset.get(position).getStockName();
            stockNumber = myDataset.get(position).getStockNumber();
            //Log.e(TAG, "stockName " + stockName + "stockNumber" + stockNumber);
        } else if (userStatus.equals("nearly")) {
            addingNear(myHistory, position);
            stockName = nearlyStock.get(position).getStockName();
            stockNumber = nearlyStock.get(position).getStockNumber();
        } else if (userStatus.equals("favorite")) {
            stockName = myFavorite.get(position).getStockName();
            stockNumber = myFavorite.get(position).getStockNumber();
        } else if (userStatus.equals("history")) {
            stockName = myHistory.get(position).getStockName();
            stockNumber = myHistory.get(position).getStockNumber();
        }
        for (int i = 0; i < stockNumbers.size(); i++) {
            if (stockNumbers.get(i).equals(stockNumber)) {
                // Log.e(TAG, "stockNumbers.size() " + stockNumbers.size() + "stockNumbers.get(i) " + stockNumbers.get(i));
                containStock = true;
            }
        }
        if (containStock) {
            countStocks(stockNumber);
            new GetStockInfo().execute(stockNumber);
            checkUsing();
            containStock = false;
        } else {
            Toast.makeText(MainActivity.this, "暫無此檔股票資訊", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetData extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                public void run() {
                    mProgressDialog = ProgressDialog.show(MainActivity.this, "更新資料", "取得資料中...請稍候", true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress(0);
            stockDividend sd = new stockDividend();
            stockHotCount sh = new stockHotCount();
            stockPayGushi stp = new stockPayGushi();
            getPaygushi = stp.getNowGuShi();
            publishProgress(20);
            hotClick = sh.getHotCount();
            stockMap = sd.getDividend(getYesterDate());
            stockPERatio sdPE = new stockPERatio();
            publishProgress(40);
            stockPEMap = sdPE.getPERatio(getYesterDate());
            getRemoteConfig grc = new getRemoteConfig(MainActivity.this);
            grc.getRemotePara();
            StockInfoParser spinfo = new StockInfoParser();
            stockChoMa = spinfo.getChoma();
            //Log.e(TAG, "tag :: " + tinydb.getString("show_floating_button"));
            userId = bundle.getString("uid");
            ref = FirebaseDatabase.getInstance().getReference();
            ref.keepSynced(true);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp.getKey().equals("EPS")) {
                            for (DataSnapshot eps : dsp.getChildren()) {
                                //Log.e(TAG, "--" +single.getKey());
                                stockInfoName = eps.child("stockName").getValue().toString();
                                stockInfoNumber = eps.child("stockNumber").getValue().toString();
                                stockInfoEPS = eps.child("stockEPS").getValue().toString();
                                myEPS.add(new StockEPS(stockInfoNumber, stockInfoName, stockInfoEPS));
                            }
                        } else if (dsp.getKey().equals("stocks")) {
                            for (DataSnapshot stock : dsp.getChildren()) {
                                releaseCount = stock.child("releaseCount").getValue().toString();
                                stockName = stock.child("stockName").getValue().toString();
                                stockNumber = stock.child("stockNumber").getValue().toString();
                                if (stockMap.get(stockNumber) != null) {
                                    //Log.e(TAG,"Get Dividend : " + dividend);
                                    dividend = stockMap.get(stockNumber).toString() + " ％";
                                } else {
                                    dividend = "無";
                                }
                                if (stockPEMap.get(stockNumber) != null) {
                                    peRatio = stockPEMap.get(stockNumber).toString();
                                } else {
                                    peRatio = "無";
                                }
                                if (hotClick.get(stockNumber) != null) {
                                    if (isVistor()) {
                                        hotclickCount = "會員獨享";
                                    } else {
                                        hotclickCount = hotClick.get(stockNumber).toString() + " 次";
                                    }
                                } else {
                                    hotclickCount = "無";
                                }
                                if (getPaygushi.get(stockNumber) != null) {
                                    payShi = getPaygushi.get(stockNumber).split(",")[0];
                                    payGu = getPaygushi.get(stockNumber).split(",")[1];
                                } else {
                                    payGu = "無";
                                    payShi = "無";
                                }
                                thisYear = stock.child("thisYear").getValue().toString();
                                tianxiDay = stock.child("tianxiDay").getValue().toString();
                                tianxiPercent = stock.child("tianxiPercent").getValue().toString();
                                tianxiCount = stock.child("tianxiCount").getValue().toString();
                                myDataset.add(new StockItem(payGu, payShi, hotclickCount, peRatio, dividend, stockNumber, stockName, tianxiCount, releaseCount, tianxiPercent, tianxiDay, thisYear));
                            }
                        } else if (!isVistor() && dsp.getKey().equals("users")) {
                            for (DataSnapshot users : dsp.getChildren()) {
                                if (userId.equals(users.getKey())) { //the same as account
                                    for (DataSnapshot fav : users.getChildren()) {
                                        if (fav.getKey().equals("favorite")) {
                                            for (DataSnapshot set : fav.getChildren()) {
                                                //Log.e(TAG, "TT :: " + set.getValue());
                                                favList.add(set.getValue().toString());
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (dsp.getKey().equals("history")) {
                            for (DataSnapshot stockNm : dsp.getChildren()) {
                                stockNumbers.add(stockNm.getKey().toString());
                                for (DataSnapshot stockInfo : stockNm.getChildren()) {
                                    if (stockInfo.getKey().toString().equals("eps")) {
                                        for (DataSnapshot stockInfos : stockInfo.getChildren()) {
                                            if (stockInfos.getKey().toString().equals("0")) {
                                                hisNumber = stockInfos.getValue().toString();
                                            } else if (stockInfos.getKey().toString().equals("1")) {
                                                hisName = stockInfos.getValue().toString();
                                            } else if (stockInfos.getKey().toString().equals("2")) {
                                                hisProduct = stockInfos.getValue().toString();
                                            } else if (stockInfos.getKey().toString().equals("3")) {
                                                hisEPS = stockInfos.getValue().toString();
                                            }
                                        }
                                    }
                                }
                                myHisEPS.add(new HistoryItem(hisNumber, hisName, hisProduct, hisEPS));
                            }
                        }

                    }
                    listAdaperr(myDataset, isVistor, selectAll);
                    try {
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        mProgressDialog = null;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //mProgressDialog.dismiss();
                    Log.e(TAG, "onCancelled...");
                }
            });
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(TAG, "Run onPostExecute ...");
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (Integer.toString(values[0]).equals("0")) {
                        mProgressDialog.setMessage("取得資料中... 請稍候");
                    } else if (Integer.toString(values[0]).equals("20")) {
                        mProgressDialog.setMessage("下載伺服器數據中...");
                    } else if (Integer.toString(values[0]).equals("40")) {
                        mProgressDialog.setMessage("正在更新手機端資料... 請稍後");
                    } else {
                        mProgressDialog.setMessage("下載更新完畢");
                    }

                }
            });

        }
    }
}

