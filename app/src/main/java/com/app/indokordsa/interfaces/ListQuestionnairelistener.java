package com.app.indokordsa.interfaces;

import com.app.indokordsa.view.model.KuesionerResult;

public interface ListQuestionnairelistener {
    void onSelect(KuesionerResult kuesionerResult);
    void onFail(String message);
    void onFailGet(String message);
    void onSuccessGet(String response);
}
