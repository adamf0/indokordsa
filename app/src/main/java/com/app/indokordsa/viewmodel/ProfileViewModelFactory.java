package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.interfaces.Profilelistener;
import com.app.indokordsa.helper.SessionManager;

public class ProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public Profilelistener Profilelistener;
    public SessionManager session;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProfileViewModel(Profilelistener, session);
    }

    public ProfileViewModelFactory(Profilelistener Profilelistener, SessionManager session){
        this.Profilelistener = Profilelistener;
        this.session = session;
    }
}
