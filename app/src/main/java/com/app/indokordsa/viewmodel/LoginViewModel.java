package com.app.indokordsa.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;
import com.app.indokordsa.interfaces.Loginlistener;
import com.app.indokordsa.view.model.LoginModel;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginViewModel extends ViewModel {
    public LoginModel model;
    private Loginlistener loginlistener;

    public LoginViewModel(Loginlistener loginlistener){
        this.model = new LoginModel("","");
        this.loginlistener = loginlistener;
    }
    public void doLogin(){
        if(!model.isValidation()){
            loginlistener.onErrorValidation(model.getMessage());
        }
        else {
            ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
            Call<String> call = getResponse.login("api/login", model.getUsername(), model.getPassword());
            Log.i("app-log [login]", "request to " + call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res_ = response.body();

                    if (response.isSuccessful()) {
                        Log.i("app-log [login]", res_);
                        try {
                            assert res_ != null;
                            JSONObject res = new JSONObject(res_);

                            String status = res.getString("status");
                            String message = res.getString("message");
                            if (status.equals("1")) {
                                loginlistener.onSuccessAuth(message, res.getString("data"));
                            } else {
                                loginlistener.onFailAuth(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        loginlistener.onFailAuth("Fail connect to server");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("app-log [login]", t.toString());
                    if (call.isCanceled()) {
                        loginlistener.onFailAuth("Request was aborted");
                    } else {
//                        loginlistener.onFailAuth(t.getMessage());
                        loginlistener.onFailAuth("Fail connect to server");
                    }
                }
            });
        }
    }

    public Loginlistener getLoginlistener() {
        return loginlistener;
    }

}