package org.ap.edu.reportingapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class MyDbAdapter {
  /*  MyDbHelper myHelper;
    public MyDbAdapter (Context context)
    {
        myHelper = new MyDbHelper(context);
    }

    public long insertData(String emailAdres) {
        SQLiteDatabase dbb = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.EMAILADRES, emailAdres);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public String getData() {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID, myDbHelper.EMAILADRES};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String emailAdres =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            buffer.append(cid+ "  " + emailAdres + " \n");
        }
        return buffer.toString();
    }

    public  int delete(String emailAdres)
    {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String[] whereArgs = {emailAdres};

        int count = db.delete(myDbHelper.TABLE_NAME, myDbHelper.EMAILADRES+"= ?",whereArgs);
        return count;
    }

    public int updateEmailAdres(String oudEmailAdres, String nieuwEmailAdres) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.EMAILADRES, nieuwEmailAdres);
        String[] whereArgs = {oudEmailAdres};
        int count = db.update(myDbHelper.TABLE_NAME, contentValues, myDbHelper.EMAILDRES+=" =?", whereArgs);
        return count;
    } */
}
