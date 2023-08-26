package com.example.foodcorner.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.foodcorner.Adapter.OrderDetailItemAdapter;
import com.example.foodcorner.Models.AddToCartModel;
import com.example.foodcorner.Models.OrderModel;
import com.example.foodcorner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    TextView OrderIdText,OrderTotalAmountText,OrderQuantity;
    List<AddToCartModel> list;
    RecyclerView recyclerView;
    OrderDetailItemAdapter orderDetailItemAdapter;
    String OrderId;
    TextView TimeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        OrderId = getIntent().getStringExtra("OrderId");
        OrderIdText = findViewById(R.id.OrderDetailOrderID);
        OrderQuantity=findViewById(R.id.OrderDetailQuantity);
        OrderIdText.setText(OrderId);
        OrderTotalAmountText = findViewById(R.id.OrderDetailPrice);
        TimeStamp=findViewById(R.id.timestamp);
        recyclerView = findViewById(R.id.OrderDetailRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>(); // Initialize the list before using it
        orderDetailItemAdapter = new OrderDetailItemAdapter(this, list);
        recyclerView.setAdapter(orderDetailItemAdapter);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Data").child("Order").child(OrderId);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrderModel orderModel = snapshot.getValue(OrderModel.class);
                OrderTotalAmountText.setText(orderModel.getTotalAmount());
                OrderQuantity.setText(orderModel.getQuantity());
                TimeStamp.setText(orderModel.getTimeStamp());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db.child("OrderItem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); 
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AddToCartModel addToCartModel = dataSnapshot.getValue(AddToCartModel.class);
                    list.add(addToCartModel);
                }
                Collections.reverse(list);
                orderDetailItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}