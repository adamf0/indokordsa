package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

import java.util.Date;

import static com.app.indokordsa.etc.Util.string2Date;

public class JawabanKuesioner extends BaseObservable implements Parcelable {
    private String id;
    private Topik topik;
    private Pertanyaan pertanyaan;
    private String val;
    private String other;
    private String start;
    private String end;
    private String remarks;
    private String message;

    JawabanKuesioner(){

    }
    public JawabanKuesioner(Topik topik, Pertanyaan pertanyaan, String val, String other, String start, String end,String remarks){
     setTopik(topik);
     setPertanyaan(pertanyaan);
     setVal(val);
     setOther(other);
     setStart(start);
     setEnd(end);
     setRemarks(remarks);
     setMessage("");
    }
    public JawabanKuesioner(String id, Topik topik, Pertanyaan pertanyaan, String val, String other, String start, String end,String remarks){
        setId(id);
        setTopik(topik);
        setPertanyaan(pertanyaan);
        setVal(val);
        setOther(other);
        setStart(start);
        setEnd(end);
        setRemarks(remarks);
        setMessage("");
    }

    protected JawabanKuesioner(Parcel in) {
        id = in.readString();
        topik = in.readParcelable(Topik.class.getClassLoader());
        pertanyaan = in.readParcelable(Pertanyaan.class.getClassLoader());
        val = in.readString();
        start = in.readString();
        end = in.readString();
        remarks = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(topik, flags);
        dest.writeParcelable(pertanyaan, flags);
        dest.writeString(val);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(remarks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JawabanKuesioner> CREATOR = new Creator<JawabanKuesioner>() {
        @Override
        public JawabanKuesioner createFromParcel(Parcel in) {
            return new JawabanKuesioner(in);
        }

        @Override
        public JawabanKuesioner[] newArray(int size) {
            return new JawabanKuesioner[size];
        }
    };

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Topik getTopik() {
        return topik;
    }

    public void setTopik(Topik topik) {
        this.topik = topik;
        this.notifyPropertyChanged(BR.topik);
    }

    @Bindable
    public Pertanyaan getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(Pertanyaan pertanyaan) {
        this.pertanyaan = pertanyaan;
        this.notifyPropertyChanged(BR.pertanyaan);
    }

    @Bindable
    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
        this.notifyPropertyChanged(BR.val);
    }

    @Bindable
    public String getStart() {
        return start;
    }

    public Date getStartFormatDate() {
        return (getStart().equals("") || getStart()==null? null:string2Date(getStart()));
    }

    public void setStart(String start) {
        this.start = start;
        this.notifyPropertyChanged(BR.start);
    }

    @Bindable
    public String getEnd() {
        return end;
    }

    public Date getEndFormatDate() {
        return (getEnd().equals("") || getEnd()==null? null:string2Date(getEnd()));
    }

    public void setEnd(String end) {
        this.end = end;
        this.notifyPropertyChanged(BR.end);
    }

    @Bindable
    public String getDurationFormatHour() {
        if(getStartFormatDate() != null && getEndFormatDate()!=null){
            long diff = getStartFormatDate().getTime() - getEndFormatDate().getTime();
            double diffInHours = diff / ((double) 1000 * 60 * 60);
            return String.valueOf((int)diffInHours);
        }
        return "0";
    }
    @Bindable
    public String getDurationFormatMinute() {
        if(getStartFormatDate() != null && getEndFormatDate()!=null) {
            long diff = getStartFormatDate().getTime() - getEndFormatDate().getTime();
            double diffInHours = diff / ((double) 1000 * 60 * 60);
            return String.valueOf((int) diffInHours * 60);
        }
        return "0";
    }
    @Bindable
    public String getDurationFormatHourMinute() {
        if(getStartFormatDate() != null && getEndFormatDate()!=null) {
            long diff = getStartFormatDate().getTime() - getEndFormatDate().getTime();
            double diffInHours = diff / ((double) 1000 * 60 * 60);
            return String.format("%s jam %s menit", (int) diffInHours, (diffInHours - (int) diffInHours) * 60);
        }
        return String.format("%s jam %s menit","0","0");
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.notifyPropertyChanged(BR.message);
    }

    public boolean isValidation() {
        if(TextUtils.isEmpty(getVal()) || getVal().equals("")){
            setMessage("value cannot be empty");
            return false;
        }
        else if(TextUtils.isEmpty(getStart())){
            setMessage("start cannot be empty");
            return false;
        }
        else if(TextUtils.isEmpty(getEnd())){
            setMessage("end cannot be empty");
            return false;
        }
        else
            setMessage(null);

        return true;
    }

    public boolean isDone(){
        return !TextUtils.isEmpty(getVal()) && !TextUtils.isEmpty(getStart()) && !TextUtils.isEmpty(getEnd());
    }

    @Bindable
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
        this.notifyPropertyChanged(BR.remarks);
    }

    @Bindable
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
        this.notifyPropertyChanged(BR.other);
    }
}
