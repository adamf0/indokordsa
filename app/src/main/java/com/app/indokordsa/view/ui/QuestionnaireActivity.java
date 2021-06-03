package com.app.indokordsa.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityQuestionnaireBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Questionnairelistener;
import com.app.indokordsa.record.db.DB;
import com.app.indokordsa.view.model.JawabanKuesioner;
import com.app.indokordsa.view.model.KuesionerResult;
import com.app.indokordsa.viewmodel.QuestionnaireViewModel;
import com.app.indokordsa.viewmodel.QuestionnaireViewModelFactory;
import com.google.android.material.snackbar.Snackbar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import static com.app.indokordsa.Util.isNetworkAvailable;

@SuppressLint({"SimpleDateFormat", "LongLogTag", "SetTextI18n"})
public class QuestionnaireActivity extends AppCompatActivity implements Questionnairelistener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    QuestionnaireViewModel vmodel;
    ActivityQuestionnaireBinding binding;
    SessionManager session;
    HashMap<String, String> data_session;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    DB db;
    int index = 0;
    int input_position=0;
    int nomor=1;
    String tmp_date="";
    String id_kuesioner_result=null;
    KuesionerResult kuestionResult = null;
    ArrayList<JawabanKuesioner> list_jawabanKuesioner = new ArrayList<>();
    ArrayList<String> list_reason = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DB(this);
        session = new SessionManager(this);
        data_session = session.getSession();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_questionnaire);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            id_kuesioner_result = b.getString("id_kuesioner_result");
            kuestionResult = db.getKuesionerResult(id_kuesioner_result,data_session.get(SessionManager.KEY_ID_USER));
            list_jawabanKuesioner = db.getListPertanyaan(id_kuesioner_result);
            logging("[78]","list_jawabanKuesioner size", String.valueOf(list_jawabanKuesioner.size()));

            binding.setAction(this);
            binding.setKuestionResult(kuestionResult);
            vmodel  = new ViewModelProvider(this,new QuestionnaireViewModelFactory(this,session)).get(QuestionnaireViewModel.class);

            list_reason.add("-- Select Value --");
            list_reason.add("Rusak");
            list_reason.add("Perlu Modifikasi");
            list_reason.add("Perlu di ganti segera");
            list_reason.add("Control mati");
            list_reason.add("CB trip");
            list_reason.add("Fault");
            list_reason.add("Perlu di kalibrasi");

            initDropdown();
            loadData();
        }
    }

    void initDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_reason);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.deopdownValQuestionnaire.setAdapter(adapter);
        binding.deopdownValQuestionnaire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list_jawabanKuesioner.get(index).setVal(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.deopdownValQuestionnaire.setSelection(0,true);
    }

    public void pick_tgl(int input_position){
        Calendar now = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.setMinDate(now);
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        this.input_position = input_position;
    }

    public void pick_jam() {
        calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.enableSeconds(false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    public void loadData(){
        binding.LayoutInputQuestionnaire.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            binding.LayoutInputQuestionnaire.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            if(kuestionResult.getJawaban().equals("2")){
                binding.setJawabanKuesioner(list_jawabanKuesioner.size()>0? list_jawabanKuesioner.get(index):null);
                binding.edtStartQuestionnaire.setText(list_jawabanKuesioner.get(index).getStart());
                binding.edtEndQuestionnaire.setText(list_jawabanKuesioner.get(index).getEnd());

                binding.LayoutInputQuestionnaireFooter.setVisibility(View.VISIBLE);
                binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
                binding.LayoutInputChoiceQuestionnaire.setVisibility(View.VISIBLE);
                binding.txtNumberQuestionnaire.setText("#"+nomor);
            }
            else{
                binding.LayoutInputQuestionnaireFooter.setVisibility(View.GONE);
                binding.LayoutInputYNQuestionnaire.setVisibility(View.VISIBLE);
                binding.LayoutInputChoiceQuestionnaire.setVisibility(View.GONE);
            }
        }, 1000);
    }

    public void selectYN(String jawaban){
        if(jawaban.equals("ya")){
            kuestionResult.setJawaban("2");
            binding.setJawabanKuesioner(list_jawabanKuesioner.size()>0? list_jawabanKuesioner.get(index):null);
            initInput();

            binding.LayoutInputQuestionnaireFooter.setVisibility(View.VISIBLE);
            binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
            binding.LayoutInputChoiceQuestionnaire.setVisibility(View.VISIBLE);
            binding.txtNumberQuestionnaire.setText("#"+nomor);
        }
        else{
            kuestionResult.setJawaban("1");
            binding.setJawabanKuesioner(null);

            for (JawabanKuesioner jk:list_jawabanKuesioner){
                jk.setVal("");
                jk.setStart("");
                jk.setEnd("");
            }
            binding.LayoutInputQuestionnaireFooter.setVisibility(View.GONE);
            binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
            binding.LayoutInputChoiceQuestionnaire.setVisibility(View.GONE);

            binding.LayoutInputQuestionnaire.setVisibility(View.GONE);
            binding.loader.layoutLoading.setVisibility(View.VISIBLE);

            if(isNetworkAvailable(this)) {
                vmodel.updateKuesioner(kuestionResult.getId_kuesioner_result(), data_session.get(SessionManager.KEY_ID_USER),"1","",0);
            }
            else {
                binding.LayoutInputQuestionnaireFooter.setVisibility(View.GONE);
                binding.LayoutInputYNQuestionnaire.setVisibility(View.VISIBLE);
                binding.LayoutInputChoiceQuestionnaire.setVisibility(View.GONE);

                binding.LayoutInputQuestionnaire.setVisibility(View.GONE);
                binding.loader.layoutLoading.setVisibility(View.GONE);
                if(db.update_kuesioner_result(
                        kuestionResult.getId_kuesioner_result(),
                        kuestionResult.getId_user(),
                        kuestionResult.getShift().getId(),
                        kuestionResult.getKuesioner().getId(),
                        "1",
                        "1",
                        "",
                        "0",
                        kuestionResult.getCreated_at(),
                        updatedAt(),
                        ""
                )>=0){
                    logging("[218]","",String.format("berhasil update id_kuesioner_result=%s", kuestionResult.getId_kuesioner_result()));
                    Toast.makeText(this, "berhasil update quesioner", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,ListQuestionnaireActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                else{
                    logging("[221]","",String.format("gagal update id_kuesioner_result=%s", kuestionResult.getId_kuesioner_result()));
                    Toast.makeText(this, "gagal update quesioner", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    String updatedAt(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        sdf.setTimeZone(tz);
        return sdf.format(new Date());
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
    public void onSuccessPost(String message, int type) {
        if(type==0){
            new Handler().postDelayed(() -> {
                Snackbar.make(binding.LayoutQuestionnaire,message,Snackbar.LENGTH_LONG).show();
                binding.LayoutInputQuestionnaireFooter.setVisibility(View.GONE);
                binding.LayoutInputYNQuestionnaire.setVisibility(View.VISIBLE);
                binding.LayoutInputChoiceQuestionnaire.setVisibility(View.GONE);

                binding.LayoutInputQuestionnaire.setVisibility(View.GONE);
                binding.loader.layoutLoading.setVisibility(View.GONE);

                updateKuesionerResult(
                        kuestionResult.getId_kuesioner_result(),
                        kuestionResult.getId_user(),
                        kuestionResult.getShift().getId(),
                        kuestionResult.getKuesioner().getId(),
                        "1",
                        "1",
                        "",
                        "0",
                        kuestionResult.getCreated_at(),
                        updatedAt(),
                        "",
                        message,
                        type
                );
            }, 1000);
        }
        else{
            new Handler().postDelayed(() -> {
                Snackbar.make(binding.LayoutQuestionnaire,message,Snackbar.LENGTH_LONG).show();
                binding.LayoutInputQuestionnaireFooter.setVisibility(View.VISIBLE);
                binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
                binding.LayoutInputChoiceQuestionnaire.setVisibility(View.VISIBLE);

                binding.LayoutInputQuestionnaire.setVisibility(View.VISIBLE);
                binding.loader.layoutLoading.setVisibility(View.GONE);

                updateKuesionerResult(
                        kuestionResult.getId_kuesioner_result(),
                        kuestionResult.getId_user(),
                        kuestionResult.getShift().getId(),
                        kuestionResult.getKuesioner().getId(),
                        "2",
                        "0",
                        "",
                        "0",
                        kuestionResult.getCreated_at(),
                        updatedAt(),
                        "",
                        message,
                        type
                );

                updateJawabanPertanyaan(
                        kuestionResult.getId_kuesioner_result(),
                        list_jawabanKuesioner.get(index).getTopik().getId(),
                        list_jawabanKuesioner.get(index).getPertanyaan().getId(),
                        list_jawabanKuesioner.get(index).getVal(),
                        list_jawabanKuesioner.get(index).getStart(),
                        list_jawabanKuesioner.get(index).getEnd(),
                        0
                );
            }, 1000);
        }
    }

    void updateKuesionerResult(String id_kuesioner_result, String id_user, String id_shift, String id_kuesioner, String jawaban, String status, String alasan, String sync, String created_at, String updated_at, String deleted_at, String message, int type){
        if(db.update_kuesioner_result(
                id_kuesioner_result,
                id_user,
                id_shift,
                id_kuesioner,
                jawaban,
                status,
                alasan,
                sync,
                created_at,
                updated_at,
                deleted_at
        )>=0){
            logging("[329]","",String.format("berhasil update id_kuesioner_result=%s", kuestionResult.getId_kuesioner_result()));
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if(type==0)
                startActivity(new Intent(this,ListQuestionnaireActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        else{
            logging("[334]","",String.format("gagal update id_kuesioner_result=%s", kuestionResult.getId_kuesioner_result()));
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailPost(String message) {
        closeDialog(message);
    }

    public void nextTopic(){
        index++;
        if(index>=0 && index<list_jawabanKuesioner.size()) {
            nomor++;
            initInput();
        }
        else{
            index=list_jawabanKuesioner.size();
            Toast.makeText(this, "akhir pertanyaan", Toast.LENGTH_SHORT).show();
        }
        binding.txtNumberQuestionnaire.setText("#"+nomor);
    }

    public void backTopic(){
        index--;
        if(index>=0 && index<list_jawabanKuesioner.size()) {
            nomor--;
            initInput();
        }
        else{
            index=0;
            Toast.makeText(this, "awal pertanyaan", Toast.LENGTH_SHORT).show();
        }
        binding.txtNumberQuestionnaire.setText("#"+nomor);
    }

    //index=0 set spinner ga jalan
    void initInput(){
        binding.edtStartQuestionnaire.setText(list_jawabanKuesioner.get(index).getStart());
        binding.edtEndQuestionnaire.setText(list_jawabanKuesioner.get(index).getEnd());
        input_position = 0;
        tmp_date = "";

        binding.setJawabanKuesioner(list_jawabanKuesioner.get(index));
        int found = 0;
        for (int i=0;i<list_reason.size();i++){
            if (list_reason.get(i).toLowerCase().equals(list_jawabanKuesioner.get(index).getVal().toLowerCase())){
                found++;
                binding.deopdownValQuestionnaire.setSelection(i,true);
            }
        }
        if(found==0){
            binding.deopdownValQuestionnaire.setSelection(0,true);
        }
    }

    public void save(){
        try{
            JSONArray jsonArray = new JSONArray();
            for (int i=0;i<list_jawabanKuesioner.size();i++){
                JSONObject jsonObject = new JSONObject();
                JawabanKuesioner jk = list_jawabanKuesioner.get(i);

                jsonObject.put("id_kuesioner_detail_1",jk.getTopik().getId());
                jsonObject.put("id_kuesioner_detail_2",jk.getPertanyaan().getId());
                jsonObject.put("val",jk.getVal());
                jsonObject.put("start",jk.getStart());
                jsonObject.put("end",jk.getEnd());
                jsonArray.put(jsonObject);
            }

            binding.LayoutInputQuestionnaireFooter.setVisibility(View.GONE);
            binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
            binding.LayoutInputChoiceQuestionnaire.setVisibility(View.GONE);

            binding.LayoutInputQuestionnaire.setVisibility(View.GONE);
            binding.loader.layoutLoading.setVisibility(View.VISIBLE);

            if(list_jawabanKuesioner.get(index).isValidation()) {
                if (isNetworkAvailable(this)) {
                    vmodel.updateKuesioner(kuestionResult.getId_kuesioner_result(), data_session.get(SessionManager.KEY_ID_USER), "2", jsonArray.toString(), 1);
                } else {
                    binding.LayoutInputQuestionnaireFooter.setVisibility(View.VISIBLE);
                    binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
                    binding.LayoutInputChoiceQuestionnaire.setVisibility(View.VISIBLE);

                    binding.LayoutInputQuestionnaire.setVisibility(View.VISIBLE);
                    binding.loader.layoutLoading.setVisibility(View.GONE);
                    updateJawabanPertanyaan(
                            kuestionResult.getId_kuesioner_result(),
                            list_jawabanKuesioner.get(index).getTopik().getId(),
                            list_jawabanKuesioner.get(index).getPertanyaan().getId(),
                            list_jawabanKuesioner.get(index).getVal(),
                            list_jawabanKuesioner.get(index).getStart(),
                            list_jawabanKuesioner.get(index).getEnd(),
                            1
                    );
                }
            }
            else{
                binding.LayoutInputQuestionnaireFooter.setVisibility(View.VISIBLE);
                binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
                binding.LayoutInputChoiceQuestionnaire.setVisibility(View.VISIBLE);

                binding.LayoutInputQuestionnaire.setVisibility(View.VISIBLE);
                binding.loader.layoutLoading.setVisibility(View.GONE);

                Snackbar.make(binding.LayoutQuestionnaire,list_jawabanKuesioner.get(index).getMessage(),Snackbar.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void updateJawabanPertanyaan(String id, String id_topik, String id_pertanyaan, String val, String start, String end, int type){
        if(db.update_kuesioner_hasil(
                id,
                id_topik,
                id_pertanyaan,
                val,
                start,
                end
        )>=0){
            logging("[459]","",String.format("berhasil update kuesioner_hasil id_kuesioner_result=%s id_topik=%s id_pertanyaan=%s val=%s start=%s end=%s", kuestionResult.getId_kuesioner_result(),list_jawabanKuesioner.get(index).getTopik().getId(),list_jawabanKuesioner.get(index).getPertanyaan().getId(),val,start,end));
            if(type==1)
                Toast.makeText(this, "success update database internal", Toast.LENGTH_SHORT).show();
        }
        else{
            logging("[463]","",String.format("gagal update kuesioner_hasil id_kuesioner_result=%s id_topik=%s id_pertanyaan=%s val=%s start=%s end=%s", kuestionResult.getId_kuesioner_result(),list_jawabanKuesioner.get(index).getTopik().getId(),list_jawabanKuesioner.get(index).getPertanyaan().getId(),val,start,end));
            if(type==1)
                Toast.makeText(this, "fail update database internal", Toast.LENGTH_SHORT).show();
        }
    }

    void logging(String line, String head, String value){
        Log.i(String.format("[%s] app-log %s",line,head),value);
    }

    void closeDialog(String message){
        new Handler().postDelayed(() -> {
            Snackbar.make(binding.LayoutQuestionnaire,message,Snackbar.LENGTH_LONG).show();
            binding.LayoutInputQuestionnaire.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
        }, 1000);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd",new Locale("in","ID"));
        Calendar cd = Calendar.getInstance();
        cd.set(year, monthOfYear, dayOfMonth);
        tmp_date = ymd.format(cd.getTime());

        pick_jam();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String jam = String.format("%02d:%02d",hourOfDay,minute);
        if(input_position==1){
            String start_ = String.format("%s %s",tmp_date,jam);
            binding.edtStartQuestionnaire.setText(start_);
            list_jawabanKuesioner.get(index).setStart(start_);
        }
        else{
            String end_ = String.format("%s %s",tmp_date,jam);
            binding.edtEndQuestionnaire.setText(end_);
            list_jawabanKuesioner.get(index).setEnd(end_);
        }
    }
}