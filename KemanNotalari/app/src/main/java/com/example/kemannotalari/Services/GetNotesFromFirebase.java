package com.example.kemannotalari.Services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.kemannotalari.R;
import com.example.kemannotalari.Violinnoteclass;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Map;

public class GetNotesFromFirebase {
    Bitmap bitmapsqlite = null;

    public void getnotefromfirebase(final Context context){
        final SQLiteDatabase database = context.openOrCreateDatabase("Violinnotes",Context.MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS violinnotes (id INTEGER PRIMARY KEY , name VARCHAR , image BLOB)");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        try {
            CollectionReference collectionReference = firebaseFirestore.collection("Kemannotaları");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(context,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                    if (value!=null){

                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            Map<String,Object> data = snapshot.getData();


                            String username = (String) data.get("name");
                            String downloadurl = (String) data.get("url");

                            getbitmapfrompicasso(downloadurl);

                            Violinnoteclass violinnoteclass =new Violinnoteclass(username,null,bitmapsqlite);
                            violinnoteclass.addSqlite(database);

                            System.out.println(downloadurl);
                            System.out.println(username);

                        }


                    }


                }
            });
        }catch (Exception ad){
            ad.printStackTrace();
            System.out.println("hata");
        }


    }
    public void getbitmapfrompicasso(String downloadurl){
        Target mtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (bitmap!=null){
                    bitmapsqlite = bitmap;
                    System.out.println("bitmap geldi");
                }else {
                    System.out.println("bitmap gelmedi");
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                System.out.println("bitmap yüklenemedi");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                System.out.println("yükleniyor");

            }
        };
        Picasso.get().load(downloadurl).into(mtarget);


    }
}
