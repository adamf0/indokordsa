package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class TodoGroup extends BaseObservable implements Parcelable {
    private int id;
    private String nama;

    TodoGroup(){

    }
    public TodoGroup(int id, String nama){
        setId(id);
        setNama(nama);
    }

    protected TodoGroup(Parcel in) {
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

    public static final Creator<TodoGroup> CREATOR = new Creator<TodoGroup>() {
        @Override
        public TodoGroup createFromParcel(Parcel in) {
            return new TodoGroup(in);
        }

        @Override
        public TodoGroup[] newArray(int size) {
            return new TodoGroup[size];
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
