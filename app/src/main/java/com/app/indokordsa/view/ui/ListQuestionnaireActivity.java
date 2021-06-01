package com.app.indokordsa.view.ui;

import android.annotation.SuppressLint;
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
import com.app.indokordsa.databinding.ActivityListQuestionnaireBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListQuestionnairelistener;
import com.app.indokordsa.record.db.DB;
import com.app.indokordsa.view.adapter.ListQuestionnaireAdapter;
import com.app.indokordsa.view.model.Area;
import com.app.indokordsa.view.model.Kuesioner;
import com.app.indokordsa.view.model.KuesionerResult;
import com.app.indokordsa.view.model.KuesionerResultDetail1;
import com.app.indokordsa.view.model.KuesionerResultDetail2;
import com.app.indokordsa.view.model.Shift;
import com.app.indokordsa.viewmodel.ListQuestionnaireViewModel;
import com.app.indokordsa.viewmodel.ListQuestionnaireViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.app.indokordsa.Util.isNetworkAvailable;

public class ListQuestionnaireActivity extends AppCompatActivity implements ListQuestionnairelistener {
    ListQuestionnaireViewModel vmodel;
    ActivityListQuestionnaireBinding binding;
    ListQuestionnaireAdapter listQuestionnaireAdapter;
    ArrayList<KuesionerResult> list_data = new ArrayList<>();
    SessionManager session;
    HashMap<String, String> data_session;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding         = DataBindingUtil.setContentView(this, R.layout.activity_list_questionnaire);
        binding.setAction(this);

        session         = new SessionManager(this);
        db              = new DB(this);
        vmodel          = new ViewModelProvider(this,new ListQuestionnaireViewModelFactory(this,session)).get(ListQuestionnaireViewModel.class);
        data_session    = session.getSession();

        binding.rvListQuestionnaire.setLayoutManager(new LinearLayoutManager(this));
        listQuestionnaireAdapter = new ListQuestionnaireAdapter(this, this);
        listQuestionnaireAdapter.setList(list_data);
        listQuestionnaireAdapter.notifyDataSetChanged();
        binding.rvListQuestionnaire.setAdapter(listQuestionnaireAdapter);

