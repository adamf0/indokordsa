package com.app.indokordsa.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
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
import com.app.indokordsa.view.model.JawabanKuesioner;
import com.app.indokordsa.view.model.Kuesioner;
import com.app.indokordsa.view.model.KuesionerResult;
import com.app.indokordsa.view.model.KuesionerResultDetail;
import com.app.indokordsa.view.model.Pertanyaan;
import com.app.indokordsa.view.model.Shift;
import com.app.indokordsa.view.model.Topik;
import com.app.indokordsa.viewmodel.ListQuestionnaireViewModel;
import com.app.indokordsa.viewmodel.ListQuestionnaireViewModelFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.Sets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.app.indokordsa.etc.Util.intersectionv2;
import static com.app.indokordsa.etc.Util.intersectionv3;
import static com.app.indokordsa.etc.Util.isNetworkAvailable;

@SuppressLint({"LongLogTag","SimpleDateFormat"})
public class ListQuestionnaireActivity extends AppCompatActivity implements ListQuestionnairelistener {
    ListQuestionnaireViewModel vmodel;
    ActivityListQuestionnaireBinding binding;
    ListQuestionnaireAdapter listQuestionnaireAdapter;
    ArrayList<KuesionerResult> list_task = new ArrayList<>();
    SessionManager session;
    HashMap<String, String> data_session;
    DB db;
    String StartDate;
    String EndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding         = DataBindingUtil.setContentView(this, R.layout.activity_list_questionnaire);
        binding.setAction(this);

        session         = new SessionManager(this);
        db              = new DB(this);
        vmodel          = new ViewModelProvider(this,new ListQuestionnaireViewModelFactory(this,session)).get(ListQuestionnaireViewModel.class);
        data_session    = session.getSession();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        StartDate = sdf0.format(cal.getTime());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Date end = c.getTime();
        EndDate = new SimpleDateFormat("yyyy-MM-dd").format(end);

