package com.app.indokordsa.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityMainBinding;
import com.app.indokordsa.helper.PermissionHelper;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ViewPagerListener;
import com.app.indokordsa.view.adapter.ViewPagerAdapter;
import com.app.indokordsa.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ViewPagerListener, ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener, PermissionHelper.PermissionListener {
    MainViewModel vmodel;
    ActivityMainBinding binding;
    ArrayList<String> list_slide = new ArrayList<>();
    int dotscount=2;
    private ImageView[] dots;
    PermissionHelper permissionHelper;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        vmodel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setAction(this);

        binding.navButtomMain.setOnNavigationItemSelectedListener(this);
        list_slide.add("slide 1");
        list_slide.add("slide 2");

        session = new SessionManager(this);
        permissionHelper = new PermissionHelper(this);
        permissionHelper.permissionListener(this);

        load_slide();
        load_greeting();
//        addShortcut();
    }

//    private void addShortcut() {
//        Intent shortcutIntent = new Intent(getApplicationContext(), SplashActivity.class);
//        shortcutIntent.setAction(Intent.ACTION_MAIN);
//
//        Intent addIntent = new Intent();
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getApplication().getResources().getString(R.string.app_name));
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher_foreground));
//        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//        addIntent.putExtra("duplicate", false);
//        getApplicationContext().sendBroadcast(addIntent);
//    }

    public void closeApp(){
        ActivityCompat.finishAffinity(this);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.navigation_profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return false;
    }

    void load_greeting(){
        HashMap<String, String> data = session.getSession();

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String jam = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("E, dd MMMM yyyy");
        String tanggal = sdf1.format(new Date());

        String greeting;
        if(Integer.parseInt(jam)>= 15 && Integer.parseInt(jam) < 18){
            greeting = "Good Afternoon";
        }
        else if(Integer.parseInt(jam) >= 18 && Integer.parseInt(jam) < 24){
            greeting = "Good Night";
        }
        else if(Integer.parseInt(jam) >= 24 && Integer.parseInt(jam) < 10){
            greeting = "Good Morning";
        }
        else {
            greeting = "Good Noon";
        }

        binding.txtSapanMain.setText(String.format("%s %s",greeting,data.get(SessionManager.KEY_Name)));
        binding.txtTanggalMain.setText(tanggal);
    }

    void load_slide(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,this,list_slide);
        binding.sliderMain.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){
            dots[i] = new ImageView(MainActivity.this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            binding.sliderdotMain.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.active_dot));
        binding.sliderMain.addOnPageChangeListener(this);
        binding.sliderMain.setCurrentItem(0);
    }

    @Override
    public void toCheckList() {
        startActivity(new Intent(this,ListCheckListActivity.class));
    }

    @Override
    public void toQuesioner() {
        startActivity(new Intent(this,ListQuestionnaireActivity.class));
    }

    @Override
    public void toTodolist() {
        startActivity(new Intent(this,ListTodolistActivity.class));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i = 0; i< dotscount; i++){
            dots[i].setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.nonactive_dot));
        }

        dots[position].setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.active_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionCheckDone() {
        Log.i("app-log","akses permission berhasil");
    }
}