package com.james.stockparser.NetWork;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.james.stockparser.dataBase.TinyDB;

/**
 * Created by 101716 on 2018/5/21.
 */

public class getRemoteConfig {
    String TAG = stockDividend.class.getSimpleName();
    Context context;
    TinyDB tinydb;
    public getRemoteConfig(Context context) {
        this.context = context;
        tinydb = new TinyDB(context);
    }

    public void getRemotePara(){
        final FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();
        long cacheExpiration = 0;
        mRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Make the values available to your app
                            mRemoteConfig.activateFetched();
                            //get value from remote config
                            //Log.e(TAG, "getPara:  " + getPara);
                            Log.e(TAG,"Run remote config Start");
                            String getPara = mRemoteConfig.getString("show_floating_button");
                            tinydb.putString("show_floating_button", getPara);
                            String rt_dividend_value = mRemoteConfig.getString("rt_dividend_value");
                            tinydb.putString("rt_dividend_value", rt_dividend_value);
                            String rt_PERatio_value = mRemoteConfig.getString("rt_PERatio_value");
                            tinydb.putString("rt_PERatio_value", rt_PERatio_value);
                            String taishi_count_value = mRemoteConfig.getString("taishi_count_value");
                            tinydb.putString("taishi_count_value", taishi_count_value);
                            String taishi_avg_day_value = mRemoteConfig.getString("taishi_avg_day_value");
                            tinydb.putString("taishi_avg_day_value", taishi_avg_day_value);
                            String suggest_value = mRemoteConfig.getString("suggest_value");
                            tinydb.putString("suggest_value", suggest_value);
                            String suprise = mRemoteConfig.getString("suprise");
                            tinydb.putString("suprise", suprise);
                            String suprise_value = mRemoteConfig.getString("suprise_value");
                            tinydb.putString("suprise_value", suprise_value);
                            Log.e(TAG,"Run Success for remote config");
                        }

                    }
                });
    }
}
