package com.app.indokordsa.interfaces;

import com.app.indokordsa.view.model.KuesionerResult;

public interface ListQuestionnairelistener {
    void onSelect(KuesionerResult item);
    void onUpdate(KuesionerResult item);
    void onFail(String message);
    void onFailGet(String message);
    void onSuccessGet(String response);
    void onFailPost(String message);
    void onSuccessPost(String response, KuesionerResult item);
}
