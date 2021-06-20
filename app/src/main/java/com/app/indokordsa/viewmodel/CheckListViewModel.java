package com.app.indokordsa.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Checklistlistener;
import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class CheckListViewModel extends ViewModel {
    SessionManager session;
    Checklistlistener listener;

    public CheckListViewModel(Checklistlistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }

    public void loadData(String id_checklist){
        HashMap<String, String> data = session.getSession();

        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.getData(String.format("api/get_checklist/%s/%s", data.get(SessionManager.KEY_ID_USER),id_checklist));

        Log.i("app-log [CheckList]", "request to " + call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [CheckList]", res_);
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if (status.equals("1")) {
                            listener.onSuccessGet(res.getString("data"));
                        } else {
                            listener.onFailGet(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    listener.onFail("Fail connect to server");
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                Log.i("app-log [CheckList]", t.toString());
                if (call.isCanceled()) {
                    listener.onFail("Request was aborted");
                } else {
//                    listener.onFail(t.getMessage());
                    listener.onFail("Fail connect to server");
                }
            }
        });
    }
    public void save_checklist(String id_penugasan, String id_operator, String no, String val, String ket, boolean isEndTask){
        ApiRoute getResponse = AppConfig.getRetrofit(30).create(ApiRoute.class);
        Call<String> call = getResponse.save_checklist("api/save_checklist", id_penugasan, id_operator, no, val, ket);
        Log.i("app-log [CheckList]", "request to " + call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [CheckList]", res_);
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if (status.equals("1")) {
                            listener.onSuccessPost(message,isEndTask);
                        } else {
                            listener.onFailPost(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    listener.onError("Fail connect to server");
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                Log.i("app-log [CheckList]", t.toString());
                if (call.isCanceled()) {
//                    listener.onError("Request was aborted");
                    listener.onFailPostAlternative("The data is successfully saved to local storage and the server data update is done in the background of the application.",isEndTask);

                } else {
//                    listener.onError(t.getMessage());
                    listener.onFailPostAlternative("The data is successfully saved to local storage and the server data update is done in the background of the application.",isEndTask);
                }
            }
        });
    }
}
