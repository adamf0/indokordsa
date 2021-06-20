package com.app.indokordsa.interfaces;

public interface Todolistlistener {
    void onFail(String message);
    void onError(String message);

    void onFailGetParam(String message);
    void onSuccessGetParam(String response);

    void onSuccessPost(String message);
    void onFailPost(String message);
}
