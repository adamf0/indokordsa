package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class Barcode extends BaseObservable implements Parcelable {
    private String id;
    private String nama;
    private String barcode;
    private String deskripsi;
    private String lokasi;
    private String foto;

    Barcode(){

    }
    public Barcode(String id, String nama, String barcode, String deskripsi, String lokasi, String foto){
        setId(id);
        setNama(nama);
        setBarcode(barcode);
        setDeskripsi(deskripsi);
        setLokasi(lokasi);
        setFoto(foto);
    }

    protected Barcode(Parcel in) {
        id = in.readString();
        nama = in.readString();
        barcode = in.readString();
        deskripsi = in.readString();
        lokasi = in.readString();
        foto = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
        dest.writeString(barcode);
        dest.writeString(deskripsi);
        dest.writeString(lokasi);
        dest.writeString(foto);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Barcode> CREATOR = new Creator<Barcode>() {
        @Override
        public Barcode createFromParcel(Parcel in) {
            return new Barcode(in);
        }

        @Override
        public Barcode[] newArray(int size) {
            return new Barcode[size];
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

    @Bindable
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
        notifyPropertyChanged(BR.barcode);
    }

    @Bindable
    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
        notifyPropertyChanged(BR.deskripsi);
    }

    @Bindable
    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
        notifyPropertyChanged(BR.lokasi);
    }

    @Bindable
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
        notifyPropertyChanged(BR.foto);
    }
}
