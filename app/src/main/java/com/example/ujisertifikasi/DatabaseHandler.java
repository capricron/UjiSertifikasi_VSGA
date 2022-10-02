package com.example.ujisertifikasi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;
    private static final String DATABASE_NAME="data.db";
    private static final int DATABASE_VERSION = 1;

    private static final String createTableQuery= "CREATE TABLE data_form "
            +  "(nama TEXT,alamat TEXT,jenis_kelamin TEXT,latitude REAL,longitude REAL,image BLOB)";

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context =  context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createTableQuery);
            Toast.makeText(context, "Table created successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void storeData(ModelClass model){
        try {
            SQLiteDatabase objectSqLiteDatabase = this.getWritableDatabase();
            Bitmap imageToStoreBitmap = model.getImage();
            objectByteArrayOutputStream  = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);

            imageInBytes= objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("nama", model.getNama());
            objectContentValues.put("alamat", model.getAlamat());
            objectContentValues.put("jenis_kelamin", model.getJenisKelamin());
            objectContentValues.put("latitude", model.getLatitude());
            objectContentValues.put("longitude", model.getLongitude());
            objectContentValues.put("image", imageInBytes);

            long checkIfQueryRuns = objectSqLiteDatabase.insert("data_form", null, objectContentValues);

            if(checkIfQueryRuns != 0){
                Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show();
                objectSqLiteDatabase.close();
            }else{
                Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<ModelClass> getAllListMember(){
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            ArrayList<ModelClass> modelClasseList = new ArrayList<>();
            @SuppressLint("Recycle") Cursor cursor  =  sqLiteDatabase.rawQuery("SELECT * FROM data_form", null);

            if(cursor.getCount()  != 0){
                while (cursor.moveToNext()){
                  String name = cursor.getString(0);
                  String address = cursor.getString(1);
                  String jenisKelamin = cursor.getString(2);
                  Double latitude = cursor.getDouble(3);
                  Double longitude =  cursor.getDouble(4);
                  byte [] image = cursor.getBlob(5);

                  Bitmap bitmap  = BitmapFactory.decodeByteArray(image,0,image.length);
                  modelClasseList.add(new ModelClass(name,address,jenisKelamin,latitude,longitude,bitmap));
                }

                return modelClasseList;
            }else{
                Toast.makeText(context, "No Found Data", Toast.LENGTH_SHORT).show();
                return null;
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void deleteMember(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("data_form", "nama = ? " + name, null);
        db.close();
    }
}