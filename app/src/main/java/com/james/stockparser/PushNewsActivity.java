package com.james.stockparser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by 101716 on 2019/3/12.
 */

public class PushNewsActivity extends Activity {
    String TAG = PushNewsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fcm_title = getSharedPreferences("myPreferences", MODE_PRIVATE).getString("title", "");
        String fcm_message = getSharedPreferences("myPreferences", MODE_PRIVATE).getString("message", "");
        Log.e(TAG, fcm_title + fcm_title);
        Log.e(TAG, fcm_message + fcm_message);


        if(getIntent().getExtras()!= null){
            showPopUp();
        }

    }



    public void showPopUp() {
        String title = getIntent().getExtras().getString("pushTitle");
        String message = getIntent().getExtras().getString("pushMsg");
        savePushString(title,message);
        AlertDialog.Builder d = new AlertDialog.Builder(PushNewsActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        d.setTitle(title);
        d.setMessage(message == null ? "Fail to get the message." : message);
        d.setNeutralButton(getResources().getString(R.string.close_action), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog ad = d.create();
        ad.show();
    }

    public void savePushString(String title, String message){
        SharedPreferences sharedpreferences = getApplicationContext()
                .getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("title", title);
        editor.putString("message", message);
        editor.apply();
    }
}
