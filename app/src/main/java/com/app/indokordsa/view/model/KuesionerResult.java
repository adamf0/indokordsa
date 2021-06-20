package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;
import com.app.indokordsa.etc.Util;

import java.util.ArrayList;

public class KuesionerResult extends BaseObservable implements Parcelable {
    private String id_kuesioner_result;
    private String id_user;
    private Shift shift;
    private ArrayList<KuesionerResultDetail> list_kuesioner = new ArrayList<>();
    private int status;
    private String alasan;
    private boolean sync;
    private int sync_;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    KuesionerResult(){

    }
    public KuesionerResult(
            String id_kuesioner_result,
            String id_user,
            Shift shift,
            ArrayList<KuesionerResultDetail> list_kuesioner,
            int status,
            String alasan,
            int sync,
            String created_at,
            String updated_at,
            String deleted_at
    ){
        setId_kuesioner_result(id_kuesioner_result);
        setId_user(id_user);
        setShift(shift);
        setList_kuesioner(list_kuesioner);
        setStatus(status);
        setAlasan(alasan);
        setSync(sync == 1);
        setSync_(sync);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
        setDeleted_at(deleted_at);
    }

    protected KuesionerResult(Parcel in) {
        id_kuesioner_result = in.readString();
        id_user = in.readString();
        shift = in.readParcelable(Shift.class.getClassLoader());
        list_kuesioner = in.createTypedArrayList(KuesionerResultDetail.CREATOR);
        status = in.readInt();
        alasan = in.readString();
        sync = in.readByte() != 0;
        sync_ = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
        deleted_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_kuesioner_result);
        dest.writeString(id_user);
        dest.writeParcelable(shift, flags);
        dest.writeTypedList(list_kuesioner);
        dest.writeInt(status);
        dest.writeString(alasan);
        dest.writeByte((byte) (sync ? 1 : 0));
        dest.writeInt(sync_);
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
    public ArrayList<KuesionerResultDetail> getKuesioner() {
        return getList_kuesioner();
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
    public int getTotalPertanyaanSelesai() {
        int tmp = 0;
        for (KuesionerResultDetail kuesionerDetail:list_kuesioner) {
            if(kuesionerDetail.getJawaban().equals("1")){
                tmp++;
            }
            else if(kuesionerDetail.getJawaban().equals("2")){
                int sub_tmp=0;
                for (JawabanKuesioner item:kuesionerDetail.getList_pertanyaan()){
                    if(item.isDone()){
                        sub_tmp++;
                    }
                }
                if(sub_tmp == kuesionerDetail.getList_pertanyaan().size()) tmp++;
            }
        }

        return tmp;
    }

    @Bindable
    public String getProgressTask() {
        return String.format("%s (%s/%s)",getList_kuesioner().size(),getTotalPertanyaanSelesai(),getList_kuesioner().size());
    }
    @Bindable
    public boolean getEnableClick() {
        return getList_kuesioner().size()==getTotalPertanyaanSelesai();
    }

    @Bindable
    public String getRealStatus() {
        return (getList_kuesioner().size()==getTotalPertanyaanSelesai()? "Finish":"Unfinish");
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
    public ArrayList<KuesionerResultDetail> getList_kuesioner() {
        return list_kuesioner;
    }

    public void setList_kuesioner(ArrayList<KuesionerResultDetail> list_kuesioner) {
        this.list_kuesioner = list_kuesioner;
        this.notifyPropertyChanged(BR.list_kuesioner);
    }
}
