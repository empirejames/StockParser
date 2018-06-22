package com.james.stockparser.NetWork;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 101716 on 2018/6/22.
 */

public class stockHotCount {
    String TAG = stockHotCount.class.getSimpleName();

    public Map<String,String> getHotCount(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

       final  Map<String, String> stockHotCount = new HashMap<String, String>();
        final DatabaseReference usersRef = ref.child("stockCount");

        usersRef.keepSynced(true);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                        stockHotCount.put(dsp.getKey().toString() , dsp.child("count").getValue().toString());
                        //Log.e(TAG,dsp.getKey().toString() +" :  "+ dsp.child("count").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return stockHotCount;
    }
}
