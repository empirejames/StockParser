package com.james.stockparser.util;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by 101716 on 2019/5/9.
 */

public class UtilFirebaseDatabase {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDataBase(){
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
