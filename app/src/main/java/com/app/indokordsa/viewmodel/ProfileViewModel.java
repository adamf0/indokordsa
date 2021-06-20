package com.app.indokordsa.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Profilelistener;
import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;
import com.app.indokordsa.view.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileViewModel extends ViewModel {
    public User model;
    public Profilelistener listener;
    SessionManager session;

    public ProfileViewModel(Profilelistener listener, SessionManager session){
        this.listener = listener;
        this.session = session;

        HashMap<String, String> data = session.getSession();
        model = new User(data.get(SessionManager.KEY_ID_USER), "", "", "", "", "", null,"");
    }

    public void updateProfile(){
        Log.i("app-log [profile]",
                model.getId()+"-"+
                        model.getUsername()+"-"+
                        model.getEmail()+"-"+
                        model.getPhone()+"-"+
                        model.getNama());

        if(!model.isValidation()) {
            listener.onFail(model.getMessage());
        }
        else {
            ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);

            RequestBody Req_id = RequestBody.create(MediaType.parse("multipart/form-data"), model.getId());
            RequestBody Req_username = RequestBody.create(MediaType.parse("multipart/form-data"), model.getUsername());
            RequestBody Req_email = RequestBody.create(MediaType.parse("multipart/form-data"), model.getEmail());
            RequestBody Req_phone = RequestBody.create(MediaType.parse("multipart/form-data"), model.getPhone());
            RequestBody Req_nama = RequestBody.create(MediaType.parse("multipart/form-data"), model.getNama());

            File file_logo = model.getImage_file();
            MultipartBody.Part Req_image=null;
            if(file_logo!=null) {
                RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file_logo);
                Req_image = MultipartBody.Part.createFormData("image", file_logo.getName(), requestFile1);
            }

            Log.i("app-log file",(model.getImage_file()==null? "-":"ada"));
            Call<String> call = (model.getImage_file()==null?
                    getResponse.updateProfilev2("api/update_profile",
                        Req_id,
                        Req_username,
                        Req_email,
                        Req_phone,
                        Req_nama
                    )
                    :
                    getResponse.updateProfile("api/update_profile",
                            Req_id,
                            Req_username,
                            Req_email,
                            Req_phone,
                            Req_nama,
                            Req_image
                    )
            );

            Log.i("app-log [profile]", "request to " + call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res_ = response.body();

                    if (response.isSuccessful()) {
                        Log.i("app-log [profile]", res_);
                        try {
                            assert res_ != null;
                            JSONObject res = new JSONObject(res_);

                            String status = res.getString("status");
                            String message = res.getString("message");
                            if (status.equals("1")) {
                                listener.onSuccess(message);
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
                    Log.i("app-log [profile]", t.toString());
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

    public void loadData(){
        ApiRoute getResponse = AppConfig.getRetrofit(0).create(ApiRoute.class);
        Call<String> call = getResponse.getData(String.format("api/profile/%s", this.model.getId()));

        Log.i("app-log [profile]", "request to " + call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();

                if (response.isSuccessful()) {
                    Log.i("app-log [profile]", res_);
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
                Log.i("app-log [profile]", t.toString());
                if (call.isCanceled()) {
                    listener.onFail("Request was aborted");
                } else {
//                    listener.onFail(t.getMessage());
                    listener.onFail("Fail connect to server");
                }
            }
        });
    }
}
