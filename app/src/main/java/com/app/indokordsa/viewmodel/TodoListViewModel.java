package com.app.indokordsa.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListTodolistlistener;
import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class TodoListViewModel extends ViewModel {
    SessionManager session;
    ListTodolistlistener listener;

    public TodoListViewModel(ListTodolistlistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
    public void loadData(){
        HashMap<String, String> data = session.getSession();
        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.getData(String.format("api/getTodoList/%s", data.get(SessionManager.KEY_ID_USER)));

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
    public void deleteData(String id_todolist){
        HashMap<String, String> data = session.getSession();
        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.delete_todolist("api/delete_todolist", id_todolist,data.get(SessionManager.KEY_ID_USER));

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
                            listener.onSuccessDelete(res.getString("data"));
                        } else {
                            listener.onFailDelete(message);
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
}
