package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;
import com.app.indokordsa.Util;
import com.google.android.material.snackbar.Snackbar;

public class Todo extends BaseObservable implements Parcelable {
    private String id;
    private String id_user;
    private String tanggal;
    private TodoArea area;
    private TodoGroup group;
    private TodoShift shift;
    private String time;
    private String remarks;
    private String action;
    private TodoStatus status;
    private TodoPIC pic;
    private String message;

    Todo(){

    }
    public Todo(String id,
            String id_user,
            String tanggal,
            TodoArea area,
            TodoGroup group,
            TodoShift shift,
            String time,
            String remarks,
            String action,
            TodoStatus status,
            TodoPIC pic
    )
    {
        setId(id);
        setId_user(id_user);
        setTanggal(tanggal);
        setArea(area);
        setGroup(group);
        setShift(shift);
        setTime(time);
        setRemarks(remarks);
        setAction(action);
        setStatus(status);
        setPic(pic);
        setMessage("");
    }

    protected Todo(Parcel in) {
        id = in.readString();
        id_user = in.readString();
        tanggal = in.readString();
        area = in.readParcelable(TodoArea.class.getClassLoader());
        group = in.readParcelable(TodoGroup.class.getClassLoader());
        shift = in.readParcelable(TodoShift.class.getClassLoader());
        time = in.readString();
        remarks = in.readString();
        action = in.readString();
        status = in.readParcelable(TodoShift.class.getClassLoader());
        pic = in.readParcelable(TodoPIC.class.getClassLoader());
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
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
    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
        this.notifyPropertyChanged(BR.id_user);
    }

    @Bindable
    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
        this.notifyPropertyChanged(BR.tanggal);
    }

    @Bindable
    public String getTanggalFormat() {
        return (TextUtils.isEmpty(getTanggal())? "":Util.reFormatDatev1(tanggal));
    }

    @Bindable
    public TodoArea getArea() {
        return area;
    }

    public void setArea(TodoArea area) {
        this.area = area;
        this.notifyPropertyChanged(BR.area);
    }

    @Bindable
    public TodoGroup getGroup() {
        return group;
    }

    public void setGroup(TodoGroup group) {
        this.group = group;
        this.notifyPropertyChanged(BR.group);
    }

    @Bindable
    public TodoShift getShift() {
        return shift;
    }

    public void setShift(TodoShift shift) {
        this.shift = shift;
        this.notifyPropertyChanged(BR.shift);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        this.notifyPropertyChanged(BR.time);
    }

    @Bindable
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
        this.notifyPropertyChanged(BR.remarks);
    }

    @Bindable
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
        this.notifyPropertyChanged(BR.action);
    }

    @Bindable
    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
        this.notifyPropertyChanged(BR.status);
    }

    @Bindable
    public TodoPIC getPic() {
        return pic;
    }

    public void setPic(TodoPIC pic) {
        this.pic = pic;
        this.notifyPropertyChanged(BR.pic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(id_user);
        dest.writeString(tanggal);
        dest.writeParcelable(area, flags);
        dest.writeParcelable(group, flags);
        dest.writeParcelable(shift, flags);
        dest.writeString(time);
        dest.writeString(remarks);
        dest.writeString(action);
        dest.writeParcelable(status, flags);
        dest.writeParcelable(pic, flags);
    }

    public boolean isValidation() {
        if (TextUtils.isEmpty(getTanggal())) {
            setMessage("Date cannot be empty");
            return false;
        }
        else if(getArea().getId()<=0 || !getArea().isValid()){
            setMessage("Area cannot be empty");
            return false;
        }
        else if(getGroup().getId()<=0 || !getGroup().isValid()){
            setMessage("Group cannot be empty");
            return false;
        }
        else if(getShift().getId()<=0 || !getShift().isValid()){
            setMessage("Shift cannot be empty");
            return false;
        }
        else if (TextUtils.isEmpty(getTime())) {
            setMessage("Time cannot be empty");
            return false;
        }
        else if (TextUtils.isEmpty(getAction())) {
            setMessage("Action cannot be empty");
            return false;
        }
        else if(getStatus().getId()<=0 || !getStatus().isValid()){
            setMessage("Status cannot be empty");
            return false;
        }
        else if(getPic().getId()<=0 || !getPic().isValid()){
            setMessage("Pic cannot be empty");
            return false;
        }
        else{
            setMessage(null);
        }

        return true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
