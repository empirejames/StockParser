package com.james.stockparser.Unit;

import android.util.Log;

/**
 * Created by 101716 on 2017/8/15.
 */

public class checkDoubleClick {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        String TAG = checkDoubleClick.class.getSimpleName();
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
