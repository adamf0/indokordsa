package com.app.indokordsa.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.indokordsa.databinding.ItemRowNotificationBinding;
import com.app.indokordsa.interfaces.Notificationlistener;
import com.app.indokordsa.view.model.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    public ArrayList<Notification> list_notification;
    private ItemRowNotificationBinding binding;
    public Notificationlistener notificationlistener;

    public NotificationAdapter(Notificationlistener notificationlistener) {
        this.notificationlistener = notificationlistener;
    }
    private ArrayList<Notification> getListOrder() {
        return list_notification;
    }
    public void setList(ArrayList<Notification> list_notification) {
        this.list_notification = list_notification;
    }
    public void selectItem(Notification Notification){
        notificationlistener.onSelect(Notification);
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRowNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new NotificationViewHolder(binding));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, final int position) {
        Notification model = list_notification.get(position);
        binding.setModel(model);
        binding.setAction(this);
    }

    @Override
    public int getItemCount() {
        return getListOrder().size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder{
        ItemRowNotificationBinding binding;

        NotificationViewHolder(ItemRowNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}