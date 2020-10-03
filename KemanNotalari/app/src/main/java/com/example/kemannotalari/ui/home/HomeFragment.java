package com.example.kemannotalari.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.kemannotalari.MainActivity2;
import com.example.kemannotalari.R;

import com.example.kemannotalari.adapters.Recyclerviewadapterhorizontal;
import com.example.kemannotalari.Services.GetBestUser;
import com.github.chrisbanes.photoview.PhotoView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import static com.example.kemannotalari.R.color.denemecolor;
import static com.example.kemannotalari.R.id.imageView20;


public class HomeFragment extends Fragment  {
    TextView textView;
    ImageView  imageview1,imageView2 ,imageView3 ,imageView4;
    TextView textView1,textView2,textView3,textView4;
    View viewview;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //bestusertext
        textView = view.findViewById(R.id.bestuser);
        GetBestUser bestUser = new GetBestUser();
        bestUser.getbestuser(getContext(),textView);
        //recyclerview
        viewview = view;
        recyclerviewmethod();
       //gridlayout
        showgridlayout(view);

    }
    private void recyclerviewmethod(){
        ArrayList<String> imagename;
        ArrayList<Bitmap> images;

        imagename = new ArrayList<>();
        images = new ArrayList<>();

        SQLiteDatabase database = viewview.getContext().openOrCreateDatabase("Violinnotes",Context.MODE_PRIVATE,null);
        getdatafromsqlite(database,imagename,images,"Fikrimin İnce Gülü");
        getdatafromsqlite(database,imagename,images,"9. Senfoni");
        getdatafromsqlite(database,imagename,images,"Samanyolu");


        RecyclerView recyclerView = viewview.findViewById(R.id.recyclerview);
        Recyclerviewadapterhorizontal adapter = new Recyclerviewadapterhorizontal(images,imagename);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    private void  randomgridlayoutimage(ImageView imageView, String imageviewname , TextView notenametext){
        try {
            Random random = new Random();
            SQLiteDatabase database = getContext().openOrCreateDatabase("Violinnotes", Context.MODE_PRIVATE,null);
            int randomint = random.nextInt(5);
            String [] args = {String.valueOf(randomint)};
            Cursor cursor = database.rawQuery("SELECT * FROM violinnotes WHERE id = ?", args);
            int nameIx = cursor.getColumnIndex("name");
            int imageIx = cursor.getColumnIndex("image");
            while (cursor.moveToNext()){
                imageviewname = cursor.getString(nameIx);
                byte []  bytes = cursor.getBlob(imageIx);
                Bitmap imagebitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(imagebitmap);
                notenametext.setText(imageviewname);
            }

            cursor.close();
        }catch (Exception e ){
            e.printStackTrace();
        }



    }
    @SuppressLint("ResourceAsColor")
    private void imagepopupmethod(){
        final ImagePopup imagePopup = new ImagePopup(getContext());
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
        imagePopup.setBackgroundColor(denemecolor);
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional*/
        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePopup.initiatePopup(imageview1.getDrawable()); // Load Image from Drawable
                imagePopup.viewPopup();

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePopup.initiatePopup(imageView2.getDrawable()); // Load Image from Drawable
                imagePopup.viewPopup();

            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePopup.initiatePopup(imageView3.getDrawable()); // Load Image from Drawable
                imagePopup.viewPopup();

            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePopup.initiatePopup(imageView4.getDrawable()); // Load Image from Drawable
                imagePopup.viewPopup();
            }
        });

    }
    private void showgridlayout(View view){
        //gridlayout
        imageview1 = view.findViewById(imageView20);
        imageView2 = view.findViewById(R.id.imageView21);
        imageView3 = view.findViewById(R.id.imageView22);
        imageView4 = view.findViewById(R.id.imageView23);

        textView1 = view.findViewById(R.id.textView20);
        textView2 = view.findViewById(R.id.textView21);
        textView3 = view.findViewById(R.id.textView22);
        textView4 = view.findViewById(R.id.textView23);

        String imageview1name="";
        String imageview2name = "";
        String imageview3name="";
        String imageview4name="";
        //imagepopup
        imagepopupmethod();

        randomgridlayoutimage(imageview1,imageview1name,textView1);
        randomgridlayoutimage(imageView2,imageview2name,textView2);
        randomgridlayoutimage(imageView3,imageview3name,textView3);
        randomgridlayoutimage(imageView4,imageview4name,textView4);

    }
    private void getdatafromsqlite(SQLiteDatabase database, ArrayList<String> names , ArrayList<Bitmap> bitmaps , String searchword ){
       try {
           Cursor cursor = database.rawQuery("SELECT * FROM violinnotes WHERE name = ?", new String []  {searchword});
           int nameIx = cursor.getColumnIndex("name");
           int imageIx = cursor.getColumnIndex("image");
           while (cursor.moveToNext()){
               names.add(cursor.getString(nameIx));
               byte [] bytes = cursor.getBlob(imageIx);
               Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
               bitmaps.add(bitmap);
           }
cursor.close();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}