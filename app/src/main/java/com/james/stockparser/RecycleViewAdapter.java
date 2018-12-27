package com.james.stockparser;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.james.stockparser.dataBase.TinyDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by 101716 on 2018/10/16.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> implements Filterable {
    private ArrayList<StockItem> itemList;
    private Context mContext;
    private String userId;
    String TAG = RecycleViewAdapter.class.getSimpleName();
    Map<String, String> stockChoMa = new HashMap<String, String>();
    ArrayList<String> myFavorite = new ArrayList<String>();
    ArrayList<String> toAdd= new ArrayList<String>();
    TinyDB tinydb;
    private LayoutInflater inflater;
    private ViewGroup mParent;
    boolean page2, isVistor;



    public RecycleViewAdapter(){

    }
    public RecycleViewAdapter(Context mContext, ArrayList<StockItem> itemList,Map<String, String> stockChoMa, ArrayList<String> myFavorite ,String userId, boolean isVistor, boolean page2){
        this.itemList = itemList;
        this.stockChoMa = stockChoMa;
        this.mContext = mContext;
        this.userId = userId;
        this.myFavorite = myFavorite;
        this.isVistor = isVistor;
        this.page2 = page2;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tinydb = new TinyDB(mContext);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
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
        private TextView VolNm, longchiNm, toshinNm, longchiUseNm, wichiNm, longChunNm, threebigNm, longChunUseNm, selfemployNm, yestoday;
        TextView stockNumber = null;
        private ImageView img_right ;
        private CheckBox cb;
        private CheckBox cbDel;
        private View frontLayout;
        private View addLayout;
        private TextView add_text;


        public MyViewHolder(View v) {
            super(v);
            frontLayout = (View) v.findViewById(R.id.front_layout);
            addLayout = (View) v.findViewById(R.id.add_layout);

            img_right = (ImageView) v.findViewById(R.id.right_select);
            cb = (CheckBox) v.findViewById(R.id.checkbox);
            cbDel = (CheckBox) v.findViewById(R.id.checkbox_delete);
            guValue = (TextView) v.findViewById(R.id.gu_value);
            shiValue = (TextView) v.findViewById(R.id.shi_value);
            stockNumber = (TextView) v.findViewById(R.id.stock_Number);
            peRatio = (TextView) v.findViewById(R.id.peRatio_data);
            dividend = (TextView) v.findViewById(R.id.now_dividend_data);
            stockName = (TextView) v.findViewById(R.id.stock_Name);
            rb1 = (RatingBar) v.findViewById(R.id.RatingBar01);
            tianxiCount = (TextView) v.findViewById(R.id.tianxiCountName);
            releaseCount = (TextView) v.findViewById(R.id.releaseName);
            tianxiDay = (TextView) v.findViewById(R.id.taixiaverageName);
            thisYear = (TextView) v.findViewById(R.id.thisyearName);
            hotCount =  (TextView) v.findViewById(R.id.hotValue_data);
            rb1 = (RatingBar) v.findViewById(R.id.RatingBar01);

            /// Hide relaytive layout using
            VolNm = v.findViewById(R.id.VolNm);
            longchiNm = v.findViewById(R.id.longchiNm);
            toshinNm = v.findViewById(R.id.toshinNm);
            longchiUseNm = v.findViewById(R.id.longchiUseNm);
            wichiNm = v.findViewById(R.id.wichiNm);
            longChunNm = v.findViewById(R.id.longChunNm);
            threebigNm = v.findViewById(R.id.threebigNm);
            longChunUseNm = v.findViewById(R.id.longChunUseNm);
            selfemployNm = v.findViewById(R.id.selfemployNm);
            yestoday = v.findViewById(R.id.yestoday);
            ////

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_stock, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        mParent = parent;
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        float transTianx = 0;
        holder.cb.setVisibility(View.GONE);
        holder.cbDel.setVisibility(View.GONE);
        holder.img_right.setVisibility(View.VISIBLE);
        holder.guValue.setText(itemList.get(position).getPayGu());
        holder.shiValue.setText(itemList.get(position).getPayShi());
        holder.stockNumber.setText(itemList.get(position).getStockNumber());
        holder.peRatio.setText(itemList.get(position).getPeRatio());
        holder.dividend.setText(itemList.get(position).getNowDividend());
        holder.stockName.setText(itemList.get(position).getStockName());
        holder.tianxiCount.setText(itemList.get(position).getTianxiCount());
        holder.releaseCount.setText(itemList.get(position).getReleaseCount());
        holder.tianxiDay.setText(itemList.get(position).getTianxiDay());
        holder.hotCount.setText(itemList.get(position).getHotclickCount());
        transTianx = Float.parseFloat(itemList.get(position).getTianxiPercent()) * 100 / 20;
        holder.rb1.setRating(transTianx);
        if (itemList.get(position).getThisYear().equals("")) {
            holder.thisYear.setText("未公告");
        } else {
            holder.thisYear.setText(itemList.get(position).getThisYear());
        }

        holder.addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAdd.add(holder.stockNumber.getText().toString());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference usersRef = ref.child("users").child(userId).child("favorite");
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && toAdd.size() != 0) {

                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    toAdd.add(dsp.getValue().toString());
                                    if(!dsp.getValue().equals(toAdd.get(0).toString())){
                                        usersRef.setValue(toAdd);
                                    }
                            }

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.frontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View relativeLy = null;
                final int height;
                relativeLy = (View) v.findViewById(R.id.relative_layout_all);
                relativeLy.measure(0, 0);
                height = relativeLy.getMeasuredHeight();
                if (relativeLy.getVisibility() == View.GONE) {
                    String stockNumber;
                    stockNumber = itemList.get(position).getStockNumber();
                    show(relativeLy, height, stockNumber, holder);
                } else {
                    dismiss(relativeLy, height);
                }
            }
        });

        holder.img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mParent.getContext()).addSome(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void show(final View v, int height, String stockNm, MyViewHolder holder) {
        if (stockChoMa.get(stockNm) != null) {
            v.setVisibility(View.VISIBLE);
            String choMa[] = stockChoMa.get(stockNm).split(":");
            holder.yestoday.setText(((BaseActivity) mParent.getContext()).getYearORDate("yesterday"));
            holder.VolNm.setText(choMa[0]);
            changeValue(choMa[1], holder.toshinNm);
            changeValue(choMa[2], holder.wichiNm);
            changeValue(choMa[3], holder.selfemployNm);
            changeValue(choMa[4], holder.threebigNm);
            changeValue(choMa[5], holder.longchiNm);
            changeValue(choMa[7], holder.longChunNm);
            holder.longchiUseNm.setText(choMa[6]);
            holder.longChunUseNm.setText(choMa[8]);
            ValueAnimator animator = ValueAnimator.ofInt(0, height);
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    v.setVisibility(View.VISIBLE);
                    v.getLayoutParams().height = value;
                    v.setLayoutParams(v.getLayoutParams());
                }
            });
            animator.start();
        } else {
            Toast.makeText(mContext, "此檔無籌碼資訊", Toast.LENGTH_LONG).show();
        }
    }
    public void dismiss(final View v, int height) {
        ValueAnimator animator = ValueAnimator.ofInt(height, 0);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                if (value == 0) {
                    v.setVisibility(View.GONE);
                }
                v.getLayoutParams().height = value;
                v.setLayoutParams(v.getLayoutParams());
            }
        });
        animator.start();
    }
    public ArrayList<String> getFavorite() {
        return myFavorite;
    }
    public void changeValue(String value, TextView v) {
        if (Integer.parseInt(value) >= 0) {
            v.setTextColor(mContext.getResources().getColor(R.color.colorRed));
        } else {
            v.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        }
        v.setText(value);
    }

}
