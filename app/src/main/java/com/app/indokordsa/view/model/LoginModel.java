package com.app.indokordsa.view.model;

import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.indokordsa.BR;

public class LoginModel extends BaseObservable {
    private String username;
    private String password;
    private String message;

    public LoginModel() {
    }

    public LoginModel(String username, String password) {
        setUsername(username);
        setPassword(password);
        setMessage(null);
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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    public boolean isValidation() {
        if(TextUtils.isEmpty(getUsername())){
            setMessage("Username cannot be empty");
            return false;
        }
        else if(TextUtils.isEmpty(getPassword())){
            setMessage("Password cannot be empty");
            return false;
        }
        else
            setMessage(null);

        return true;
//        return !TextUtils.isEmpty(getEmail()) &&
//                Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches() &&
//                !TextUtils.isEmpty(getPassword()) &&
//                getPassword().length() > 3;
    }
}