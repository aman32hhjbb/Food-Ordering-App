package com.example.foodcorner.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodcorner.Models.AddToCartModel;
import com.example.foodcorner.Models.MenuModel;
import com.example.foodcorner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    String UserId, MenuId;
    TextView DishName, DishPrice, DishDescription;
    ImageView DishImage;
    MenuModel menuModel;
    AppCompatButton AddCartButton;
    DatabaseReference  menuItemsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        UserId = getIntent().getStringExtra("UserId");
        MenuId = getIntent().getStringExtra("MenuId");
        initializeViews();
        getModel();
    }

    private void initializeViews() {
        DishName = findViewById(R.id.detailActivityDishName);
        DishPrice = findViewById(R.id.detailActivityDishPrice);
        DishDescription = findViewById(R.id.detailActivityDishDescription);
        DishImage = findViewById(R.id.detailActivityDishImage);
        AddCartButton = findViewById(R.id.detailActivityAddToCartButton);
    }

    private void showInfo(MenuModel menuModel) {
        DishName.setText(menuModel.getItemName());
        DishPrice.setText(menuModel.getItemPrice());
        DishDescription.setText(menuModel.getItemDescription());
        Log.d(TAG, "showInfo: "+menuModel.getItemDescription());
        Glide.with(getApplicationContext())
                .load(Uri.parse(menuModel.getItemImage()))
                .into(DishImage);

        AddCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(menuModel);
            }
        });
    }

    private void getModel() {
        menuItemsDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Data").child("MenuItems").child(MenuId);

        menuItemsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuModel = snapshot.getValue(MenuModel.class);
                showInfo(menuModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }

    private void addToCart(MenuModel menuModel) {
        Log.d(TAG, "addToCart: buttonCLick");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child("Data").child("User").child(UserId).child("AddCart").child(menuModel.getMenuId());

        // Check if the menuId already exists in the AddCart node
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // menuId does not exist, add the addToCartModel
                    AddToCartModel addToCartModel = new AddToCartModel(menuModel.getMenuId());
                    database.setValue(addToCartModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // menuId already exists, show a message or handle accordingly
                    Toast.makeText(getApplicationContext(), "Item is already in the cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }
}