        loadData();
    }

    public void back(){
        startActivity(new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    @SuppressLint("SimpleDateFormat")
    public void loadData(){
        binding.scrollListQuestionnaire.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);

        if(isNetworkAvailable(this)) {
            vmodel.loadData();
        }
        else{
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
            String StartDate = sdf0.format(cal.getTime());

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            Date end = c.getTime();

            if(db.countKuesionerResultByIdUser(data_session.get(SessionManager.KEY_ID_USER),StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end))>0){
                new Handler().postDelayed(() -> {
                    binding.scrollListQuestionnaire.setVisibility(View.VISIBLE);
                    binding.loader.layoutLoading.setVisibility(View.GONE);

                    list_data = db.getListKuesionerResult(data_session.get(SessionManager.KEY_ID_USER),StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end));
                    listQuestionnaireAdapter = new ListQuestionnaireAdapter(this, this);
                    listQuestionnaireAdapter.setList(list_data);
                    listQuestionnaireAdapter.notifyDataSetChanged();
                    binding.rvListQuestionnaire.setAdapter(listQuestionnaireAdapter);
                }, 1000);
            }
            else{
                onFailGet("data not found");
            }
        }
    }

    void closeDialog(String message){
        new Handler().postDelayed(() -> {
            binding.scrollListQuestionnaire.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
            Snackbar.make(binding.layoutListQuestionnaire,message,Snackbar.LENGTH_LONG).show();
        }, 1000);
    }

    @Override
    public void onSelect(KuesionerResult kuesionerResult) {
        startActivity(
                new Intent(this,QuestionnaireActivity.class)
                        .putExtra("id_kuesioner_result",kuesionerResult.getId())
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        );
    }

    @Override
    public void onFail(String message) {
        closeDialog(message);
    }

    @Override
    public void onFailGet(String message) {
        closeDialog(message);
    }

    @Override
    public void onSuccessGet(String response) {
        new Handler().postDelayed(() -> {
            binding.scrollListQuestionnaire.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            try {
                JSONArray arr  = new JSONArray(response);

                int no_kuesioer = 1;
                for(int i=0;i<arr.length();i++){
                    JSONObject data             = arr.getJSONObject(i);
                    Log.i("app-log ["+i+"]",data.toString());

                    String id                   = data.getString("id");
                    JSONObject shift            = data.getJSONObject("shift");
                    JSONObject area             = data.getJSONObject("area");
                    JSONObject pertanyaan       = data.getJSONObject("pertanyaan");
                    String jawaban              = data.getString("jawaban");

                    if(db.countShiftById(shift.getString("id"))<1) {
                        if(db.save_shift(
                                shift.getString("id"),
                                shift.getString("nama")
                        )!= -1){
                            logging(String.format("berhasil simpan id_shift=%s",shift.getString("id")));
                        }
                        else{
                            logging(String.format("gagal simpan id_shift=%s",shift.getString("id")));
                        }
                    }
                    else{
                        if(db.update_shift(
                                shift.getString("id"),
                                shift.getString("nama")
                        )>= 0){
                            logging(String.format("berhasil update id_shift=%s",shift.getString("id")));
                        }
                        else{
                            logging(String.format("gagal update id_shift=%s",shift.getString("id")));
                        }
                    }

                    if(db.countAreaById(area.getString("id"))<1) {
                        if(db.save_area(
                                area.getString("id"),
                                area.getString("nama")
                        )!= -1){
                            logging(String.format("berhasil simpan id_kategori=%s",area.getString("id")));
                        }
                        else{
                            logging(String.format("gagal simpan id_kategori=%s",area.getString("id")));
                        }
                    }
                    else{
                        if(db.update_area(
                                area.getString("id"),
                                area.getString("nama")
                        )>= 0){
                            logging(String.format("berhasil update id_area=%s",area.getString("id")));
                        }
                        else{
                            logging(String.format("gagal update id_area=%s",area.getString("id")));
                        }
                    }

                    if(db.countKuesionerById(pertanyaan.getString("id"))<1) {
                        if(db.save_kuesioner(
                                pertanyaan.getString("id"),
                                pertanyaan.getString("nama")
                        )!= -1){
                            logging(String.format("berhasil simpan id_kuesioner=%s",pertanyaan.getString("id")));
                        }
                        else{
                            logging(String.format("gagal simpan id_kuesioner=%s",pertanyaan.getString("id")));
                        }
                    }
                    else{
                        if(db.update_kuesioner(
                                pertanyaan.getString("id"),
                                pertanyaan.getString("nama")
                        )>= 0){
                            logging(String.format("berhasil update id_kuesioner=%s",pertanyaan.getString("id")));
                        }
                        else{
                            logging(String.format("gagal update id_kuesioner=%s",pertanyaan.getString("id")));
                        }
                    }

                    JSONArray data_pertanyaan                           = data.getJSONArray("data_pertanyaan");
                    ArrayList<KuesionerResultDetail1> list_pertanyaan   = new ArrayList<>();

                    int no_topik=1;
                    for (int j=0;j<data_pertanyaan.length();j++){
                        JSONObject obj                  = data_pertanyaan.getJSONObject(j);
                        Log.i("app-log [topik]["+j+"]",obj.toString());

                        String id_kuesioner_detail_1    = obj.getString("id");
                        String tentang                  = obj.getString("tentang");
                        JSONArray subpertanyaan         = obj.getJSONArray("subpertanyaan");

                        ArrayList<KuesionerResultDetail2> detail = new ArrayList<>();
                        int no_pertanyaan = 1;
                        for (int k=0;k<subpertanyaan.length();k++){
                            JSONObject obj_                 =subpertanyaan.getJSONObject(k);
                            Log.i("app-log [subpertanyaan]["+k+"]",obj_.toString());

                            String id_kuesioner_detail_2    = obj_.getString("id");
                            String pertanyaan_detail        = obj_.getString("pertanyaan");
                            String val                      = obj_.getString("val");
                            String start                    = obj_.getString("start");
                            String end                      = obj_.getString("end");
                            String duration                 = obj_.getString("duration");

                            detail.add(new KuesionerResultDetail2(
                                    String.valueOf(no_pertanyaan),
                                    id_kuesioner_detail_2,
                                    pertanyaan_detail,
                                    val,
                                    start,
                                    end,
                                    duration
                            ));
                            no_pertanyaan++;

                            if(db.countKuesionerDetail2ById(id_kuesioner_detail_2)<1) {
                                if(db.save_kuesioner_detail2(
                                        id_kuesioner_detail_2,
                                        id_kuesioner_detail_1,
                                        pertanyaan_detail
                                )!= -1){
                                    logging(String.format("berhasil simpan id_kuesioner_detail2=%s",id_kuesioner_detail_2));
                                }
                                else{
                                    logging(String.format("gagal simpan id_kuesioner_detail2=%s",id_kuesioner_detail_2));
                                }
                            }
                            else{
                                if(db.update_kuesioner_detail2(
                                        id_kuesioner_detail_2,
                                        id_kuesioner_detail_1,
                                        pertanyaan_detail
                                )>= 0){
                                    logging(String.format("berhasil update id_kuesioner_detail2=%s",id_kuesioner_detail_2));
                                }
                                else{
                                    logging(String.format("gagal update id_kuesioner_detail2=%s",id_kuesioner_detail_2));
                                }
                            }
                        }

                        list_pertanyaan.add(
                                new KuesionerResultDetail1(
                                        String.valueOf(no_topik),
                                        id_kuesioner_detail_1,
                                        tentang,
                                        detail
                                )
                        );
                        no_topik++;

                        if(db.countKuesionerDetail1ById(id_kuesioner_detail_1)<1) {
                            if(db.save_kuesioner_detail1(
                                    id_kuesioner_detail_1,
                                    pertanyaan.getString("id"),
                                    tentang
                            )!= -1){
                                logging(String.format("berhasil simpan id_kuesioner_detail1=%s",id_kuesioner_detail_1));
                            }
                            else{
                                logging(String.format("gagal simpan id_kuesioner_detail1=%s",id_kuesioner_detail_1));
                            }
                        }
                        else{
                            if(db.update_kuesioner_detail1(
                                    id_kuesioner_detail_1,
                                    pertanyaan.getString("id"),
                                    tentang
                            )>= 0){
                                logging(String.format("berhasil update id_kuesioner_detail1=%s",id_kuesioner_detail_1));
                            }
                            else{
                                logging(String.format("gagal update id_kuesioner_detail1=%s",id_kuesioner_detail_1));
                            }
                        }
                    }
                    String result               = data.getString("result");
                    String status               = data.getString("status");
                    String tanggal              = data.getString("created_at");

                    list_data.add(
                            new KuesionerResult(
                                    String.valueOf(no_kuesioer),
                                    id,
                                    new Shift(shift.getString("id"),shift.getString("nama")),
                                    new Area(area.getString("id"),area.getString("nama")),
                                    new Kuesioner(pertanyaan.getString("id"),pertanyaan.getString("nama")),
                                    jawaban,
                                    list_pertanyaan,
                                    result,
                                    status,
                                    tanggal
                            )
                    );
                    no_kuesioer++;

                    if(db.countKuesionerResultById(id)<1) {
                        if(db.save_kuesioner_result(
                                id,
                                data_session.get(SessionManager.KEY_ID_USER),
                                shift.getString("id"),
                                area.getString("id"),
                                pertanyaan.getString("id"),
                                jawaban,
                                result,
                                status,
                                tanggal
                        )!= -1){
                            logging(String.format("berhasil simpan id_kuesioner_result=%s",id));
                        }
                        else{
                            logging(String.format("gagal simpan id_kuesioner_result=%s",id));
                        }
                    }
                    else{
                        if(db.update_kuesioner_result(
                                id,
                                data_session.get(SessionManager.KEY_ID_USER),
                                shift.getString("id"),
                                area.getString("id"),
                                pertanyaan.getString("id"),
                                jawaban,
                                result,
                                status,
                                tanggal
                        )>= 0){
                            logging(String.format("berhasil update id_kuesioner_result=%s",id));
                        }
                        else{
                            logging(String.format("gagal update id_kuesioner_result=%s",id));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 1000);
    }

    void logging(String message){
        Log.i("app-log [Questionnaire]",message);
    }
}