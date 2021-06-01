package com.app.indokordsa.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.indokordsa.databinding.ItemRowListChecklistBinding;
import com.app.indokordsa.interfaces.ListChecklistlistener;
import com.app.indokordsa.view.model.CheckList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListChecklistAdapter extends RecyclerView.Adapter<ListChecklistAdapter.ListChecklistViewHolder> {
    public ArrayList<CheckList> list_checkList;
    private ItemRowListChecklistBinding binding;
    public ListChecklistlistener listchecklistlistener;
    Context context;

    public ListChecklistAdapter(Context context, ListChecklistlistener listchecklistlistener) {
        this.listchecklistlistener = listchecklistlistener;
        this.context = context;
    }

    private ArrayList<CheckList> getList() {
        return list_checkList;
    }

    public void setList(ArrayList<CheckList> list_checkList) {
        this.list_checkList = list_checkList;
    }

    public void selectItem(CheckList checkList, boolean isFinishTask) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
        String now = sdf.format(new Date());
        if(checkList.getTanggal().equals(now)){
           if(!isFinishTask){
               listchecklistlistener.onSelect(checkList);
           }
        }
//        if(!isFinishTask)
//            listchecklistlistener.onSelect(checkList);
    }
    public void updateItem(CheckList checkList) {
        listchecklistlistener.onUpdate(checkList);
    }

    @NonNull
    @Override
    public ListChecklistAdapter.ListChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRowListChecklistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new ListChecklistAdapter.ListChecklistViewHolder(binding));
    }

    @SuppressLint({"UseCompatLoadingForDrawables","SimpleDateFormat","RecyclerView"})
    @Override
    public void onBindViewHolder(@NonNull ListChecklistAdapter.ListChecklistViewHolder holder, final int position) {
        CheckList model = list_checkList.get(position);
        binding.setModel(model);
        binding.setAction(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
        String now = sdf.format(new Date());

        if(!model.getTanggal().equals(now)){
            if(!model.getTotalTugasSelesai().equals(model.getTotalTugas())){
                if(model.getAlasan().equals("0")){
                    binding.layoutAlasanItemRowListChecklist.setVisibility(View.VISIBLE);
                }
                else{
                    binding.layoutAlasanItemRowListChecklist.setVisibility(View.GONE);
                }
            }
        }
        else{
            binding.layoutAlasanItemRowListChecklist.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    static class ListChecklistViewHolder extends RecyclerView.ViewHolder {
        ItemRowListChecklistBinding binding;

        ListChecklistViewHolder(ItemRowListChecklistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
