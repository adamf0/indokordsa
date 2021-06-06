package com.app.indokordsa.view.ui;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityListChecklistBinding;
import com.app.indokordsa.helper.NdefMessageParser;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListChecklistlistener;
import com.app.indokordsa.interfaces.ParsedNdefRecord;
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
import java.util.regex.Pattern;

import static com.app.indokordsa.Util.dumpTagData;
import static com.app.indokordsa.Util.intersection;
import static com.app.indokordsa.Util.isNetworkAvailable;

@SuppressLint("SimpleDateFormat")
public class ListCheckListActivity extends AppCompatActivity implements ListChecklistlistener {
    ListCheckListViewModel vmodel;
    ActivityListChecklistBinding binding;
    ListChecklistAdapter listChecklistAdapter;
    ArrayList<CheckList> list_checkList = new ArrayList<>();
    SessionManager session;
    CheckList checkList = null;
    CheckList tmp;
    DB db;
    MutableLiveData<CheckList> tmp_select = new MutableLiveData<>();
    MutableLiveData<String> live_message = new MutableLiveData<>();
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

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
        checkList = initModel();
        tmp_select.observe(this, item -> checkList = item);
        live_message.observe(this, msg -> Snackbar.make(binding.scrollListChecklist,msg, Snackbar.LENGTH_LONG).show());
        loadData();


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            live_message.postValue("Not Support NFC");
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            displayMsgs(msgs);
        }
    }

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }

        binding.scrollListChecklist.setVisibility(View.VISIBLE);
        binding.loader.layoutLoading.setVisibility(View.GONE);
        binding.layoutScanNFCPage.setVisibility(View.GONE);

        HashMap<String, String> s = session.getSession();
        if(checkList==null){
            live_message.postValue("Checklist is null");
        }
        else if(checkList.getCek_mesin()==null){
            live_message.postValue("CheckMachine is null");
        }
        else if(checkList.getCek_mesin().getMesin()==null){
            live_message.postValue("Machine is null");
        }
        else if(checkList.getCek_mesin().getMesin().getKode_nfc().equals( builder.toString() )){
            Intent intent = new Intent(this,CheckListActivity.class);
            intent.putExtra("id_checklist",checkList.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
//        else if(s.get(SessionManager.KEY_NFC).equals( builder.toString() )){
//            Intent intent = new Intent(this,CheckListActivity.class);
//            intent.putExtra("id_checklist",s.get(SessionManager.KEY_ID_CHECKLIST));
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
        else if(Pattern.compile(checkList.getCek_mesin().getMesin().getKode_nfc()).matcher(builder.toString()).find()){
            Intent intent = new Intent(this,CheckListActivity.class);
            intent.putExtra("id_checklist",checkList.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
//        else if(Pattern.compile(s.get(SessionManager.KEY_NFC)).matcher( builder.toString() ).find()){
//            Intent intent = new Intent(this,CheckListActivity.class);
//            intent.putExtra("id_checklist",s.get(SessionManager.KEY_ID_CHECKLIST));
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
        else{
            live_message.postValue( String.format("%s=%s",checkList.getCek_mesin().getMesin().getKode_nfc(),builder.toString()) );
        }

        checkList = initModel();
    }

    CheckList initModel(){
        return new CheckList(null, null, null, null, null, null, false, null,0);
    }

    public void cancelScan(){
        binding.scrollListChecklist.setVisibility(View.VISIBLE);
        binding.loader.layoutLoading.setVisibility(View.GONE);
        binding.layoutScanNFCPage.setVisibility(View.GONE);
        tmp_select.postValue(initModel());

//        checkList = initModel();
//        Intent intent = new Intent(this,CheckListActivity.class);
//        intent.putExtra("id_checklist",checkList.getId());
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
    }

    public void back(){
        startActivity(new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

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
            live_message.postValue(message);
        }, 1000);
    }

    @Override
    public void onSelect(CheckList checkList) {
        logging("line 344=> "+checkList.toJson());
        session.SessionTapNfc(checkList.getId(),checkList.getCek_mesin().getMesin().getKode_nfc());
        tmp_select.postValue(checkList);

        binding.scrollListChecklist.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.GONE);
        binding.layoutScanNFCPage.setVisibility(View.VISIBLE);
//        this.checkList = new CheckList(
//                checkList.getId(),
//                checkList.getTanggal(),
//                checkList.getSupervisor(),
//                checkList.getOperator(),
//                new MachineCheck(
//                        checkList.getCek_mesin().getId(),
//                        new Machine(
//                                checkList.getCek_mesin().getMesin().getId(),
//                                checkList.getCek_mesin().getMesin().getKode_nfc(),
//                                checkList.getCek_mesin().getMesin().getNama(),
//                                new MachineCategory(
//                                        checkList.getCek_mesin().getMesin().getKategori().getId(),
//                                        checkList.getCek_mesin().getMesin().getKategori().getNama()
//                                )
//                        )
//                ),
//                checkList.getTugas(),
//                checkList.getTotalTugas().equals(checkList.getTotalTugasSelesai()),
//                checkList.getAlasan(),
//                0
//        );
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

                    CheckList checkList = new CheckList(
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
                    );
//                    logging("line 763=> "+checkList.toJson());

                    list_checkList.add(checkList);
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
//        int callersLineNumber = Thread.currentThread().getStackTrace()[1].getLineNumber();
        Log.i("app-log [ListCheckList]",message);
    }
}