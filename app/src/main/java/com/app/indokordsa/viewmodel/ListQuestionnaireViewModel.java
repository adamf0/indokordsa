package com.app.indokordsa.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.ListQuestionnairelistener;
import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;
import com.app.indokordsa.view.model.KuesionerResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

@SuppressLint("LongLogTag")
public class ListQuestionnaireViewModel extends ViewModel {
    SessionManager session;
    ListQuestionnairelistener listener;

    public ListQuestionnaireViewModel(ListQuestionnairelistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
    public void loadData(){
        HashMap<String, String> data = session.getSession();

        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.getData(String.format("api/get_questionneir_data/%s", data.get(SessionManager.KEY_ID_USER)));

        Log.i("app-log [ListQuestionneir]", "request to " + call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [ListQuestionneir]", res_);
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
                Log.i("app-log [ListQuestionneir]", t.toString());
                if (call.isCanceled()) {
                    listener.onFail("Request was aborted");
                } else {
                    listener.onFail(t.getMessage());
                }
            }
        });
    }
    public void update_alasan_questionnaire(String id_kuesioner_result, String alasan, KuesionerResult item){
        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.update_alasan_questionnaire("api/update_alasan_questionneir", id_kuesioner_result, alasan);

        Log.i("app-log [ListCheckList]", "request to " + call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [ListCheckList]", res_);
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if (status.equals("1")) {
                            listener.onSuccessPost(message,item);
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
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
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
