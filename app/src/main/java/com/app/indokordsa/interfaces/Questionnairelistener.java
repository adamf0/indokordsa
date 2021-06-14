package com.app.indokordsa.interfaces;

public interface Questionnairelistener {
    void onFail(String message);
    void onError(String message);
    void onSuccessPost(String message,int type);
    void onFailPost(String message,int type);
}
