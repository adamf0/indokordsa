package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

import org.json.JSONException;
import org.json.JSONObject;

public class Job extends BaseObservable implements Parcelable{
    private String id_tugas;
    private String id_penugasan;
    private String no;
    private String cek;
    private String kat;
    private String val;
    private String ket;

    private String message;

    Job(){

    }
    public Job(
            String id_tugas,
            String id_penugasan,
            String no,
            String cek,
            String kat,
            String val,
            String ket
    ){
        setId_tugas(id_tugas);
        setId_penugasan(id_penugasan);
        setNo(no);
        setCek(cek);
        setKat(kat);
        setVal(val);
        setKet(ket);
        setMessage(null);
    }

    protected Job(Parcel in) {
        id_tugas = in.readString();
        id_penugasan = in.readString();
        no = in.readString();
        cek = in.readString();
        kat = in.readString();
        val = in.readString();
        ket = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_tugas);
        dest.writeString(id_penugasan);
        dest.writeString(no);
        dest.writeString(cek);
        dest.writeString(kat);
        dest.writeString(val);
        dest.writeString(ket);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    @Bindable
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
        notifyPropertyChanged(BR.no);
    }

    @Bindable
    public String getCek() {
        return cek;
    }

    public void setCek(String cek) {
        this.cek = cek;
        notifyPropertyChanged(BR.cek);
    }

    @Bindable
    public String getKat() {
        return kat;
    }

    public void setKat(String kat) {
        this.kat = kat;
        notifyPropertyChanged(BR.kat);
    }

    @Bindable
    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
        notifyPropertyChanged(BR.val);
    }

    @Bindable
    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
        notifyPropertyChanged(BR.ket);
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    public boolean isDone(){
        if(TextUtils.isEmpty(getVal())){
            setMessage("Value tidak boleh kosong");
            return false;
        }
//        else if(TextUtils.isEmpty(getKet())){
//            setMessage("Keterangan tidak boleh kosong");
//            return false;
//        }
        else{
            return true;
        }
    }

    public JSONObject getTugas(){
        try {
            return new JSONObject()
//                       .put("id_tugas",this.getId_tugas())
//                       .put("id_penugasan",this.getId_penugasan())
                       .put("no",this.getNo())
                       .put("cek",this.getCek())
                       .put("kat",this.getKat())
                       .put("val",this.getVal())
                       .put("ket",this.getKet());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Bindable
    public String getId_tugas() {
        return id_tugas;
    }

    public void setId_tugas(String id_tugas) {
        this.id_tugas = id_tugas;
        notifyPropertyChanged(BR.id_tugas);
    }

    @Bindable
    public String getId_penugasan() {
        return id_penugasan;
    }

    public void setId_penugasan(String id_penugasan) {
        this.id_penugasan = id_penugasan;
        notifyPropertyChanged(BR.id_penugasan);
    }

    public boolean isValidation() {
        if(TextUtils.isEmpty(getVal())){
            setMessage("value cannot be empty");
            return false;
        }
//        else if(TextUtils.isEmpty(getKet())){
//            setMessage("description cannot be empty");
//            return false;
//        }
        else
            setMessage(null);

        return true;
    }
}
