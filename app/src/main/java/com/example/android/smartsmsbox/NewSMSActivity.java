package com.example.android.smartsmsbox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class NewSMSActivity extends AppCompatActivity {
    private EditText ed1;
    private EditText ed2;
    private Button button;
    private DatabaseHelper dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sms);
        ed1 = findViewById(R.id.to);
        ed2 = findViewById(R.id.message);
        button = findViewById(R.id.send);
        checkSMSPermissions();
        checkPhonePermissions();

        dh = new DatabaseHelper(this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = ed1.getText().toString();
                String message  = ed2.getText().toString();
                sendSMS(phoneNumber, message);
            }
        });
    }

    private void sendSMS(String phoneNumber, String message) {
        SMS s = new SMS();
        s.setLatitude(SmsService.getLastLocation().getLatitude());
        s.setLongitude(SmsService.getLastLocation().getLongitude());
        s.setTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        s.setBody(message);
        s.setSender(phoneNumber);
        s.setType(2);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        dh.insertSMS(s);
    }

    public void checkSMSPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("abc", "9");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.i("abc", "2");
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        20);
                Log.i("abc", "40");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
//            readInbox();

            // Permission has already been granted
        }
    }

    public void checkPhonePermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("abc", "9");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.i("abc", "2");
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        20);
                Log.i("abc", "40");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
//            readInbox();

            // Permission has already been granted
        }
    }

}
