package com.app.indokordsa.view.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Shift extends BaseObservable implements Parcelable {
    private String id;
    private String nama;
    private String mulai;
    private String selesai;
    private String ganti_hari;

    Shift(){

    }
    public Shift(String id, String nama, String mulai, String selesai, String ganti_hari){
        setId(id);
        setNama(nama);
        setMulai(mulai);
        setSelesai(selesai);
        setGanti_hari(ganti_hari);
    }

    protected Shift(Parcel in) {
        id = in.readString();
        nama = in.readString();
        mulai = in.readString();
        selesai = in.readString();
        ganti_hari = in.readString();
    }

    public static final Creator<Shift> CREATOR = new Creator<Shift>() {
        @Override
        public Shift createFromParcel(Parcel in) {
            return new Shift(in);
        }

        @Override
        public Shift[] newArray(int size) {
            return new Shift[size];
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
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
        this.notifyPropertyChanged(BR.nama);
    }

    @Bindable
    public String getFullShift() {
        return String.format("%s (%s - %s)", getNama(),getMulai(),getSelesai());
    }

    @Bindable
    public String getMulai() {
        return mulai;
    }

    public void setMulai(String mulai) {
        this.mulai = mulai;
        this.notifyPropertyChanged(BR.mulai);
    }

    @Bindable
    public String getSelesai() {
        return selesai;
    }

    public void setSelesai(String selesai) {
        this.selesai = selesai;
        this.notifyPropertyChanged(BR.selesai);
    }

    @Bindable
    public String getGanti_hari() {
        return ganti_hari;
    }

    public void setGanti_hari(String ganti_hari) {
        this.ganti_hari = ganti_hari;
        this.notifyPropertyChanged(BR.ganti_hari);
    }

    @SuppressLint("SimpleDateFormat")
    @Bindable
    public String getMulaiv1() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        return sdf.format(c.getTime())+" "+getMulai();
    }

    @SuppressLint("SimpleDateFormat")
    @Bindable
    public Long getMulaiv2(){
        Date date = null;
        try {
            TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(tz);
            date = sdf.parse(getMulaiv1());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date==null? 0:date.getTime();
    }

    @SuppressLint("SimpleDateFormat")
    @Bindable
    public String getSelesaiv1() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if(Integer.parseInt(getGanti_hari())>0)
            c.add(Calendar.DATE, 1);

        return sdf.format(c.getTime())+" "+getSelesai();
    }

    @SuppressLint("SimpleDateFormat")
    @Bindable
    public Long getSelesaiv2(){
        Date date = null;
        try {
            TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(tz);
            date = sdf.parse(getSelesaiv1());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date==null? 0:date.getTime();
    }

    @SuppressLint("SimpleDateFormat")
    @Bindable
    public String getSekarangv1() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        sdf.setTimeZone(tz);

        return sdf.format(c.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    @Bindable
    public Long getSekarangv2(){
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        sdf.setTimeZone(tz);

        return c.getTime().getTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
        dest.writeString(mulai);
        dest.writeString(selesai);
        dest.writeString(ganti_hari);
    }
}
