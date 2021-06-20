package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

import java.util.ArrayList;

public class KuesionerResultDetail extends BaseObservable implements Parcelable {
    private String id;
    private Kuesioner kuesioner;
    private String jawaban;
    private ArrayList<JawabanKuesioner> list_pertanyaan = new ArrayList<>();

    KuesionerResultDetail(){

    }
    public KuesionerResultDetail(
            String id,
            Kuesioner kuesioner,
            String jawaban,
            ArrayList<JawabanKuesioner> list_pertanyaan
    ){
        setId(id);
        setKuesioner(kuesioner);
        setJawaban(jawaban);
        setList_pertanyaan(list_pertanyaan);
    }

    protected KuesionerResultDetail(Parcel in) {
        id = in.readString();
        kuesioner = in.readParcelable(Kuesioner.class.getClassLoader());
        jawaban = in.readString();
        list_pertanyaan = in.createTypedArrayList(JawabanKuesioner.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(kuesioner, flags);
        dest.writeString(jawaban);
        dest.writeTypedList(list_pertanyaan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KuesionerResultDetail> CREATOR = new Creator<KuesionerResultDetail>() {
        @Override
        public KuesionerResultDetail createFromParcel(Parcel in) {
            return new KuesionerResultDetail(in);
        }

        @Override
        public KuesionerResultDetail[] newArray(int size) {
            return new KuesionerResultDetail[size];
        }
    };

    @Bindable
    public Kuesioner getKuesioner() {
        return kuesioner;
    }
    @Bindable
    public String getAreaKuesioner() {
        return getKuesioner().getArea().getNama();
//        if(getKuesioner()!=null && getKuesioner().getArea()!=null){
//            return getKuesioner().getArea().getNama();
//        }
//        return "";
    }

    public void setKuesioner(Kuesioner kuesioner) {
        this.kuesioner = kuesioner;
        this.notifyPropertyChanged(BR.kuesioner);
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
    public boolean getEnableClick() {
        return getList_pertanyaan().size()==getTotalPertanyaanSelesai();
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
    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
        this.notifyPropertyChanged(BR.jawaban);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.notifyPropertyChanged(BR.id);
    }
}
