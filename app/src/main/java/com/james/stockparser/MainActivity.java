package com.james.stockparser;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.james.stockparser.Fragment.FragmentMain;
import com.james.stockparser.Unit.User;
import com.james.stockparser.dataBase.TinyDB;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    private ListView listV;
    public MyAdapter adapter;
    ArrayList<StockItem> myDataset = new ArrayList<StockItem>();
    ArrayList<StockEPS> myEPS = new ArrayList<StockEPS>();
    ArrayList<StockItem> myDataFilter = new ArrayList<StockItem>();
    ArrayList<StockItem> myFavorite = new ArrayList<StockItem>();
    ArrayList<String> favList = new ArrayList<String>();
    ArrayAdapter<String> countNumAdapter;
    ArrayAdapter<String> avgNumAdapter;
    String[] countList = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
    String[] avgList = {"10","20","30","40"};
    private String[] nextLine;
    private Toolbar mToolbar;
    SearchView searchView;
    String releaseCount, stockName, stockNumber, thisYear, tianxiCount, tianxiDay, tianxiPercent;
    String stockInfoName, stockInfoNumber, stockInfoEPS;
    DatabaseReference ref;
    ProgressDialog mProgressDialog;
    Bundle bundle;
    TinyDB tinydb;
    SharedPreferences prefs;
    RatingBar ratingbarStart;
    Button btnDiglog;
    Spinner countNumber, avgNumber;
    String userId;
    boolean isVistor;
    boolean mDisPlayFav = false;
    boolean selectAll = false;
    int PageNumber = 0;
    private Menu menuItem;
    Boolean countHigh = false;
    Boolean avgLow = false;
    float percentTaixi;
    String countSelectNumber, avgSelectNumber;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menuItem = menu;
        if (isVistor()) {
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
        }
        if (PageNumber == 0) {
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        } else if (PageNumber == 1) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(4).setVisible(false);
        }
        SearchManager searchManager = (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("股票代號/名稱");
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorGray));
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
                    Log.e(TAG, "onQueryTextChange : " + s);
                    myDataFilter = filterResult(s, true, 0);
                    adapter = new MyAdapter(getApplicationContext(), myDataFilter, true, selectAll);
                    listV.setAdapter(adapter);
                    listV.invalidateViews();
                } else {
                    adapter = new MyAdapter(getApplicationContext(), myDataset, true, selectAll);
                    listV.setAdapter(adapter);
                    listV.invalidateViews();
                }
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle = getIntent().getExtras();
        isVistor = isVistor();
        if (!isVistor) {
            writeNewUserIfNeeded();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listV = (ListView) findViewById(R.id.list_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        new GetData().execute();
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stockName = "";
                String stocknumber = "";
                if (!searchView.getQuery().toString().equals("")) {
                    stockName = myDataFilter.get(position).getStockName();
                    stocknumber = myDataFilter.get(position).getStockNumber();
                } else {
                    stockName = myDataset.get(position).getStockName();
                    stocknumber = myDataset.get(position).getStockNumber();
                }
                for (int i = 0; i < myEPS.size(); i++) {
                    if (myEPS.get(i).getStockNumber().equals(stocknumber)) {
                        Log.e(TAG, stocknumber + " " + stockName + " " + myEPS.get(i).getStockEPS());
                        Intent in = new Intent(getApplicationContext(), FragmentMain.class);
                        in.putExtra("stockNumber", stocknumber);
                        in.putExtra("stockName", stockName);
                        startActivity(in);
                    }
                }
                StockInfoParser stockinfo = new StockInfoParser();
                //stockinfo.start(stocknumber);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //readFav();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isVistor()) {
            saveUserData(compareNewData(favList, adapter.getToDelete(), false));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isVistor()) {
            saveUserData(compareNewData(favList, adapter.getToDelete(), false));
        }
    }

    public boolean isVistor() {
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    PageNumber = 0;
                    invalidateOptionsMenu(); //update toolbar
                    adapter = new MyAdapter(getApplicationContext(), myDataset, true, selectAll);
                    listV.setAdapter(adapter);
                    listV.invalidateViews();
                    return true;
                case R.id.navigation_dashboard:
                    PageNumber = 1;
                    if (isVistor()) {
                        alertDialog("訪客身分無法使用我的最愛功能");
                    } else {
                        invalidateOptionsMenu();//update toolbar
                        //Log.e(TAG, tinydb.getListString("myFav") + "");
                        //saveUserData(adapter.getFavorite());  //Upload Server
                        //writeFav();
                        myFavovResult(compareNewData(favList, adapter.getFavorite(), true)); //summary main item
                    }
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

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
        countNumAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, countList);
        avgNumAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, avgList);
        countNumber.setAdapter(countNumAdapter);
        avgNumber.setAdapter(avgNumAdapter);
        countNumber.setSelection(10); //sharepreffence
        avgNumber.setSelection(2);//sharepreffence

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


        ratingbarStart.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                percentTaixi = rating / 5;
            }
        });
        SwitchCompat swCount = (SwitchCompat) myView.findViewById(R.id.switchCountBtn);
        SwitchCompat swAvgDay = (SwitchCompat) myView.findViewById(R.id.switchAvgDayBtn);
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
                Log.e(TAG, "Btn_avgLow = " + avgLow);
                Log.e(TAG, "Btn_countHigh = " + countHigh);
                Log.e(TAG, "Btn_percent :: " + percentTaixi);
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
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            final View v = inflater.inflate(R.layout.alert_layout, null);
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    break;
                case R.id.action_update:
                    Log.e(TAG, "action_update");
                    saveUserData(compareNewData(favList, adapter.getToDelete(), false));
                    myFavovResult(compareNewData(favList, adapter.getToDelete(), false)); // reset List View
                    break;
                case R.id.action_trash:
                    Log.e(TAG, "action_trash");
                    multipleDelete(mDisPlayFav);
                    break;
                case R.id.action_favorite:
                    Log.e(TAG, compareNewData(favList, adapter.getToDelete(), false) + "");
                    if (PageNumber == 0) {
                        multipleDelete(mDisPlayFav);
                    } else {
                        Log.e(TAG, "PAGE 11 ");
                    }
                    break;
                case R.id.action_filter:
                    showDialog();
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setTitle("條件式搜尋")
//                            .setView(v)
//                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    EditText editTextPercent = (EditText) (v.findViewById(R.id.alert_edit_view));
//                                    EditText editTextDay = (EditText) (v.findViewById(R.id.alert_edit_view1));
//                                    myDataFilter = filterResult("null", true, editTextPercent.getText().toString(), editTextDay.getText().toString());
//
//                                    //Toast.makeText(getApplicationContext(), "你的id是" +editText.getText().toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .show();
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
    public ArrayList<StockItem> myFavovResult(ArrayList list) {
        ArrayList<StockItem> item = new ArrayList<StockItem>();
        item.clear();
        for (int i = 0; i < myDataset.size(); i++) {
            if (list.contains(myDataset.get(i).getStockNumber())) {
                item.add(new StockItem(myDataset.get(i).getStockNumber(),
                        myDataset.get(i).getStockName(),
                        myDataset.get(i).getTianxiCount(),
                        myDataset.get(i).getReleaseCount(),
                        myDataset.get(i).getTianxiPercent(),
                        myDataset.get(i).getTianxiDay(),
                        myDataset.get(i).getThisYear())
                );
            }
        }
        adapter = new MyAdapter(getApplicationContext(), item, isVistor, true);
        listV.setAdapter(adapter);
        listV.invalidateViews();
        return item;
    }

    public ArrayList<StockItem> filterResult(String input, boolean hasPattern, float taixiPercent) {
        ArrayList<StockItem> item = new ArrayList<StockItem>();
        item.clear();
        float percent = 0;
        float avgDay = 0;
        if (hasPattern && !input.equals("null")) {
            for (int i = 0; i < myDataset.size(); i++) {
                if (myDataset.get(i).getStockNumber().contains(input) || myDataset.get(i).getStockName().contains(input)) {
                    item.add(new StockItem(myDataset.get(i).getStockNumber(),
                            myDataset.get(i).getStockName(),
                            myDataset.get(i).getTianxiCount(),
                            myDataset.get(i).getReleaseCount(),
                            myDataset.get(i).getTianxiPercent(),
                            myDataset.get(i).getTianxiDay(),
                            myDataset.get(i).getThisYear())
                    );
                }
            }
        } else {
            float taixiAvgdayTOint = Float.parseFloat("0.8");
            float count;
            float avgdayCount;
            int avgthread;
            int countthread;
            if (taixiPercent > 0) {
                taixiAvgdayTOint = taixiPercent;
            }
            for (int i = 0; i < myDataset.size(); i++) {
                percent = Float.parseFloat(myDataset.get(i).getTianxiPercent());
                count = Float.parseFloat(myDataset.get(i).getTianxiCount());
                avgdayCount = Float.parseFloat(myDataset.get(i).getTianxiDay());
                countthread = Integer.parseInt(countSelectNumber);
                avgthread = Integer.parseInt(avgSelectNumber);
                if (countHigh) {
                    if (avgLow) {
                        if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && count > countthread && avgdayCount < avgthread) {
                            item.add(new StockItem(myDataset.get(i).getStockNumber(),
                                    myDataset.get(i).getStockName(),
                                    myDataset.get(i).getTianxiCount(),
                                    myDataset.get(i).getReleaseCount(),
                                    myDataset.get(i).getTianxiPercent(),
                                    myDataset.get(i).getTianxiDay(),
                                    myDataset.get(i).getThisYear())
                            );
                        }
                    } else {
                        if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && count > countthread) {
                            item.add(new StockItem(myDataset.get(i).getStockNumber(),
                                    myDataset.get(i).getStockName(),
                                    myDataset.get(i).getTianxiCount(),
                                    myDataset.get(i).getReleaseCount(),
                                    myDataset.get(i).getTianxiPercent(),
                                    myDataset.get(i).getTianxiDay(),
                                    myDataset.get(i).getThisYear())
                            );
                        }
                    }
                } else {
                    if (avgLow) {
                        if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1 && avgdayCount < avgthread) {
                            item.add(new StockItem(myDataset.get(i).getStockNumber(),
                                    myDataset.get(i).getStockName(),
                                    myDataset.get(i).getTianxiCount(),
                                    myDataset.get(i).getReleaseCount(),
                                    myDataset.get(i).getTianxiPercent(),
                                    myDataset.get(i).getTianxiDay(),
                                    myDataset.get(i).getThisYear())
                            );
                        }
                    } else {
                        if (percent >= taixiAvgdayTOint - 0.1 && percent <= taixiAvgdayTOint + 0.1) {
                            item.add(new StockItem(myDataset.get(i).getStockNumber(),
                                    myDataset.get(i).getStockName(),
                                    myDataset.get(i).getTianxiCount(),
                                    myDataset.get(i).getReleaseCount(),
                                    myDataset.get(i).getTianxiPercent(),
                                    myDataset.get(i).getTianxiDay(),
                                    myDataset.get(i).getThisYear())
                            );
                        }
                    }
                }
            }
            adapter = new MyAdapter(getApplicationContext(), item, isVistor, selectAll);
            listV.setAdapter(adapter);
            listV.invalidateViews();
            avgLow = false;
            countHigh = false;
            if (item.size() > 0) {
                Toast.makeText(getApplicationContext(), "搜尋到 : " + item.size() + "筆", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "搜索不到資料! 請更改一下篩選條件", Toast.LENGTH_LONG).show();
            }

        }
        return item;
    }

    public void CSVRead() {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new BufferedReader(new InputStreamReader(getAssets().open("data.csv"), "UTF-8")));
            while ((nextLine = reader.readNext()) != null) {
                //Log.e(TAG, nextLine[0] + nextLine[1] + nextLine[2] + nextLine[3] + nextLine[4] + nextLine[5] + nextLine[6]);
                myDataset.add(new StockItem(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5], nextLine[6]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
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

    private class GetData extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                public void run() {
                    mProgressDialog = ProgressDialog.show(MainActivity.this, "連線至伺服器", "取得資料中...請稍候", true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
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
                                thisYear = stock.child("thisYear").getValue().toString();
                                tianxiDay = stock.child("tianxiDay").getValue().toString();
                                tianxiPercent = stock.child("tianxiPercent").getValue().toString();
                                tianxiCount = stock.child("tianxiCount").getValue().toString();
                                myDataset.add(new StockItem(stockNumber, stockName, tianxiCount, releaseCount, tianxiPercent, tianxiDay, thisYear));
                            }
                        } else if (!isVistor() && dsp.getKey().equals("users")) {
                            for (DataSnapshot users : dsp.getChildren()) {
                                if (userId.equals(users.getKey())) { //the same as account
                                    for (DataSnapshot fav : users.getChildren()) {
                                        if (fav.getKey().equals("favorite")) {
                                            for (DataSnapshot set : fav.getChildren()) {
                                                Log.e(TAG, "TT :: " + set.getValue());
                                                favList.add(set.getValue().toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    adapter = new MyAdapter(getApplicationContext(), myDataset, true, selectAll);
                    listV.setAdapter(adapter);
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
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
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            runOnUiThread(new Runnable() {
                public void run() {
                    mProgressDialog.setMessage(values[0].toString());
                }
            });

        }
    }


}

