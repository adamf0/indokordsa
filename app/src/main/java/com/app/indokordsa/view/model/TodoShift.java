package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class TodoShift extends BaseObservable implements Parcelable {
    private int id;
    private String nama;

    TodoShift(){

    }
    public TodoShift(int id, String nama){
        setId(id);
        setNama(nama);
    }

    protected TodoShift(Parcel in) {
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

    public static final Creator<TodoShift> CREATOR = new Creator<TodoShift>() {
        @Override
        public TodoShift createFromParcel(Parcel in) {
            return new TodoShift(in);
        }

        @Override
        public TodoShift[] newArray(int size) {
            return new TodoShift[size];
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
