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
import android.widget.TextView;

import com.example.foodcorner.Adapter.OrderAdapter;
import com.example.foodcorner.Models.OrderModel;
import com.example.foodcorner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryFragment extends Fragment {
    String UserId;
    RecyclerView recyclerView;
    List<OrderModel> list;
    TextView PendingText, CompletedText;
    String Pending = "Pending", Completed = "Completed";
    OrderAdapter orderAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        list = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            UserId = bundle.getString("key");
        }

        recyclerView = view.findViewById(R.id.historyFragmentRecycleView);
        orderAdapter = new OrderAdapter(getContext(), list,UserId);
        recyclerView.setAdapter(orderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getData(Pending);
        pendingMethod(view, Pending);
        CompletedMethod(view, Completed);
        return view;
    }

    private void CompletedMethod(View view, String condition) {
        CompletedText = view.findViewById(R.id.historyFragmentCompleted);
        CompletedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(condition);
            }
        });

    }

    private void pendingMethod(View view, String condition) {
        PendingText = view.findViewById(R.id.historyFragmentPending);
        PendingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(condition);
            }
        });
    }

    private void getData(String condition) {
        FirebaseDatabase.getInstance().getReference().child("Data").child("Order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding new data
                for (DataSnapshot data : snapshot.getChildren()) { // Use getChildren() here
                    OrderModel orderModel = data.getValue(OrderModel.class);
                    if (orderModel.getDeliveryStatus().equals(condition)) {
                        list.add(orderModel);
                    }
                }
                Log.d(TAG, "onDataChange: "+list.size());
                Collections.reverse(list);
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }
}
