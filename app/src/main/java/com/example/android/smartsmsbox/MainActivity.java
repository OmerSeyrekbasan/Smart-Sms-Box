package com.example.android.smartsmsbox;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SMSAdapter.ItemClickListener {
    private static SMSAdapter adapter;
    private static ArrayList<SMS> smsList;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private static ArrayList<Contact> contactsList;
    private static ArrayList<Chat> chatsList;
    private ArrayList<Chat> whiteListChat;
    private ArrayList<Chat> commercialChat;
    private ArrayList<Chat> spamChat;
    private ArrayList<Chat> otpChat;
    private NavigationView navigationView;

    private FloatingActionButton send;

    private ArrayList<Contact> whiteList;
    private ArrayList<String> blackList;
    private int currentList;
    private enum Nav {
        Inbox, Personal, Commercial, Spam, OTP, Whitelist, Blacklist, Map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// TODO ilk kontrol ekle

        currentList = 0;
        contactsList = new ArrayList<Contact>();

        Intent smsService = new Intent(MainActivity.this, SmsService.class);
        startService(smsService);
        //checkPermissions
        checkSMSPermissions();
        checkContactsPermissions();
        checkLocationPermissions();

        makeWhiteList();
        getBlacklist();

//        for (Contact c : contactsList) {
//            Log.d("Contac= ", c.toString());
//        }
//        readSMSFromDatabase();
//        getContacts();

        //generate chat list
        getChats();


        // set up the RecyclerView
        recyclerView = findViewById(R.id.sms_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SMSAdapter(this, chatsList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        Toast.makeText(getApplicationContext(), menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        Nav n = Nav.valueOf(menuItem.getTitle().toString());
                        switch (n) {
                            case Map:
                                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                                i.putExtra("SMSLIST", smsList);
                                startActivity(i);
                                break;

                            case Inbox:
                                currentList = 0;
                                adapter.swapDataSet(chatsList);
                                break;

                            case Personal:
                                currentList = 1;
                                whiteListChat = new ArrayList<Chat>();
                                for (Chat c: chatsList) {
                                    if (c.getCategory() == 1)
                                        whiteListChat.add(c);
                                }
                                adapter.swapDataSet(whiteListChat);
                                break;

                            case Commercial:
                                currentList = 2;
                                commercialChat = new ArrayList<Chat>();
                                for (Chat c: chatsList) {
                                    if (c.getCategory() == 2 && c.getSize()>2)
                                        commercialChat.add(c);
                                }
                                adapter.swapDataSet(commercialChat);
                                break;

                            case Spam:
                                currentList = 3;
                                spamChat = new ArrayList<Chat>();
                                for (Chat c: chatsList) {
                                    if ((c.getCategory() != 1 && c.getCategory()!=4 && c.getSize()<2) ||
                                            blackList.contains(c.getAddress()))
                                        spamChat.add(c);
                                }
                                adapter.swapDataSet(spamChat);
                                break;

                            case OTP:
                                currentList = 4;
                                otpChat = new ArrayList<Chat>();
                                for (Chat c: chatsList) {
                                    if (c.getCategory() == 4)
                                        otpChat.add(c);
                                }
                                adapter.swapDataSet(otpChat);
                                break;

                            case Blacklist:
                                i = new Intent(MainActivity.this, BlacklistActivity.class);
                                startActivity(i);
                                break;



                        }
                        return true;
                    }
                });

        send = findViewById(R.id.send_sms);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewSMSActivity.class);
                startActivity(i);
            }
        });


    }

    public void checkSMSPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("abc", "9");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.i("abc", "2");
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        20);
                Log.i("abc", "40");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
//            readInbox();
            readSMSFromDatabase();
            // Permission has already been granted
        }
    }

    public void checkContactsPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("abc", "9");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.i("abc", "2");
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        30);
                Log.i("abc", "40");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
//            readInbox();
            getContacts();
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 20:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("abc", "3");
//                    readInbox();
                } else {
                    Log.i("abc", "4");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

            case 30:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("abc", "3");
                    getContacts();
                } else {
                    Log.i("abc", "4");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;


            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public void readSMSFromDatabase() {
        DatabaseHelper dh = new DatabaseHelper(this);
        smsList = dh.getAllSMS();
        for (SMS s : smsList) {
//            Log.d("SMS = ", String.valueOf(s.getType()));
//            smsList.add(s);
        }
    }

    public void readInbox() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
        ArrayList<SMS> smss = new ArrayList<SMS>();
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                SMS sms = new SMS();
                String sender = cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS"));
                if (!sender.contains("+")) {
                    if ((!sender.matches(".*[A-Z].*") && !sender.matches(".*[a-z].*")) && sender.length()>9) {
                        StringBuilder sb = new StringBuilder(sender);
                        sb.insert(0,"+9");
                        sender = sb.toString();
                    }
                }

                sms.setSender(sender.replace(" ", ""));

//                sms.setSender(cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS")));
                sms.setBody(cursor.getString(cursor.getColumnIndexOrThrow("BODY")));
                sms.setTime(cursor.getString(cursor.getColumnIndexOrThrow("DATE")));
                sms.setRead(Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("READ"))));
