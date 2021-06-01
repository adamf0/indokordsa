package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Checklistlistener;

public class CheckListViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public Checklistlistener listener;
    public SessionManager session;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CheckListViewModel(listener, session);
    }

    public CheckListViewModelFactory(Checklistlistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
}
