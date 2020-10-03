package com.example.kemannotalari.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kemannotalari.MainActivity2;
import com.example.kemannotalari.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
private EditText emailtext,passwordtext;
private FirebaseAuth auth;
private String email = "";
private String password = "";
private SharedPreferences preferences;
int controlfirstsignin;
Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        emailtext = findViewById(R.id.emailtext);
        passwordtext = findViewById(R.id.passwordtext);
        preferences = this.getSharedPreferences("com.example.kemannotalari",Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        controlfirstsignin = preferences.getInt("control",0);

        if (controlfirstsignin==0){
            System.out.println("ilkgiriş");

        }else{
            context = getApplicationContext();
            String email = "";
            String password = "";
            preferencesvoid(preferences,context,email,password,auth);
        }
    }
    public  void preferencesvoid(SharedPreferences preferences, final Context context, String email1, String password1, final FirebaseAuth firebaseAuth){
        preferences = context.getSharedPreferences("com.example.kemannotalari", Context.MODE_PRIVATE);
        firebaseAuth.signOut();
        email1 = preferences.getString("email","");
        password1 = preferences.getString("password","");
        if (!email1.matches("")&&!password1.matches("")){

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Giriş Yapılıyor");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email1,password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else {

        }


    }






    public void login(View view){
        email = emailtext.getText().toString();
        password = passwordtext.getText().toString();
        if (!email.matches("") && !password.matches("")){
            auth.signInWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Giriş Yaparken Bir Hata Oldu", Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                    preferences.edit().putString("email",email).apply();
                    preferences.edit().putString("password",password).apply();
                    preferences.edit().putInt("control",1).apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
            });


        }


    }
    public void forgetpassword(View view){
        Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
        startActivity(intent);
    }
    public void signupintent(View view) {
        Intent intent = new Intent(LoginActivity.this, Register_Activity.class);
        startActivity(intent);
    }
}