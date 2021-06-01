package com.app.indokordsa.interfaces;

import com.app.indokordsa.view.model.Todo;

public interface ListTodolistlistener {
    void onEdit(Todo todo);
    void onDelete(Todo todo);
    void onFail(String message);
    void onError(String message);

    void onFailGet(String message);
    void onSuccessGet(String response);

    void onFailDelete(String message);
    void onSuccessDelete(String response);
}
