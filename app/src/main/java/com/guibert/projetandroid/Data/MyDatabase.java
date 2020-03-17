package com.guibert.projetandroid.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE_NAME = "comic";

    //Column name
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String IMG = "img";
    private static final String CHARS_URL = "characterUrl";
    private static final String NB_HEROS = "nbCharacters";
    private static final String NB_PAGE = "nbPage";
    private static final String URL = "purchaseUrl";
    private static final String FORMAT = "format";

    public MyDatabase(Context context) {
        super(context, DATABASE_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY," +
                NAME + " TEXT," +
                DESCRIPTION + " TEXT," +
                IMG + " TEXT," +
                CHARS_URL + " TEXT," +
                NB_HEROS + " INTEGER," +
                NB_PAGE + " INTEGER," +
                URL + " TEXT," +
                FORMAT + " TEXT);";
        db.execSQL(DATABASE_TABLE_CREATE);
    }

    public void deleteComic(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM " + DATABASE_TABLE_NAME +
                " WHERE " + ID + " = " + id);
        //db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM " + DATABASE_TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void insertData(Comic c) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(ID, c.getId());
        values.put(NAME, c.getName());
        values.put(DESCRIPTION, c.getDescription());
        values.put(IMG, c.getImg());
        values.put(CHARS_URL, c.getCharactersUrl());
        values.put(NB_HEROS, c.getNbCharacters());
        values.put(NB_PAGE, c.getNbPage());
        values.put(URL, c.getPurchaseUrl());
        values.put(FORMAT, c.getFormat());
        db.insertOrThrow(DATABASE_TABLE_NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Comic> readData(){
        ArrayList<Comic> results = new ArrayList<>();
        String select = new String("SELECT * from " + DATABASE_TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.i("JFL", "Number of entries: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Comic c = new Comic();
                c.setId(cursor.getInt((cursor.getColumnIndex(ID))));
                c.setName(cursor.getString((cursor.getColumnIndex(NAME))));
                c.setDescription(cursor.getString((cursor.getColumnIndex(DESCRIPTION))));
                c.setImg(cursor.getString((cursor.getColumnIndex(IMG))));
                c.setCharactersUrl(cursor.getString((cursor.getColumnIndex(CHARS_URL))));
                c.setNbCharacters(cursor.getInt((cursor.getColumnIndex(NB_HEROS))));
                c.setNbPage(cursor.getInt((cursor.getColumnIndex(NB_PAGE))));
                c.setPurchaseUrl(cursor.getString((cursor.getColumnIndex(URL))));
                c.setFormat(cursor.getString((cursor.getColumnIndex(FORMAT))));
                results.add(c);
            } while (cursor.moveToNext());
        }
        return results;
    }

    public boolean isFav(int id){
        String select = new String("SELECT * from " + DATABASE_TABLE_NAME + " WHERE comic.id = " + id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
