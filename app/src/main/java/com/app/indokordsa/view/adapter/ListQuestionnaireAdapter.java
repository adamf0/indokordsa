package com.app.indokordsa.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.indokordsa.databinding.ItemRowQuestionnaireBinding;
import com.app.indokordsa.interfaces.ListQuestionnairelistener;
import com.app.indokordsa.view.model.KuesionerResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.app.indokordsa.etc.Util.reFormatDatev3;

public class ListQuestionnaireAdapter extends RecyclerView.Adapter<ListQuestionnaireAdapter.ListQuestionnaireViewHolder> {
    public ArrayList<KuesionerResult> list_data;
    private ItemRowQuestionnaireBinding binding;
    public ListQuestionnairelistener ListQuestionnairelistener;
    Context context;

    public ListQuestionnaireAdapter(Context context, ListQuestionnairelistener ListQuestionnairelistener) {
        this.ListQuestionnairelistener = ListQuestionnairelistener;
        this.context = context;
    }

    private ArrayList<KuesionerResult> getList() {
        return list_data;
    }

    public void setList(ArrayList<KuesionerResult> list_data) {
        this.list_data = list_data;
    }

    public void selectItem(KuesionerResult item, boolean isFinishTask) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());

        if(reFormatDatev3(item.getCreated_at()).equals(now)){
           if(!isFinishTask){
               ListQuestionnairelistener.onSelect(item);
           }
        }
//        if(!isFinishTask)
//            ListQuestionnairelistener.onSelect(item);
    }
    public void updateItem(KuesionerResult item) {
        ListQuestionnairelistener.onUpdate(item);
    }

    @NonNull
    @Override
    public ListQuestionnaireAdapter.ListQuestionnaireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRowQuestionnaireBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new ListQuestionnaireAdapter.ListQuestionnaireViewHolder(binding));
    }

    @Override
    public void onBindViewHolder(@NonNull ListQuestionnaireAdapter.ListQuestionnaireViewHolder holder, final int position) {
        KuesionerResult model = list_data.get(position);
        binding.setModel(model);
        binding.setAction(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());

        if(!reFormatDatev3(model.getCreated_at()).equals(now)){
            if(model.getList_kuesioner().size()!=model.getTotalPertanyaanSelesai()){
                if(model.getAlasan().equals("0") || model.getAlasan().equals("")){
                    binding.layoutInputAlasanItemRowListQuestionnaire.setVisibility(View.VISIBLE);
                    binding.layoutAlasanItemRowListQuestionnaire.setVisibility(View.GONE);
                }
                else{
                    binding.layoutInputAlasanItemRowListQuestionnaire.setVisibility(View.GONE);
                    binding.layoutAlasanItemRowListQuestionnaire.setVisibility(View.VISIBLE);
                }
            }
            else{
                binding.layoutInputAlasanItemRowListQuestionnaire.setVisibility(View.GONE);
                binding.layoutAlasanItemRowListQuestionnaire.setVisibility(View.GONE);
            }
        }
        else{
            binding.layoutInputAlasanItemRowListQuestionnaire.setVisibility(View.GONE);
            binding.layoutAlasanItemRowListQuestionnaire.setVisibility(View.GONE);
        }

        Log.i("app-log [ListQuestionnaireAdapter]",String.valueOf(model.getList_kuesioner().size()));
        Log.i("app-log [ListQuestionnaireAdapter]",String.valueOf(model.getTotalPertanyaanSelesai()));
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
