package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class MachineCategory extends BaseObservable implements Parcelable {
    private String id;
    private String nama;

    MachineCategory(){

    }
    public MachineCategory(String id, String nama){
        setId(id);
        setNama(nama);
    }

    protected MachineCategory(Parcel in) {
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

    public static final Creator<MachineCategory> CREATOR = new Creator<MachineCategory>() {
        @Override
        public MachineCategory createFromParcel(Parcel in) {
            return new MachineCategory(in);
        }

        @Override
        public MachineCategory[] newArray(int size) {
            return new MachineCategory[size];
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
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
        notifyPropertyChanged(BR.nama);
    }
}
