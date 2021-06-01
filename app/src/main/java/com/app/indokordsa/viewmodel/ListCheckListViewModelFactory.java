package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListChecklistlistener;
import com.app.indokordsa.interfaces.Profilelistener;

public class ListCheckListViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public ListChecklistlistener listener;
    public SessionManager session;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListCheckListViewModel(listener, session);
    }

    public ListCheckListViewModelFactory(ListChecklistlistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
}
