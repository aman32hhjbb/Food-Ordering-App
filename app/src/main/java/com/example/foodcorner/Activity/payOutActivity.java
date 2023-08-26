package com.example.foodcorner.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodcorner.Models.AddToCartModel;
import com.example.foodcorner.Models.OrderModel;
import com.example.foodcorner.Models.UserModel;
import com.example.foodcorner.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class payOutActivity extends AppCompatActivity {
    private String UserId, TotalAmount;
    private EditText userName, userAddress, userEmail, userPhone;
    private TextView totalAmount;
    private AppCompatButton PlaceOrderButton;
    private List<AddToCartModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_out);

        UserId = getIntent().getStringExtra("UserId");
        TotalAmount = getIntent().getStringExtra("Total");

        initialise();
        getData();
        PlaceOrder();
    }

    private void PlaceOrder() {
        list = new ArrayList<>();

        PlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllFieldsFilled()) {
                    DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference()
                            .child("Data").child("User").child(UserId).child("AddCart");

                    cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                AddToCartModel addToCartModel = snapshot1.getValue(AddToCartModel.class);
                                list.add(addToCartModel);
                            }

                            DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference()
                                    .child("Data").child("Order");
                            String OrderId = orderReference.push().getKey();

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
                            Date date = new Date(System.currentTimeMillis());
                            String formattedDate = sdf.format(date);
                            OrderModel orderModel = new OrderModel(UserId, "Pending", TotalAmount, Integer.toString(list.size()), OrderId,formattedDate);
                            orderReference.child(OrderId).setValue(orderModel);

                            for (AddToCartModel model : list) {
                                orderReference.child(OrderId).child("OrderItem").push().setValue(model);
                            }

                            // Clear the cart and proceed to the next activity

                             setProfile();
                            clearCartAndProceed();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled event
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void setProfile() {
        UserModel userModel=new UserModel();
        userModel.setName(userName.getText().toString());
        userModel.setEmail(userEmail.getText().toString());
        userModel.setAddress(userAddress.getText().toString());
        userModel.setMobileNo(userPhone.getText().toString());
        FirebaseDatabase.getInstance().getReference()
                .child("Data").child("User").child(UserId).child("PersonalData").setValue(userModel);
    }

    private void clearCartAndProceed() {
        FirebaseDatabase.getInstance().getReference().child("Data").child("User").child(UserId).child("AddCart")
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(getApplicationContext(), congratulationActivity.class);
                        intent.putExtra("UserId", UserId);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void getData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Data").child("User").child(UserId).child("PersonalData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                setData(userModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    private void setData(UserModel userModel) {
        userName.setText(userModel.getName());
        userAddress.setText(userModel.getAddress());
        userEmail.setText(userModel.getEmail());
        userPhone.setText(userModel.getMobileNo());
        totalAmount.setText(TotalAmount);
    }

    private void initialise() {
        userName = findViewById(R.id.payActivityName);
        userAddress = findViewById(R.id.payActivityAddress);
        userEmail = findViewById(R.id.payActivityEmail);
        userPhone = findViewById(R.id.payActivityPhone);
        totalAmount = findViewById(R.id.payActivityAmount);
        PlaceOrderButton = findViewById(R.id.payActivityOrderButton);
    }

    private boolean isAllFieldsFilled() {
        return !userName.getText().toString().isEmpty() &&
                !userAddress.getText().toString().isEmpty() &&
                !userEmail.getText().toString().isEmpty() &&
                !userPhone.getText().toString().isEmpty();
    }
}
