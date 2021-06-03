package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class Kuesioner extends BaseObservable implements Parcelable {
    private String id;
    private Area area;
    private String pertanyaan;

    Kuesioner(){

    }
    public Kuesioner(String id, Area area, String pertanyaan){
        setId(id);
        setPertanyaan(pertanyaan);
        setArea(area);
    }

    protected Kuesioner(Parcel in) {
        id = in.readString();
        pertanyaan = in.readString();
        area = in.readParcelable(Area.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pertanyaan);
        dest.writeParcelable(area, flags);
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
    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
        this.notifyPropertyChanged(BR.pertanyaan);
    }

    @Bindable
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
        this.notifyPropertyChanged(BR.area);
    }
}
