package com.course.cokefacts.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.app.TaskStackBuilder;
import android.content.res.Resources;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateFormat;

import com.course.cokefacts.Data.FactsContentProvider;
import com.course.cokefacts.Data.FactsContract;
import com.course.cokefacts.UI.FactsActivity;
import com.course.cokefacts.R;

/**
 * Service to insert new facts to the DB
 */
public class FactsIntentService extends IntentService{

    final int NOTIFICATION_ID = 1;
    final static int FACTS_COUNT = 5;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FactsIntentService() {
        super("FactsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ContentValues values = new ContentValues();
        Resources res = getResources();
        String factText;
        getContentResolver().delete(FactsContentProvider.CONTENT_URI, null, null); //delete old facts
        for(int i=0; i<FACTS_COUNT; i++) {
            factText = String.format(res.getString(R.string.new_fact), i+1, DateFormat.format("dd-MM-yyyy hh:mm:ss",new java.util.Date()).toString());
            values.put(FactsContract.FactsEntry.COLUMN_FACT, factText);
            getContentResolver().insert(FactsContentProvider.CONTENT_URI, values);//insert new facts
        }

        //Send notification
        sendNotification();
    }

    public PendingIntent createPendingIntent(){
        //Add notification action
        Intent resultIntent = new Intent(this, FactsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        stackBuilder.addParentStack(FactsActivity.class);
        PendingIntent pi = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    public void sendNotification(){
        Notification n = new NotificationCompat.Builder(this)
                .setContentTitle("New coke facts")
                .setContentText("5 new facts were added to cokeApp")
                .setSmallIcon(R.drawable.coke_icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setContentIntent(createPendingIntent())
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, n);
    }
}
