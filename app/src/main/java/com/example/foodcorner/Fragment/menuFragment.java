package com.example.foodcorner.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodcorner.Adapter.MenuItemAdapter;
import com.example.foodcorner.Models.MenuModel;
import com.example.foodcorner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class menuFragment extends Fragment {
private String userId;
private MenuItemAdapter menuItemAdapter;
private List<MenuModel> list;
    public menuFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("key");
        }
        list=new ArrayList<>();
        Log.d(TAG, "onCreateView: "+userId);
        View view=inflater.inflate(R.layout.fragment_menu_fragement, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.menuFragmentRecycleView);
         menuItemAdapter=new MenuItemAdapter(list,userId,getContext());
        recyclerView.setAdapter(menuItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();
        return view;
    }

    private void getData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference();
        databaseReference.child("Data").child("MenuItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot menuSnapshot : snapshot.getChildren()) {
                    MenuModel menuModel = menuSnapshot.getValue(MenuModel.class);
                    if (menuModel != null) {
                        list.add(menuModel);
                    }
                }
                menuItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}