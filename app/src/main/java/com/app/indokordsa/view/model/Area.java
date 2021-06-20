package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class Area extends BaseObservable implements Parcelable {
    private String id;
    private String nama;

    Area(){

    }
    public Area(String id, String nama){
        setId(id);
        setNama(nama);
    }

    protected Area(Parcel in) {
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

    public static final Creator<Area> CREATOR = new Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
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
