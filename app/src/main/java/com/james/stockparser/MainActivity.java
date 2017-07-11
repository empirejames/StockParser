package com.james.stockparser;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.james.stockparser.Fragment.FragmentMain;
import com.james.stockparser.Unit.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    private ListView listV;
    public MyAdapter adapter;
    ArrayList<StockItem> myDataset = new ArrayList<StockItem>();
    ArrayList<StockEPS> myEPS = new ArrayList<StockEPS>();
    ArrayList<StockItem> myDataFilter = new ArrayList<StockItem>();
    private String[] nextLine;
    private Toolbar mToolbar;
    SearchView searchView;
    String releaseCount, stockName, stockNumber, thisYear, tianxiCount, tianxiDay, tianxiPercent;
    String stockInfoName, stockInfoNumber, stockInfoEPS;
    DatabaseReference ref;
    ProgressDialog mProgressDialog;
    boolean mDisPlayFav = false;
    Bundle bundle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("股票代號/名稱");
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.gray));
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
                    myDataFilter = filterResult(s, true, "0", "0");
                    adapter = new MyAdapter(getApplicationContext(), myDataFilter);
                    listV.setAdapter(adapter);
                    listV.invalidateViews();
                } else {
                    adapter = new MyAdapter(getApplicationContext(), myDataset);
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
        writeNewUserIfNeeded();
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
                //Log.e(TAG, stocknumber + " " + stockName);
                for (int i = 0; i < myEPS.size(); i++) {
                    //Log.e(TAG,myEPS.get(i).getStockNumber() );
                    //Log.e(TAG,"..0 " + stocknumber );
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

    public boolean isVistor(){
        if(bundle.getString("isVistor")!= null) {
            String vistor  = bundle.getString("isVistor");
            Log.e(TAG,vistor + " " );
            if (vistor.toString().equals("Y")){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            final View v = inflater.inflate(R.layout.alert_layout, null);
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    break;
                case R.id.my_favorite:
                    multipleDelete(mDisPlayFav);
                    break;
                case R.id.action_filter:
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("條件式搜尋")
                            .setView(v)
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText editTextPercent = (EditText) (v.findViewById(R.id.alert_edit_view));
                                    EditText editTextDay = (EditText) (v.findViewById(R.id.alert_edit_view1));
                                    myDataFilter = filterResult("null", true, editTextPercent.getText().toString(), editTextDay.getText().toString());

                                    //Toast.makeText(getApplicationContext(), "你的id是" +editText.getText().toString(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                    break;
            }

            if (!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    public void multipleDelete(boolean isMultipleDelete) {
        if (isMultipleDelete){
            adapter.showCheckBox(false);
            mDisPlayFav = false;
        } else{
            adapter.showCheckBox(true);
            mDisPlayFav = true;
        }
    }

    public ArrayList<StockItem> filterResult(String input, boolean hasPattern, String taixiPercent, String taixiAvgday) {
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
            if (taixiPercent.equals("") || taixiAvgday.equals("")) {
                Toast.makeText(getApplicationContext(), "條件不得為空", Toast.LENGTH_SHORT).show();
            } else {
                float taixiPercentTOint = Float.parseFloat(taixiPercent) / 5;
                float taixiAvgdayTOint = Float.parseFloat(taixiAvgday);
                String s = searchView.getQuery().toString();
                for (int i = 0; i < myDataset.size(); i++) {
                    percent = Float.parseFloat(myDataset.get(i).getTianxiPercent());
                    avgDay = Float.parseFloat(myDataset.get(i).getTianxiDay());
                    if (percent >= taixiPercentTOint && avgDay < taixiAvgdayTOint) {
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
            adapter = new MyAdapter(getApplicationContext(), item);
            listV.setAdapter(adapter);
            listV.invalidateViews();
            Toast.makeText(getApplicationContext(), "搜尋到 : " + item.size() + "筆", Toast.LENGTH_LONG).show();
        }
        return item;
    }

    public void settingSet() {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.TANXI_PERCENT, MODE_PRIVATE).edit();
        editor.putString(Constants.TANXI_PERCENT, "0");
        editor.putString(Constants.TANXI_ACGDAY, "100");
        editor.putString(Constants.NOW_DAY, "100");
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

    private void writeNewUserIfNeeded() {
        ref = FirebaseDatabase.getInstance().getReference();
        String userId  = bundle.getString("uid");
        final String username  = bundle.getString("name");
        final String email  = bundle.getString("email");
        final DatabaseReference usersRef = ref.child("users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    usersRef.setValue(new User(username, email));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                        }
                    }
                    adapter = new MyAdapter(getApplicationContext(), myDataset);
                    listV.setAdapter(adapter);
                    multipleDelete(isVistor());
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

