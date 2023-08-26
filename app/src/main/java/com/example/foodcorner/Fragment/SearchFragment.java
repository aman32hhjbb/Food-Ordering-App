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
import android.widget.SearchView;

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

public class SearchFragment extends Fragment {
    private List<MenuModel> list = new ArrayList<>();
    String userId;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("key");
        }
        Log.d(TAG, "onCreateView: "+userId);
        RecyclerView recyclerView = view.findViewById(R.id.searchFragmentRecycleView);
        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(list, userId, getContext());
        recyclerView.setAdapter(menuItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getData(menuItemAdapter);
        searchItem(view, menuItemAdapter);
        return view;
    }

    private void searchItem(View view, MenuItemAdapter menuItemAdapter) {
        SearchView searchView = view.findViewById(R.id.searchFragmentSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menuItemAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void getData(MenuItemAdapter menuItemAdapter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference menuItemsRef = databaseReference.child("Data").child("MenuItems");

        menuItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the existing list before adding new data
                // Iterate through the children of the snapshot
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
                // Handle error if needed
            }
        });
    }
}
