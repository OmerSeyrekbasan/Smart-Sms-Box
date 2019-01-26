package com.example.android.smartsmsbox;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class FileManager {
    private static final String TAG = "FileManager";

    public static void writeFile(Contact contact, String filename,Context context) {
        ArrayList<Contact> list = new ArrayList<Contact>();
        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            list = (ArrayList<Contact>) is.readObject();
            is.close();
            fis.close();

        } catch (FileNotFoundException e) {
            list.add(contact);
            try {
                FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(list);
                fos.flush();
                os.close();
                fos.close();
            } catch (Exception except) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        list.add(contact);

        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(list);
            fos.flush();
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public static ArrayList<Contact> readFile(String filename, Context context) {

        ArrayList<Contact> list = new ArrayList<Contact>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            list = (ArrayList<Contact>) is.readObject();
            is.close();
            fis.close();

        } catch (FileNotFoundException e) {
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ArrayList<String> readStringFile(String filename,Context context) {

        ArrayList<String> list = new ArrayList<String>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            list = (ArrayList<String>) is.readObject();
            is.close();
            fis.close();
            Log.d("file",list.toString());

        } catch (FileNotFoundException e) {
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("file",list.toString());
        return list;
    }

    public static void writeFile(String s, String filename,Context context) {
        ArrayList<String> list = new ArrayList<String >();
        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            list = (ArrayList<String>) is.readObject();
            is.close();
            fis.close();

        } catch (FileNotFoundException e) {
            list.add(s);
            try {
                FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(list);
                fos.flush();
                os.close();
                fos.close();
            } catch (Exception except) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        list.add(s);

        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(list);
            fos.flush();
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void deleteElem(String s, String filename,Context context) {
        ArrayList<String> list = new ArrayList<String >();
        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            list = (ArrayList<String>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        list.remove(s);

        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(list);
            fos.flush();
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }



//    public static void clearList(Context context) {
//        ArrayList<MyActivity> list = new ArrayList<MyActivity>();
//        MyActivity tmp;
//        try {
//            FileInputStream fis = context.openFileInput(filename);
//            ObjectInputStream is = new ObjectInputStream(fis);
//            list = (ArrayList<MyActivity>) is.readObject();
//            is.close();
//            fis.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        list.clear();
//        try {
//
//            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
//            ObjectOutputStream os = new ObjectOutputStream(fos);
//            os.writeObject(list);
//            fos.flush();
//            os.close();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
