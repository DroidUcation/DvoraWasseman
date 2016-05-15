package com.course.cokefacts.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Alarm receiver to start the pushing new facts service
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, FactsIntentService.class);
        context.startService(i);
    }
}