//                Log.d("msg",String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("Type"))));
                sms.setType(cursor.getInt(cursor.getColumnIndexOrThrow("Type")));
//                Log.d("sms",String.valueOf(sms.getType()));
                smss.add(sms);
//                Log.d("SMS: = ", sms.getBody() + " " +String.valueOf(sms.getType()));
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        DatabaseHelper dh = new DatabaseHelper(this);
        dh.onUpgrade(dh.getWritableDatabase(),0,1);
        for (SMS s : smss) {
            Log.d("sms",String.valueOf(s.getType()));
            dh.insertSMS(s);
        }

    }


    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked " + adapter.getItem(position)
//                + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent i;
        switch (currentList) {
            case 0:
                i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra("SMSLIST",chatsList.get(position).getSmsList());
                startActivity(i);
                break;
            case 1:
                i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra("SMSLIST",whiteListChat.get(position).getSmsList());
                startActivity(i);
                break;
            case 2:
                i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra("SMSLIST",commercialChat.get(position).getSmsList());
                startActivity(i);
                break;
            case 3:
                i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra("SMSLIST",spamChat.get(position).getSmsList());
                startActivity(i);
                break;
            case 4:
                i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra("SMSLIST",otpChat.get(position).getSmsList());
                startActivity(i);
                break;
        }


    }

    public void getContacts() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            Contact c = new Contact();
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            c.setName(name);
            c.setPhoneNo(phoneNumber);
            if (!c.getPhoneNo().contains("+")) {

                StringBuilder sb = new StringBuilder(c.getPhoneNo());
                sb.insert(0,"+9");
                c.setPhoneNo(sb.toString());
            }

            c.setPhoneNo(c.getPhoneNo().replace(" ", ""));

            if (!checkListForContact(c.getName())) {
                contactsList.add(c);
            }
        }
        phones.close();
    }

    public boolean checkListForContact(String name) {
        int i = 0;
        while (i < contactsList.size()) {
            if (contactsList.get(i).getName().equals(name)) {
                return true;
            }
            else i++;
        }
        return false;
    }

    public void getChats() {
        chatsList = new ArrayList<Chat>();
        for(SMS s : smsList) {
            String tmp = s.getSender();
            int result = checkListForAddress(tmp);
            if (result == -1) {
                Chat c1 = new Chat(tmp);
                c1.addSmsToList(s);
                if (blackList.contains(c1.getAddress())) {
                    c1.setCategory(3);
                } else {
                    int res = checkWhiteListForAdress(c1.getAddress());
                    if (res == 1){
                        c1.setName(findContactName(c1.getAddress()));
                        c1.setCategory(1);
                    } else if ((s.getBody().matches(".*[0-9]{4}.*") || s.getBody().matches(".*[0-9]{6}.*") ||
                            s.getBody().matches(".*[0-9]{3}-[0-9]{3}.*")) && s.getBody().length() < 150) {
                        c1.setCategory(4);
                    } else if(s.getBody().matches(".*B[0-9]{3}.*")) {
                        c1.setCategory(2);
                    }
                }

                chatsList.add(c1);
            } else {
                chatsList.get(result).addSmsToList(s);
            }
        }
    }

    public static int checkListForAddress(String address) {
        int i = 0;
        while (i < chatsList.size()) {
            if (chatsList.get(i).getAddress().equals(address)) {
                return i;
            }
            else i++;
        }
        return -1;
    }

    public static void addToList(SMS sms) {
        smsList.add(0, sms);
        String tmp = sms.getSender();
        int result = checkListForAddress(tmp);
        if (result == -1) {
            Chat c1 = new Chat(tmp);
            c1.addSmsToList(sms);
            chatsList.add(c1);
        } else {
            chatsList.get(result).addSmsToFrontofList(sms);
        }
    }

    public static void notifyAddedData() {
        adapter.swapDataSet(chatsList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("STATUS", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("STATUS", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.commit();

    }

    public void checkLocationPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                       90);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
        }
    }

    public void makeWhiteList() {
       whiteList =  FileManager.readFile("WhiteList", this);
       for (Contact c : contactsList) {
           if (!whiteList.contains(c)) {
               whiteList.add(c);
               FileManager.writeFile(c, "WhiteList", this);
           }
       }
       Log.d("WhiteList = ", whiteList.toString());
    }

    public int checkWhiteListForAdress(String a) {
        for (Contact c : whiteList) {
            if (c.getPhoneNo().equals(a)) {
                return 1;
            }
        }
        return -1;
    }

    public String findContactName(String phoneNo ) {
        for(Contact c : contactsList) {
            if (c.getPhoneNo().equals(phoneNo))
                return c.getName();
        }
        return "";
    }

    public void getBlacklist() {
        blackList = (ArrayList<String>) FileManager.readStringFile("Blacklist", this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getBlacklist();
        getChats();
    }

}
