package com.james.stockparser.Fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.james.stockparser.R;
import com.james.stockparser.RecycleViewAdapter;

import java.util.ArrayList;

/**
 * Created by SONU on 10/09/15.
 */
public class RecyclerView_paygushi_Adapter extends RecyclerView.Adapter<RecyclerView_paygushi_Adapter.MyViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;
    private ViewGroup mParent;
    String TAG = RecyclerView_paygushi_Adapter.class.getSimpleName();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView his_year;
        private TextView his_paygu;
        private TextView his_payshi;


        public MyViewHolder(View v) {
            super(v);
            his_year = (TextView) v.findViewById(R.id.his_year);
            his_paygu = (TextView) v.findViewById(R.id.his_paygu);
            his_payshi   = (TextView) v.findViewById(R.id.his_payshi);
        }
    }

    public RecyclerView_paygushi_Adapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size()/2 : 0);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_paygushi_row, parent, false);
        RecyclerView_paygushi_Adapter.MyViewHolder vh = new RecyclerView_paygushi_Adapter.MyViewHolder(v);
        mParent = parent;
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String year ;
        String payGui ;
        String payShi;
        if(position==0){
            payShi = arrayList.get(position+1).substring(arrayList.get(position+1).indexOf(")")+1,arrayList.get(position+1).length());
            payGui = arrayList.get(position).substring(arrayList.get(position).indexOf(")")+1,arrayList.get(position).length());
            year = arrayList.get(position*2).substring(0,arrayList.get(position*2).indexOf(")")+1);
        }else{
            payShi = arrayList.get(position*2+1).substring(arrayList.get(position*2+1).indexOf(")")+1,arrayList.get(position*2+1).length());
            year = arrayList.get(position*2).substring(0,arrayList.get(position*2).indexOf(")")+1);
            payGui = arrayList.get(position*2).substring(arrayList.get(position*2).indexOf(")")+1,arrayList.get(position*2).length());
        }
        holder.his_paygu.setText(payGui);
        holder.his_payshi.setText(payShi);
        holder.his_year.setText(year);

        Log.e(TAG, "GG .... " + arrayList.get(position));

    }



}
