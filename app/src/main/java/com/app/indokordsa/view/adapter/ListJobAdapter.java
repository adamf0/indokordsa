package com.app.indokordsa.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.indokordsa.databinding.ItemRowChecklistJobBinding;
import com.app.indokordsa.interfaces.Checklistlistener;
import com.app.indokordsa.view.model.Job;

import java.util.ArrayList;
import java.util.Collections;

public class ListJobAdapter extends RecyclerView.Adapter<ListJobAdapter.ChecklistJobViewHolder> {
    public ArrayList<Job> list_job;
    private ItemRowChecklistJobBinding binding;
    public Checklistlistener checklistlistener;
    Context context;

    public ListJobAdapter(Context context, Checklistlistener checklistlistener) {
        this.checklistlistener = checklistlistener;
        this.context = context;
    }

    private ArrayList<Job> getList() {
        return list_job;
    }

    @SuppressLint("NewApi")
    public void setList(ArrayList<Job> list_job) {
        this.list_job = list_job;
    }

    public void toNumber(Job job){
        checklistlistener.loadData(job);
    }
    @NonNull
    @Override
    public ListJobAdapter.ChecklistJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRowChecklistJobBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new ListJobAdapter.ChecklistJobViewHolder(binding));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ListJobAdapter.ChecklistJobViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Job model = list_job.get(position);
        binding.setModel(model);
        binding.setAction(this);
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    static class ChecklistJobViewHolder extends RecyclerView.ViewHolder {
        ItemRowChecklistJobBinding binding;

        ChecklistJobViewHolder(ItemRowChecklistJobBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
