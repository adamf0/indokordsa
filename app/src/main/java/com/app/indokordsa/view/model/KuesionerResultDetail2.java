package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

import org.json.JSONObject;

public class KuesionerResultDetail2 extends BaseObservable implements Parcelable {
    private String no;
    private String id;
    private String pertanyaan;
    private String val;
    private String start;
    private String end;
    private String duration;

    public KuesionerResultDetail2(){

    }
    public KuesionerResultDetail2(String no, String id, String pertanyaan, String val, String start, String end, String duration){
        setNo(no);
        setId(id);
        setPertanyaan(pertanyaan);
        setVal(val);
        setStart(start);
        setEnd(end);
        setDuration(duration);
    }

    protected KuesionerResultDetail2(Parcel in) {
        no = in.readString();
        id = in.readString();
        pertanyaan = in.readString();
        val = in.readString();
        start = in.readString();
        end = in.readString();
        duration = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(no);
        dest.writeString(id);
        dest.writeString(pertanyaan);
        dest.writeString(val);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(duration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KuesionerResultDetail2> CREATOR = new Creator<KuesionerResultDetail2>() {
        @Override
        public KuesionerResultDetail2 createFromParcel(Parcel in) {
            return new KuesionerResultDetail2(in);
        }

        @Override
        public KuesionerResultDetail2[] newArray(int size) {
            return new KuesionerResultDetail2[size];
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
    public String getPertanyaan() {
        return pertanyaan;
    }

    @Bindable
    public boolean isSelected() {
        if(getPertanyaan().equals("Rusak")){
            return true;
        }
        else if(getPertanyaan().equals("Perlu modifikasi")){
            return true;
        }
        else if(getPertanyaan().equals("Perlu ganti segera")){
            return true;
        }
        else if(getPertanyaan().equals("Control mati")){
            return true;
        }
        else if(getPertanyaan().equals("CB trip")){
            return true;
        }
        else if(getPertanyaan().equals("Fault")){
            return true;
        }
        else if(getPertanyaan().equals("Perlu di kalibrasi")){
            return true;
        }
        return false;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
        notifyPropertyChanged(BR.pertanyaan);
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
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
        notifyPropertyChanged(BR.start);
    }

    @Bindable
    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
        notifyPropertyChanged(BR.end);
    }

    @Bindable
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    @Bindable
    public boolean isDone(){
        return !getVal().equals("") && !getStart().equals("") && !getEnd().equals("") && !getDuration().equals("");
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
