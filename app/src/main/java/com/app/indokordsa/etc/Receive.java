package com.app.indokordsa.etc;

//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.DefaultConsumer;
//import com.rabbitmq.client.Envelope;
//
//import java.io.IOException;
//import java.util.concurrent.TimeoutException;

//public class Receive extends BroadcastReceiver {
public class Receive{
//    ConnectionFactory factory = new ConnectionFactory();

//    public void onReceive(final Context context, Intent intent) {
//        Log.i("app-log","Run Service Sender");
//
//        Handler incomingMessageHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                String message = msg.getData().getString("msg");
//                Log.i("app-log", "msg:" + message);
//            }
//        };
//
//        new Thread(() -> basicConsume(incomingMessageHandler)).start();
//    }

//    private void basicConsume(final Handler handler){
//        try {
//            factory = new ConnectionFactory();
//            factory.setHost("192.168.219.35"); //wsl
//            factory.setPort(5672);
//            factory.setUsername("test");
//            factory.setPassword("test");
////            factory =  new ConnectionFactory();
////            factory.setHost("barnacle.rmq.cloudamqp.com");
////            factory.setPort(5672);
////            factory.setUsername("hnnmtjju");
////            factory.setPassword("aGkn6Ia-bsidQZ7X_vtCGs0y_g3I03og");
////            factory.setVirtualHost("hnnmtjju");
//
//            Connection connection = factory.newConnection() ;
//            final Channel channel = connection.createChannel() ;
//            channel.basicConsume("direct_logs" , false ,  new DefaultConsumer(channel){
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    super.handleDelivery(consumerTag, envelope, properties, body);
//
//                    String msg = new String(body) ;
//                    long deliveryTag = envelope.getDeliveryTag() ;
//                    channel.basicAck(deliveryTag , false);
//                    // Get the object from the message msg pool more efficient
//                    Message uimsg = handler.obtainMessage();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("msg", msg);
//                    uimsg.setData(bundle);
//                    handler.sendMessage(uimsg);
//
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
//    }
}