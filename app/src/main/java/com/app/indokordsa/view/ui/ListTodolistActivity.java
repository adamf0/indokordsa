package com.app.indokordsa.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityListTodolistBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListTodolistlistener;
import com.app.indokordsa.view.adapter.ListTodolistAdapter;
import com.app.indokordsa.view.model.Todo;
import com.app.indokordsa.view.model.TodoArea;
import com.app.indokordsa.view.model.TodoGroup;
import com.app.indokordsa.view.model.TodoPIC;
import com.app.indokordsa.view.model.TodoShift;
import com.app.indokordsa.view.model.TodoStatus;
import com.app.indokordsa.viewmodel.TodoListViewModel;
import com.app.indokordsa.viewmodel.TodoListViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListTodolistActivity extends AppCompatActivity implements ListTodolistlistener {
    TodoListViewModel vmodel;
    ActivityListTodolistBinding binding;
    ListTodolistAdapter ListTodolistAdapter;
    ArrayList<Todo> list_todo = new ArrayList<>();
    SessionManager session;
    HashMap<String, String> data_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_todolist);
        binding.setAction(this);

        session = new SessionManager(this);
        data_session = session.getSession();
        vmodel  = new ViewModelProvider(this,new TodoListViewModelFactory(this,session)).get(TodoListViewModel.class);

        binding.rvListTodolist.setLayoutManager(new LinearLayoutManager(this));
        ListTodolistAdapter = new ListTodolistAdapter(this, this);
        ListTodolistAdapter.setList(list_todo);
        ListTodolistAdapter.notifyDataSetChanged();
        binding.rvListTodolist.setAdapter(ListTodolistAdapter);
        loadData();
    }

    public void back(){
        startActivity(new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void add(){
        startActivity(
                new Intent(this,TodolistActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("isUpdate",false)
        );
    }

    void loadData(){
        binding.scrollListTodolist.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);
        vmodel.loadData();
    }

    void closeDialog(String message){
        new Handler().postDelayed(() -> {
            binding.scrollListTodolist.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
            Snackbar.make(binding.layoutListTodolist,message,Snackbar.LENGTH_LONG).show();
        }, 1000);
    }

    @Override
    public void onEdit(Todo todo) {
        startActivity(
                new Intent(this,TodolistActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("todo",todo)
                        .putExtra("isUpdate",true)
        );
    }

    @Override
    public void onDelete(Todo todo) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Notification")
                .setContentText("Are you sure you want to delete this data?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(SweetAlertDialog::cancel)
                .setConfirmClickListener(dialog -> {
                    dialog.dismissWithAnimation();

                    binding.scrollListTodolist.setVisibility(View.GONE);
                    binding.loader.layoutLoading.setVisibility(View.VISIBLE);
                    vmodel.deleteData(todo.getId());
                })
                .show();
    }

    @Override
    public void onFail(String message) {
        closeDialog(message);
    }

    @Override
    public void onError(String message) {
        closeDialog(message);
    }

    @Override
    public void onFailGet(String message) {
        closeDialog(message);
    }

    @Override
    public void onSuccessGet(String response) {
        new Handler().postDelayed(() -> {
            binding.scrollListTodolist.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            try {
                Log.i("app-log [list_todo]",response);
                JSONArray data = new JSONArray(response);
                for (int i=0;i<data.length();i++){
                    JSONObject obj  = data.getJSONObject(i);
                    String id       = obj.getString("id");
                    Log.i("app-log [list_todo]",id);
                    String id_user  = obj.getString("id_user");
                    Log.i("app-log [list_todo]",id_user);
                    String tanggal  = obj.getString("tanggal");
                    Log.i("app-log [list_todo]",tanggal);

                    TodoArea Area;
                    if(obj.isNull("area")){
                        Area = new TodoArea(-1,"");
                    }
                    else{
                        JSONObject area = obj.getJSONObject("area");
                        Area = new TodoArea(area.getInt("id"),area.getString("nama"));
                        Log.i("app-log [list_todo]",area.getString("nama"));
                    }

                    TodoGroup Group;
                    if(obj.isNull("group")){
                        Group = new TodoGroup(-1, "");
                    }
                    else{
                        JSONObject group = obj.getJSONObject("group");
                        Group = new TodoGroup(group.getInt("id"),group.getString("nama"));
                        Log.i("app-log [list_todo]",group.getString("nama"));
                    }

                    TodoShift Shift;
                    if(obj.isNull("shift")){
                        Shift = new TodoShift(-1,"");
                    }
                    else{
                        JSONObject shift = obj.getJSONObject("shift");
                        Shift = new TodoShift(shift.getInt("id"),shift.getString("nama"));
                        Log.i("app-log [list_todo]",shift.getString("nama"));
                    }
                    String time = obj.getString("time");
                    Log.i("app-log [list_todo]",time);
                    String remarks = obj.getString("remarks");
                    Log.i("app-log [list_todo]",remarks);
                    String action = obj.getString("action");
                    Log.i("app-log [list_todo]",action);

                    TodoStatus Status;
                    if(obj.isNull("status")){
                        Status = new TodoStatus(-1,"");
                    }
                    else{
                        JSONObject status = obj.getJSONObject("status");
                        Status = new TodoStatus(status.getInt("id"),status.getString("nama"));
                        Log.i("app-log [list_todo]",status.getString("nama"));
                    }

                    TodoPIC PIC;
                    if(obj.isNull("pic")){
                        PIC = new TodoPIC(-1,"");
                    }
                    else{
                        JSONObject pic = obj.getJSONObject("pic");
                        PIC = new TodoPIC(pic.getInt("id"),pic.getString("nama"));
                        Log.i("app-log [list_todo]",pic.getString("nama"));
                    }

                    list_todo.add(new Todo(
                            id,
                            id_user,
                            tanggal,
                            Area,
                            Group,
                            Shift,
                            time,
                            remarks,
                            action,
                            Status,
                            PIC
                    ));

                    binding.rvListTodolist.setLayoutManager(new LinearLayoutManager(this));
                    ListTodolistAdapter = new ListTodolistAdapter(this, this);
                    ListTodolistAdapter.setList(list_todo);
                    ListTodolistAdapter.notifyDataSetChanged();
                    binding.rvListTodolist.setAdapter(ListTodolistAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 1000);
    }

    @Override
    public void onFailDelete(String message) {
        closeDialog(message);
    }

    @Override
    public void onSuccessDelete(String response) {
        new Handler().postDelayed(() -> {
            binding.scrollListTodolist.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            startActivity(new Intent(this,ListTodolistActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        },1000);
    }
}