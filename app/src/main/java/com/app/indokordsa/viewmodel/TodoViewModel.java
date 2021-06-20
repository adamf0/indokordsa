package com.app.indokordsa.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Todolistlistener;
import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class TodoViewModel extends ViewModel {
    SessionManager session;
    Todolistlistener listener;

    public TodoViewModel(Todolistlistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
    public void loadAllParam(){
        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.getData("api/getAllParamTodoList");

        Log.i("app-log [Todolist]", "request to " + call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [Todolist]", res_);
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if (status.equals("1")) {
                            listener.onSuccessGetParam(res.getString("data"));
                        } else {
                            listener.onFailGetParam(message);
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
                Log.i("app-log [Todolist]", t.toString());
                if (call.isCanceled()) {
                    listener.onError("Request was aborted");
                } else {
//                    listener.onError(t.getMessage());
                    listener.onError("Fail connect to server");
                }
            }
        });
    }
    public void save_todolist(String id_todolist, String id_user, String tanggal, String id_area, String id_group, String id_shift, String time, String remarks, String action, String id_status, String id_pic, boolean isUpdate){
        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.save_todolist((!isUpdate? "api/save_todolist":"api/update_todolist"), id_todolist, id_user, tanggal, id_area, id_group, id_shift, time, remarks, action, id_status, id_pic);
        Log.i("app-log [Todolist]", "request to " + call.request().url().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [Todolist]", res_);
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if (status.equals("1")) {
                            listener.onSuccessPost(message);
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
                Log.i("app-log [Todolist]", t.toString());
                if (call.isCanceled()) {
                    listener.onError("Request was aborted");

                } else {
//                    listener.onError(t.getMessage());
                    listener.onError("Fail connect to server");
                }
            }
        });
    }
}
