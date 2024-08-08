package com.example.memory2024;

import static android.R.drawable.ic_lock_idle_low_battery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class BatteryCheck extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra("level", 0);

        if (level < 100) {
            timerDelayRemoveDialog(
                    3000,
                    new AlertDialog.
                            Builder(context).
                            setTitle("LowButtery").
                            setIcon(ic_lock_idle_low_battery).
                            show()
            );
        }
    }

    public void timerDelayRemoveDialog(long delayMillis, final Dialog d) {
        new Handler().
                postDelayed(
                        d::dismiss,
                        delayMillis
                );
    }
}
