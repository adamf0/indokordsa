package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class Fitur extends BaseObservable implements Parcelable {
    private String id;
    private int img;
    private String judul;

    Fitur(){

    }
    public Fitur(String id, int img, String judul){
        setId(id);
        setImg(img);
        setJudul(judul);
    }

    public Fitur(Parcel in) {
        id = in.readString();
        judul = in.readString();
    }

    public static final Creator<Fitur> CREATOR = new Creator<Fitur>() {
        @Override
        public Fitur createFromParcel(Parcel in) {
            return new Fitur(in);
        }

        @Override
        public Fitur[] newArray(int size) {
            return new Fitur[size];
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
    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
        notifyPropertyChanged(BR.img);
    }

    @Bindable
    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
        notifyPropertyChanged(BR.judul);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(judul);
    }
}