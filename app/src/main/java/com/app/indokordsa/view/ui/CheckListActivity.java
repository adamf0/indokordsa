package com.app.indokordsa.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityCheckListBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Checklistlistener;
import com.app.indokordsa.record.db.DB;
import com.app.indokordsa.view.adapter.ListJobAdapter;
import com.app.indokordsa.view.model.CheckList;
import com.app.indokordsa.view.model.Job;
import com.app.indokordsa.viewmodel.CheckListViewModel;
import com.app.indokordsa.viewmodel.CheckListViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import cn.pedant.SweetAlert.SweetAlertDialog;
import static com.app.indokordsa.Util.intersection;
import static com.app.indokordsa.Util.isNetworkAvailable;

public class CheckListActivity extends AppCompatActivity implements Checklistlistener {
    CheckListViewModel vmodel;
    ActivityCheckListBinding binding;
    ListJobAdapter ListJobAdapter;
    ArrayList<Job> list_job = new ArrayList<>();
    SessionManager session;
    HashMap<String, String> data_session;
    String id_checklist=null;
//    String nfc=null;
    DB db;
    Job job;
    String no_terakhir=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DB(this);
        job = new Job("","","","","","","");
        session = new SessionManager(this);
        data_session = session.getSession();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_list);
        binding.setAction(this);
        binding.setModel(job);
        vmodel  = new ViewModelProvider(this,new CheckListViewModelFactory(this,session)).get(CheckListViewModel.class);

        binding.rvChecklist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ListJobAdapter = new ListJobAdapter(this, this);
        ListJobAdapter.setList(list_job);
        ListJobAdapter.notifyDataSetChanged();
        binding.rvChecklist.setAdapter(ListJobAdapter);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            id_checklist = b.getString("id_checklist",null);
//            nfc = b.getString("nfc",null);
            loadData(id_checklist);
//            cek_nfc(id_checklist,nfc);
        }
    }