        loadData();
    }

    public void back(){
        startActivity(new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("slide2",true));
    }

    public void loadData(){
        binding.scrollListQuestionnaire.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);

        if(isNetworkAvailable(this)) {
            vmodel.loadData();
        }
        else{
            if(db.countKuesionerResultByIdUser(data_session.get(SessionManager.KEY_ID_USER),StartDate,EndDate)>0){
                new Handler().postDelayed(() -> {
                    binding.scrollListQuestionnaire.setVisibility(View.VISIBLE);
                    binding.loader.layoutLoading.setVisibility(View.GONE);

                    ArrayList<KuesionerResult> tmp1 = new ArrayList<>(db.getTaskKuesioner(data_session.get(SessionManager.KEY_ID_USER),StartDate,EndDate));
                    for (int i=0;i<tmp1.size();i++){
                        KuesionerResult tmp2 = tmp1.get(i);
                        if(tmp2.getShift().getSekarangv2()>=tmp2.getShift().getMulaiv2() && tmp2.getShift().getSekarangv2()<tmp2.getShift().getSelesaiv2()){
                            list_task.add(tmp2);
                        }
                    }
//                    list_task.addAll(tmp1);
                    binding.rvListQuestionnaire.setLayoutManager(new LinearLayoutManager(this));
                    listQuestionnaireAdapter = new ListQuestionnaireAdapter(this, this);
                    listQuestionnaireAdapter.setList(list_task);
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
    public void onSelect(KuesionerResult item) {
        startActivity(
                new Intent(this,QuestionnaireActivity.class)
                        .putExtra("id_kuesioner_result",item.getId_kuesioner_result())
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        );
    }

    @Override
    public void onUpdate(KuesionerResult item) {
        if(!TextUtils.isEmpty(item.getAlasan()) && item.getAlasan().length()>0){
            binding.scrollListQuestionnaire.setVisibility(View.GONE);
            binding.loader.layoutLoading.setVisibility(View.VISIBLE);

            if(isNetworkAvailable(this)) {
                vmodel.update_alasan_questionnaire(item.getId_kuesioner_result(), item.getAlasan(),item);
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String updated_at = sdf.format(new Date());
                AddUpdateKuesionerResult(item.getId_kuesioner_result(),item.getId_user(),item.getShift().getId(), String.valueOf(item.getStatus()), item.getAlasan(), "0", item.getCreated_at(), updated_at, item.getDeleted_at(),true);
            }
        }
        else{
            Snackbar.make(binding.layoutListQuestionnaire,"reason cannot empty",Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFail(String message) {
        closeDialog(message);
    }

    @Override
    public void onFailGet(String message) {
        closeDialog(message);
    }

    void AddUpdateShift(JSONObject shift){
        try {
            if(db.countShiftById(shift.getString("id"))<1){
                if(db.save_shift(
                        shift.getString("id"),
                        shift.getString("name"),
                        shift.getString("mulai"),
                        shift.getString("selesai"),
                        shift.getString("ganti_hari")
                ) != -1) {
                    logging(String.format("berhasil simpan id_shift=%s", shift.getString("id")));
                }
                else{
                    logging(String.format("gagal simpan id_shift=%s", shift.getString("id")));
                }
            }
            else{
                if(db.update_shift(
                        shift.getString("id"),
                        shift.getString("name"),
                        shift.getString("mulai"),
                        shift.getString("selesai"),
                        shift.getString("ganti_hari")
                ) >= 0){
                    logging(String.format("berhasil update id_shift=%s", shift.getString("id")));
                }
                else{
                    logging(String.format("gagal update id_shift=%s", shift.getString("id")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void AddUpdateArea(JSONObject area){
        try {
            if(db.countAreaById(area.getString("id"))<1){
                if(db.save_area(area.getString("id"), area.getString("name")) != -1){
                    logging(String.format("berhasil simpan id_area=%s", area.getString("id")));
                }
                else{
                    logging(String.format("gagal simpan id_area=%s", area.getString("id")));
                }
            }
            else{
                if(db.update_area(area.getString("id"), area.getString("name"))>=0){
                    logging(String.format("berhasil update id_area=%s", area.getString("id")));
                }
                else{
                    logging(String.format("gagal update id_area=%s", area.getString("id")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void AddUpdateKuesioner(JSONObject kuesioner, Area tmp_area){
        try {
            if(db.countKuesionerById(kuesioner.getString("id_kuesioner"))<1){
                if(db.save_kuesioner(kuesioner.getString("id_kuesioner"), (tmp_area!=null? tmp_area.getId():"0"),kuesioner.getString("pertanyaan")) != -1){
                    logging(String.format("berhasil simpan id_kuesioner=%s", kuesioner.getString("id_kuesioner")));
                }
                else{
                    logging(String.format("gagal simpan id_kuesioner=%s", kuesioner.getString("id_kuesioner")));
                }
            }
            else{
                if(db.update_kuesioner(kuesioner.getString("id_kuesioner"), (tmp_area!=null? tmp_area.getId():"0"),kuesioner.getString("pertanyaan"))>=0){
                    logging(String.format("berhasil update id_kuesioner=%s", kuesioner.getString("id_kuesioner")));
                }
                else{
                    logging(String.format("gagal update id_kuesioner=%s", kuesioner.getString("id_kuesioner")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void AddUpdateTopik(JSONObject topik, String id){
        try {
            if(db.countTopikById(topik.getString("id"))<1){
                if(db.save_topik_kuesioner(topik.getString("id"), id,topik.getString("name")) !=-1){
                    logging(String.format("berhasil simpan id_topik_kuesioner=%s", topik.getString("id")));
                }
                else{
                    logging(String.format("gagal simpan id_topik_kuesioner=%s", topik.getString("id")));
                }
            }
            else{
                if(db.update_topik_kuesioner(topik.getString("id"), id,topik.getString("name"))>=0){
                    logging(String.format("berhasil simpan id_topik_kuesioner=%s", topik.getString("id")));
                }
                else{
                    logging(String.format("gagal simpan id_topik_kuesioner=%s", topik.getString("id")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void AddUpdatePertanyaan(JSONObject pertanyaan, JSONObject topik){
        try {
            if(db.countPertanyaanById(pertanyaan.getString("id"))<1){
                if(db.save_pertanyaan(pertanyaan.getString("id"), topik.getString("id"),pertanyaan.getString("name")) != -1){
                    logging(String.format("berhasil simpan id_pertanyaan_kuesioner=%s", pertanyaan.getString("id")));
                }
                else{
                    logging(String.format("gagal simpan id_pertanyaan_kuesioner=%s", pertanyaan.getString("id")));
                }
            }
            else{
                if(db.update_pertanyaan(pertanyaan.getString("id"), topik.getString("id"),pertanyaan.getString("name"))>0){
                    logging(String.format("berhasil update id_pertanyaan_kuesioner=%s", pertanyaan.getString("id")));
                }
                else{
                    logging(String.format("gagal update id_pertanyaan_kuesioner=%s", pertanyaan.getString("id")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void AddUpdateKuesionerResult(String id,String id_user,String id_shift, String status, String alasan, String sync, String created_at, String updated_at, String deleted_at,boolean isRecall){
        String message = "";
        if(db.countKuesionerResultById(id)<1){
            if(db.save_kuesioner_result(
                    id,
                    id_user,
                    id_shift,
                    String.valueOf(status),
                    alasan,
                    sync,
                    created_at,
                    updated_at,
                    deleted_at
            ) != -1){
                message = "successfully save the questionnaire";
                logging(String.format("berhasil simpan id_kuesioner_result=%s", id));
            }
            else{
                message = "failed to save the questionnaire";
                logging(String.format("gagal simpan id_kuesioner_result=%s", id));
            }
        }
        else{
            KuesionerResult tmp = db.getKuesionerResult(id,id_user);
            if(db.update_kuesioner_result(
                    id,
                    id_user,
                    id_shift,
                    String.valueOf(status),
                    alasan,
                    String.valueOf(tmp==null? "0":tmp.getSync_()),
                    created_at,
                    updated_at,
                    deleted_at
            )>=0){
                message = "successfully updated the questionnaire";
                logging(String.format("berhasil update id_kuesioner_result=%s", id));
            }
            else{
                message = "failed to update the questionnaire";
                logging(String.format("gagal update id_kuesioner_result=%s", id));
            }
        }

        if(isRecall) {
            closeDialog(message);
            startActivity(new Intent(this, ListQuestionnaireActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onSuccessGet(String response) {
        new Handler().postDelayed(() -> {
            binding.scrollListQuestionnaire.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            try {
                JSONArray arr  = new JSONArray(response);

                for(int i=0;i<arr.length();i++){
                    JSONObject data             = arr.getJSONObject(i);
                    Log.i("app-log ["+i+"]",data.toString());

                    String id                   = data.getString("id");
                    String id_user              = data.getString("id_user");
                    JSONObject shift            = data.getJSONObject("shift");
                    Shift tmp_shift             = new Shift(
                            shift.getString("id"),
                            shift.getString("name"),
                            shift.getString("mulai"),
                            shift.getString("selesai"),
                            shift.getString("ganti_hari")
                    );
                    AddUpdateShift(shift);

                    int status                  = data.getInt("status");
                    int sync                    = data.getInt("sync");
                    String alasan               = data.getString("alasan");
                    String created_at           = data.getString("created_at");
                    String updated_at           = data.getString("updated_at");
                    String deleted_at           = data.getString("deleted_at");
                    AddUpdateKuesionerResult(id,id_user,tmp_shift.getId(), String.valueOf(status),alasan, String.valueOf(sync), created_at, updated_at, deleted_at,false);

                    KuesionerResult tmp_kr = new KuesionerResult(
                            id,
                            id_user,
                            tmp_shift,
                            new ArrayList<>(),
                            status,
                            alasan,
                            sync,
                            created_at,
                            updated_at,
                            deleted_at
                    );

                    JSONArray list_kuesioner                                = data.getJSONArray("kuesioner");
                    ArrayList<KuesionerResultDetail> list_kuesioner_server  = new ArrayList<>();
                    Set<String> list_kuesioner_server_s                     = new HashSet<>();
                    for (int a=0;a<list_kuesioner.length();a++){
                        JSONObject kuesioner        = list_kuesioner.getJSONObject(a);
                        Area tmp_area               = null;
                        if(!kuesioner.isNull("area")){
                            JSONObject area = kuesioner.getJSONObject("area");
                            tmp_area = new Area(
                                    area.getString("id"),
                                    area.getString("name")
                            );
                            AddUpdateArea(area);
                        }

                        Kuesioner tmp_kuesioner     = new Kuesioner(
                                kuesioner.getString("id_kuesioner"),
                                tmp_area,
                                kuesioner.getString("pertanyaan")
                        );
                        AddUpdateKuesioner(kuesioner,tmp_area);

                        KuesionerResultDetail tmp_krd = new KuesionerResultDetail(
                                kuesioner.getString("id"),
                                tmp_kuesioner,
                                kuesioner.getString("jawaban"),
                                new ArrayList<>()
                        );

                        list_kuesioner_server.add(tmp_krd);
                        list_kuesioner_server_s.add(tmp_krd.getId());

                        ArrayList<JawabanKuesioner> list_pertanyaan_server  = new ArrayList<>();
                        Set<String> list_pertanyaan_server_s                = new HashSet<>();

                        JSONArray data_pertanyaan                           = kuesioner.getJSONArray("data_pertanyaan");
                        for(int j=0;j<data_pertanyaan.length();j++){
                            JSONObject obj = data_pertanyaan.getJSONObject(j);
                            JSONObject topik = obj.getJSONObject("topik");
                            Topik tmp_topik = new Topik(
                                    topik.getString("id"),
                                    topik.getString("name")
                            );
                            AddUpdateTopik(topik, id);

                            JSONObject pertanyaan = obj.getJSONObject("pertanyaan");
                            Pertanyaan tmp_pertanyaan = new Pertanyaan(
                                    pertanyaan.getString("id"),
                                    pertanyaan.getString("name")
                            );
                            AddUpdatePertanyaan(pertanyaan,topik);

                            String val = obj.getString("val");
                            String other = obj.getString("other");
                            String start = obj.getString("start");
                            String end = obj.getString("end");
                            String remarks = obj.getString("remarks");

                            list_pertanyaan_server.add(new JawabanKuesioner(
                                    tmp_topik,
                                    tmp_pertanyaan,
                                    val,
                                    other,
                                    start,
                                    end,
                                    remarks
                            ));
                            list_pertanyaan_server_s.add(String.format("%s-%s",tmp_topik.getId(),tmp_pertanyaan.getId()));

                        }
                        ArrayList<JawabanKuesioner> list_pertanyaan_lokal   = db.getListPertanyaan(kuesioner.getString("id"));
                        Set<String> list_pertanyaan_lokal_s                 = new HashSet<>();
                        for (JawabanKuesioner tmp_jk:list_pertanyaan_lokal){
                            list_pertanyaan_lokal_s.add(String.format("%s-%s",tmp_jk.getTopik().getId(),tmp_jk.getPertanyaan().getId()));
                        }

                        List<String> inter = new ArrayList<>();
                        ArrayList<JawabanKuesioner> intersection = new ArrayList<>(intersectionv2(list_pertanyaan_server, list_pertanyaan_lokal));
                        for (JawabanKuesioner jk : intersection) {
                            db.update_kuesioner_hasil(
                                    kuesioner.getString("id"),
                                    jk.getTopik().getId(),
                                    jk.getPertanyaan().getId(),
                                    jk.getVal(),
                                    jk.getOther(),
                                    jk.getStart(),
                                    jk.getEnd(),
                                    jk.getRemarks()
                            );
                            tmp_krd.getList_pertanyaan().add(jk);

                            JSONObject x = new JSONObject();
                            x.put("id_kuesioner_hasil", jk.getId());
                            x.put("id_topik", jk.getTopik().getId());
                            x.put("id_pertanyaan", jk.getPertanyaan().getId());
                            x.put("val", jk.getVal());
                            x.put("start", jk.getStart());
                            x.put("end", jk.getEnd());
                            inter.add(String.valueOf(x));

//                            logging(String.format("berhasil update kuesioner_hasil id_pertanyaan_kuesioner=%s", jk.getId()));
                        }
                        logging("[I][1]" + new JSONArray(inter));

                        Set<String> diff_a = Sets.difference(list_pertanyaan_server_s, list_pertanyaan_lokal_s);
                        logging("[A][1]" + new JSONArray(diff_a.toArray()));
                        for (String idtp : diff_a) {
                            String id_topik = idtp.split("-")[0];
                            String id_pertanyaan = idtp.split("-")[1];
                            for (JawabanKuesioner jk : list_pertanyaan_server) {
                                if (jk.getTopik().getId().equals(id_topik) && jk.getPertanyaan().getId().equals(id_pertanyaan)) {
                                    db.save_kuesioner_hasil(
                                            kuesioner.getString("id"),
                                            jk.getTopik().getId(),
                                            jk.getPertanyaan().getId(),
                                            jk.getVal(),
                                            jk.getOther(),
                                            jk.getStart(),
                                            jk.getEnd(),
                                            jk.getRemarks()
                                    );
                                    tmp_krd.getList_pertanyaan().add(jk);
//                                    logging(String.format("berhasil simpan kuesioner_hasil id_pertanyaan_kuesioner=%s", jk.getId()));
                                }
                            }
                        }

                        Set<String> diff_b = Sets.difference(list_pertanyaan_lokal_s, list_pertanyaan_server_s);
                        logging("[B][1]" + new JSONArray(diff_b.toArray()));

                        for (String idtp : diff_b) {
                            String id_topik = idtp.split("-")[0];
                            String id_pertanyaan = idtp.split("-")[1];
                            db.delete_KuesionerHasilById(kuesioner.getString("id"),id_topik,id_pertanyaan);
                        }
//                        logging("[list_pertanyaan] "+ tmp_krd.getList_pertanyaan().size());
                    }

                    ArrayList<KuesionerResultDetail> list_kuesioner_lokal   = db.getListKuesioner(id);
                    Set<String> list_kuesioner_lokal_s                      = new HashSet<>();
                    for (KuesionerResultDetail tmp_krd:list_kuesioner_lokal){
                        list_kuesioner_lokal_s.add(tmp_krd.getId());
                    }

                    List<String> inter = new ArrayList<>();
                    ArrayList<KuesionerResultDetail> intersection = new ArrayList<>(intersectionv3(list_kuesioner_server, list_kuesioner_lokal));
                    for (KuesionerResultDetail krd : intersection) {
                        db.update_kuesioner_result_detail(
                                krd.getId(),
                                id,
                                krd.getKuesioner().getId(),
                                krd.getJawaban()
                        );
                        tmp_kr.getList_kuesioner().add(krd);

                        JSONObject x = new JSONObject();
                        x.put("id_kuesioner_result_detail", krd.getId());
                        x.put("id_kuesioner", krd.getKuesioner().getId());
                        x.put("jawaban", krd.getJawaban());
                        inter.add(String.valueOf(x));

//                        logging(String.format("berhasil update kuesioner_result_detail id=%s", krd.getId()));
                        logging(String.format("[1] berhasil update kuesioner_result_detail %s %s %s",id,krd.getId(),krd.getKuesioner().getId() ));
                    }
                    logging("[I][0]" + new JSONArray(inter));

                    Set<String> diff_a = Sets.difference(list_kuesioner_server_s, list_kuesioner_lokal_s);
                    logging("[A][0]" + new JSONArray(diff_a.toArray()));
                    logging("[A][0][local]" + new JSONArray(list_kuesioner_lokal_s));
                    logging("[A][0][server]" + new JSONArray(list_kuesioner_server_s));
                    for (String id_kuesioner_result_detail : diff_a) {
                        for (KuesionerResultDetail krd : list_kuesioner_server) {
                            if (krd.getId().equals(id_kuesioner_result_detail)) {
                                db.save_kuesioner_result_detail(
                                        krd.getId(),
                                        id,
                                        krd.getKuesioner().getId(),
                                        krd.getJawaban()
                                );
                                tmp_kr.getList_kuesioner().add(krd);
                                logging(String.format("[1] berhasil simpan kuesioner_result_detail %s %s %s",id,krd.getId(),krd.getKuesioner().getId() ));
                            }
                        }
                    }

                    Set<String> diff_b = Sets.difference(list_kuesioner_lokal_s, list_kuesioner_server_s);
                    logging("[B][0]" + new JSONArray(diff_b.toArray()));
                    logging("[B][0][local]" + new JSONArray(list_kuesioner_lokal_s));
                    logging("[B][0][server]" + new JSONArray(list_kuesioner_server_s));
                    for (String id_kuesioner_result_detail:diff_b) {
                        db.delete_KuesionerResultDetailById(id_kuesioner_result_detail);
                        db.delete_KuesionerHasilByIdKuesionerResultDetail(id_kuesioner_result_detail);
                        logging(String.format("[1] berhasil hapus kuesioner_hasil id_kuesioner_result_detail=%s", id_kuesioner_result_detail));
                    }
                    logging("[list_pertanyaan] "+ tmp_kr.getList_kuesioner().size());

                    list_task.add(tmp_kr);
                }

                binding.rvListQuestionnaire.setLayoutManager(new LinearLayoutManager(this));
                listQuestionnaireAdapter = new ListQuestionnaireAdapter(this, this);
                listQuestionnaireAdapter.setList(list_task);
                listQuestionnaireAdapter.notifyDataSetChanged();
                binding.rvListQuestionnaire.setAdapter(listQuestionnaireAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 1000);
    }

    @Override
    public void onFailPost(String message) {
        closeDialog(message);
    }

    @Override
    public void onSuccessPost(String response, KuesionerResult item) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String updated_at = sdf.format(new Date());
        AddUpdateKuesionerResult(item.getId_kuesioner_result(),item.getId_user(),item.getShift().getId(), String.valueOf(item.getStatus()), item.getAlasan(), "0", item.getCreated_at(), updated_at, item.getDeleted_at(),true);

        db.update_sinkron_kuesioner_result(item.getId_kuesioner_result(),"0");
        closeDialog(response);
    }

    void logging(String message){
        Log.i("app-log [ListQuestionnaire]",message);
    }
}