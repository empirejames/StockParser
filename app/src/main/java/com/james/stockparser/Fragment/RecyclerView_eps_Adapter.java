package com.james.stockparser.Fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.james.stockparser.R;

import java.util.ArrayList;

/**
 * Created by SONU on 10/09/15.
 */
public class RecyclerView_eps_Adapter extends RecyclerView.Adapter<RecyclerView_eps_Adapter.MyViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;
    private ViewGroup mParent;
    String TAG = RecyclerView_eps_Adapter.class.getSimpleName();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView his_two_year_avg, his_three_year_avg, his_four_year_avg , his_five_year_avg ,his_six_year_avg, his_seven_year_avg, his_eight_year_avg, his_night_year_avg;
        private TextView his_first_est_value1,his_first_est_value2,his_first_est_value3,his_first_est_value4,his_first_est_value5,his_first_est_value6,his_first_est_value7, his_first_est_value8,his_first_est_value9;
        private TextView first_est_value_number1,first_est_value_number2,first_est_value_number3,first_est_value_number4,first_est_value_number5,first_est_value_number6,first_est_value_number7,first_est_value_number8,first_est_value_number9;


        public MyViewHolder(View v) {
            super(v);
            his_two_year_avg = (TextView) v.findViewById(R.id.memberPoint);
            his_three_year_avg = (TextView) v.findViewById(R.id.memberPoint1);
            his_four_year_avg   = (TextView) v.findViewById(R.id.memberPoint2);
            his_five_year_avg   = (TextView) v.findViewById(R.id.memberPoint3);
            his_six_year_avg   = (TextView) v.findViewById(R.id.memberPoint4);
            his_seven_year_avg   = (TextView) v.findViewById(R.id.memberPoint5);
            his_eight_year_avg   = (TextView) v.findViewById(R.id.memberPoint6);
            his_night_year_avg   = (TextView) v.findViewById(R.id.memberPoint7);

            his_first_est_value1 = (TextView) v.findViewById(R.id.first_est_value1);
            his_first_est_value2 = (TextView) v.findViewById(R.id.first_est_value2);
            his_first_est_value3= (TextView) v.findViewById(R.id.first_est_value3);
            his_first_est_value4 = (TextView) v.findViewById(R.id.first_est_value4);
            his_first_est_value5 = (TextView) v.findViewById(R.id.first_est_value5);
            his_first_est_value6 = (TextView) v.findViewById(R.id.first_est_value6);
            his_first_est_value7 = (TextView) v.findViewById(R.id.first_est_value7);
            his_first_est_value8 = (TextView) v.findViewById(R.id.first_est_value8);
            his_first_est_value9 = (TextView) v.findViewById(R.id.first_est_value9);

            first_est_value_number1 = (TextView) v.findViewById(R.id.first_est_value_number);
            first_est_value_number2 = (TextView) v.findViewById(R.id.first_est_value_number1);
            first_est_value_number3= (TextView) v.findViewById(R.id.first_est_value_number2);
            first_est_value_number4 = (TextView) v.findViewById(R.id.first_est_value_number3);
            first_est_value_number5 = (TextView) v.findViewById(R.id.first_est_value_number4);
            first_est_value_number6 = (TextView) v.findViewById(R.id.first_est_value_number5);
            first_est_value_number7 = (TextView) v.findViewById(R.id.first_est_value_number6);
            first_est_value_number8 = (TextView) v.findViewById(R.id.first_est_value_number7);
            first_est_value_number9 = (TextView) v.findViewById(R.id.first_est_value_number8);
        }
    }

    public RecyclerView_eps_Adapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() /17 : 0);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_eps_row, parent, false);
        RecyclerView_eps_Adapter.MyViewHolder vh = new RecyclerView_eps_Adapter.MyViewHolder(v);
        mParent = parent;
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String year2 = arrayList.get(position).substring(arrayList.get(position).indexOf(":")+1,arrayList.get(position).length());
        String year3 = arrayList.get(position+1).substring(arrayList.get(position+1).indexOf(":")+1,arrayList.get(position+1).length());
        String year4 = arrayList.get(position+2).substring(arrayList.get(position+2).indexOf(":")+1,arrayList.get(position+2).length());
        String year5 = arrayList.get(position+3).substring(arrayList.get(position+3).indexOf(":")+1,arrayList.get(position+3).length());
        String year6 = arrayList.get(position+4).substring(arrayList.get(position+4).indexOf(":")+1,arrayList.get(position+4).length());
        String year7 = arrayList.get(position+5).substring(arrayList.get(position+5).indexOf(":")+1,arrayList.get(position+5).length());
        String year8 = arrayList.get(position+6).substring(arrayList.get(position+6).indexOf(":")+1,arrayList.get(position+6).length());
        String year9 = arrayList.get(position+7).substring(arrayList.get(position+7).indexOf(":")+1,arrayList.get(position+7).length());

        String q1_value = arrayList.get(position+8).substring(arrayList.get(position+8).indexOf(":")+1,arrayList.get(position+8).length());
        String q2_value = arrayList.get(position+9).substring(arrayList.get(position+9).indexOf(":")+1,arrayList.get(position+9).length());
        String q3_value = arrayList.get(position+10).substring(arrayList.get(position+10).indexOf(":")+1,arrayList.get(position+10).length());
        String q4_value = arrayList.get(position+11).substring(arrayList.get(position+11).indexOf(":")+1,arrayList.get(position+11).length());
        String q5_value = arrayList.get(position+12).substring(arrayList.get(position+12).indexOf(":")+1,arrayList.get(position+12).length());

        String after_tax_value1 = arrayList.get(position+13).substring(arrayList.get(position+13).indexOf(":")+1,arrayList.get(position+13).length());
        String after_tax_value2 = arrayList.get(position+14).substring(arrayList.get(position+14).indexOf(":")+1,arrayList.get(position+14).length());
        String after_tax_value3 = arrayList.get(position+15).substring(arrayList.get(position+15).indexOf(":")+1,arrayList.get(position+15).length());
        String after_tax_value4 = arrayList.get(position+16).substring(arrayList.get(position+16).indexOf(":")+1,arrayList.get(position+16).length());

        String his_first_est_value_text1 = arrayList.get(position+8).substring(0,arrayList.get(position+8).indexOf("每"));
        String his_first_est_value_text2 = arrayList.get(position+9).substring(0,arrayList.get(position+9).indexOf("每"));
        String his_first_est_value_text3 = arrayList.get(position+10).substring(0,arrayList.get(position+10).indexOf("每"));
        String his_first_est_value_text4 = arrayList.get(position+11).substring(0,arrayList.get(position+11).indexOf("每"));
        String his_first_est_value_text5 = arrayList.get(position+12).substring(0,arrayList.get(position+12).indexOf("每"));
        String his_first_est_value_text6 = arrayList.get(position+13).substring(0,arrayList.get(position+13).indexOf("每"));
        String his_first_est_value_text7 = arrayList.get(position+14).substring(0,arrayList.get(position+14).indexOf("每"));
        String his_first_est_value_text8 = arrayList.get(position+15).substring(0,arrayList.get(position+15).indexOf("每"));
        String his_first_est_value_text9 = arrayList.get(position+16).substring(0,arrayList.get(position+16).indexOf("每"));



        holder.his_two_year_avg.setText(year2);
        holder.his_three_year_avg.setText(year3);
        holder.his_four_year_avg.setText(year4);
        holder.his_five_year_avg.setText(year5);
        holder.his_six_year_avg.setText(year6);
        holder.his_seven_year_avg.setText(year7);
        holder.his_eight_year_avg.setText(year8);
        holder.his_night_year_avg.setText(year9);

        holder.first_est_value_number1.setText(q1_value);
        holder.first_est_value_number2.setText(q2_value);
        holder.first_est_value_number3.setText(q3_value);
        holder.first_est_value_number4.setText(q4_value);
        holder.first_est_value_number5.setText(q5_value);
        holder.first_est_value_number6.setText(after_tax_value1);
        holder.first_est_value_number7.setText(after_tax_value2);
        holder.first_est_value_number8.setText(after_tax_value3);
        holder.first_est_value_number9.setText(after_tax_value4);

        holder.his_first_est_value1.setText(his_first_est_value_text1);
        holder.his_first_est_value2.setText(his_first_est_value_text2);
        holder.his_first_est_value3.setText(his_first_est_value_text3);
        holder.his_first_est_value4.setText(his_first_est_value_text4);
        holder.his_first_est_value5.setText(his_first_est_value_text5);
        holder.his_first_est_value6.setText(his_first_est_value_text6);
        holder.his_first_est_value7.setText(his_first_est_value_text7);
        holder.his_first_est_value8.setText(his_first_est_value_text8);
        holder.his_first_est_value9.setText(his_first_est_value_text9);

        Log.e(TAG, "GG .... " + arrayList.get(position+8));
        Log.e(TAG, "GG .... " + arrayList.get(position+13));

    }



}
