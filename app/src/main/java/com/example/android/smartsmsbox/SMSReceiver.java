package com.example.android.smartsmsbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMS Receiver";
    private Location mCurrentLocation;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            //kendi sms türüne çevir
            SMS sms = new SMS();
            sms.setCategory(0);
            sms.setType(1);
            Calendar c = Calendar.getInstance();
            sms.setTime(String.valueOf(c.getTimeInMillis()));
            sms.setRead(false);
            sms.setSender(msgs[0].getDisplayOriginatingAddress());
            sms.setBody(msgs[0].getMessageBody());

            mCurrentLocation = SmsService.getLastLocation();

            if (mCurrentLocation != null) {
                sms.setLongitude(mCurrentLocation.getLongitude());
                sms.setLatitude(mCurrentLocation.getLatitude());
            }

//            Intent i = new Intent(context, SmsService.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
//
//            Notification.Builder mBuilder = new Notification.Builder(context, "0")
//                    .setSmallIcon(R.mipmap.ic_launcher_round)
//                    .setContentTitle("New Sms!")
//                    .setContentText(sms.getBody())
//                    .setContentIntent(pendingIntent);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//
//// notificationId is a unique int for each notification that you must define
//            notificationManager.notify(0, mBuilder.build());

            Context mContext = context;
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext.getApplicationContext(), "notify_001");
            Intent ii = new Intent(mContext.getApplicationContext(), SmsService.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, 0);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(sms.getBody());
            bigText.setBigContentTitle(sms.getSender());

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setContentTitle(sms.getSender());
            mBuilder.setContentText(sms.getBody());
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManager mNotificationManager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("notify_001",
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationManager.createNotificationChannel(channel);
            }

            mNotificationManager.notify(0, mBuilder.build());

            SharedPreferences sp = context.getSharedPreferences("STATUS", MODE_PRIVATE);
            boolean check;
            check = sp.getBoolean("active", false);

            if (check) {
                //calisan listeye ekle
                MainActivity.addToList(sms);
                //recycler viewi updatele
                MainActivity.notifyAddedData();
            }

            DatabaseHelper dh = new DatabaseHelper(context);
            dh.insertSMS(sms);

            Log.d(TAG, String.valueOf(sms.getLatitude()));

//            ChatActivity.notifyDataAdded(sms);
            //TODO notification oluştur
        }
    }


}
