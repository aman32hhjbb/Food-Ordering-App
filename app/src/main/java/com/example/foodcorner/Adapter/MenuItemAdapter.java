package com.example.foodcorner.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> implements Filterable {
    private List<MenuModel> originalList;
    private List<MenuModel> filteredList;
    String UserID;
    Context context;

    public MenuItemAdapter(List<MenuModel> list, String userId, Context context) {
        this.originalList = list;
        this.filteredList = list;
        UserID = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_popular_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuModel menuModel = filteredList.get(position);
        String DishName = menuModel.getItemName();
        String DishPrice = menuModel.getItemPrice();
        holder.dishName.setText(DishName);
        holder.dishPrice.setText(DishPrice);
        Glide.with(context)
                .load(Uri.parse(menuModel.getItemImage()))
                .into(holder.dishImage);
        Log.d(TAG, "onBindViewHolder: " + menuModel.getItemImage());
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(menuModel);
            }
        });
        holder.dishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("MenuId",menuModel.getMenuId());
                intent.putExtra("UserId",UserID);
                context.startActivity(intent);

            }
        });
    }

    private void addToCart(MenuModel menuModel) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child("Data").child("User").child(UserID).child("AddCart").child(menuModel.getMenuId());

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
                                        Toast.makeText(context, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // menuId already exists, show a message or handle accordingly
                    Toast.makeText(context, "Item is already in the cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().toLowerCase();
                List<MenuModel> filteredResults = new ArrayList<>();

                if (query.isEmpty()) {
                    filteredResults.addAll(originalList);
                } else {
                    for (MenuModel menuModel : originalList) {
                        // Change the condition to check if the itemName starts with the query letter
                        if (menuModel.getItemName().toLowerCase().startsWith(query)) {
                            filteredResults.add(menuModel);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredResults;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (List<MenuModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, dishPrice, addToCart;
        ImageView dishImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.popularItemDishName);
            dishPrice = itemView.findViewById(R.id.popularItemDishPrice);
            dishImage = itemView.findViewById(R.id.popularItemImage);
            addToCart = itemView.findViewById(R.id.popularItemAddToCartButton);
        }
    }
}
