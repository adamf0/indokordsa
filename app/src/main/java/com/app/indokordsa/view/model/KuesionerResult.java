package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;
import com.app.indokordsa.Util;

import java.util.ArrayList;

public class KuesionerResult extends BaseObservable implements Parcelable {
    private String id_kuesioner_result;
    private String id_user;
    private Shift shift;
    private Kuesioner kuesioner;
    private String jawaban;
    private int status;
    private String alasan;
    private boolean sync;
    private int sync_;
    private ArrayList<JawabanKuesioner> list_pertanyaan = new ArrayList<>();
    private String created_at;
    private String updated_at;
    private String deleted_at;

    KuesionerResult(){

    }
    public KuesionerResult(
            String id_kuesioner_result,
            String id_user,
            Shift shift,
            Kuesioner kuesioner,
            String jawaban,
            int status,
            String alasan,
            int sync,
            ArrayList<JawabanKuesioner> list_pertanyaan,
            String created_at,
            String updated_at,
            String deleted_at
    ){
        setId_kuesioner_result(id_kuesioner_result);
        setId_user(id_user);
        setShift(shift);
        setKuesioner(kuesioner);
        setJawaban(jawaban);
        setStatus(status);
        setAlasan(alasan);
        setSync(sync == 0);
        setSync_(sync);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
        setDeleted_at(deleted_at);
    }

    protected KuesionerResult(Parcel in) {
        id_kuesioner_result = in.readString();
        id_user = in.readString();
        shift = in.readParcelable(Shift.class.getClassLoader());
        kuesioner = in.readParcelable(Kuesioner.class.getClassLoader());
        jawaban = in.readString();
        status = in.readInt();
        alasan = in.readString();
        sync = in.readByte() != 0;
        sync_ = in.readInt();
        list_pertanyaan = in.createTypedArrayList(JawabanKuesioner.CREATOR);
        created_at = in.readString();
        updated_at = in.readString();
        deleted_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_kuesioner_result);
        dest.writeString(id_user);
        dest.writeParcelable(shift, flags);
        dest.writeParcelable(kuesioner, flags);
        dest.writeString(jawaban);
        dest.writeInt(status);
        dest.writeString(alasan);
        dest.writeByte((byte) (sync ? 1 : 0));
        dest.writeInt(sync_);
        dest.writeTypedList(list_pertanyaan);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(deleted_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KuesionerResult> CREATOR = new Creator<KuesionerResult>() {
        @Override
        public KuesionerResult createFromParcel(Parcel in) {
            return new KuesionerResult(in);
        }

        @Override
        public KuesionerResult[] newArray(int size) {
            return new KuesionerResult[size];
        }
    };

    @Bindable
    public String getId_kuesioner_result() {
        return id_kuesioner_result;
    }

    public void setId_kuesioner_result(String id_kuesioner_result) {
        this.id_kuesioner_result = id_kuesioner_result;
        this.notifyPropertyChanged(BR.id_kuesioner_result);
    }

    @Bindable
    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
        this.notifyPropertyChanged(BR.id_user);
    }

    @Bindable
    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
        this.notifyPropertyChanged(BR.shift);
    }

    @Bindable
    public Kuesioner getKuesioner() {
        return kuesioner;
    }
    @Bindable
    public String getAreaKuesioner() {
        if(getKuesioner()!=null && getKuesioner().getArea()!=null){
            return getKuesioner().getArea().getNama();
        }
        return "";
    }

    public void setKuesioner(Kuesioner kuesioner) {
        this.kuesioner = kuesioner;
        this.notifyPropertyChanged(BR.kuesioner);
    }

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        this.notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
        this.notifyPropertyChanged(BR.alasan);
    }

    @Bindable
    public boolean getSync() {
        return sync;
    }

    @Bindable
    public String getRealSync() {
        return getSync()? "Finish":"Unfinish";
    }

    public void setSync(boolean sync) {
        this.sync = sync;
        this.notifyPropertyChanged(BR.sync);
    }

    @Bindable
    public int getSync_() {
        return sync_;
    }

    public void setSync_(int sync_) {
        this.sync_ = sync_;
        this.notifyPropertyChanged(BR.sync_);
    }

    @Bindable
    public ArrayList<JawabanKuesioner> getList_pertanyaan() {
        return list_pertanyaan;
    }

    @Bindable
    public int getTotalPertanyaanSelesai() {
        if(getJawaban().equals("1")){
            return getList_pertanyaan().size();
        }
        else if(getJawaban().equals("2")){
            int tmp = 0;
            for (JawabanKuesioner item:getList_pertanyaan()){
                if(item.isDone()){
                    tmp++;
                }
            }
            return tmp;
        }
        return 0;
    }

    @Bindable
    public String getProgressTask() {
        return String.format("%s (%s/%s)",getList_pertanyaan().size(),getTotalPertanyaanSelesai(),getList_pertanyaan().size());
    }

    @Bindable
    public String getRealStatus() {
        return (getList_pertanyaan().size()==getTotalPertanyaanSelesai()? "Finish":"Unfinish");
    }

    public void setList_pertanyaan(ArrayList<JawabanKuesioner> list_pertanyaan) {
        this.list_pertanyaan = list_pertanyaan;
        this.notifyPropertyChanged(BR.list_pertanyaan);
    }

    @Bindable
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
        this.notifyPropertyChanged(BR.created_at);
    }

    @Bindable
    public String getCreated_atFormat() {
        return (TextUtils.isEmpty(getCreated_at())? "": Util.reFormatDatev1(getCreated_at()));
    }

    @Bindable
    public String getUpdated_at() {
        return updated_at;
    }

    @Bindable
    public String getUpdated_atFormat() {
        return (TextUtils.isEmpty(getUpdated_at())? "": Util.reFormatDatev1(getUpdated_at()));
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
        this.notifyPropertyChanged(BR.updated_at);
    }

    @Bindable
    public String getDeleted_at() {
        return deleted_at;
    }

    @Bindable
    public String getDeleted_atFormat() {
        return (TextUtils.isEmpty(getDeleted_at())? "": Util.reFormatDatev1(getDeleted_at()));
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
        this.notifyPropertyChanged(BR.deleted_at);
    }

    @Bindable
    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
        this.notifyPropertyChanged(BR.jawaban);
    }
}
