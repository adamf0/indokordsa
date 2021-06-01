package com.app.indokordsa.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListChecklistlistener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class ListCheckListViewModel extends ViewModel {
    SessionManager session;
    ListChecklistlistener listener;

    public ListCheckListViewModel(ListChecklistlistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
    public void loadData(){
        HashMap<String, String> data = session.getSession();

        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.getData(String.format("api/get_checklist_data/%s", data.get(SessionManager.KEY_ID_USER)));

        Log.i("app-log [ListCheckList]", "request to " + call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [ListCheckList]", res_);
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
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [ListCheckList]", t.toString());
                if (call.isCanceled()) {
                    listener.onFail("Request was aborted");
                } else {
                    listener.onFail(t.getMessage());
                }
            }
        });
    }
    public void update_alasan_checklist(String id_penugasan, String alasan){
        HashMap<String, String> data = session.getSession();

        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.update_alasan_checklist("api/update_alasan_checklist", id_penugasan, alasan);

        Log.i("app-log [ListCheckList]", "request to " + call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [ListCheckList]", res_);
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if (status.equals("1")) {
                            listener.onSuccessPost(res.getString("data"));
                        } else {
                            listener.onFailPost(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    listener.onFail("Fail connect to server");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [ListCheckList]", t.toString());
                if (call.isCanceled()) {
                    listener.onFail("Request was aborted");
                } else {
                    listener.onFail(t.getMessage());
                }
            }
        });
    }
}
