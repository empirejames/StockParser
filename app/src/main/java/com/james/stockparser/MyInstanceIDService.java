package com.james.stockparser;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by 101716 on 2019/3/13.
 */

public class MyInstanceIDService extends FirebaseInstanceIdService {
    String TAG = MyInstanceIDService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "onTokenRefresh : "+token);
    }
}
