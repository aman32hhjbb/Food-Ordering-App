package com.example.foodcorner.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.foodcorner.BottomFragment.NotificationBottomFragment;
import com.example.foodcorner.Fragment.CartFragment;
import com.example.foodcorner.Fragment.HistoryFragment;
import com.example.foodcorner.Fragment.HomeFragment;
import com.example.foodcorner.Fragment.ProfileFragment;
import com.example.foodcorner.Fragment.SearchFragment;
import com.example.foodcorner.Models.NotificationModel;
import com.example.foodcorner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
    private String UserId;
    Fragment selectedFragment;
    Bundle bundle;
    ImageView NotificationIcon,NotificationDot,LogIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        UserId= getIntent().getStringExtra("UserId");
        NotificationIcon=findViewById(R.id.homeActivityNotification);
        NotificationDot=findViewById(R.id.homeActivityNotificationNew);
        LogIcon=findViewById(R.id.menuLogout);
        LogIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDilog();
            }
        });
        NotificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationBottomFragment notificationFragment = new NotificationBottomFragment();
                notificationFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, notificationFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        selectedFragment=new HomeFragment();
        bundle = new Bundle();
        bundle.putString("key",UserId);
        selectedFragment.setArguments(bundle);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit();
        }
        fragmentChange();
         getData();
    }

    private void showDilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Are you want to Logout")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getData() {
        List<NotificationModel> list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Data").child("User").child(UserId).child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    NotificationModel notificationModel=dataSnapshot.getValue(NotificationModel.class);
                    if(!notificationModel.isSeen()){
                        list.add(notificationModel);
                    }
                }

                if(list.size()>0){
                    NotificationDot.setVisibility(View.VISIBLE);
                }
                else {
                    NotificationDot.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fragmentChange() {
        bottomNavigationView=findViewById(R.id.homeActivityBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        break;
                    case R.id.history:
                        selectedFragment = new HistoryFragment();
                        break;
                    case R.id.cart:
                        selectedFragment = new CartFragment();
                        break;
                }
                selectedFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();

                return true;
            }
        });
    }
}