package com.app.indokordsa.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.indokordsa.databinding.ItemRowListChecklistBinding;
import com.app.indokordsa.databinding.ItemRowQuestionnaireBinding;
import com.app.indokordsa.interfaces.ListChecklistlistener;
import com.app.indokordsa.interfaces.ListQuestionnairelistener;
import com.app.indokordsa.view.model.CheckList;
import com.app.indokordsa.view.model.KuesionerResult;

import java.util.ArrayList;

public class ListQuestionnaireAdapter extends RecyclerView.Adapter<ListQuestionnaireAdapter.ListQuestionnaireViewHolder> {
    public ArrayList<KuesionerResult> list_data;
    private ItemRowQuestionnaireBinding binding;
    public ListQuestionnairelistener listtener;
    Context context;

    public ListQuestionnaireAdapter(Context context, ListQuestionnairelistener listener) {
        this.listtener = listener;
        this.context = context;
    }

    private ArrayList<KuesionerResult> getList() {
        return list_data;
    }

    public void setList(ArrayList<KuesionerResult> list_data) {
        this.list_data = list_data;
    }

    public void selectItem(KuesionerResult kuesionerResult, boolean isFinishTask) {
        if(!isFinishTask)
            listtener.onSelect(kuesionerResult);
    }

    @NonNull
    @Override
    public ListQuestionnaireAdapter.ListQuestionnaireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRowQuestionnaireBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new ListQuestionnaireAdapter.ListQuestionnaireViewHolder(binding));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ListQuestionnaireAdapter.ListQuestionnaireViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        KuesionerResult model = list_data.get(position);
        binding.setModel(model);
        binding.setAction(this);
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    static class ListQuestionnaireViewHolder extends RecyclerView.ViewHolder {
        ItemRowQuestionnaireBinding binding;

        ListQuestionnaireViewHolder(ItemRowQuestionnaireBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
