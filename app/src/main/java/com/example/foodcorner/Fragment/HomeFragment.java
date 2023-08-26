package com.example.foodcorner.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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


public class HomeFragment extends Fragment {
private View view;
 private List<MenuModel> list;
 FirebaseDatabase firebaseDatabase;
 TextView textView;
private MenuItemAdapter menuItemAdapter;
private String userId;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("key");
        }
        Log.d(TAG, "onCreateView: "+userId);
        list = new ArrayList<>();
         checkAllMenu(view);
         SlideImage();
         setRecycleView(view);
        return view;
    }

    private void checkAllMenu(View view) {

                textView=view.findViewById(R.id.homeFragmentViewMenu);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("key", userId);
                        menuFragment menu = new menuFragment();
                        menu.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainer, menu);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
    }

    private void setRecycleView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.homeFragmentRecycleView);
        menuItemAdapter=new MenuItemAdapter(list,userId,getContext());
        recyclerView.setAdapter(menuItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference();
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

    private void SlideImage() {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        String banner1 = "https://firebasestorage.googleapis.com/v0/b/street-food-bd277.appspot.com/o/menu_item_images%2Fbanner1.jpg?alt=media&token=827ef647-f01d-4bcd-b42e-f3864448e93c";
        String banner2 = "https://firebasestorage.googleapis.com/v0/b/street-food-bd277.appspot.com/o/menu_item_images%2Fbanner2.jpg?alt=media&token=848a04f2-77cc-4ea8-bba7-00dd99235564";
        String banner3 = "https://firebasestorage.googleapis.com/v0/b/street-food-bd277.appspot.com/o/menu_item_images%2Fbanner3.jpg?alt=media&token=733c1d60-c18a-42e7-8d47-cb5986e45d10";

        imageList.add(new SlideModel(banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(banner3, ScaleTypes.FIT));

        ImageSlider imageSlider = view.findViewById(R.id.homeFragmentImageSlider);
        imageSlider.setImageList(imageList, ScaleTypes.FIT);
    }



}