package com.app.indokordsa.interfaces;

public interface Profilelistener{
    void onFail(String message);
    void onError(String message);
    void onSuccess(String message);
    void onFailGet(String message);
    void onSuccessGet(String response);
}