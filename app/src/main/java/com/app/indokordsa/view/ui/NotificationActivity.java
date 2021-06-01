package com.app.indokordsa.view.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityNotificationBinding;
import com.app.indokordsa.interfaces.Notificationlistener;
import com.app.indokordsa.view.adapter.NotificationAdapter;
import com.app.indokordsa.view.model.Notification;
import com.app.indokordsa.viewmodel.NotificationViewModel;

public class NotificationActivity extends AppCompatActivity implements Notificationlistener {
    NotificationViewModel vmodel;
    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        vmodel = new ViewModelProvider(this).get(NotificationViewModel.class);
        binding.setAction(this);

        binding.toolbarNotification.setTitle("Notifikasi");
        setSupportActionBar(binding.toolbarNotification);

        binding.rvNotification.setLayoutManager(new LinearLayoutManager(this));
        NotificationAdapter notificationAdapter = new NotificationAdapter(this);
//        if(notificationAdapter.list_fitur !=null && notificationAdapter.list_fitur.size()>0)
//            notificationAdapter.list_fitur.clear();
        notificationAdapter.setList(vmodel.list_notification);
        notificationAdapter.notifyDataSetChanged();
        binding.rvNotification.setAdapter(notificationAdapter);
    }

    @Override
    public void onSelect(Notification notification) {

    }
}