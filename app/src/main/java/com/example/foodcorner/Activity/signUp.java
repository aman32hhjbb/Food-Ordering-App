package com.example.foodcorner.Activity;



import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.foodcorner.Models.UserModel;
import com.example.foodcorner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


public class signUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference firebaseDatabase;
    private EditText email,password,name;
    private TextView signupBack;
    private AppCompatButton createAccount;
    private UserModel userModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();
        signup();
        login();

    }

    private void login() {
        signupBack =findViewById(R.id.signupBack);
        signupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    private void signup() {
        name=findViewById(R.id.signupNameEditText);
        email=findViewById(R.id.signupEmailEditText);
        password=findViewById(R.id.signupPasswordEditText);
        createAccount=findViewById(R.id.signupCreateAccountButton);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=name.getText().toString();
                String userEmail=email.getText().toString();
                String userPassword=password.getText().toString();
                userModel=new UserModel(userName,userEmail);
                if(userEmail.isEmpty()||userName.isEmpty()||userPassword.isEmpty()){
                    Toast.makeText(signUp.this, "Fill Details", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(signUp.this, "Your Account is Created", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);

                                    } else {
                                        Toast.makeText(signUp.this, "Try After Some Time", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                private void updateUI(FirebaseUser user) {
                                    firebaseDatabase=FirebaseDatabase.getInstance().getReference();
                                    firebaseDatabase.child("Data").child("User").child(user.getUid()).child("PersonalData").setValue(userModel);
                                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                    intent.putExtra("UserId",user.getUid());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });
    }


}