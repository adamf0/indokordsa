package com.app.indokordsa;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;
import com.app.indokordsa.record.db.DB;
import com.app.indokordsa.view.model.JawabanKuesioner;
import com.app.indokordsa.view.model.KuesionerResult;
import com.app.indokordsa.view.model.KuesionerResultDetail;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

import static com.app.indokordsa.Util.isNetworkAvailable;

//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;

public class SendingQuestionnaire extends BroadcastReceiver {
//    ConnectionFactory factory = new ConnectionFactory();
    DB db;

    public void onReceive(final Context context, Intent intent) {
        Log.i("app-log","Run Service Questionnaire");

        db = new DB(context);
        new Thread(this::init).start();
    }

    @SuppressLint("LongLogTag")
    private void init() {
        try{
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
            String StartDate = sdf0.format(cal.getTime());

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            Date end = c.getTime();
            String EndDate = new SimpleDateFormat("yyyy-MM-dd").format(end);


            ArrayList<KuesionerResult> list_data = new ArrayList<>(db.getAllTaskKuesioner(StartDate,EndDate));
            ArrayList<String> list_id = new ArrayList<>();
            JSONArray list_kuesioner_result = new JSONArray();
            for (int i=0;i<list_data.size();i++){
                KuesionerResult kr = list_data.get(i);
                list_id.add(kr.getId_kuesioner_result());

                JSONArray list_kuesioner_jawaban = new JSONArray();
                for (int j=0;j<kr.getList_kuesioner().size();j++){
                    KuesionerResultDetail krd = kr.getList_kuesioner().get(j);

                    JSONArray list_jawaban = new JSONArray();
                    for (int k=0;k<krd.getList_pertanyaan().size();k++){
                        JawabanKuesioner jk = krd.getList_pertanyaan().get(k);

                        JSONObject jawaban = new JSONObject();
                        jawaban.put("id_kuesioner_detail_1",jk.getTopik().getId());
                        jawaban.put("id_kuesioner_detail_2",jk.getPertanyaan().getId());
                        jawaban.put("val",jk.getVal());
                        jawaban.put("other",jk.getOther());
                        jawaban.put("start",jk.getStart());
                        jawaban.put("end",jk.getEnd());
                        jawaban.put("remarks",jk.getRemarks());
                        list_jawaban.put(jawaban);
                    }

                    JSONObject kuesioner_jawaban = new JSONObject();
                    kuesioner_jawaban.put("id",krd.getId());
                    kuesioner_jawaban.put("jawaban",krd.getJawaban());
                    kuesioner_jawaban.put("result",list_jawaban);
                    list_kuesioner_jawaban.put(kuesioner_jawaban);
                }

                JSONObject kuesioner_result = new JSONObject();
                kuesioner_result.put("id_kuesioner_result",list_data.get(i).getId_kuesioner_result());
                kuesioner_result.put("id_user",list_data.get(i).getId_user());
                kuesioner_result.put("alasan",list_data.get(i).getAlasan());
                kuesioner_result.put("kuesioner_jawaban",list_kuesioner_jawaban);
                list_kuesioner_result.put(kuesioner_result);
            }
            Log.i("app-log [pending questionnaire]",list_kuesioner_result.toString());

            if(list_kuesioner_result.length()>0) {
                ApiRoute getResponse = AppConfig.getRetrofitV2(10).create(ApiRoute.class);
                Call<String> call = getResponse.sync("api/receive_questionnaire", list_kuesioner_result.toString());

                Log.i("app-log [Service Questionnaire]", "request to " + call.request().url().toString());
                call.enqueue(new Callback<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                        String res_ = response.body();
                        Log.i("app-log [Service Questionnaire]", res_);

                        if (response.isSuccessful()) {
                            try {
                                assert res_ != null;
                                JSONObject res = new JSONObject(res_);

                                String status = res.getString("status");
                                String message = res.getString("message");
                                if (status.equals("1")) {
                                    Log.i("app-log [Service Questionnaire]",message);
                                    for (String _id:list_id) {
                                        Log.i("app-log background",_id);
                                        db.update_sinkron_kuesioner_result(_id,"1");
                                    }
                                } else {
                                    Log.i("app-log [Service Questionnaire]",message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("app-log [Service Questionnaire]", "Fail connect to server");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                        Log.i("app-log [Service Questionnaire]", t.toString());
                        if (call.isCanceled()) {
                            Log.i("app-log [Service Questionnaire]", "Request was aborted");
                        } else {
                            Log.i("app-log [Service Questionnaire]", t.getMessage());
                        }
                    }
                });

//                factory.setHost("barnacle.rmq.cloudamqp.com");
//                factory.setPort(5672);
//                factory.setUsername("hnnmtjju");
//                factory.setPassword("aGkn6Ia-bsidQZ7X_vtCGs0y_g3I03og");
//                factory.setVirtualHost("hnnmtjju");
//
//                    Connection connection;
//                    Channel channel;
//                    connection = factory.newConnection();
//                    channel = connection.createChannel();
//
//                    if (channel.isOpen()) {
//                        channel.queueDeclare("queue_questionnaire", false, false, false, null);
//                        channel.basicPublish("", "queue_questionnaire", null, arr0.toString().getBytes());
//                        Log.i("app-log", " [x] Sent '" + arr0.toString());
//                        channel.close();
//                        connection.close();
//
//                        for (String _id:list_id) {
//                            Log.i("app-log background",_id);
//                            db.update_sinkron_kuesioner_result(_id,"1");
//                        }
//                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}