package com.app.indokordsa.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityQuestionnaireBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Questionnairelistener;
import com.app.indokordsa.record.db.DB;
import com.app.indokordsa.view.adapter.ListJobQuestionnaireAdapter;
import com.app.indokordsa.view.model.KuesionerResult;
import com.app.indokordsa.view.model.KuesionerResultDetail1;
import com.app.indokordsa.view.model.KuesionerResultDetail2;
import com.app.indokordsa.viewmodel.QuestionnaireViewModel;
import com.app.indokordsa.viewmodel.QuestionnaireViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionnaireActivity extends AppCompatActivity implements Questionnairelistener {
    QuestionnaireViewModel vmodel;
    ActivityQuestionnaireBinding binding;
    SessionManager session;
    HashMap<String, String> data_session;
    String id_kuesioner_result=null;
    DB db;

    MutableLiveData<KuesionerResult> KUESIONER= new MutableLiveData<>();
    MutableLiveData<KuesionerResultDetail1> TOPIK= new MutableLiveData<>();
    MutableLiveData<KuesionerResultDetail2> PERTANYAAN= new MutableLiveData<>();

    int _topik=0;
    int _topik_end=0;
    int _pertanyaan=0;
    int _pertanyaan_end=0;
    boolean isDetail = false;
    boolean isNew = false;
    String result;

    ListJobQuestionnaireAdapter ListJobQuestionnaireAdapter;
    ArrayList<KuesionerResultDetail2> list_data = new ArrayList<>();

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DB(this);
        session = new SessionManager(this);
        data_session = session.getSession();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_questionnaire);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            id_kuesioner_result = b.getString("id_kuesioner_result",null);
            loadData();

            binding.setAction(this);

            KUESIONER.observe(this, kuesioner -> {
                logging("75", "[id]", kuesioner.getId());
                logging("77", "kuesioner [nama]", kuesioner.getKuesioner().getNama());
                logging("78", "kuesioner [jawaban]", kuesioner.getJawaban());
                logging("79", "kuesioner [topik]", String.valueOf(kuesioner.getList_pertanyaan().size()));

                _topik = 0;
                ArrayList<KuesionerResultDetail1> _tmp_list_topik = kuesioner.getList_pertanyaan();
                _topik_end=_tmp_list_topik.size()-1;
                if(_topik!=_topik_end){
                    TOPIK.postValue(_tmp_list_topik.get(_topik));
                }
                else{
                    Toast.makeText(this, "akhir nomor pertanyaan", Toast.LENGTH_SHORT).show();
                }

                binding.setKuesioner(kuesioner);
            });

            TOPIK.observe(QuestionnaireActivity.this, topik -> {
                logging("95", "topik [no]", topik.getNo());
                logging("96", "topik [id]", topik.getId());
                logging("97", "topik [tntang]", topik.getTentang());

                if(isNew) {
                    list_data.clear();
                    isNew = false;
                }

                for(int i=0;i<topik.getDetail().size();i++){
                    KuesionerResultDetail2 pertanyaan = topik.getDetail().get(i);

                    logging("107","pertanyaan [no]", pertanyaan.getNo());
                    logging("108","pertanyaan [id]", pertanyaan.getId());
                    logging("109","pertanyaan [nama]", pertanyaan.getPertanyaan());

                    if(i==0){
                        PERTANYAAN.postValue(pertanyaan);
                        _pertanyaan = i;
                    }

                    if(isDetail) {
                        list_data.add(pertanyaan);
                    }

                    if(i==topik.getDetail().size()-1){
                        _pertanyaan_end=topik.getDetail().size()-1;
                    }
                }

                binding.setTopik(topik);

                binding.rvQuestionnaire.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                ListJobQuestionnaireAdapter = new ListJobQuestionnaireAdapter(this, this);
                ListJobQuestionnaireAdapter.setList(list_data);
                ListJobQuestionnaireAdapter.notifyDataSetChanged();
                binding.rvQuestionnaire.setAdapter(ListJobQuestionnaireAdapter);
            });

            PERTANYAAN.observe(QuestionnaireActivity.this, pertanyaan -> {
//                if(pertanyaan.getVal().equals("Rusak")){
//                    binding.rgChoiceQuestionnaire.check(binding.btnChoice1Questionnaire.getId());
//                }
//                else if(pertanyaan.getVal().equals("Perlu modifikasi")){
//                    binding.rgChoiceQuestionnaire.check(binding.btnRbchoice2Questionnaire.getId());
//                }
//                else if(pertanyaan.getVal().equals("Perlu ganti segera")){
//                    binding.rgChoiceQuestionnaire.check(binding.btnRbchoice3Questionnaire.getId());
//                }
//                else if(pertanyaan.getVal().equals("Control mati")){
//                    binding.rgChoiceQuestionnaire.check(binding.btnRbchoice4Questionnaire.getId());
//                }
//                else if(pertanyaan.getVal().equals("CB trip")){
//                    binding.rgChoiceQuestionnaire.check(binding.btnRbchoice5Questionnaire.getId());
//                }
//                else if(pertanyaan.getVal().equals("Fault")){
//                    binding.rgChoiceQuestionnaire.check(binding.btnRbchoice6Questionnaire.getId());
//                }
//                else if(pertanyaan.getVal().equals("Perlu di kalibrasi")){
//                    binding.rgChoiceQuestionnaire.check(binding.btnRbchoice7Questionnaire.getId());
//                }

                for (int i=0;i<list_data.size();i++) {
                    KuesionerResultDetail2 tmp = list_data.get(i);
                    logging("137","PERTANYAAN [id]", String.format("%s=%s",pertanyaan.getId(),tmp.getId()));

                    if(tmp.getId().equals(pertanyaan.getId())){
                        _pertanyaan = i;
                        binding.setPertanyaan(pertanyaan);
                        logging("142","PERTANYAAN [x]",tmp.getId());

                        list_data.get(i).setVal(pertanyaan.getVal());
                        list_data.get(i).setStart(pertanyaan.getStart());
                        list_data.get(i).setEnd(pertanyaan.getEnd());
                        list_data.get(i).setDuration(pertanyaan.getDuration());
                    }
                }
            });

            vmodel  = new ViewModelProvider(this,new QuestionnaireViewModelFactory(this,session)).get(QuestionnaireViewModel.class);
        }
    }

    public void save(){
        logging("157","topik [s]", String.valueOf(_topik));
        logging("158","topik [e]", String.valueOf(_topik_end));
        logging("159","pertanyaan [s]", String.valueOf(_pertanyaan));
        logging("160","pertanyaan [e]", String.valueOf(_pertanyaan_end));
//        Log.i("app-log topik [s]["+_topik+"]",pertanyaan.toString());
        //searc topik match -> save list_data json

        if(KUESIONER.getValue().getJawaban().equals("2")){
            try{
                ArrayList<KuesionerResultDetail1> daftar_pertanyaan = KUESIONER.getValue().getList_pertanyaan();
                JSONArray _tmp_daftar_pertanyaan = new JSONArray();
                for(int i=0;i<daftar_pertanyaan.size();i++){ //topik
                    KuesionerResultDetail1 _tmp_sub_pertanyaan = daftar_pertanyaan.get(i);

                    JSONObject _obj_topik = new JSONObject();
                    _obj_topik.put("id",_tmp_sub_pertanyaan.getId());
                    JSONArray list_sub = new JSONArray();
                    if(_topik==i){
                        for (KuesionerResultDetail2 _tmp_pertanyaan:list_data){
                            try {
                                JSONObject _obj_pertanyaan = new JSONObject();
                                _obj_pertanyaan.put("id",_tmp_pertanyaan.getId());
                                _obj_pertanyaan.put("val",_tmp_pertanyaan.getStart());
                                _obj_pertanyaan.put("start",_tmp_pertanyaan.getStart());
                                _obj_pertanyaan.put("end",_tmp_pertanyaan.getEnd());
                                _obj_pertanyaan.put("duration",_tmp_pertanyaan.getDuration());
                                list_sub.put(_obj_pertanyaan);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        for(KuesionerResultDetail2 _tmp_pertanyaan:_tmp_sub_pertanyaan.getDetail()){
                            JSONObject _obj_pertanyaan = new JSONObject();
                            _obj_pertanyaan.put("id",_tmp_pertanyaan.getId());
                            _obj_pertanyaan.put("val",_tmp_pertanyaan.getStart());
                            _obj_pertanyaan.put("start",_tmp_pertanyaan.getStart());
                            _obj_pertanyaan.put("end",_tmp_pertanyaan.getEnd());
                            _obj_pertanyaan.put("duration",_tmp_pertanyaan.getDuration());
                            list_sub.put(_obj_pertanyaan);
                        }
                    }

                    _obj_topik.put("sub",list_sub.toString());
                    _tmp_daftar_pertanyaan.put(_obj_topik);
                }
                result = _tmp_daftar_pertanyaan.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        binding.LayoutInputQuestionnaire.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);
        vmodel.updateKuesioner(KUESIONER.getValue().getId(),KUESIONER.getValue().getJawaban(),result);
    }

    @SuppressLint({"SimpleDateFormat", "LongLogTag"})
    public void loadData(){
        binding.rvQuestionnaire.setVisibility(View.GONE);
        binding.LayoutInputQuestionnaire.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            binding.LayoutInputQuestionnaire.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            KuesionerResult tmp = db.getKuesionerResult(id_kuesioner_result);
            if(tmp.getJawaban().equals("2")){
                isDetail=true;
                binding.btnBackTopicQuestionnaire.setVisibility(View.VISIBLE);
                binding.btnNextTopicQuestionnaire.setVisibility(View.VISIBLE);
                binding.btnSaveQuestionnaire.setVisibility(View.VISIBLE);

                binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
                binding.LayoutInputChoiceQuestionnaire.setVisibility(View.VISIBLE);
                binding.rvQuestionnaire.setVisibility(View.VISIBLE);
            }
            KUESIONER.postValue(tmp);
//            logging("230","[init data]", tmp.getResultDone().toString());
        }, 1000);
    }

    public void selectYN(String jawaban, KuesionerResult kuesioner){
        if(jawaban.equals("2")){
            isDetail=true;
            kuesioner.setJawaban(jawaban);
            KUESIONER.postValue(kuesioner);
            binding.btnBackTopicQuestionnaire.setVisibility(View.VISIBLE);
            binding.btnNextTopicQuestionnaire.setVisibility(View.VISIBLE);
            binding.btnSaveQuestionnaire.setVisibility(View.VISIBLE);

            binding.LayoutInputYNQuestionnaire.setVisibility(View.GONE);
            binding.LayoutInputChoiceQuestionnaire.setVisibility(View.VISIBLE);
            binding.rvQuestionnaire.setVisibility(View.VISIBLE);
        }
        else{
            kuesioner.setJawaban(jawaban);
            kuesioner.setResult("");
            KUESIONER.postValue(kuesioner);
            binding.btnBackTopicQuestionnaire.setVisibility(View.GONE);
            binding.btnNextTopicQuestionnaire.setVisibility(View.GONE);
            binding.btnSaveQuestionnaire.setVisibility(View.GONE);

            binding.LayoutInputYNQuestionnaire.setVisibility(View.VISIBLE);
            binding.LayoutInputChoiceQuestionnaire.setVisibility(View.GONE);

            binding.LayoutInputQuestionnaire.setVisibility(View.GONE);
            binding.loader.layoutLoading.setVisibility(View.VISIBLE);
            vmodel.updateKuesioner(KUESIONER.getValue().getId(),KUESIONER.getValue().getJawaban(),null);
            Toast.makeText(this, "selesai", Toast.LENGTH_SHORT).show();
//            logging("259","",binding.getKuesioner().getResultDone().toString());
        }
    }

    @SuppressLint("LongLogTag")
    public void selectChoice(String jawaban, KuesionerResultDetail2 pertanyaan){
//        if(jawaban.equals("Rusak")){
//            binding.rgChoiceQuestionnaire.check(binding.btnChoice1Questionnaire.getId());
//        }
//        else if(jawaban.equals("Perlu modifikasi")){
//            binding.rgChoiceQuestionnaire.check(binding.btnRbchoice2Questionnaire.getId());
//        }
//        else if(jawaban.equals("Perlu ganti segera")){
//            binding.rgChoiceQuestionnaire.check(binding.btnRbchoice3Questionnaire.getId());
//        }
//        else if(jawaban.equals("Control mati")){
//            binding.rgChoiceQuestionnaire.check(binding.btnRbchoice4Questionnaire.getId());
//        }
//        else if(jawaban.equals("CB trip")){
//            binding.rgChoiceQuestionnaire.check(binding.btnRbchoice5Questionnaire.getId());
//        }
//        else if(jawaban.equals("Fault")){
//            binding.rgChoiceQuestionnaire.check(binding.btnRbchoice6Questionnaire.getId());
//        }
//        else if(jawaban.equals("Perlu di kalibrasi")){
//            binding.rgChoiceQuestionnaire.check(binding.btnRbchoice7Questionnaire.getId());
//        }

        pertanyaan.setVal(jawaban);
        PERTANYAAN.postValue(pertanyaan);

        if(_topik<=_topik_end){
            if(_pertanyaan<=_pertanyaan_end){
                for (int i=0;i<list_data.size();i++){
                    KuesionerResultDetail2 tmp = list_data.get(i);
                    if(tmp.getId().equals(pertanyaan.getId())){
                        list_data.get(i).setVal(pertanyaan.getVal());
                        list_data.get(i).setStart(pertanyaan.getStart());
                        list_data.get(i).setEnd(pertanyaan.getEnd());
                        list_data.get(i).setDuration(pertanyaan.getDuration());

                        logging("278","id_pertanyaan",list_data.get(i).getId());
                        logging("279","pertanyaan",list_data.get(i).getPertanyaan());
                        logging("280","val",list_data.get(i).getVal());
                    }
                }
            }
            else{
                Toast.makeText(this, "akhir nomor sub pertanyaan", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "akhir nomor pertanyaan", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void select(KuesionerResultDetail2 item) {
        logging("296","select [id]",item.getId());
        logging("297","select [pertanyaan]",item.getPertanyaan());
        logging("298","select [val]",item.getVal());

        KuesionerResult kuesioner = db.getKuesionerResult(id_kuesioner_result);
        KuesionerResultDetail1 topik = kuesioner.getList_pertanyaan().get(_topik);
        if(topik!=null){
            for(int i=0;i<topik.getDetail().size();i++){
                KuesionerResultDetail2 pertanyaan = topik.getDetail().get(i);
                if(pertanyaan.getId().equals(item.getId())){
                    PERTANYAAN.postValue(item);
                    _pertanyaan = i;
                }
            }
        }
        else{
            Toast.makeText(this, "line 229 null", Toast.LENGTH_SHORT).show();
        }
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
    public void onSuccessPost(String message) {
        new Handler().postDelayed(() -> {
            Snackbar.make(binding.LayoutQuestionnaire,message,Snackbar.LENGTH_LONG).show();
            binding.LayoutInputQuestionnaire.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            if(KUESIONER.getValue().getJawaban().equals("2")){
                if(db.update_kuesioner_result(KUESIONER.getValue().getId(),KUESIONER.getValue().getJawaban(), result)>= 0){
                    startActivity(new Intent(this,ListQuestionnaireActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    logging("211","", String.format("berhasil update id_kuesioner_result %s",KUESIONER.getValue().getId()));
//                    update_data_kuesioner();
                }
                else{
                    logging("214","", String.format("berhasil update id_kuesioner_result %s",KUESIONER.getValue().getId()));
                }
            }
            else{
                if(db.update_kuesioner_result(KUESIONER.getValue().getId(),KUESIONER.getValue().getJawaban(),"")>= 0){
                    startActivity(new Intent(this,ListQuestionnaireActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    logging("262","", String.format("berhasil update id_kuesioner_result %s",KUESIONER.getValue().getId()));
                }
                else{
                    logging("265","", String.format("berhasil update id_kuesioner_result %s",KUESIONER.getValue().getId()));
                }
            }
        }, 1000);
    }

    @Override
    public void onFailPost(String message) {
        closeDialog(message);
    }

    public void nextTopic(){
        if(KUESIONER.getValue().getJawaban().equals("2")){
//            update_data_kuesioner();
            _topik++;
            isDetail = true;
            isNew = true;
            logging("321","next",String.format("%s=%s",_topik,_topik_end));

            if(_topik<=_topik_end){
                list_data.clear();

                KuesionerResult kuesioner = db.getKuesionerResult(id_kuesioner_result);
                KuesionerResultDetail1 topik = kuesioner.getList_pertanyaan().get(_topik);
                if(topik!=null){
                    TOPIK.postValue(topik);
                }
                else{
                    Toast.makeText(this, "line 254 null", Toast.LENGTH_SHORT).show();
                }
            }
            else{
//            Toast.makeText(this, "akhir topik", Toast.LENGTH_SHORT).show();
                _topik = _topik_end;
            }
        }
    }

    public void backTopic(){
        if(KUESIONER.getValue().getJawaban().equals("2")){
//            update_data_kuesioner();
            _topik--;
            isDetail = true;
            isNew = true;
            if(_topik>=0){
                logging("248","next",String.format("%s=%s",_topik,_topik_end));

                list_data.clear();

                KuesionerResult kuesioner = db.getKuesionerResult(id_kuesioner_result);
                KuesionerResultDetail1 topik = kuesioner.getList_pertanyaan().get(_topik);
                if(topik!=null){
                    TOPIK.postValue(topik);
                }
                else{
                    Toast.makeText(this, "line 254 null", Toast.LENGTH_SHORT).show();
                }
            }
            else{
//            Toast.makeText(this, "akhir topik", Toast.LENGTH_SHORT).show();
                _topik=0;
            }
        }
    }

    void logging(String line, String head, String value){
        Log.i(String.format("[%s] app-log %s",line,head),value);
    }

//    void update_data_kuesioner(){
//        KUESIONER.getValue().setJawaban(KUESIONER.getValue().getJawaban());
//        KUESIONER.getValue().setResult(result.toString());
//        int index=0;
//        for(KuesionerResultDetail2 tmp:KUESIONER.getValue().getList_pertanyaan().get(_topik).getDetail()){
//            KuesionerResult _tmp = KUESIONER.getValue();
//            _tmp.getList_pertanyaan().get(_topik).getDetail().get(index).setVal(list_data.get(index).getVal());
//            _tmp.getList_pertanyaan().get(_topik).getDetail().get(index).setStart(list_data.get(index).getStart());
//            _tmp.getList_pertanyaan().get(_topik).getDetail().get(index).setEnd(list_data.get(index).getEnd());
//            _tmp.getList_pertanyaan().get(_topik).getDetail().get(index).setDuration(list_data.get(index).getDuration());
//            KUESIONER.postValue(_tmp);
//            index++;
//        }
//        logging("428","",KUESIONER.getValue().getResultDone().toString());
//    }
    void closeDialog(String message){
        new Handler().postDelayed(() -> {
            Snackbar.make(binding.LayoutQuestionnaire,message,Snackbar.LENGTH_LONG).show();
            binding.LayoutInputQuestionnaire.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
        }, 1000);
    }
}