package com.app.indokordsa.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

import java.io.File;

public class User extends BaseObservable implements Parcelable {
    private String id;
    private String username;
    private String password;
    private String active;
    private String email;
    private String phone;
    private String image;
    private File image_file;
    private String nama;

    private String message;
    private int level_warning;
    public User(){

    }
    public User(String id, String username, String active, String email, String phone, String image, File image_file, String nama){
        setId(id);
        setUsername(username);
        setActive(active);
        setEmail(email);
        setPhone(phone);
        setImage(image);
        setImage_file(image_file);
        setNama(nama);
        setMessage(null);
        setPassword(null);
        setLevel_warning(-1);
    }

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        password = in.readString();
        active = in.readString();
        email = in.readString();
        phone = in.readString();
        image = in.readString();
        nama = in.readString();
        message = in.readString();
        level_warning = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(active);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(image);
        dest.writeString(nama);
        dest.writeString(message);
        dest.writeInt(level_warning);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
        notifyPropertyChanged(BR.active);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    @Bindable
    public String getPhone62() {
        return phone.replaceFirst("0", "+62");
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
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
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public int getLevel_warning() {
        return level_warning;
    }

    public void setLevel_warning(int level_warning) {
        this.level_warning = level_warning;
        notifyPropertyChanged(BR.level_warning);
    }

    public boolean isValidation() {
        if(TextUtils.isEmpty(getUsername())){
            setMessage("Username tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getEmail())){
            setMessage("Email tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getPhone())){
            setMessage("Nomor Telp tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getNama())){
            setMessage("Nama tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getId())){
            setMessage("id pengguna masih kosong");
            return false;
        }
        else {
            setMessage(null);
            return true;
        }
    }

    @Bindable
    public File getImage_file() {
        return image_file;
    }

    public void setImage_file(File image_file) {
        this.image_file = image_file;
        notifyPropertyChanged(BR.image_file);
    }
}
