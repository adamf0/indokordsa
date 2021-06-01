package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.interfaces.Loginlistener;

public class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public Loginlistener Loginlistener;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(Loginlistener);
    }

    public LoginViewModelFactory(Loginlistener Loginlistener){
        this.Loginlistener = Loginlistener;
    }
}
