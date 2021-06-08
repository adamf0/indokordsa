package com.app.indokordsa;

//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.util.Log;
//
//import com.app.indokordsa.helper.SessionManager;
//import com.app.indokordsa.record.db.DB;
//import com.app.indokordsa.view.model.CheckList;
//import com.app.indokordsa.view.model.JawabanKuesioner;
//import com.app.indokordsa.view.model.Job;
//import com.app.indokordsa.view.model.KuesionerResult;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.concurrent.TimeoutException;

//public class SendingQuestionnaire extends BroadcastReceiver {
public class SendingQuestionnaire{
//    ConnectionFactory factory = new ConnectionFactory();
//    DB db;

//    public void onReceive(final Context context, Intent intent) {
//        Log.i("app-log","Run Service Sender");
//
//        db = new DB(context);
//        new Thread(this::init).start();
//    }

//    private void init() {
//        try{
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.DATE, -1);
//            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
//            String StartDate = sdf0.format(cal.getTime());
//
//            Calendar c = Calendar.getInstance();
//            c.setTime(new Date());
//            Date end = c.getTime();
//
//            ArrayList<KuesionerResult> list_data = new ArrayList<>(db.getListKuesioner(StartDate,new SimpleDateFormat("yyyy-MM-dd").format(end)));
//            ArrayList<String> list_id = new ArrayList<>();
//            JSONArray arr0 = new JSONArray();
//            for (int i=0;i<list_data.size();i++){
//                JSONObject obj0 = new JSONObject();
//                list_id.add(list_data.get(i).getId_kuesioner_result());
//                obj0.put("id_kuesioner_result",list_data.get(i).getId_kuesioner_result());
//                obj0.put("id_user",list_data.get(i).getId_user());
//                obj0.put("jawaban",list_data.get(i).getJawaban());
//
//                JSONArray arr1 = new JSONArray();
//                ArrayList<JawabanKuesioner> list_pertanyaan = new ArrayList<>(list_data.get(i).getList_pertanyaan());
//                for (int j=0;j<list_pertanyaan.size();j++){
//                    JSONObject obj1 = new JSONObject();
//
//                    JawabanKuesioner jk = list_pertanyaan.get(i);
//                    obj1.put("id_kuesioner_detail_1",jk.getTopik().getId());
//                    obj1.put("id_kuesioner_detail_2",jk.getPertanyaan().getId());
//                    obj1.put("val",jk.getVal());
//                    obj1.put("other",jk.getOther());
//                    obj1.put("start",jk.getStart());
//                    obj1.put("end",jk.getEnd());
//                    obj1.put("remarks",jk.getRemarks());
//                    arr1.put(obj1);
//                }
//                obj0.put("result",arr1);
//                obj0.put("alasan",list_data.get(i).getAlasan());
//                arr0.put(obj0);
//            }
//            Log.i("app-log [pending questionnaire]",arr0.toString());
//
//            if(arr0.length()>0) {
////                factory = new ConnectionFactory();
////                factory.setHost("192.168.219.35"); //wsl
////                factory.setPort(5672);
////                factory.setUsername("test");
////                factory.setPassword("test");
//            factory.setHost("barnacle.rmq.cloudamqp.com");
//            factory.setPort(5672);
//            factory.setUsername("hnnmtjju");
//            factory.setPassword("aGkn6Ia-bsidQZ7X_vtCGs0y_g3I03og");
//            factory.setVirtualHost("hnnmtjju");
//
//                Connection connection;
//                Channel channel;
//                connection = factory.newConnection();
//                channel = connection.createChannel();
//
//                if (channel.isOpen()) {
//                    channel.queueDeclare("queue_questionnaire", false, false, false, null);
//                    channel.basicPublish("", "queue_questionnaire", null, arr0.toString().getBytes());
//                    Log.i("app-log", " [x] Sent '" + arr0.toString());
//                    channel.close();
//                    connection.close();
//
//                    for (String _id:list_id) {
//                        Log.i("app-log background",_id);
//                        db.update_sinkron_kuesioner_result(_id,"1");
//                    }
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}