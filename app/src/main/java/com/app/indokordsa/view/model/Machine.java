package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class Machine extends BaseObservable implements Parcelable {
    private String id;
    private String kode_nfc;
    private String nama;
    private MachineCategory kategori;

    Machine(){

    }
    public Machine(String id, String kode_nfc, String nama, MachineCategory kategori){
        setId(id);
        setKode_nfc(kode_nfc);
        setNama(nama);
        setKategori(kategori);
    }

    protected Machine(Parcel in) {
        setId(in.readString());
        setKode_nfc(in.readString());
        setNama(in.readString());
    }

    public static Creator<Machine> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getKode_nfc());
        dest.writeString(getNama());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private static final Creator<Machine> CREATOR = new Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel in) {
            return new Machine(in);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
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
    public String getKode_nfc() {
        return kode_nfc;
    }

    public void setKode_nfc(String kode_nfc) {
        this.kode_nfc = kode_nfc;
        notifyPropertyChanged(BR.kode_nfc);
    }

    @Bindable
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
        notifyPropertyChanged(BR.nama);
    }

    @Bindable
    public MachineCategory getKategori() {
        return kategori;
    }

    public void setKategori(MachineCategory kategori) {
        this.kategori = kategori;
        notifyPropertyChanged(BR.kategori);
    }
}
