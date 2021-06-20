package com.app.indokordsa.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.indokordsa.databinding.ItemRowFiturBinding;
import com.app.indokordsa.interfaces.Fiturlistener;
import com.app.indokordsa.view.model.Fitur;

import java.util.ArrayList;

public class FiturAdapter extends RecyclerView.Adapter<FiturAdapter.FiturViewHolder>{
    public ArrayList<Fitur> list_fitur;
    private ItemRowFiturBinding binding;
    public Fiturlistener fiturlistener;
    Context context;

    public FiturAdapter(Context context, Fiturlistener fiturlistener) {
        this.fiturlistener = fiturlistener;
        this.context = context;
    }
    private ArrayList<Fitur> getListOrder() {
        return list_fitur;
    }
    public void setList(ArrayList<Fitur> list_fitur) {
        this.list_fitur = list_fitur;
    }
    public void selectItem(Fitur Fitur){
        fiturlistener.onSelect(Fitur);
    }

    @NonNull
    @Override
    public FiturAdapter.FiturViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRowFiturBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new FiturViewHolder(binding));
    }

    @Override
    public void onBindViewHolder(@NonNull FiturAdapter.FiturViewHolder holder, final int position) {
        Fitur model = list_fitur.get(position);
        binding.setModel(model);
        binding.setAction(this);

        binding.imgMenuItemRowFitur.setBackground(context.getResources().getDrawable(model.getImg()));
    }

    @Override
    public int getItemCount() {
        return getListOrder().size();
    }

    static class FiturViewHolder extends RecyclerView.ViewHolder{
        ItemRowFiturBinding binding;

        FiturViewHolder(ItemRowFiturBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}