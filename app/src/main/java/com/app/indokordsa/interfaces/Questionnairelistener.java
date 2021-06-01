package com.app.indokordsa.interfaces;

import com.app.indokordsa.view.model.KuesionerResultDetail2;

public interface Questionnairelistener {
    void select(KuesionerResultDetail2 item);
    void onFail(String message);
    void onError(String message);
    void onSuccessPost(String message);
    void onFailPost(String message);
}
