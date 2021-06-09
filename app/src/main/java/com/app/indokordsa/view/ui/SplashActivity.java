package com.app.indokordsa.view.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.R;
import com.app.indokordsa.SendingCheckList;
import com.app.indokordsa.SendingQuestionnaire;
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
//        Intent iReceive = new Intent(this, Receive.class);
        Intent iSending1 = new Intent(this, SendingCheckList.class);
        Intent iSending2 = new Intent(this, SendingQuestionnaire.class);
//
//        PendingIntent piReceive = PendingIntent.getBroadcast(this, 11, iReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piSending1 = PendingIntent.getBroadcast(this, 12, iSending1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piSending2 = PendingIntent.getBroadcast(this, 13, iSending2, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.MILLISECOND, 0);
        setcalendar.set(Calendar.SECOND, 0);
        setcalendar.set(Calendar.MINUTE, 0);
        setcalendar.set(Calendar.HOUR, 0);

        assert am != null;
//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), 1000, piReceive);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), 1000, piSending1);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), 1500, piSending2);
        Log.i("app-log", "init service");
    }

    public String versionApp(){
        PackageManager packageManager = getApplication().getPackageManager();
        String packageName = getApplication().getPackageName();
        String versionName = "Version Undefined";

        try {
            versionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "Version "+versionName;
    }
    @Override
    public void waitingFinish() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}