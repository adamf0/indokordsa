package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class Topik extends BaseObservable implements Parcelable {
    private String id;
    private String tentang;

    Topik(){

    }
    public Topik(String id, String tentang){
        setId(id);
        setTentang(tentang);
    }

    protected Topik(Parcel in) {
        id = in.readString();
        tentang = in.readString();
    }

    public static final Creator<Topik> CREATOR = new Creator<Topik>() {
        @Override
        public Topik createFromParcel(Parcel in) {
            return new Topik(in);
        }

        @Override
        public Topik[] newArray(int size) {
            return new Topik[size];
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
    public String getTentang() {
        return tentang;
    }

    public void setTentang(String tentang) {
        this.tentang = tentang;
        this.notifyPropertyChanged(BR.tentang);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tentang);
    }
}
