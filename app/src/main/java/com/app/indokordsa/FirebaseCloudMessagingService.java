package com.app.indokordsa;

//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//
//import com.app.indokordsa.view.ui.LoginActivity;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Random;

public class FirebaseCloudMessagingService{
//public class FirebaseCloudMessagingService extends FirebaseMessagingService {
//    public String TAG = "FCM";
//    public static String daily7_notification_channel_id = "10001";
//    public static String default_notification_channel_id = "default";
//    int low = 0;
//    int high = Integer.MAX_VALUE;
//
//    String title = "App Indokorsa";
//    String description = "";
//    String payload = null;
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        if (remoteMessage.getData().size() > 0) {
//            payload = remoteMessage.getData().toString();
//            Log.d(TAG, "Message data payload: " + payload);
//        }
//        if (remoteMessage.getNotification() != null) {
//            title = remoteMessage.getNotification().getTitle();
//            description = remoteMessage.getNotification().getBody();
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        getNotifikasi(getBaseContext(), title, description, payload);
//    }
//
//    void getNotifikasi(Context context, String title, String description, String payload){
//        Random r = new Random();
//        int i = r.nextInt(high-low) + low;
//
//        Intent notificationIntent = new Intent(context, LoginActivity.class);
//        notificationIntent.putExtra("payload",payload);
//        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
//
//        PendingIntent pendingIntent = PendingIntent. getActivity ( context, i , notificationIntent , 0 ) ;
//
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService( NOTIFICATION_SERVICE ) ;
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context , default_notification_channel_id ) ;
//
//        mBuilder.setContentTitle(title);
//        mBuilder.setContentIntent(pendingIntent);
//        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(description));
//        mBuilder.setContentText(description);
//        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
//        mBuilder.setAutoCancel(true) ;
//        Log.i(TAG,"show notif");
//
//        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
//            int importance = NotificationManager. IMPORTANCE_HIGH ;
//            NotificationChannel notificationChannel = new NotificationChannel(daily7_notification_channel_id , "daily_notification_channel" , importance) ;
//            mBuilder.setChannelId(daily7_notification_channel_id) ;
//            assert mNotificationManager != null;
//            mNotificationManager.createNotificationChannel(notificationChannel) ;
//        }
//
//        Notification notification = mBuilder.build();
//        if (mNotificationManager != null) {
//            mNotificationManager.notify(i, notification);
//        }
//    }
//
//    public void onNewToken(@NotNull String token){
//        super.onNewToken(token);
//    }
}