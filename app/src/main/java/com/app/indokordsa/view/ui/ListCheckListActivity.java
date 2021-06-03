package com.app.indokordsa.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityListChecklistBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListChecklistlistener;
import com.app.indokordsa.record.db.DB;
import com.app.indokordsa.view.adapter.ListChecklistAdapter;
import com.app.indokordsa.view.model.CheckList;
import com.app.indokordsa.view.model.Job;
import com.app.indokordsa.view.model.Machine;
import com.app.indokordsa.view.model.MachineCategory;
import com.app.indokordsa.view.model.MachineCheck;
import com.app.indokordsa.view.model.Operator;
import com.app.indokordsa.view.model.Supervisor;
import com.app.indokordsa.viewmodel.ListCheckListViewModel;
import com.app.indokordsa.viewmodel.ListCheckListViewModelFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.Sets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.app.indokordsa.Util.intersection;
import static com.app.indokordsa.Util.isNetworkAvailable;

@SuppressLint("SimpleDateFormat")
public class ListCheckListActivity extends AppCompatActivity implements ListChecklistlistener {
    ListCheckListViewModel vmodel;
    ActivityListChecklistBinding binding;
    ListChecklistAdapter listChecklistAdapter;
    ArrayList<CheckList> list_checkList = new ArrayList<>();
    SessionManager session;
    CheckList checkList;
    String id_checklist=null;
    String kode_nfc=null;
    CheckList tmp;
    NfcAdapter nfcAdapter;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_checklist);
        binding.setAction(this);

        session = new SessionManager(this);
        vmodel  = new ViewModelProvider(this,new ListCheckListViewModelFactory(this,session)).get(ListCheckListViewModel.class);

        binding.rvListChecklist.setLayoutManager(new LinearLayoutManager(this));
        listChecklistAdapter = new ListChecklistAdapter(this, this);
        listChecklistAdapter.setList(list_checkList);
        listChecklistAdapter.notifyDataSetChanged();
        binding.rvListChecklist.setAdapter(listChecklistAdapter);

        db = new DB(this);
        loadData();
        checkList = initModel();

        //init nfc
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Snackbar.make(binding.scrollListChecklist,"this device does not support NFC",Snackbar.LENGTH_LONG).show();
        }
        //read nfc
        readFromIntent(getIntent());
    }

    //read nfc from intent
    private void readFromIntent(Intent intent){
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++){
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    //build data nfc from intent to text
    @SuppressLint("DefaultLocale")
    private void buildTagViews(NdefMessage[] msgs){
        if (msgs == null || msgs.length == 0) return;
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;

        try {
            String text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);

            binding.scrollListChecklist.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
            binding.layoutScanNFCPage.setVisibility(View.GONE);

//            if(this.checkList==null){
//                Snackbar.make(binding.scrollListChecklist,"CheckList is null",Snackbar.LENGTH_LONG).show();
//            }
//            else if(this.checkList.getCek_mesin()==null){
//                Snackbar.make(binding.scrollListChecklist,"Cekmesin is null",Snackbar.LENGTH_LONG).show();
//            }
//            else if(this.checkList.getCek_mesin().getMesin()==null){
//                Snackbar.make(binding.scrollListChecklist,"Machine cannot selected",Snackbar.LENGTH_LONG).show();
//            }
            if(this.id_checklist==null){
                Snackbar.make(binding.scrollListChecklist,"id_checklist is null",Snackbar.LENGTH_LONG).show();
            }
            else if(this.kode_nfc==null){
                Snackbar.make(binding.scrollListChecklist,"kode_nfc is null",Snackbar.LENGTH_LONG).show();
            }
            else if(text.equals(this.kode_nfc)){
                Intent intent = new Intent(this,CheckListActivity.class);
//                intent.putExtra("id_checklist",checkList.getId());
                intent.putExtra("id_checklist",this.id_checklist);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                Snackbar.make(binding.scrollListChecklist,String.format("NFC not valid. nfc % expected %s",text,this.kode_nfc),Snackbar.LENGTH_LONG).show();
            }
        }catch (UnsupportedEncodingException e){
            Log.e("UnsupportedEncoding", e.toString());
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        //read nfc
        readFromIntent(intent);
    }

    CheckList initModel(){
        return new CheckList(null, null, null, null, null, null, false, null,0);
    }

    public void cancelScan(){
        binding.scrollListChecklist.setVisibility(View.VISIBLE);
        binding.loader.layoutLoading.setVisibility(View.GONE);
        binding.layoutScanNFCPage.setVisibility(View.GONE);

        checkList = initModel();
        id_checklist=null;
        kode_nfc=null;
//        Intent intent = new Intent(this,CheckListActivity.class);
//        intent.putExtra("id_checklist",checkList.getId());
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
    }

    public void back(){
        startActivity(new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

//    protected void onResume() {
//        super.onResume();
//
//        if (nfcAdapter != null) {
//            if (!nfcAdapter.isEnabled())
//                showWirelessSettings();
//
//            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
//        }
//    }

//    private void showWirelessSettings() {
//        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//        startActivity(intent);
//    }

//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        resolveIntent(intent);
//    }
//
//    private void resolveIntent(Intent intent) {
//        String action = intent.getAction();
//
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
//            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            NdefMessage[] msgs;
//
//            if (rawMsgs != null) {
//                msgs = new NdefMessage[rawMsgs.length];
//
//                for (int i = 0; i < rawMsgs.length; i++) {
//                    msgs[i] = (NdefMessage) rawMsgs[i];
//                }
//
//            } else {
//                byte[] empty = new byte[0];
//                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
//                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//                byte[] payload = dumpTagData(tag).getBytes();
//                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
//                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
//                msgs = new NdefMessage[] {msg};
//            }
//
//            displayMsgs(msgs);
//        }
//    }

//    private void displayMsgs(NdefMessage[] msgs) {
//        if (msgs == null || msgs.length == 0)
//            return;
//
//        StringBuilder builder = new StringBuilder();
//        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
//        final int size = records.size();
//
//        for (int i = 0; i < size; i++) {
//            ParsedNdefRecord record = records.get(i);
//            String str = record.str();
//            builder.append(str).append("\n");
//        }
//
//        binding.scrollListChecklist.setVisibility(View.VISIBLE);
//        binding.loader.layoutLoading.setVisibility(View.GONE);
//        binding.layoutScanNFCPage.setVisibility(View.GONE);
//
//        if(checkList.getCek_mesin()==null){
//            Snackbar.make(binding.scrollListChecklist,"Machine cannot selected",Snackbar.LENGTH_LONG).show();
//        }
//        else if(builder.toString().equals(checkList.getCek_mesin().getMesin().getKode_nfc())){
//            Intent intent = new Intent(this,CheckListActivity.class);
//            intent.putExtra("id_checklist",checkList.getId());
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//        else{
//            Log.i("app-log masalah",String.format("%s=%s",checkList.getCek_mesin().getMesin().getKode_nfc(),builder.toString()));
//            Intent intent = new Intent(this,CheckListActivity.class);
//            intent.putExtra("id_checklist",checkList.getId());
//            intent.putExtra("nfc",builder.toString());
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//        checkList = initModel();
//    }

    public void loadData(){
        binding.scrollListChecklist.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);
        binding.layoutScanNFCPage.setVisibility(View.GONE);

        if(isNetworkAvailable(this)) {
            vmodel.loadData();
        }
        else{
            DB db = new DB(this);
            HashMap<String, String> data = session.getSession();

            try {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1);
                SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-01");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                String StartDate = sdf0.format(cal.getTime());

                String dateAsString = sdf1.format(new Date());
                Date dateFromString = sdf1.parse(dateAsString);
                assert dateFromString != null;

                Calendar c = Calendar.getInstance();
                c.setTime(dateFromString);
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date end = c.getTime();

                if(db.countListCheckList(data.get(SessionManager.KEY_ID_USER),StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end))>0){
                    new Handler().postDelayed(() -> {
                        binding.scrollListChecklist.setVisibility(View.VISIBLE);
                        binding.loader.layoutLoading.setVisibility(View.GONE);

                        list_checkList = db.getListCheckList(data.get(SessionManager.KEY_ID_USER),StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end));
                        listChecklistAdapter.setList(list_checkList);
                        listChecklistAdapter.notifyDataSetChanged();
                        binding.rvListChecklist.setAdapter(listChecklistAdapter);
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

    void closeDialog(String message){
        new Handler().postDelayed(() -> {
            binding.scrollListChecklist.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
            binding.layoutScanNFCPage.setVisibility(View.GONE);
            Snackbar.make(binding.layoutListChecklist,message,Snackbar.LENGTH_LONG).show();
        }, 1000);
    }

    @Override
    public void onSelect(CheckList checkList, String id_checklist, String kode_nfc) {
        binding.scrollListChecklist.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.GONE);
        binding.layoutScanNFCPage.setVisibility(View.VISIBLE);
        this.id_checklist=id_checklist;
        this.kode_nfc=kode_nfc;
        this.checkList = new CheckList(
                checkList.getId(),
                checkList.getTanggal(),
                checkList.getSupervisor(),
                checkList.getOperator(),
                new MachineCheck(
                        checkList.getCek_mesin().getId(),
                        new Machine(
                                checkList.getCek_mesin().getMesin().getId(),
                                checkList.getCek_mesin().getMesin().getKode_nfc(),
                                checkList.getCek_mesin().getMesin().getNama(),
                                new MachineCategory(
                                        checkList.getCek_mesin().getMesin().getKategori().getId(),
                                        checkList.getCek_mesin().getMesin().getKategori().getNama()
                                )
                        )
                ),
                checkList.getTugas(),
                checkList.getTotalTugas().equals(checkList.getTotalTugasSelesai()),
                checkList.getAlasan(),
                0
        );
        readFromIntent(getIntent());

//        binding.scrollListChecklist.setVisibility(View.VISIBLE);
//        binding.loader.layoutLoading.setVisibility(View.GONE);
//        binding.layoutScanNFCPage.setVisibility(View.GONE);
//
//        Intent intent = new Intent(this,CheckListActivity.class);
//        intent.putExtra("id_checklist",checkList.getId());
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
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
    public void onUpdate(CheckList checkList) {
        binding.scrollListChecklist.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);
        binding.layoutScanNFCPage.setVisibility(View.GONE);

        if(isNetworkAvailable(this)) {
            vmodel.update_alasan_checklist(checkList.getId(),checkList.getAlasan());
            tmp = checkList;
            this.checkList = checkList;
        }
        else{
            update_alasan(checkList);
        }
    }

    @Override
    public void onFailGet(String message) {
        closeDialog(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onSuccessGet(String response) {
        new Handler().postDelayed(() -> {
            binding.scrollListChecklist.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            DB db = new DB(this);
            HashMap<String, String> data_session = session.getSession();

            try {
                SimpleDateFormat Yformat = new SimpleDateFormat("yyyy");
                SimpleDateFormat Mformat = new SimpleDateFormat("MM");
                SimpleDateFormat normal = new SimpleDateFormat("yyyy-MM-dd");

                JSONArray data = new JSONArray(response);
                for(int i=0;i<data.length();i++){
                    JSONObject obj          = data.getJSONObject(i);
                    JSONObject supervisor   = obj.getJSONObject("supervisor");
                    JSONObject operator     = obj.getJSONObject("operator");
                    JSONObject cek_mesin    = obj.getJSONObject("cek_mesin");
                    JSONObject mesin        = cek_mesin.getJSONObject("mesin");
                    JSONObject kategori     = mesin.getJSONObject("kategori");
                    JSONArray tugas         = obj.getJSONArray("tugas");

                    ArrayList<Job> server_list_job  = new ArrayList<>();
                    ArrayList<Job> list_job         = new ArrayList<>();
                    Set<String> list_job_s          = new HashSet<>();
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
                    }
                    logging("[server]"+new JSONArray(list_job_s));

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
                                            list_job.add(new Job(
                                                    null,
                                                    obj.getString("id"),
                                                    obj_.getString("no"),
                                                    obj_.getString("cek"),
                                                    obj_.getString("kat"),
                                                    obj_.getString("val"),
                                                    obj_.getString("ket")
                                            ));
                                            logging(String.format("berhasil simpan tugas id_penugasan=%s no=%s",obj.getString("id"),obj_.getString("no")));
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
                                    Mformat.format(normal.parse(_tmp.getTanggal())),
                                    Yformat.format(normal.parse(_tmp.getTanggal())),
                                    (_tmp.isStatus()? "1":"0"),
                                    String.valueOf(_tmp.getSync_()),
                                    _tmp.getAlasan(),
                                    obj.getString("created_at"),
                                    obj.getString("updated_at"),
                                    obj.getString("deleted_at")
                            )>= 0) {
                                logging(String.format("berhasil update id_penugasan=%s", obj.getString("id")));

                                Set<String> local_list_job_s = new HashSet<>();
                                ArrayList<Job> local_list_job = db.getCheckList(obj.getString("id"), data_session.get(SessionManager.KEY_ID_USER)).getTugas();
                                for (Job job : local_list_job) { //B
                                    local_list_job_s.add(job.getNo());
                                }
                                logging("[local]" + new JSONArray(local_list_job));

                                List<String> inter = new ArrayList<>();
                                ArrayList<Job> intersection = new ArrayList<>(intersection(server_list_job, local_list_job));
                                for (Job job : intersection) {
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
                                    x.put("id_penugasan", job.getId_penugasan());
                                    x.put("no", job.getNo());
                                    x.put("value", job.getVal());
                                    x.put("ket", job.getKet());
                                    inter.add(String.valueOf(x));

                                    logging(String.format("berhasil update tugas id_penugasan=%s no%s", job.getId_penugasan(), job.getNo()));
                                }
                                logging("[I]" + new JSONArray(inter));

//                                List<String> diff_a = list_job_s.stream()
//                                        .filter(aObject -> !((List<String>) local_list_job_s).contains(aObject))
//                                        .collect(Collectors.toList());
                                Set<String> diff_a = Sets.difference(list_job_s, local_list_job_s);
                                logging("[A]" + new JSONArray(diff_a.toArray()));

                                for (String no : diff_a) {
                                    for (Job job : server_list_job) {
                                        if (job.getNo().equals(no)) {
                                            db.save_tugas(
                                                    job.getId_penugasan(),
                                                    job.getNo(),
                                                    job.getCek(),
                                                    job.getKat(),
                                                    job.getVal(),
                                                    job.getKet()
                                            );
                                            list_job.add(job);
                                            logging(String.format("berhasil tambah tugas id_penugasan=%s no%s", job.getId_penugasan(), job.getNo()));
                                        }
                                    }
                                }

//                                List<String> diff_b = local_list_job_s.stream()
//                                        .filter(bObject -> !((List<String>) list_job_s).contains(bObject))
//                                        .collect(Collectors.toList());
                                Set<String> diff_b = Sets.difference(local_list_job_s, list_job_s);
                                logging("[B]" + new JSONArray(diff_b));

                                for (String no : diff_b) {
                                    if (db.countTugasByIdPenugasanNo(obj.getString("id"), no) >= 1) {
                                        db.delete_tugasByIdPenugasanNo(obj.getString("id"), no);
                                        logging(String.format("berhasil hapus tugas id_penugasan=%s no%s", obj.getString("id"), no));
                                    }
                                }
                            }
                            else{
                                logging(String.format("gagal update id_penugasan=%s", obj.getString("id")));
                            }
                        }
                    }

                    list_checkList.add(new CheckList(
                            obj.getString("id"),
                            obj.getString("tanggal"),
                            new Supervisor(
                                    supervisor.getString("id"),
                                    supervisor.getString("nama"),
                                    supervisor.getString("image"),
                                    supervisor.getString("phone"),
                                    supervisor.getString("level")
                            ),
                            new Operator(
                                    operator.getString("id"),
                                    operator.getString("nama"),
                                    operator.getString("image"),
                                    operator.getString("phone"),
                                    operator.getString("level")
                            ),
                            new MachineCheck( // error di sini
                                    cek_mesin.getString("id"),
                                    new Machine(
                                            mesin.getString("id"),
                                            mesin.getString("kode_nfc"),
                                            mesin.getString("nama"),
                                            new MachineCategory(
                                                    kategori.getString("id"),
                                                    kategori.getString("nama")
                                            )
                                    )
                            ),
                            list_job,
                            (obj.getInt("status") >= 1),
                            obj.getString("alasan"),
                            obj.getInt("sync")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }, 1000);
    }

    @Override
    public void onFailPost(String message) {
        closeDialog(message);
    }

    @Override
    public void onSuccessPost(String response) {
        update_alasan(tmp);
    }

    void update_alasan(CheckList checkList){
        SimpleDateFormat Yformat = new SimpleDateFormat("yyyy");
        SimpleDateFormat Mformat = new SimpleDateFormat("MM");
        SimpleDateFormat normal = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if(db.update_penugasan(
                    checkList.getId(),
                    checkList.getSupervisor().getId(),
                    checkList.getOperator().getId(),
                    checkList.getCek_mesin().getId(),
                    Mformat.format(Objects.requireNonNull(normal.parse(checkList.getTanggal()))),
                    Yformat.format(Objects.requireNonNull(normal.parse(checkList.getTanggal()))),
                    (checkList.isStatus()? "1":"0"),
                    "0",
                    checkList.getAlasan(),
                    checkList.getTanggal(),
                    "",
                    ""
            )>= 0) {
                db.update_sinkron_penugasan(checkList.getId(),"0");
                logging(String.format("berhasil update id_penugasan=%s", checkList.getId()));
                startActivity(new Intent(this,ListCheckListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            else{
                logging(String.format("berhasil update id_penugasan=%s", checkList.getId()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void logging(String message){
        Log.i("app-log [ListCheckList]",message);
    }
}