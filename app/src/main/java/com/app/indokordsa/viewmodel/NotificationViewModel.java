package com.app.indokordsa.viewmodel;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.view.model.Notification;

import java.util.ArrayList;

public class NotificationViewModel extends ViewModel {
    public ArrayList<Notification> list_notification = new ArrayList<>();

    public NotificationViewModel(){
        list_notification.add(new Notification("1", "title 1", "description", 1, 0));
        list_notification.add(new Notification("2", "title 2", "description", 10, 0));
        list_notification.add(new Notification("3", "title 3", "description", 50, 0));
        list_notification.add(new Notification("4", "title 4", "description", 100, 1));
        list_notification.add(new Notification("5", "title 5", "description", 1000, 1));
    }
}