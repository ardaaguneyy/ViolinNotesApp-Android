package com.example.kemannotalari;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddNoteActivity extends AppCompatActivity {
    private Uri imagedata;
    private Bitmap bitmap;
    private Bitmap smallbitmap;
    Context context;
    EditText editText;
    PhotoView imageView ;
    String itemname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        context = getApplicationContext();
        editText = findViewById(R.id.notenametext);
        imageView = findViewById(R.id.imageView9);
        getitem();
    }
    public void addimage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }

    }
    public void addnote(View view){
        String name = editText.getText().toString();
        if (smallbitmap!=null && !name.matches("") && itemname == null){
           ImageButton  button = findViewById(R.id.button2);
            button.setEnabled(false);
            try {
                Violinnoteclass note = new Violinnoteclass(name,"",smallbitmap);
                SQLiteDatabase database = this.openOrCreateDatabase("Violinnotes", MODE_PRIVATE,null);
                note.addSqlite(database);

                Toast.makeText(context, "NOTA EKLENDİ", Toast.LENGTH_SHORT).show();
                System.out.println("Sıkıntı yok");
                Intent intent = new Intent(context,MainActivity2.class);
                startActivity(intent);
                finish();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("hata");
            }

        }
        if (itemname!=null && imageView!=null && !editText.getText().toString().matches("")){
            Violinnoteclass violinnoteclass = new Violinnoteclass(itemname,"",null);
            SQLiteDatabase database = this.openOrCreateDatabase("Violinnotes", MODE_PRIVATE,null);
            if (smallbitmap!=null){
                violinnoteclass.updatesqlite(database,editText.getText().toString(),smallbitmap,itemname);
                Intent intent = new Intent(AddNoteActivity.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
            if (smallbitmap==null && itemname.equals(editText.getText().toString())){
                finish();
            }
            if (smallbitmap == null && !itemname.equals(editText.getText().toString())){
                try {
                    SQLiteStatement statement = database.compileStatement("UPDATE violinnotes SET name = ?  WHERE name = ?");
                    statement.bindString(1,editText.getText().toString());
                    statement.bindString(2,itemname);
                    statement.execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(AddNoteActivity.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }

        }



    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if (grantResults.length>0){
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,2);
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==2 && resultCode== RESULT_OK && data!=null){
            imagedata = data.getData();
            try {
                if (Build.VERSION.SDK_INT>=28){

                    ImageDecoder.Source source= ImageDecoder.createSource(this.getContentResolver(),imagedata);
                    bitmap = ImageDecoder.decodeBitmap(source);
                    smallbitmap = makeSmallerImage(bitmap,800);
                    imageView.setImageBitmap(smallbitmap);


                }else{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagedata);
                    smallbitmap = makeSmallerImage(bitmap,800);
                        imageView.setImageBitmap(smallbitmap);
                }
                Toast.makeText(context, "Resim Başarıyla Seçildi", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Resim Yüklenemedi", Toast.LENGTH_SHORT).show();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    public Bitmap makeSmallerImage (Bitmap image , int maximumsize){
        int widtht = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)widtht/ (float)height;
        if (bitmapRatio>1){
            widtht = maximumsize;
            height = (int)(widtht/bitmapRatio);

        }else{
            height = maximumsize;
            widtht = (int) (height*bitmapRatio);

        }
        return Bitmap.createScaledBitmap(image,widtht,height,true);
    }
    public void getitem(){
        try {
        Intent intent = getIntent();
        itemname = intent.getStringExtra("edititem");
        if (!itemname.matches("")) {

            SQLiteDatabase database = this.openOrCreateDatabase("Violinnotes", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM violinnotes WHERE name = ?", new String[]{itemname});
            int imageIx = cursor.getColumnIndex("image");

            while (cursor.moveToNext()) {
                editText.setText(itemname);
                byte[] bytes = cursor.getBlob(imageIx);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }

        }
        return;
            }catch (Exception e){
                e.printStackTrace();
            }


        try {
            Intent intent = getIntent();
            itemname = intent.getStringExtra("item_name");
            if (!itemname.matches("")) {

                SQLiteDatabase database = this.openOrCreateDatabase("Violinnotes", MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("SELECT * FROM violinnotes WHERE name = ?", new String[]{itemname});
                int imageIx = cursor.getColumnIndex("image");

                while (cursor.moveToNext()) {
                    editText.setText(itemname);
                    byte[] bytes = cursor.getBlob(imageIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                }

            }
            return;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
}