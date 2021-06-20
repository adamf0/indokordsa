package com.app.indokordsa.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Questionnairelistener;
import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class QuestionnaireViewModel extends ViewModel {
    public Questionnairelistener listener;
    SessionManager session;

    public QuestionnaireViewModel(Questionnairelistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;
    }
    public void updateKuesioner(String id_kuesioner_result_detail, String id_kuesioner_result, String id_user, String jawaban, String result, int type){
        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.save_questionneir("api/save_questionneir", id_kuesioner_result_detail,id_kuesioner_result, id_user, jawaban, result);

        Log.i("app-log [Questionneir]", "request to " + call.request().url().toString());
        Log.i("app-log [Questionneir]", String.format("id_kuesioner_result_detail=%s id_kuesioner_result=%s id_user=%s jawaban=%s result=%s",id_kuesioner_result_detail,id_kuesioner_result,id_user,jawaban,result));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [Questionneir]", res_);
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if (status.equals("1")) {
                            listener.onSuccessPost(message,type);
                        } else {
                            listener.onFailPost(message,(jawaban.equals("1")? 0:1));
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
                Log.i("app-log [Questionneir]", t.toString());
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
