package com.example.kemannotalari;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class Violinnoteclass {
    String name;
    String url;
    Bitmap imagebitmap;

    public Violinnoteclass(String name, String url, Bitmap imagebitmap) {
        this.name = name;
        this.url = url;
        this.imagebitmap = imagebitmap;
    }

    public void addSqlite(SQLiteDatabase database){
        if (name!=null && imagebitmap!=null){
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
                final byte[] byteArray = stream.toByteArray();
                database.execSQL("CREATE TABLE IF NOT EXISTS violinnotes (id INTEGER PRIMARY KEY , name VARCHAR , image BLOB)");
                String sqlstring = "INSERT INTO violinnotes ( name , image ) VALUES ( ? , ? )";
                SQLiteStatement statement = database.compileStatement(sqlstring);
                statement.bindString(1,name);
                statement.bindBlob(2,byteArray);
                statement.execute();
                System.out.println("hata yok devam");

            }catch (Exception e ){
                e.printStackTrace();
                System.out.println("hata var ");
            }

        }

    }
    public void  deletesqlite(SQLiteDatabase database, String itemname, Context context){
        try {
            SQLiteStatement statement = database.compileStatement("DELETE FROM violinnotes WHERE name = ?");
            statement.bindString(1,itemname);
            statement.execute();
            Toast.makeText(context, "Nota Silindi", Toast.LENGTH_LONG).show();
        }catch (Exception a){
            a.printStackTrace();
        }




    }
    public void updatesqlite(SQLiteDatabase database, String newname , Bitmap newbitmap ,String oldname){
        try {
            SQLiteStatement statement = database.compileStatement("UPDATE violinnotes SET name = ? , image = ? WHERE name = ?");
            statement.bindString(1,newname);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            newbitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
            byte[] byteArray = stream.toByteArray();
            statement.bindBlob(2,byteArray);
            statement.bindString(3,oldname);
            statement.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
