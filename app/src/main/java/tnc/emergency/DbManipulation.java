package tnc.emergency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DbManipulation {


    private DbHelper Db;
    private SQLiteDatabase database;

    public void dataInsertion(Context ctx, String Contact_Name, String Phone_Number) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CONTACT_NAME", Contact_Name);
        values.put("PHONE_NUMBER", Phone_Number);
        database.insert(Db.Table, "", values);
        database.close();
    }

    public void setData(Context ctx, String Latitude, String Longitude, String Type, String Phonenumber) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("LATITUDE", Latitude);
        values.put("LONGITUDE", Longitude);
        values.put("TYPE", Type);
        values.put("PHONE_NUMBER", Phonenumber);
        database.insert(Db.Table1, "", values);
        database.close();
    }


    public String[] dataSelect(Context ctx) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        Cursor cur = Db.query(database, "SELECT * FROM TBL_CONTACT");
        String[] array = new String[cur.getCount()];
        int i = 0;
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {

                array[i++] = cur.getString(cur.getColumnIndex("CONTACT_NAME"));
                Log.d("Name ", cur.getString(cur.getColumnIndex("CONTACT_NAME")));
            } while (cur.moveToNext());
        }
        database.close();
        cur.close();
        return array;
    }

    public List getPhoneNumber(Context ctx) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        List<String> numbers = new ArrayList<>();
        Cursor cur = Db.query(database, "SELECT * FROM TBL_CONTACT");
        int i = 0;
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {

                numbers.add(cur.getString(cur.getColumnIndex("PHONE_NUMBER")));
                Log.d("PhoneNumber", cur.getString(cur.getColumnIndex("PHONE_NUMBER")));
            } while (cur.moveToNext());
        }

        cur.close();
        database.close();
        return numbers;
    }


    public List getLatitude(Context ctx, String type) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        List<String> latitude = new ArrayList<>();
        Cursor cur = Db.query(database, "SELECT * FROM TBL_DATA WHERE TYPE = '" + type + "'");
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {

                latitude.add(cur.getString(cur.getColumnIndex("LATITUDE")));
                Log.d("zma LATITUDE", cur.getString(cur.getColumnIndex("LATITUDE")));
            } while (cur.moveToNext());
        }
        database.close();
        cur.close();
        return latitude;
    }

    public boolean getNumberCount(Context ctx, String type) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        Cursor cur = Db.query(database, "SELECT * FROM TBL_DATA WHERE TYPE = '" + type + "'");
        cur.moveToFirst();
        if (cur.getCount() > 0) {
            return true;
        }
        return false;
    }

    public List getLongitude(Context ctx, String type) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        List<String> longtitude = new ArrayList<>();
        Cursor cur = Db.query(database, "SELECT * FROM TBL_DATA WHERE TYPE = '" + type + "'");
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {

                longtitude.add(cur.getString(cur.getColumnIndex("LONGITUDE")));
                Log.d("zma LONGITUDE", cur.getString(cur.getColumnIndex("LONGITUDE")));
            } while (cur.moveToNext());
        }
        database.close();
        cur.close();
        return longtitude;
    }


    public List getPhone(Context ctx, String type) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        List<String> phone = new ArrayList<>();
        Cursor cur = Db.query(database, "SELECT * FROM TBL_DATA WHERE TYPE = '" + type + "'");
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            do {

                phone.add(cur.getString(cur.getColumnIndex("PHONE_NUMBER")));
                Log.d("zma PHONE_NUMBER", cur.getString(cur.getColumnIndex("PHONE_NUMBER")));
            } while (cur.moveToNext());
        }
        database.close();
        cur.close();
        return phone;
    }

    public int dataCount(Context ctx) {

        Db = new DbHelper(ctx);
        database = Db.getReadableDatabase();
        Cursor cur = Db.query(database, "SELECT * FROM TBL_CONTACT");
        int count = cur.getCount();
        database.close();
        cur.close();
        return count;
    }

    public void dataUpdate(Context ctx, String name, String message) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        database.execSQL("UPDATE  TBL_CONTACT  SET BODY = '" + message
                + "' WHERE CONTACT_NAME = '" + name + "'");
        Toast.makeText(ctx, "Message Updated", Toast.LENGTH_LONG).show();
        database.close();
    }

    public void dataDelete(Context ctx, String phoneNumber) {
        Db = new DbHelper(ctx);
        database = Db.getWritableDatabase();
        database.execSQL("DELETE FROM TBL_CONTACT WHERE PHONE_NUMBER = '"
                + phoneNumber + "'");
        database.close();
        Toast.makeText(ctx, phoneNumber + " Deleted ", Toast.LENGTH_LONG)
                .show();
    }

    public boolean checkdatabase(Context ctx, String name) {
        Db = new DbHelper(ctx);
        database = Db.getReadableDatabase();
        Cursor cur = Db
                .query(database,
                        "SELECT * FROM TBL_CONTACT WHERE CONTACT_NAME = '"
                                + name + "'");
        int count = cur.getCount();
        if (count > 0) {
            database.close();
            cur.close();
            return false;
        }
        database.close();
        cur.close();
        return true;
    }

    public boolean hasdata(Context ctx) {
        Db = new DbHelper(ctx);
        database = Db.getReadableDatabase();
        Cursor cur = Db
                .query(database,
                        "SELECT * FROM TBL_CONTACT");
        int count = cur.getCount();
        if (count > 0) {
            database.close();
            cur.close();
            return true;
        }
        database.close();
        cur.close();
        return false;
    }
}
