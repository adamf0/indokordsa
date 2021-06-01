package com.app.indokordsa.interfaces;

import com.app.indokordsa.view.model.CheckList;
import com.app.indokordsa.view.model.Fitur;

public interface ListChecklistlistener {
    void onSelect(CheckList checkList);
    void onFail(String message);
    void onError(String message);
    void onUpdate(CheckList checkList);
    void onFailGet(String message);
    void onSuccessGet(String response);
    void onFailPost(String message);
    void onSuccessPost(String response);
}
