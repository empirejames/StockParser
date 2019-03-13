package com.james.stockparser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

        Log.e(TAG,"PushNewsActivity run away");
        showPopUp();
    }

    public void showPopUp() {
        String title = getIntent().getExtras().getString("pushTitle");
        String message = getIntent().getExtras().getString("pushMsg");

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
}
