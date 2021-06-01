package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListQuestionnairelistener;

public class ListQuestionnaireViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public ListQuestionnairelistener listener;
    public SessionManager session;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListQuestionnaireViewModel(listener, session);
    }

    public ListQuestionnaireViewModelFactory(ListQuestionnairelistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
}

