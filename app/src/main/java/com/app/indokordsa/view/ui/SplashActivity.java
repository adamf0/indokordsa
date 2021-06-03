package com.app.indokordsa.view.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.R;
import com.app.indokordsa.Receive;
import com.app.indokordsa.Sending;
import com.app.indokordsa.databinding.ActivitySplashBinding;
import com.app.indokordsa.interfaces.Splashlistener;
import com.app.indokordsa.viewmodel.SplashViewModel;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity implements Splashlistener {
    SplashViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        model = new ViewModelProvider(this).get(SplashViewModel.class);
        binding.setAction(this);

        model.loading(this);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent iReceive = new Intent(this, Receive.class);
        Intent iSending = new Intent(this, Sending.class);
//
        PendingIntent piReceive = PendingIntent.getBroadcast(this, 11, iReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piSending = PendingIntent.getBroadcast(this, 11, iSending, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.MILLISECOND, 0);
        setcalendar.set(Calendar.SECOND, 0);
        setcalendar.set(Calendar.MINUTE, 0);
        setcalendar.set(Calendar.HOUR, 0);

        assert am != null;
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), 1000, piReceive);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), 1000, piSending);
        Log.i("app-log", "init service");

    }

    @Override
    public void waitingFinish() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}