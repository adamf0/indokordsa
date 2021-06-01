package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Kuesioner extends BaseObservable implements Parcelable {
    private String id;
    private String nama;

    Kuesioner(){

    }
    public Kuesioner(String id, String nama){
        setId(id);
        setNama(nama);
    }

    protected Kuesioner(Parcel in) {
        id = in.readString();
        nama = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Kuesioner> CREATOR = new Creator<Kuesioner>() {
        @Override
        public Kuesioner createFromParcel(Parcel in) {
            return new Kuesioner(in);
        }

        @Override
        public Kuesioner[] newArray(int size) {
            return new Kuesioner[size];
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
}
