package com.app.indokordsa.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.interfaces.Barcodelistener;
import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class BarcodeViewModel extends ViewModel {
    public Barcodelistener listener;

    public BarcodeViewModel(Barcodelistener listener){
        this.listener = listener;
    }

    public void doLogin(String barcode){
        if(barcode.length()==0){
            listener.onError("Barcode must be scanned");
        }
        else {
            ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
            Call<String> call = getResponse.loginV2("api/loginV2", barcode);
            Log.i("app-log [Barcode]", "request to " + call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                    String res_ = response.body();

                    if (response.isSuccessful()) {
                        Log.i("app-log [Barcode]", res_);
                        try {
                            assert res_ != null;
                            JSONObject res = new JSONObject(res_);

                            String status = res.getString("status");
                            String message = res.getString("message");
                            if (status.equals("1")) {
                                listener.onSuccess(message, res.getString("data"));
                            } else {
                                listener.onFail(message);
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
                    Log.i("app-log [Barcode]", t.toString());
                    if (call.isCanceled()) {
                        listener.onFail("Request was aborted");
                    } else {
//                        listener.onFail(t.getMessage());
                        listener.onFail("Fail connect to server");
                    }
                }
            });
        }
    }
}