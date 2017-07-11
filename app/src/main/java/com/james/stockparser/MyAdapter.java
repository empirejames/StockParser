package com.james.stockparser;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 101716 on 2017/6/27.
 */

public class MyAdapter extends BaseAdapter implements Filterable {
    private ArrayList<StockItem> mListItems;
    private LayoutInflater inflater;
    Context context;
    private boolean deleteContext;
    String TAG = MyAdapter.class.getSimpleName();
    ArrayList<String> myFavorite = new ArrayList<String>();

    public MyAdapter(Context context, ArrayList<StockItem> itemList) {
        this.context = context;
        mListItems = itemList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        float transTianx  = 0;
        final View row = inflater.inflate(R.layout.list_stock, parent, false);
        final StockItem item = mListItems.get(position);
        CheckBox cb = (CheckBox) row.findViewById(R.id.checkbox);
        if(deleteContext){
            cb.setVisibility(View.VISIBLE);
        }else{
            cb.setVisibility(View.GONE);
        }
        final TextView stockNumber = (TextView) row.findViewById(R.id.stock_Number);
        stockNumber.setText(item.getStockNumber());

        TextView stockName = (TextView) row.findViewById(R.id.stock_Name);
        stockName.setText(item.getStockName());

        RatingBar rb1 = (RatingBar)row.findViewById(R.id.RatingBar01);

        transTianx = Float.parseFloat(item.getTianxiPercent()) *100 /20;

        rb1.setRating(transTianx);

        TextView tianxiCount = (TextView) row.findViewById(R.id.tianxiCountName);
        tianxiCount.setText(item.getTianxiCount());

        TextView releaseCount = (TextView) row.findViewById(R.id.releaseName);
        releaseCount.setText(item.getReleaseCount());


        TextView tianxiDay = (TextView) row.findViewById(R.id.taixiaverageName);
        tianxiDay.setText(item.getTianxiDay());

        TextView thisYear = (TextView) row.findViewById(R.id.thisyearName);
        if (item.getThisYear().equals("")) {
            thisYear.setText("未公告");
        } else {
            thisYear.setText(item.getThisYear());
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String sn = stockNumber.getText().toString();
                if(isChecked){
                    Log.e(TAG, " ADD : " + sn);
                    myFavorite.add(sn);
                } else{
                    Log.e(TAG, " Remove : " + sn);
                    myFavorite.remove(sn);
                }
            }
        });
        return row;
    }

    @Override
    public Filter getFilter() {

        return null;
    }
    public void showCheckBox(boolean show){
        if(show){
            deleteContext = true;
        }else{
            deleteContext = false;
        }
        notifyDataSetChanged();
    }
}