//    @SuppressLint("SimpleDateFormat")
//    public void cek_nfc(String id_checklist,String nfc){
//        Log.i("app-log [nfc]",nfc);
//        binding.LayoutInputChecklist.setVisibility(View.GONE);
//        binding.loader.layoutLoading.setVisibility(View.VISIBLE);
//        vmodel.cek_nfc(id_checklist,nfc);
//    }
    public void loadData(String id_checklist){
        binding.LayoutInputChecklist.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);

        if(isNetworkAvailable(this)) {
            vmodel.loadData(id_checklist);
        }
        else{
            HashMap<String, String> data = session.getSession();

            try {
                SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-01");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                String StartDate = sdf0.format(new Date());

                String dateAsString = sdf1.format(new Date());
                Date dateFromString = sdf1.parse(dateAsString);
                assert dateFromString != null;

//                Toast.makeText(this, dateAsString, Toast.LENGTH_SHORT).show();

                Calendar c = Calendar.getInstance();
                c.setTime(dateFromString);
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date end = c.getTime();

                if(db.countListCheckList(data.get(SessionManager.KEY_ID_USER),StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end))>0){
                    new Handler().postDelayed(() -> {
                        binding.LayoutInputChecklist.setVisibility(View.VISIBLE);
                        binding.loader.layoutLoading.setVisibility(View.GONE);

                        CheckList checkList = db.getCheckList(id_checklist,data.get(SessionManager.KEY_ID_USER));
//                        list_job.sort((o1, o2) -> o1.getNo().compareTo(o2.getNo()));
                        list_job.addAll(checkList.getTugas());

                        ListJobAdapter.setList(list_job);
                        ListJobAdapter.notifyDataSetChanged();
                        binding.rvChecklist.setAdapter(ListJobAdapter);

                        binding.txtNamaMesinCheckList.setText(checkList.getCek_mesin().getMesin().getNama());
                        updateInput(list_job.get(0));
                        no_terakhir = list_job.get(checkList.getTugas().size()-1).getNo();
                    }, 1000);
                }
                else{
                    onFailGet("data not found");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(Job job){
        if(!job.isValidation()){
            Snackbar.make(binding.LayoutCheklist,job.getMessage(),Snackbar.LENGTH_LONG).show();
        }
        else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Notification")
                    .setContentText("Are you sure you want to save this data?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setCancelClickListener(SweetAlertDialog::cancel)
                    .setConfirmClickListener(dialog -> {
                        dialog.dismissWithAnimation();

                        binding.LayoutInputChecklist.setVisibility(View.GONE);
                        binding.loader.layoutLoading.setVisibility(View.VISIBLE);

                        if (db.update_tugas(job.getId_penugasan(), job.getNo(), job.getCek(), job.getKat(), job.getVal(), job.getKet()) > 0) {
                            Job tmp = null;
                            for (Job old : list_job) {
                                if (old.getNo().equals(job.getNo())) {
                                    old.setVal(job.getVal());
                                    old.setKet(job.getKet());
                                    tmp = old;
                                }
                            }

                            if (isNetworkAvailable(CheckListActivity.this)) {
                                vmodel.save_checklist(id_checklist, data_session.get(SessionManager.KEY_ID_USER), Objects.requireNonNull(tmp).getNo(), tmp.getVal(), tmp.getKet(), tmp.getNo().equals(no_terakhir));
                            } else {
                                onSuccessPostAlternative("successfully save to local storage", Objects.requireNonNull(tmp).getNo().equals(no_terakhir));
                            }
                        } else {
                            onFail("failed to save to local storage");
                        }
                    })
                    .show();
        }
    }

    void closeDialog(String message){
        new Handler().postDelayed(() -> {
            binding.LayoutInputChecklist.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
            Snackbar.make(binding.LayoutCheklist,message,Snackbar.LENGTH_LONG).show();
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
    public void onFailGet(String message) {
        closeDialog(message);
    }

    @SuppressLint({"SimpleDateFormat","NewApi"})
    @Override
    public void onSuccessGet(String response) {
        new Handler().postDelayed(() -> {
            binding.LayoutInputChecklist.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            try {
                SimpleDateFormat Yformat = new SimpleDateFormat("yyyy");
                SimpleDateFormat Mformat = new SimpleDateFormat("MM");
                SimpleDateFormat normal = new SimpleDateFormat("yyyy-MM-dd");

                JSONObject obj          = new JSONObject(response);
                JSONObject supervisor   = obj.getJSONObject("supervisor");
                JSONObject operator     = obj.getJSONObject("operator");
                JSONObject cek_mesin    = obj.getJSONObject("cek_mesin");
                JSONObject mesin        = cek_mesin.getJSONObject("mesin");
                JSONObject kategori     = mesin.getJSONObject("kategori");
                JSONArray tugas         = obj.getJSONArray("tugas");

                binding.txtNamaMesinCheckList.setText(cek_mesin.getJSONObject("mesin").getString("nama"));

                ArrayList<Job> server_list_job  = new ArrayList<>();
                ArrayList<String> list_job_s    = new ArrayList<>();
                for(int j=0;j<tugas.length();j++){
                    JSONObject obj_             = tugas.getJSONObject(j);
                    server_list_job.add(
                            new Job(
                                    null,
                                    obj.getString("id"),
                                    obj_.getString("no"),
                                    obj_.getString("cek"),
                                    obj_.getString("kat"),
                                    obj_.getString("val"),
                                    obj_.getString("ket")
                            )
                    );
                    list_job_s.add(obj_.getString("no")); //A

                    if(j==tugas.length()-1){
                        no_terakhir = obj_.getString("no");
                    }
                }
                logging("[server]"+new JSONArray(list_job_s));

//                for(int a=0;a<tugas.length();a++) {
//                    JSONObject obj_ = tugas.getJSONObject(a);
//                    Job tmp = new Job(
//                            null,
//                            obj.getString("id"),
//                            obj_.getString("no"),
//                            obj_.getString("cek"),
//                            obj_.getString("kat"),
//                            obj_.getString("val"),
//                            obj_.getString("ket")
//                    );
//                    list_job.add(tmp);
//                    if(a==0){
//                        updateInput(tmp);
//                    }
//                }

                if(db.countListCheckListById(data_session.get(SessionManager.KEY_ID_USER),obj.getString("id"))<1){
                    if(db.countKategoriMesinById(kategori.getString("id"))<1) {
                        if(db.save_kategori_mesin(
                                kategori.getString("id"),
                                kategori.getString("nama")
                        )!= -1){
                            logging(String.format("berhasil simpan id_kategori=%s",kategori.getString("id")));
                        }
                        else{
                            logging(String.format("gagal simpan id_kategori=%s",kategori.getString("id")));
                        }
                    }
                    if(db.countMesinById(mesin.getString("id"))<1) {
                        if(db.save_mesin(
                                mesin.getString("id"),
                                mesin.getString("kode_nfc"),
                                mesin.getString("nama"),
                                kategori.getString("id")
                        )!= -1){
                            logging(String.format("berhasil simpan id_mesin=%s",mesin.getString("id")));
                        }
                        else{
                            logging(String.format("gagal simpan id_mesin=%s",mesin.getString("id")));
                        }
                    }
                    if(db.countCekMesinById(cek_mesin.getString("id"))<1){
                        if(db.save_cek_mesin(
                                cek_mesin.getString("id"),
                                mesin.getString("id")
                        )!= -1){
                            logging(String.format("berhasil simpan id_cek_mesin=%s",cek_mesin.getString("id")));
                        }
                        else{
                            logging(String.format("gagal simpan id_cek_mesin=%s",cek_mesin.getString("id")));
                        }
                    }
                    if(db.countUsersById(operator.getString("id"))<1){
                        if(db.save_users(
                                operator.getString("id"),
                                operator.getString("phone"),
                                operator.getString("image"),
                                operator.getString("nama"),
                                operator.getString("level")
                        )!= -1){
                            logging(String.format("berhasil simpan id_operator=%s",operator.getString("id")));
                        }
                        else{
                            logging(String.format("gagal simpan id_operator=%s",operator.getString("id")));
                        }
                    }
                    if(db.countUsersById(supervisor.getString("id"))<1){
                        if(db.save_users(
                                supervisor.getString("id"),
                                supervisor.getString("phone"),
                                supervisor.getString("image"),
                                supervisor.getString("nama"),
                                supervisor.getString("level")
                        )!= -1){
                            logging(String.format("berhasil simpan id_supervisor=%s",supervisor.getString("id")));
                        }
                        else{
                            logging(String.format("gagal simpan id_supervisor=%s",supervisor.getString("id")));
                        }
                    }
                    if(db.countPenugasanById(obj.getString("id"))<1){
                        if(db.save_penugasan(
                                obj.getString("id"),
                                supervisor.getString("id"),
                                operator.getString("id"),
                                cek_mesin.getString("id"),
                                Mformat.format(normal.parse(obj.getString("tanggal"))),
                                Yformat.format(normal.parse(obj.getString("tanggal"))),
                                obj.getString("status"),
                                obj.getString("alasan"),
                                obj.getString("sync"),
                                obj.getString("created_at"),
                                obj.getString("updated_at"),
                                obj.getString("deleted_at")
                        )!= -1){
                            logging(String.format("berhasil simpan id_penugasan=%s",obj.getString("id")));

                            for(int a=0;a<tugas.length();a++){
                                JSONObject obj_      = tugas.getJSONObject(a);

                                if(db.countTugasByIdPenugasanNo(obj.getString("id"),obj_.getString("no"))<1){
                                    if(db.save_tugas(
                                            obj.getString("id"),
                                            obj_.getString("no"),
                                            obj_.getString("cek"),
                                            obj_.getString("kat"),
                                            obj_.getString("val"),
                                            obj_.getString("ket")
                                    )!= -1){
                                        Job tmp = new Job(
                                                null,
                                                obj.getString("id"),
                                                obj_.getString("no"),
                                                obj_.getString("cek"),
                                                obj_.getString("kat"),
                                                obj_.getString("val"),
                                                obj_.getString("ket")
                                        );
                                        list_job.add(tmp);
                                        logging(String.format("berhasil simpan tugas id_penugasan=%s no=%s",obj.getString("id"),obj_.getString("no")));

                                        if(obj_.getString("no").equals("1")){
                                            updateInput(tmp);
                                        }
                                    }
                                    else{
                                        logging(String.format("gagal simpan tugas id_penugasan=%s no=%s",obj.getString("id"),obj_.getString("no")));
                                    }
                                }
                            }
                        }
                        else{
                            logging(String.format("gagal simpan id_penugasan=%s",obj.getString("id")));
                        }
                    }
                }
                else{
                    if(db.countKategoriMesinById(kategori.getString("id"))>=1) {
                        if(db.update_kategori_mesin(
                                kategori.getString("id"),
                                kategori.getString("nama")
                        )>= 0){
                            logging(String.format("berhasil update id_kategori=%s",kategori.getString("id")));
                        }
                        else{
                            logging(String.format("gagal update id_kategori=%s",kategori.getString("id")));
                        }
                    }
                    if(db.countMesinById(mesin.getString("id"))>=1) {
                        if(db.update_mesin(
                                mesin.getString("id"),
                                mesin.getString("kode_nfc"),
                                mesin.getString("nama"),
                                kategori.getString("id")
                        )>= 0){
                            logging(String.format("berhasil update id_mesin=%s",mesin.getString("id")));
                        }
                        else{
                            logging(String.format("gagal update id_mesin=%s",mesin.getString("id")));
                        }
                    }
                    if(db.countCekMesinById(cek_mesin.getString("id"))>=1){
                        if(db.update_cek_mesin(
                                cek_mesin.getString("id"),
                                mesin.getString("id")
                        )>= 0){
                            logging(String.format("berhasil update id_cek_mesin=%s",cek_mesin.getString("id")));
                        }
                        else{
                            logging(String.format("gagal update id_cek_mesin=%s",cek_mesin.getString("id")));
                        }
                    }
                    if(db.countUsersById(operator.getString("id"))>=1){
                        if(db.update_users(
                                operator.getString("id"),
                                operator.getString("phone"),
                                operator.getString("image"),
                                operator.getString("nama"),
                                operator.getString("level")
                        )>= 0){
                            logging(String.format("berhasil update id_operator=%s",operator.getString("id")));
                        }
                        else{
                            logging(String.format("gagal update id_operator=%s",operator.getString("id")));
                        }
                    }
                    if(db.countUsersById(supervisor.getString("id"))>=1){
                        if(db.update_users(
                                supervisor.getString("id"),
                                supervisor.getString("phone"),
                                supervisor.getString("image"),
                                supervisor.getString("nama"),
                                supervisor.getString("level")
                        )>= 0){
                            logging(String.format("berhasil update id_supervisor=%s",supervisor.getString("id")));
                        }
                        else{
                            logging(String.format("gagal update id_supervisor=%s",supervisor.getString("id")));
                        }
                    }
                    if(db.countPenugasanById(obj.getString("id"))>=1){
                        CheckList _tmp = db.getCheckList(obj.getString("id"),data_session.get(SessionManager.KEY_ID_USER));
                        if(db.update_penugasan(
                                _tmp.getId(),
                                _tmp.getSupervisor().getId(),
                                _tmp.getOperator().getId(),
                                _tmp.getCek_mesin().getId(),
                                Mformat.format(Objects.requireNonNull(normal.parse(_tmp.getTanggal()))),
                                Yformat.format(Objects.requireNonNull(normal.parse(_tmp.getTanggal()))),
                                (_tmp.isStatus()? "1":"0"),
                                String.valueOf(_tmp.getSync_()),
                                _tmp.getAlasan(),
                                obj.getString("created_at"),
                                obj.getString("updated_at"),
                                obj.getString("deleted_at")
                        )>= 0) {
                            logging(String.format("berhasil update id_penugasan=%s", obj.getString("id")));

                            ArrayList<String> local_list_job_s  = new ArrayList<>();
                            ArrayList<Job> local_list_job       = db.getCheckList(obj.getString("id"),data_session.get(SessionManager.KEY_ID_USER)).getTugas();
                            for (Job job:local_list_job) { //B
                                local_list_job_s.add(job.getNo());
                            }
                            logging("[local]"+new JSONArray(local_list_job));

                            List<String> inter          = new ArrayList<>();
                            ArrayList<Job> intersection = new ArrayList<>(intersection(server_list_job,local_list_job));
                            for (Job job:intersection){
                                db.update_tugas(
                                        job.getId_penugasan(),
                                        job.getNo(),
                                        job.getCek(),
                                        job.getKat(),
                                        job.getVal(),
                                        job.getKet()
                                );
                                list_job.add(job);

                                JSONObject x = new JSONObject();
                                x.put("id_penugasan",job.getId_penugasan());
                                x.put("no",job.getNo());
                                x.put("value",job.getVal());
                                x.put("ket",job.getKet());
                                inter.add(String.valueOf(x));

                                logging(String.format("berhasil update tugas id_penugasan=%s no%s",job.getId_penugasan(),job.getNo()));

                                if(job.getNo().equals("1")){
                                    updateInput(job);
                                }
                            }
                            logging("[I]"+new JSONArray(inter));

                            List<String> diff_a = list_job_s.stream()
                                    .filter(aObject -> ! ((List<String>) local_list_job_s).contains(aObject))
                                    .collect(Collectors.toList());
                            logging("[A]"+new JSONArray(diff_a));

                            for (String no:diff_a){
                                for (Job job:server_list_job){
                                    if(job.getNo().equals(no)){
                                        db.save_tugas(
                                                job.getId_penugasan(),
                                                job.getNo(),
                                                job.getCek(),
                                                job.getKat(),
                                                job.getVal(),
                                                job.getKet()
                                        );
                                        list_job.add(job);
                                        logging(String.format("berhasil tambah tugas id_penugasan=%s no%s",job.getId_penugasan(),job.getNo()));
                                    }
                                    if(job.getNo().equals("1")){
                                        updateInput(job);
                                    }
                                }
                            }

                            List<String> diff_b = local_list_job_s.stream()
                                    .filter(bObject -> ! ((List<String>) list_job_s).contains(bObject))
                                    .collect(Collectors.toList());
                            logging("[B]"+new JSONArray(diff_b));

                            for (String no:diff_b){
                                if(db.countTugasByIdPenugasanNo(obj.getString("id"),no)>=1){
                                    db.delete_tugasByIdPenugasanNo(obj.getString("id"),no);
                                    logging(String.format("berhasil hapus tugas id_penugasan=%s no%s",obj.getString("id"),no));
                                }
                            }
                        }
                        else{
                            logging(String.format("gagal update id_penugasan=%s",obj.getString("id")));
                        }
                    }
                }
            } catch (JSONException | ParseException e) {
//            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 1000);
    }

    @Override
    public void onFailPost(String message) {
        closeDialog(message);
    }

    @Override
    public void onSuccessPostAlternative(String message, boolean isEndTask) {
        if(!isEndTask) {
            closeDialog(message);
            update_sinkron();
        }
        else{
            update_sinkron();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,ListCheckListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    @Override
    public void onFailPostAlternative(String message,boolean isEndTask) {
        if(!isEndTask) {
            closeDialog(message);
        }
        else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,ListCheckListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

//    @Override
//    public void onSuccessCheck(String message) {
//        Toast.makeText(this, (message.equals("1")? "nfc valid":"nfc not valid"), Toast.LENGTH_SHORT).show();
//        binding.LayoutInputChecklist.setVisibility(View.VISIBLE);
//        binding.loader.layoutLoading.setVisibility(View.GONE);
//        loadData(id_checklist);
//    }
//
//    @Override
//    public void onFailCheck(String message) {
//        closeDialog("nfc not valid");
//    }

    @Override
    public void onSuccessPost(String message,boolean isEndTask) {
        if(!isEndTask) {
            closeDialog(message);
            update_sinkron();
        }
        else{
            update_sinkron();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,ListCheckListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    void update_sinkron(){
        CheckList checkList = db.getCheckList(id_checklist,data_session.get(SessionManager.KEY_ID_USER));
        db.update_sinkron_penugasan(checkList.getId(),"0");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void loadData(Job job) {
        updateInput(job);
    }

    void updateInput(Job job){
//        this.job.setId_penugasan(list_job.get(0).getId_penugasan());
        this.job.setId_penugasan(job.getId_penugasan());
        this.job.setNo(job.getNo());
        this.job.setCek(job.getCek());
        this.job.setKat(job.getKat());
        this.job.setVal(job.getVal());
        this.job.setKet(job.getKet());
    }
    void logging(String message){
        Log.i("app-log [CheckList]",message);
    }
}