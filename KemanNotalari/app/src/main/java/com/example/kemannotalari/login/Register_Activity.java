package com.example.kemannotalari.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register_Activity extends AppCompatActivity {
    private EditText emailtext,passwordtext;
    private FirebaseAuth auth;
    private String email = "";
    private String password = "";
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        emailtext = findViewById(R.id.emailtext2);
        passwordtext = findViewById(R.id.passwordtext2);
        preferences = this.getSharedPreferences("com.example.kemannotalari", Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
    }

    public void signup(View view){
        email = emailtext.getText().toString();
        password = passwordtext.getText().toString();
        if (!email.matches("") && !password.matches("")){
            auth.createUserWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register_Activity.this, "Giriş Yaparken Bir Hata Oldu", Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(Register_Activity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
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
}