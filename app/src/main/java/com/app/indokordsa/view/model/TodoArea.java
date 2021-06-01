package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class TodoArea extends BaseObservable implements Parcelable {
    private int id;
    private String nama;

    TodoArea(){

    }
    public TodoArea(int id, String nama){
        setId(id);
        setNama(nama);
    }

    protected TodoArea(Parcel in) {
        id = in.readInt();
        nama = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nama);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TodoArea> CREATOR = new Creator<TodoArea>() {
        @Override
        public TodoArea createFromParcel(Parcel in) {
            return new TodoArea(in);
        }

        @Override
        public TodoArea[] newArray(int size) {
            return new TodoArea[size];
        }
    };

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public boolean isValid() {
        return id>=0;
    }

    @Override
    public String toString() {
        return nama;
    }
}
