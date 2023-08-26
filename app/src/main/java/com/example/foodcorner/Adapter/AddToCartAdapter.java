package com.example.foodcorner.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodcorner.Activity.DetailActivity;
import com.example.foodcorner.Models.AddToCartModel;
import com.example.foodcorner.Models.MenuModel;
import com.example.foodcorner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddToCartAdapter extends RecyclerView.Adapter<AddToCartAdapter.ViewHolder> {
     List<AddToCartModel> cartItemList;
     String userId;
    Context context;
    MenuModel menuModel;

    public AddToCartAdapter(List<AddToCartModel> cartItemList, String userId, Context context) {
        this.cartItemList = cartItemList;
        this.userId = userId;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddToCartModel cartItem = cartItemList.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Data").child("MenuItems").child(cartItem.getMenuId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 menuModel = snapshot.getValue(MenuModel.class);

                if (menuModel != null) {
                    holder.dishName.setText(menuModel.getItemName());
                    holder.dishPrice.setText(menuModel.getItemPrice());
                    Glide.with(context)
                            .load(Uri.parse(menuModel.getItemImage()))
                            .into(holder.dishImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.dishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("MenuId",menuModel.getMenuId());
                intent.putExtra("UserId",userId);
                context.startActivity(intent);

            }
        });
        holder.dishQuantity.setText(cartItem.getQuantity());

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Data").child("User").child(userId).child("AddCart");

        holder.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = Integer.parseInt(holder.dishQuantity.getText().toString()) + 1;
                cartRef.child(cartItem.getMenuId()).child("quantity").setValue(String.valueOf(newQuantity));
            }
        });

        holder.negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.dishQuantity.getText().toString());
                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;
                    cartRef.child(cartItem.getMenuId()).child("quantity").setValue(String.valueOf(newQuantity));
                }
            }
        });

        holder.trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartRef.child(cartItem.getMenuId()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, dishPrice, dishQuantity;
        ImageButton positiveButton, negativeButton, trashButton;
        ImageView dishImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.cartItemDishName);
            dishPrice = itemView.findViewById(R.id.CartItemDishPrice);
            dishQuantity = itemView.findViewById(R.id.cartItemQuantity);
            positiveButton = itemView.findViewById(R.id.cartItemPositiveButton);
            negativeButton = itemView.findViewById(R.id.cartItemNegativeButton);
            trashButton = itemView.findViewById(R.id.cartItemTrashButton);
            dishImage = itemView.findViewById(R.id.cartItemImage);
        }
    }
}
