package com.course.cokefacts.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.course.cokefacts.R;
import com.course.cokefacts.Services.AlarmReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button show_btn = ((Button)this.findViewById(R.id.show_facts_btn));
        show_btn.setOnClickListener(this);

        //Set alarm that be called every day
        setAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * manage the clicks
     * @param view
     */
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.show_facts_btn: /** Start a new Activity FactsActivity */
                Intent intent = new Intent(this, FactsActivity.class);
                startActivity(intent);
                Log.i("MainActivity","Open FactsActivity");
                break;
        }
    }

    /**
     * Set alarm that be called every day
     */
    public void setAlarm(){
        Context cnx = getBaseContext();

        //Create calander
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);

        Intent intent = new Intent(cnx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cnx, 0, intent, 0);
        AlarmManager am = (AlarmManager) cnx.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        AlarmManager.INTERVAL_DAY, pendingIntent);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pendingIntent);
    }
}
