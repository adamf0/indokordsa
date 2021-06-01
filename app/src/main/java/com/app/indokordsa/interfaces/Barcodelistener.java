package com.app.indokordsa.interfaces;

public interface Barcodelistener {
    void onError(String message);
    void onFail(String message);
    void onSuccess(String message, String respon);
}