package com.example.kemannotalari;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kemannotalari.Services.GetNotesFromFirebase;
import com.example.kemannotalari.Services.PushNotifications;
import com.example.kemannotalari.adapters.Recyclerviewadapter;
import com.example.kemannotalari.login.LoginActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth firebaseAuth;
    SharedPreferences preferences;
    Context context;
    SQLiteDatabase database;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    EditText commenttext;
    private Uri imagedata;
    private Bitmap bitmap;
    private Bitmap smallbitmap;
    DrawerLayout drawerlayout ;
    private StorageReference storageReference;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    ImageButton imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naw_drawer_layout);
        setNavigationViewListner();

        imageView = findViewById(R.id.imageView3);
        context = getApplicationContext();
        database = this.openOrCreateDatabase("Violinnotes", MODE_PRIVATE,null);
        drawerlayout = findViewById(R.id.drawer_layout);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController( this,navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Firebase sign in
        firebaseAuth = FirebaseAuth.getInstance();
        preferences = this.getSharedPreferences("com.example.kemannotalari",Context.MODE_PRIVATE);

         //adviews

        //Push Notifications Service
        PushNotifications notifications = new PushNotifications();
        notifications.push(this);

        //getnotes
        int control = preferences.getInt("first",0);
        if (control == 0 ){
            try {
                database.execSQL("DELETE FROM violinnotes ");
            }catch (Exception e){
                e.printStackTrace();
            }

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity2.this);
            progressDialog.setTitle("Notalar Yükleniyor");
            progressDialog.setMessage("Bu işlem bir kerelik yapılacaktır");
            progressDialog.setCancelable(false);
            progressDialog.show();

            System.out.println("ilk giriş");
            GetNotesFromFirebase fromFirebase = new GetNotesFromFirebase();
            fromFirebase.getnotefromfirebase(MainActivity2.this);
            CountDownTimer timer = new CountDownTimer(10000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    GetNotesFromFirebase fromFirebase2 = new GetNotesFromFirebase();
                    fromFirebase2.getnotefromfirebase(MainActivity2.this);
                    progressDialog.dismiss();

                    Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
            };
            timer.start();


            preferences.edit().putInt("first",1).apply();
        }




    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }
     mBackPressed = System.currentTimeMillis();

    }


    //chat
    public void  restart(){
        FirebaseFirestore firestore;
        final ArrayList<String> emaillist;
        final ArrayList<String> urlist;
        final ArrayList<String> commentlist;
        emaillist = new ArrayList<>();
        urlist = new ArrayList<>();
        commentlist = new ArrayList<>();
        emaillist.clear();
        commentlist.clear();
        urlist.clear();
        firestore = FirebaseFirestore.getInstance();
        try {
            CollectionReference collectionReference = firestore.collection("Posts");

            collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                    if (value!=null){

                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            Map<String,Object> data = snapshot.getData();

                            //Casting
                            String comment = (String) data.get("comment");
                            String userEmail = (String) data.get("email");
                            String downloadUrl = (String) data.get("url");
                            commentlist.add(comment);
                            emaillist.add(userEmail);
                            urlist.add(downloadUrl);

                        }
                        RecyclerView recyclerView;
                        recyclerView = findViewById(R.id.RecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                        Recyclerviewadapter recyclerviewadapter = new Recyclerviewadapter(emaillist,urlist,commentlist);
                        recyclerView.setAdapter(recyclerviewadapter);
                        recyclerviewadapter.notifyDataSetChanged();

                    }


                }
            });
        }catch (Exception ad){
            ad.printStackTrace();
            System.out.println("hata");
        }




    }
    public void uploaddata(final View view) {
        try {
            commenttext = findViewById(R.id.commentedittext);
        final String comment = commenttext.getText().toString();
         if (!comment.matches("") && smallbitmap != null) {
             commenttext.setText("");
             firestore = FirebaseFirestore.getInstance();
             storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                //universal unique id
                UUID uuid = UUID.randomUUID();
                final String imageName = "images/" + uuid + ".jpg";

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                smallbitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
                final byte[] byteArray = stream.toByteArray();

                storageReference.child(imageName).putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(Uri uri) {
                String downloadUrl = uri.toString();
                 Map<String, Object> userdata = new HashMap<>();
                userdata.put("url", downloadUrl);
                 userdata.put("email", firebaseAuth.getCurrentUser().getEmail().toString());
                userdata.put("comment", comment);
                userdata.put("date", FieldValue.serverTimestamp());



                                firestore.collection("Posts").add(userdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(context, "Mesaj Paylaşıldı", Toast.LENGTH_SHORT).show();
                                        commenttext.setText("");
                                        smallbitmap=null;
                                        imageView.setEnabled(false);
                                        CountDownTimer timer = new CountDownTimer(15000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                            }

                                            @Override
                                            public void onFinish() {
                                                imageView.setEnabled(true);
                                            }
                                        };

                                        timer.start();
                                        Toast.makeText(context, "Birdaha ki mesaj için 15 saniye bekleyiniz", Toast.LENGTH_SHORT).show();
                                        restart();

                                    }






                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Mesaj Paylaşılamadı", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
            if (!comment.matches("") && smallbitmap == null) {
                firestore = FirebaseFirestore.getInstance();
                Map<String, Object> userdata = new HashMap<>();
                userdata.put("email", firebaseAuth.getCurrentUser().getEmail().toString());
                userdata.put("comment", comment);
                userdata.put("date", FieldValue.serverTimestamp());
                userdata.put("url","null");


                firestore.collection("Posts").add(userdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Mesaj Paylaşıldı", Toast.LENGTH_SHORT).show();
                        commenttext.setText("");
                        smallbitmap=null;
                        imageView.setEnabled(false);

                        CountDownTimer timer = new CountDownTimer(15000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                imageView.setEnabled(true);
                            }
                        };
                        timer.start();
                        Toast.makeText(context, "Birdaha ki mesaj için 15 saniye bekleyiniz", Toast.LENGTH_SHORT).show();
                        restart();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Mesaj Paylaşılamadı", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        } catch (Exception a) {
            a.printStackTrace();
        }
    }
    public void uploadimage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if (grantResults.length>0){
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
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


                }else{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagedata);
                    smallbitmap = makeSmallerImage(bitmap,800);

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
    @Override

    //navigationdrawer
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_help: {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ardaaguneyy13@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Yardım");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity2.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                drawerlayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_change_password : {

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Emin Misin");
                alert.setMessage("Şifre Değiştirme e postası gönderilsin mi ? ");
                alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            firebaseAuth.sendPasswordResetEmail(firebaseAuth.getCurrentUser().getEmail().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setCancelable(false);
                drawerlayout.closeDrawer(GravityCompat.START);
                alert.show();
                break;
            }
            case R.id.nav_log_out :{
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Emin Misin");
                alert.setMessage("Oturum Kapatılacak");
                alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        preferences.edit().putString("email","").apply();
                        preferences.edit().putString("password","").apply();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                drawerlayout.closeDrawer(GravityCompat.START);
                alert.setCancelable(false);
                alert.show();
            }

        }


        return true;
    }
    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view); navigationView.setNavigationItemSelectedListener(this);
    }
    public void openmenu(View view){
        drawerlayout.openDrawer(GravityCompat.START);


    }

    //intent add note
    public void addnote(View view){
        Intent intent = new Intent(getApplicationContext(),AddNoteActivity.class);
        startActivity(intent);



    }

    }


