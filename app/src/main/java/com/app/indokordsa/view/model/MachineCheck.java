package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class MachineCheck extends BaseObservable implements Parcelable {
    private String id;
    private Machine mesin;

    MachineCheck(){

    }
    public MachineCheck(String id, Machine mesin){
        setId(id);
        setMesin(mesin);
    }

    protected MachineCheck(Parcel in) {
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MachineCheck> CREATOR = new Creator<MachineCheck>() {
        @Override
        public MachineCheck createFromParcel(Parcel in) {
            return new MachineCheck(in);
        }

        @Override
        public MachineCheck[] newArray(int size) {
            return new MachineCheck[size];
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
    public Machine getMesin() {
        return mesin;
    }

    public void setMesin(Machine mesin) {
        this.mesin = mesin;
        notifyPropertyChanged(BR.mesin);
    }
}
