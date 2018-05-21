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
                            String getPara = mRemoteConfig.getString("show_floating_button");
                            Log.e(TAG, "getPara:  " + getPara);
                            tinydb.putString("show_floating_button", getPara);
                        }

                    }
                });
    }
}
