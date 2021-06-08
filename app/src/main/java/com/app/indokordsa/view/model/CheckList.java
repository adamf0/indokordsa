package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;
import com.app.indokordsa.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckList extends BaseObservable implements Parcelable {
    private String id;
    private String tanggal;
    private Supervisor supervisor;
    private Operator operator;
    private MachineCheck cek_mesin;
    private ArrayList<Job> tugas = new ArrayList<>();
    private boolean status;
    private String alasan;
    private boolean sync;
    private int sync_;

    CheckList(){

    }
    public CheckList(
            String id,
            String tanggal,
            Supervisor supervisor,
            Operator operator,
            MachineCheck cek_mesin,
            ArrayList<Job> tugas,
            boolean status,
            String alasan,
            int sync
    ){
        setId(id);
        setTanggal(tanggal);
        setSupervisor(supervisor);
        setOperator(operator);
        setCek_mesin(cek_mesin);
        setTugas(tugas);
        setStatus(status);
        setAlasan(alasan);
        setSync_(sync);
        setSync(sync == 1);
    }

    protected CheckList(Parcel in) {
        id = in.readString();
        tanggal = in.readString();
        supervisor = in.readParcelable(Supervisor.class.getClassLoader());
        operator = in.readParcelable(Operator.class.getClassLoader());
        cek_mesin = in.readParcelable(MachineCheck.class.getClassLoader());
        tugas = in.createTypedArrayList(Job.CREATOR);
        status = in.readByte() != 0;
        alasan = in.readString();
        sync = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tanggal);
        dest.writeParcelable(supervisor, flags);
        dest.writeParcelable(operator, flags);
        dest.writeParcelable(cek_mesin, flags);
        dest.writeTypedList(tugas);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(alasan);
        dest.writeByte((byte) (sync ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckList> CREATOR = new Creator<CheckList>() {
        @Override
        public CheckList createFromParcel(Parcel in) {
            return new CheckList(in);
        }

        @Override
        public CheckList[] newArray(int size) {
            return new CheckList[size];
        }
    };

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
        notifyPropertyChanged(BR.supervisor);
    }

    @Bindable
    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
        notifyPropertyChanged(BR.operator);
    }

    @Bindable
    public MachineCheck getCek_mesin() {
        return cek_mesin;
    }

    public void setCek_mesin(MachineCheck cek_mesin) {
        this.cek_mesin = cek_mesin;
        notifyPropertyChanged(BR.cek_mesin);
    }

    @Bindable
    public ArrayList<Job> getTugas() {
        return tugas;
    }

    @Bindable
    public String getTotalTugas() {
        return String.valueOf(tugas.size());
    }

    @Bindable
    public String getTotalTugasSelesai() {
        int num = 0;

        for (Job tugas: tugas) {
            if(tugas.isDone()){
                num++;
            }
        }
        return String.valueOf(num);
    }

    public void setTugas(ArrayList<Job> tugas) {
        this.tugas = tugas;
        notifyPropertyChanged(BR.tugas);
    }

    @Bindable
    public String getProgressTask(){
        return String.format("%s (%s/%s)",getTotalTugas(),getTotalTugasSelesai(),getTotalTugas());
    }
    @Bindable
    public boolean getEnableClick(){
        return getTotalTugasSelesai().equals(getTotalTugas());
    }

    @Bindable
    public String getTanggal() {
        return tanggal;
    }

    @Bindable
    public String getTanggalFormat() {
        return Util.reFormatDate(tanggal);
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
        notifyPropertyChanged(BR.tanggal);
    }

    @Bindable
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    public boolean isDone(){
        return false;
    }

    @Bindable
    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
        notifyPropertyChanged(BR.sync);
    }

    @Bindable
    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
        notifyPropertyChanged(BR.alasan);
    }

    public int getSync_() {
        return sync_;
    }

    public void setSync_(int sync_) {
        this.sync_ = sync_;
    }

    public String toJson(){
        JSONObject obj = new JSONObject();
        try {
            JSONObject supervisor = new JSONObject();
            supervisor.put("id",getSupervisor().getId());
            supervisor.put("nama",getSupervisor().getNama());
            supervisor.put("image",getSupervisor().getImage());
            supervisor.put("phone",getSupervisor().getPhone());
            supervisor.put("level",getSupervisor().getLevel());

            JSONObject operator = new JSONObject();
            operator.put("id",getOperator().getId());
            operator.put("nama",getOperator().getNama());
            operator.put("image",getOperator().getImage());
            operator.put("phone",getOperator().getPhone());
            operator.put("level",getOperator().getLevel());

            JSONObject machinecheck = new JSONObject();
            machinecheck.put("id",getCek_mesin().getId());
                JSONObject machine = new JSONObject();
                machine.put("id",getCek_mesin().getMesin().getId());
                machine.put("kode_nfc",getCek_mesin().getMesin().getKode_nfc());
                machine.put("nama",getCek_mesin().getMesin().getNama());
                    JSONObject kategori = new JSONObject();
                    kategori.put("id",getCek_mesin().getMesin().getKategori().getId());
                    kategori.put("nama",getCek_mesin().getMesin().getKategori().getNama());
                machine.put("kategori",kategori);
            machinecheck.put("mesin",machine);

            obj.put("id",getId());
            obj.put("tanggal",getTanggal());
            obj.put("supervisor",supervisor);
            obj.put("operator",operator);
            obj.put("cek_mesin",machinecheck);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString().replace("\\","");
    }
}
