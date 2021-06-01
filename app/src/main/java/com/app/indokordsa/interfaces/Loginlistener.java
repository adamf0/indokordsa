package com.app.indokordsa.interfaces;

public interface Loginlistener{
    void onErrorValidation(String message);
    void onFailAuth(String message);
    void onSuccessAuth(String message, String respon);
}
