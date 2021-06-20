package com.app.indokordsa.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.indokordsa.databinding.ItemRowTodolistBinding;
import com.app.indokordsa.interfaces.ListTodolistlistener;
import com.app.indokordsa.view.model.Todo;

import java.util.ArrayList;

public class ListTodolistAdapter extends RecyclerView.Adapter<ListTodolistAdapter.ListTodolistViewHolder> {
    public ArrayList<Todo> list_todo;
    private ItemRowTodolistBinding binding;
    public com.app.indokordsa.interfaces.ListTodolistlistener ListTodolistlistener;
    Context context;

    public ListTodolistAdapter(Context context, ListTodolistlistener ListTodolistlistener) {
        this.ListTodolistlistener = ListTodolistlistener;
        this.context = context;
    }

    private ArrayList<Todo> getList() {
        return list_todo;
    }

    public void setList(ArrayList<Todo> list_todo) {
        this.list_todo = list_todo;
    }

    public void deleteItem(Todo Todo) {
        ListTodolistlistener.onDelete(Todo);
    }

    public void editItem(Todo Todo) {
        ListTodolistlistener.onEdit(Todo);
    }

    @NonNull
    @Override
    public ListTodolistAdapter.ListTodolistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRowTodolistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return (new ListTodolistAdapter.ListTodolistViewHolder(binding));
    }

    @Override
    public void onBindViewHolder(@NonNull ListTodolistAdapter.ListTodolistViewHolder holder, final int position) {
        Todo model = list_todo.get(position);
        binding.setModel(model);
        binding.setAction(this);
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    static class ListTodolistViewHolder extends RecyclerView.ViewHolder {
        ItemRowTodolistBinding binding;

        ListTodolistViewHolder(ItemRowTodolistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
