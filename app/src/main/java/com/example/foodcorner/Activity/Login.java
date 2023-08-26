package com.example.foodcorner.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodcorner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
  private EditText email,password;
  private FirebaseAuth mAuth;
  private TextView LoginBack;
  private AppCompatButton SignButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        login();
        registration();
    }

    private void registration() {
        LoginBack=findViewById(R.id.loginBack);
        LoginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signUp.class));
                finish();
            }
        });
    }

    private void login() {
        email=findViewById(R.id.loginEmailEditText);
        password=findViewById(R.id.loginPasswordEditText);
        SignButton=findViewById(R.id.loginAccountSign);
        SignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=email.getText().toString();
                String userPassword=password.getText().toString();
                if(userPassword.isEmpty()||userEmail.isEmpty()){
                    Toast.makeText(Login.this, "Fill All Details", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                        intent.putExtra("UserId",mAuth.getCurrentUser().getUid());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}