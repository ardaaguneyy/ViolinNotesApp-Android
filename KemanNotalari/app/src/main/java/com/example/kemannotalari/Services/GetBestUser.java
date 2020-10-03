package com.example.kemannotalari.Services;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class GetBestUser {
    public void  getbestuser(final Context context, final TextView textView){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        try {
            CollectionReference collectionReference = firebaseFirestore.collection("Bestuser");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(context,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                    if (value!=null){

                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            Map<String,Object> data = snapshot.getData();

                            //Casting
                            String user = (String) data.get("bestuser");
                            textView.setText( user);


                        }


                    }


                }
            });
        }catch (Exception ad){
            ad.printStackTrace();
            System.out.println("hata");
        }

    }

}
