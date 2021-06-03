package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class Pertanyaan extends BaseObservable implements Parcelable {
    private String id;
    private String pertanyaan;

    Pertanyaan(){

    }
    public Pertanyaan(String id, String pertanyaan){
        setId(id);
        setPertanyaan(pertanyaan);
    }

    protected Pertanyaan(Parcel in) {
        id = in.readString();
        pertanyaan = in.readString();
    }

    public static final Creator<Pertanyaan> CREATOR = new Creator<Pertanyaan>() {
        @Override
        public Pertanyaan createFromParcel(Parcel in) {
            return new Pertanyaan(in);
        }

        @Override
        public Pertanyaan[] newArray(int size) {
            return new Pertanyaan[size];
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
    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
        this.notifyPropertyChanged(BR.pertanyaan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pertanyaan);
    }
}
