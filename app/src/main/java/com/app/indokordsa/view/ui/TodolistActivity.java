package com.app.indokordsa.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityTodolistBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Todolistlistener;
import com.app.indokordsa.view.model.Todo;
import com.app.indokordsa.view.model.TodoArea;
import com.app.indokordsa.view.model.TodoGroup;
import com.app.indokordsa.view.model.TodoPIC;
import com.app.indokordsa.view.model.TodoShift;
import com.app.indokordsa.view.model.TodoStatus;
import com.app.indokordsa.viewmodel.TodoViewModel;
import com.app.indokordsa.viewmodel.TodoViewModelFactory;
import com.google.android.material.snackbar.Snackbar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TodolistActivity extends AppCompatActivity implements Todolistlistener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    TodoViewModel vmodel;
    ActivityTodolistBinding binding;
    SessionManager session;
    HashMap<String, String> data_session;

    List<TodoArea> list_area = new ArrayList<>();
    List<TodoGroup> list_group = new ArrayList<>();
    List<TodoShift> list_shift = new ArrayList<>();
    List<TodoStatus> list_status = new ArrayList<>();
    List<TodoPIC> list_pic = new ArrayList<>();

    Todo _todo;
    Todo todo;
    TodoArea area = null;
    TodoGroup group = null;
    TodoShift shift = null;
    TodoStatus status = null;
    TodoPIC pic = null;
    boolean isUpdate;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if(b!=null) {
            _todo = b.getParcelable("todo");
            isUpdate = b.getBoolean("isUpdate");

            binding = DataBindingUtil.setContentView(this, R.layout.activity_todolist);
            binding.setAction(this);

            if (!isUpdate) {
                todo = init();
            }
            else{
                todo = new Todo(_todo.getId(), _todo.getId_user(), _todo.getTanggal(), _todo.getArea(), _todo.getGroup(), _todo.getShift(), _todo.getTime(), _todo.getRemarks(), _todo.getAction(), _todo.getStatus(), _todo.getPic());
            }
            binding.setModel(todo);
            binding.edtTanggalTodo.setText(todo.getTanggalFormat());
            binding.edtTimeTodo.setText(todo.getTime());

            session = new SessionManager(this);
            data_session = session.getSession();
            vmodel = new ViewModelProvider(this, new TodoViewModelFactory(this, session)).get(TodoViewModel.class);

            loadData();
        }
    }

    Todo init(){
        return new Todo("", "", "", null, null, null, "", "", "", null, null);
    }

    public void pick_tgl(){
        Calendar now = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.setMinDate(now);
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    public void pick_jam() {
        calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.enableSeconds(false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    void loadData(){
        binding.scrollTodo.setVisibility(View.GONE);
        binding.layoutHeaderTodo.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);
        vmodel.loadAllParam();
    }

    public void back(){
        startActivity(new Intent(this,ListTodolistActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void save(Todo todo){
        if(!todo.isValidation()){
            Snackbar.make(binding.layoutTodo,todo.getMessage(),Snackbar.LENGTH_LONG).show();
        }
        else {
            binding.scrollTodo.setVisibility(View.GONE);
            binding.layoutHeaderTodo.setVisibility(View.GONE);
            binding.loader.layoutLoading.setVisibility(View.VISIBLE);
            vmodel.save_todolist( (!isUpdate? "":todo.getId()), data_session.get(SessionManager.KEY_ID_USER), todo.getTanggal(), String.valueOf(todo.getArea().getId()), String.valueOf(todo.getGroup().getId()), String.valueOf(todo.getShift().getId()), String.valueOf(todo.getTime()), String.valueOf(todo.getRemarks()), String.valueOf(todo.getAction()), String.valueOf(todo.getStatus().getId()), String.valueOf(todo.getPic().getId()), isUpdate);
        }
    }

    void closeDialog(String message){
        new Handler().postDelayed(() -> {
            binding.scrollTodo.setVisibility(View.VISIBLE);
            binding.layoutHeaderTodo.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
            Snackbar.make(binding.layoutTodo,message,Snackbar.LENGTH_LONG).show();
        }, 1000);
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
    public void onFailGetParam(String message) {
        closeDialog(message);
    }

    @Override
    public void onSuccessGetParam(String response) {
        new Handler().postDelayed(() -> {
            binding.scrollTodo.setVisibility(View.VISIBLE);
            binding.layoutHeaderTodo.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            try {
                JSONObject obj = new JSONObject(response);
                JSONArray Area = obj.getJSONArray("area");
                JSONArray Group = obj.getJSONArray("group");
                JSONArray Shift = obj.getJSONArray("shift");
                JSONArray Status = obj.getJSONArray("status");
                JSONArray PIC = obj.getJSONArray("pic");

                int idx_area = 0;
                list_area.add(new TodoArea(-1,"-- Select Area --"));
                for (int i=0;i<Area.length();i++){
                    JSONObject _obj = Area.getJSONObject(i);
                    list_area.add(new TodoArea(_obj.getInt("id_area"),_obj.getString("nama_area")));
                    if(todo.getArea()!=null && _obj.getInt("id_area")==todo.getArea().getId()){
                        idx_area=i+1;
                    }
                }

                int idx_group = 0;
                list_group.add(new TodoGroup(-1,"-- Select Group --"));
                for (int i=0;i<Group.length();i++){
                    JSONObject _obj = Group.getJSONObject(i);
                    list_group.add(new TodoGroup(_obj.getInt("id_group"),_obj.getString("nama_group")));
                    if(todo.getGroup()!=null && _obj.getInt("id_group")==todo.getGroup().getId()){
                        idx_group=i+1;
                    }
                }

                int idx_shift = 0;
                list_shift.add(new TodoShift(-1,"-- Select Shift --"));
                for (int i=0;i<Shift.length();i++){
                    JSONObject _obj = Shift.getJSONObject(i);
                    list_shift.add(new TodoShift(_obj.getInt("id_shift"),_obj.getString("nama_shift")));
                    if(todo.getShift()!=null && _obj.getInt("id_shift")==todo.getShift().getId()){
                        idx_shift=i+1;
                    }
                }

                int idx_status = 0;
                list_status.add(new TodoStatus(-1,"-- Select Status --"));
                for (int i=0;i<Status.length();i++){
                    JSONObject _obj = Status.getJSONObject(i);
                    list_status.add(new TodoStatus(_obj.getInt("id_status"),_obj.getString("nama_status")));
                    if(todo.getStatus()!=null && _obj.getInt("id_status")==todo.getStatus().getId()){
                        idx_status=i+1;
                    }
                }

                int idx_pic = 0;
                list_pic.add(new TodoPIC(-1,"-- Select PIC --"));
                for (int i=0;i<PIC.length();i++){
                    JSONObject _obj = PIC.getJSONObject(i);
                    list_pic.add(new TodoPIC(_obj.getInt("id_pic"),_obj.getString("nama_pic")));
                    if(todo.getPic()!=null && _obj.getInt("id_pic")==todo.getPic().getId()){
                        idx_pic=i+1;
                    }
                }

                ArrayAdapter<TodoArea> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_area);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.dropAreaTodo.setAdapter(adapter1);
                binding.dropAreaTodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        area = (TodoArea) parent.getSelectedItem();
                        todo.setArea(area);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                binding.dropAreaTodo.setSelection(idx_area,true);

                ArrayAdapter<TodoGroup> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_group);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.dropGroupTodo.setAdapter(adapter2);
                binding.dropGroupTodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        group = (TodoGroup) parent.getSelectedItem();
                        todo.setGroup(group);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                binding.dropGroupTodo.setSelection(idx_group,true);

                ArrayAdapter<TodoShift> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_shift);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.dropShiftTodo.setAdapter(adapter3);
                binding.dropShiftTodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        shift = (TodoShift) parent.getSelectedItem();
                        todo.setShift(shift);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                binding.dropShiftTodo.setSelection(idx_shift,true);

                ArrayAdapter<TodoPIC> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_pic);
                adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.dropPicTodo.setAdapter(adapter4);
                binding.dropPicTodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        pic = (TodoPIC) parent.getSelectedItem();
                        todo.setPic(pic);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                binding.dropPicTodo.setSelection(idx_pic,true);

                ArrayAdapter<TodoStatus> adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_status);
                adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.dropStatusTodo.setAdapter(adapter5);
                binding.dropStatusTodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        status = (TodoStatus) parent.getSelectedItem();
                        todo.setStatus(status);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                binding.dropStatusTodo.setSelection(idx_status,true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 1000);
    }

    @Override
    public void onSuccessPost(String message) {
        new Handler().postDelayed(() -> {
            closeDialog(message);
            startActivity(new Intent(this,ListTodolistActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        },1000);
    }

    @Override
    public void onFailPost(String message) {
        closeDialog(message);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd",new Locale("in","ID"));
        Calendar cd = Calendar.getInstance();
        cd.set(year, monthOfYear, dayOfMonth);
        String ymd_s = ymd.format(cd.getTime());

        todo.setTanggal(ymd_s);
        binding.edtTanggalTodo.setText(todo.getTanggalFormat());
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        todo.setTime(String.format("%02d:%02d",hourOfDay,minute));
        binding.edtTimeTodo.setText(todo.getTime());
    }

    void logging(String line, String head, String value){
        Log.i(String.format("[%s] app-log %s",line,head),value);
    }
}