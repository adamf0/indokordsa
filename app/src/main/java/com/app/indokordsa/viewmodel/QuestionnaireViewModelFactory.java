package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Questionnairelistener;

public class QuestionnaireViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public Questionnairelistener Questionnairelistener;
    SessionManager session;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuestionnaireViewModel(Questionnairelistener,session);
    }

    public QuestionnaireViewModelFactory(Questionnairelistener Questionnairelistener, SessionManager session){
        this.Questionnairelistener = Questionnairelistener;
        this.session = session;
    }
}