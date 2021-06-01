package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListTodolistlistener;

public class TodoListViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public ListTodolistlistener listener;
    public SessionManager session;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TodoListViewModel(listener, session);
    }

    public TodoListViewModelFactory(ListTodolistlistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
}
