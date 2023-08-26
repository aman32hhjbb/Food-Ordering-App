package com.example.foodcorner.BottomFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodcorner.Adapter.NotificationAdapter;
import com.example.foodcorner.Models.NotificationModel;
import com.example.foodcorner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationBottomFragment extends Fragment {

   private View view;
   List<NotificationModel> list;
   String UserId;
    public NotificationBottomFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
        markAllNotificationsAsSeen();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_notification_bottom, container, false);
        list=new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            UserId = bundle.getString("key");
        }
        RecyclerView recyclerView=view.findViewById(R.id.notificationFragmentRecycleView);
        NotificationAdapter notificationAdapter=new NotificationAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notificationAdapter);
        getData(notificationAdapter);
        return view;
    }

    private void getData(NotificationAdapter notificationAdapter) {
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference()
                .child("Data").child("User").child(UserId).child("Notification");

        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);
                    if (!notificationModel.isSeen()) {
                        list.add(notificationModel);
                    }
                }
                Collections.reverse(list);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
    private void markAllNotificationsAsSeen() {
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference()
                .child("Data").child("User").child(UserId).child("Notification");

        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().child("seen").setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

}