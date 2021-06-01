package com.app.indokordsa.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.indokordsa.databinding.ItemRowListJobQuestionnaireBinding;
import com.app.indokordsa.interfaces.Questionnairelistener;
import com.app.indokordsa.view.model.KuesionerResultDetail2;

import java.util.ArrayList;

public class ListJobQuestionnaireAdapter extends RecyclerView.Adapter<ListJobQuestionnaireAdapter.ListJobQuestionnaireViewHolder> {
    public ArrayList<KuesionerResultDetail2> list_job;
    private ItemRowListJobQuestionnaireBinding binding;
    public Questionnairelistener listener;
    Context context;

    public ListJobQuestionnaireAdapter(Context context, Questionnairelistener listener) {
        this.listener = listener;
        this.context = context;
    }

    private ArrayList<KuesionerResultDetail2> getList() {
        return list_job;
    }

    public void setList(ArrayList<KuesionerResultDetail2> list_job) {
        this.list_job = list_job;
    }

    public void toNumber(KuesionerResultDetail2 job){
        listener.select(job);
    }

    @NonNull
    @Override
    public ListJobQuestionnaireAdapter.ListJobQuestionnaireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRowListJobQuestionnaireBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new ListJobQuestionnaireAdapter.ListJobQuestionnaireViewHolder(binding));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ListJobQuestionnaireAdapter.ListJobQuestionnaireViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        KuesionerResultDetail2 model = list_job.get(position);
        binding.setModel(model);
        binding.setAction(this);
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    static class ListJobQuestionnaireViewHolder extends RecyclerView.ViewHolder {
        ItemRowListJobQuestionnaireBinding binding;

        ListJobQuestionnaireViewHolder(ItemRowListJobQuestionnaireBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
