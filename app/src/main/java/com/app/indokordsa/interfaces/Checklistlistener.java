package com.app.indokordsa.interfaces;

import com.app.indokordsa.view.model.Job;

public interface Checklistlistener {
    void onSuccessGet(String response);
    void onFailGet(String message);
    void onFail(String message);
    void onError(String message);

    void onSuccessPost(String message,boolean isEndTask);
    void onFailPost(String message);
    void onSuccessPostAlternative(String message,boolean isEndTask);
    void onFailPostAlternative(String message,boolean isEndTask);

//    void loadData(Job job);
}
