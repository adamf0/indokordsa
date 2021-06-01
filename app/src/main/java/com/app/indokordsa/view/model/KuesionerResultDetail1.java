package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

import java.util.ArrayList;

public class KuesionerResultDetail1 extends BaseObservable implements Parcelable {
    private String no;
    private String id;
    private String tentang;
    private ArrayList<KuesionerResultDetail2> detail;

    KuesionerResultDetail1(){

    }
    public KuesionerResultDetail1(String no, String id, String tentang, ArrayList<KuesionerResultDetail2> detail){
        setNo(no);
        setId(id);
        setTentang(tentang);
        setDetail(detail);
    }

    protected KuesionerResultDetail1(Parcel in) {
        no = in.readString();
        id = in.readString();
        tentang = in.readString();
        detail = in.createTypedArrayList(KuesionerResultDetail2.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(no);
        dest.writeString(id);
        dest.writeString(tentang);
        dest.writeTypedList(detail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KuesionerResultDetail1> CREATOR = new Creator<KuesionerResultDetail1>() {
        @Override
        public KuesionerResultDetail1 createFromParcel(Parcel in) {
            return new KuesionerResultDetail1(in);
        }

        @Override
        public KuesionerResultDetail1[] newArray(int size) {
            return new KuesionerResultDetail1[size];
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
    public String getTentang() {
        return tentang;
    }

    public void setTentang(String tentang) {
        this.tentang = tentang;
        notifyPropertyChanged(BR.tentang);
    }

    @Bindable
    public ArrayList<KuesionerResultDetail2> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<KuesionerResultDetail2> detail) {
        this.detail = detail;
        notifyPropertyChanged(BR.detail);
    }

    @Bindable
    public int getTotalDetailPertanyaan(){
        return detail.size();
    }

    @Bindable
    public int getTotalSelesaiDetailPertanyaan(){
        int tmp=0;
        for (KuesionerResultDetail2 detail_:detail){
            if(detail_.isDone())
                tmp++;
        }
        return tmp;
    }

    @Bindable
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
        notifyPropertyChanged(BR.no);
    }
}
