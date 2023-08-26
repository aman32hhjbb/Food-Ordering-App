package com.example.foodcorner.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodcorner.Activity.payOutActivity;
import com.example.foodcorner.Adapter.AddToCartAdapter;
import com.example.foodcorner.Models.AddToCartModel;
import com.example.foodcorner.Models.MenuModel;
import com.example.foodcorner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private List<AddToCartModel> list;
    private String userId;
    TextView TotalAmount;
    private int totalAmount = 0;
    private AddToCartAdapter addToCartAdapter;
    AppCompatButton appCompatButton;
    public CartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        list = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("key");
        }
        TotalAmount=view.findViewById(R.id.cartFragmentTotalAmount);

        RecyclerView recyclerView = view.findViewById(R.id.cartFragmentRecycleView);
        addToCartAdapter = new AddToCartAdapter(list, userId,getContext());
        recyclerView.setAdapter(addToCartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getData();
        proceedOrder(view);
        return view;
    }

    private void proceedOrder(View view) {
        appCompatButton=view.findViewById(R.id.cartFragmentProceedButton);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.isEmpty()){
                    Toast.makeText(getContext(),"Cart Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(getContext(), payOutActivity.class);
                intent.putExtra("UserId",userId);
                intent.putExtra("Total",Integer.toString(totalAmount));
                startActivity(intent);
            }
        });
    }

    private void getData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Data").child("User").child(userId).child("AddCart");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                    AddToCartModel addToCartModel = menuSnapshot.getValue(AddToCartModel.class);
                    if (addToCartModel != null) {
                        list.add(addToCartModel);
                    }
                }

                updateTotal(list);
                addToCartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }

    private void updateTotal(List<AddToCartModel> list) {
        totalAmount = 0; // Reset the total amount before calculating
       if(list.isEmpty()){
           updateTotalAmountTextView();
       }
        for (AddToCartModel addToCartModel : list) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Data").child("MenuItems").child(addToCartModel.getMenuId());

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    MenuModel menuModel = snapshot.getValue(MenuModel.class);
                    if (menuModel != null) {
                        totalAmount += Integer.parseInt(menuModel.getItemPrice())*Integer.parseInt(addToCartModel.getQuantity());
                        updateTotalAmountTextView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors here
                }
            });
        }
    }

    private void updateTotalAmountTextView() {

        TotalAmount.setText(String.valueOf(totalAmount));
    }
}
