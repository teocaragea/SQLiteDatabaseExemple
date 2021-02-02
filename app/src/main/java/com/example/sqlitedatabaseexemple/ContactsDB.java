package com.example.sqlitedatabaseexemple;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDB {

    private static final String KEY_ROWID="_id";///u have prob if _ is missing
    private static final String KEY_NAME="person_name";
    private static final String KEY_CELL="_cell";/// if doesn't work use _

    private final String DATABASE_NAME="ContactsDB";
    private final String DATABASE_TABLE="ContactsTable";
    private final int DATABASE_VERSION=1;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDataBase;

    public ContactsDB(Context ourContext) {
        this.ourContext = ourContext;
    }
    private class DBHelper extends SQLiteOpenHelper
    {   public DBHelper(Context context)///we create
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);//construct a new database

    }
        @Override
        public void onCreate(SQLiteDatabase db) {//we run
        /*
        CREATE TABLE ContactsTable(_id INTEGER PRIMARY KEY AUTOINCREMENT,
                           person_name Text NOT NULL, _cell TEXT NOT NULL);
         */
        String sqlCode="CREATE TABLE " + DATABASE_TABLE + " (" +///DATABASE_TABLE IS CONTEXT
                KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME +" TEXT NOT NULL, " +
                KEY_CELL + " TEXT NOT NULL);";

        db.execSQL(sqlCode);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);//DROP AND CREATE NEW TABLE
            onCreate(db);


        }
    }
    public ContactsDB open() throws SQLException
    {
        ourHelper=new DBHelper(ourContext);
        ourDataBase=ourHelper.getWritableDatabase();
        return this;

    }
    public void close()
    {
        ourHelper.close();
    }
    public long createEntry(String name,String cell)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_CELL, cell);
        return ourDataBase.insert(DATABASE_TABLE,null,cv);

    }
    public  String getData()//you can return array list with data
    {
        String [] columns=new String [] {KEY_ROWID,KEY_NAME,KEY_CELL};
        Cursor c=ourDataBase.query(DATABASE_TABLE,columns,null,null,null,null,null);///poti sa selectezi datele ca la scl
        String result="";
        int iRowID=c.getColumnIndex(KEY_ROWID);
        int iName=c.getColumnIndex(KEY_NAME);
        int iCell=c.getColumnIndex(KEY_CELL);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result=result + c.getString(iRowID)+ ": " + c.getString(iName) + " " + c.getString(iCell) + "\n";
        }
        c.close();
        return result;
    }
    public long deleteEntry(String rowID)
    {
        return ourDataBase.delete(DATABASE_TABLE,KEY_ROWID +"=?", new String[]{rowID});///? is replaced with third argument
    }
    public long updateEntry(String rowID,String name, String cell)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_CELL,cell);

        return ourDataBase.update(DATABASE_TABLE,cv,KEY_ROWID+"=?",new String[]{rowID});
    }
}
