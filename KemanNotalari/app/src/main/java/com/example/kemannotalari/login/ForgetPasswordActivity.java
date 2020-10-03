package com.example.kemannotalari.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.kemannotalari.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
EditText emailtext;
FirebaseAuth auth;
RelativeLayout button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailtext = findViewById(R.id.username);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.relativelayout);
    }
    public void forgetpasswordonclick(View view){
        if (!emailtext.getText().toString().matches("")){
            auth.sendPasswordResetEmail(emailtext.getText().toString());
            button.setEnabled(false);
            Toast.makeText(ForgetPasswordActivity.this, "Doğrulama E-Postası Gönderildi", Toast.LENGTH_LONG).show();
        }

    }
}