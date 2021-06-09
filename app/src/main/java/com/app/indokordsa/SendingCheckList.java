package com.app.indokordsa;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.indokordsa.record.api.ApiRoute;
import com.app.indokordsa.record.api.AppConfig;
import com.app.indokordsa.record.db.DB;
import com.app.indokordsa.view.model.CheckList;
import com.app.indokordsa.view.model.Job;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;

public class SendingCheckList extends BroadcastReceiver {
//public class SendingCheckList{
//    ConnectionFactory factory = new ConnectionFactory();
    DB db;

    public void onReceive(final Context context, Intent intent) {
        Log.i("app-log","Run Service Checklist");

        db = new DB(context);
        new Thread(this::init).start();
    }

    @SuppressLint("LongLogTag")
    private void init() {
        try{
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-01");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            String StartDate = sdf0.format(cal.getTime());

            String dateAsString = sdf1.format(new Date());
            Date dateFromString = sdf1.parse(dateAsString);
            assert dateFromString != null;

            Calendar c = Calendar.getInstance();
            c.setTime(dateFromString);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date end = c.getTime();

            ArrayList<CheckList> list_data = db.getListCheckList(StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end));
            JSONArray list = new JSONArray();
            ArrayList<String> list_id = new ArrayList<>();
            for (CheckList checkList:list_data){
                JSONArray list_tugas = new JSONArray();
                for (Job job:checkList.getTugas()) {
                    list_tugas.put(job.getTugas());
                }
                list.put(new JSONObject()
                        .put("id_penugasan",checkList.getId())
                        .put("id_operator",checkList.getOperator().getId())
                        .put("tugas",list_tugas)
                        .put("alasan",checkList.getAlasan())
                        .put("status",(checkList.isStatus()? "1":"0"))
                );
                list_id.add(checkList.getId());
            }
            Log.i("app-log [pending cheklist]",list.toString());

            if(list.length()>0) {
                ApiRoute getResponse = AppConfig.getRetrofitV2(10).create(ApiRoute.class);
                Call<String> call = getResponse.sync("api/receive_cheklist", list.toString());

                Log.i("app-log [Service Checklist]", "request to " + call.request().url().toString());
                call.enqueue(new Callback<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                        String res_ = response.body();
                        Log.i("app-log [Service Checklist]", res_);

                        if (response.isSuccessful()) {
                            try {
                                assert res_ != null;
                                JSONObject res = new JSONObject(res_);

                                String status = res.getString("status");
                                String message = res.getString("message");
                                if (status.equals("1")) {
                                    Log.i("app-log [Service Checklist]",message);
                                    for (String _id:list_id) {
                                        Log.i("app-log background",_id);
                                        db.update_sinkron_penugasan(_id,"1");
                                    }
                                } else {
                                    Log.i("app-log [Service Checklist]",message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("app-log [Service Checklist]", "Fail connect to server");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                        Log.i("app-log [Service Checklist]", t.toString());
                        if (call.isCanceled()) {
                            Log.i("app-log [Service Checklist]", "Request was aborted");
                        } else {
                            Log.i("app-log [Service Checklist]", t.getMessage());
                        }
                    }
                });
            }
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
//                        channel.queueDeclare("queue_cheklist", false, false, false, null);
//                        channel.basicPublish("", "queue_cheklist", null, list.toString().getBytes());
//                        Log.i("app-log", " [x] Sent '" + list.toString());
//                        channel.close();
//                        connection.close();
//
//                        for (String _id:list_id) {
//                            Log.i("app-log background",_id);
//                            db.update_sinkron_penugasan(_id,"1");
//                        }
//                    }
//                }
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }
    }
}