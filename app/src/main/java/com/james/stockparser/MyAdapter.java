package com.james.stockparser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.james.stockparser.dataBase.TinyDB;

import java.util.ArrayList;

/**
 * Created by 101716 on 2017/6/27.
 */

public class MyAdapter extends BaseAdapter implements Filterable {
    private ArrayList<StockItem> mListItems;
    private LayoutInflater inflater;
    Context context;
    private boolean deleteContext;
    private boolean isVistor;
    String TAG = MyAdapter.class.getSimpleName();
    ArrayList<String> myFavorite = new ArrayList<String>();
    ArrayList<String> toDelete = new ArrayList<String>();
    TinyDB tinydb;
    String stockNumber;
    boolean page2;

    public MyAdapter(Context context) {
        this.context = context;
    }

    public MyAdapter(Context context, ArrayList<StockItem> itemList, boolean isVistor, boolean page2) {
        this.context = context;
        this.isVistor = isVistor;
        this.page2 = page2;
        mListItems = itemList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tinydb = new TinyDB(context);
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
    class ViewHolder
    {
        private TextView hotCount;
        private TextView peRatio;
        private TextView dividend;
        private TextView stockName;
        private RatingBar rb1;
        private TextView tianxiCount;
        private TextView releaseCount;
        private TextView tianxiDay;
        private TextView thisYear;
        private TextView guValue;
        private TextView shiValue;
        TextView stockNumber = null;
        private ImageView img_right ;
        private CheckBox cb;
        private CheckBox cbDel;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        float transTianx = 0;
        final StockItem item = mListItems.get(position);

        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_stock, parent, false);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.img_right = (ImageView) convertView.findViewById(R.id.right_select);
        holder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
        holder.cbDel = (CheckBox) convertView.findViewById(R.id.checkbox_delete);
        holder.cb.setVisibility(View.GONE);
        holder.cbDel.setVisibility(View.GONE);
        holder.img_right.setVisibility(View.VISIBLE);
        if (page2) {
            holder.cb.setVisibility(View.GONE);
            //cbDel.setVisibility(View.VISIBLE);
        }
        if (deleteContext) {

            if (!page2) {
                holder.cb.setVisibility(View.VISIBLE);

                holder.img_right.setVisibility(View.GONE);
            } else {
                holder.cbDel.setVisibility(View.VISIBLE);
                holder.img_right.setVisibility(View.VISIBLE);
            }
            if (page2 && deleteContext) {
                holder.cbDel.setVisibility(View.VISIBLE);
                holder.img_right.setVisibility(View.INVISIBLE);
            } else{
                holder.cbDel.setVisibility(View.GONE);
            }
        }
        holder.guValue = (TextView) convertView.findViewById(R.id.gu_value);
        holder.guValue.setText(item.getPayGu());
        holder.shiValue = (TextView) convertView.findViewById(R.id.shi_value);
        holder.shiValue.setText(item.getPayShi());
        holder.stockNumber = (TextView) convertView.findViewById(R.id.stock_Number);
        holder.stockNumber.setText(item.getStockNumber());
        stockNumber = item.getStockNumber();
        holder.peRatio = (TextView) convertView.findViewById(R.id.peRatio_data);
        holder.peRatio.setText(item.getPeRatio());
        holder.dividend = (TextView) convertView.findViewById(R.id.now_dividend_data);
        holder.dividend.setText(item.getNowDividend());

        holder.stockName = (TextView) convertView.findViewById(R.id.stock_Name);
        holder.stockName.setText(item.getStockName());

        holder.rb1 = (RatingBar) convertView.findViewById(R.id.RatingBar01);

        transTianx = Float.parseFloat(item.getTianxiPercent()) * 100 / 20;

        holder.rb1.setRating(transTianx);

        holder.tianxiCount = (TextView) convertView.findViewById(R.id.tianxiCountName);
        holder.tianxiCount.setText(item.getTianxiCount());

        holder.releaseCount = (TextView) convertView.findViewById(R.id.releaseName);
        holder.releaseCount.setText(item.getReleaseCount());

        holder.tianxiDay = (TextView) convertView.findViewById(R.id.taixiaverageName);
        holder.tianxiDay.setText(item.getTianxiDay());

        holder.thisYear = (TextView) convertView.findViewById(R.id.thisyearName);
        holder.hotCount =  (TextView) convertView.findViewById(R.id.hotValue_data);
        holder.hotCount.setText(item.getHotclickCount());
        if (item.getThisYear().equals("")) {
            holder.thisYear.setText("未公告");
        } else {
            holder.thisYear.setText(item.getThisYear());
        }
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String sn = stockNumber;
                if (isChecked && !myFavorite.contains(sn)) {
                    Log.e(TAG, " ADD : " + sn);
                    myFavorite.add(sn);
                } else if (!isChecked && myFavorite.contains(sn)) {
                    Log.e(TAG, " Remove : " + sn);
                    myFavorite.remove(sn);
                }
            }
        });
        holder.cbDel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String sn = stockNumber;
                if (isChecked) {
                    Log.e(TAG, " Remove : " + sn);
                    toDelete.add(sn);
                    myFavorite.remove(sn);
                }
            }
        });
        holder.img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) parent.getContext()).addSome(position);
                Log.e(TAG,"Click right selector......");
            }
        });
        holder.cb.setChecked(myFavorite.contains(item.getStockNumber()));
        return convertView;
    }

    @Override
    public Filter getFilter() {

        return null;
    }

    public ArrayList<String> getFavorite() {
        return myFavorite;
    }

    public ArrayList<String> getToDelete() {
        return toDelete;
    }

    public void showCheckBox(boolean show) {
        Log.e(TAG," Show : " + show);
        if (show) {
            deleteContext = true;

        } else {
            deleteContext = false;
        }
        notifyDataSetChanged();
    }
}
