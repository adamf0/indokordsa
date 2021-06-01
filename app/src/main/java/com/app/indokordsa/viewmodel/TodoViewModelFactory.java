package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListTodolistlistener;
import com.app.indokordsa.interfaces.Todolistlistener;

public class TodoViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public Todolistlistener listener;
    public SessionManager session;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TodoViewModel(listener, session);
    }

    public TodoViewModelFactory(Todolistlistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
}
