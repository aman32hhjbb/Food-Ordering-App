package com.example.foodcorner.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodcorner.Models.UserModel;
import com.example.foodcorner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    EditText Name, Address, Phone, Email;
    AppCompatButton SaveInfoButton;
    boolean isEditing = false;
    String userId;
    UserModel userModel;
    DatabaseReference db;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("key");
        }
        Log.d(TAG, "onCreateView: " + userId);
        db = FirebaseDatabase.getInstance().getReference().child("Data").child("User").child(userId).child("PersonalData");
        Name = view.findViewById(R.id.profileFragmentName);
        Address = view.findViewById(R.id.profileFragmentAddress);
        Phone = view.findViewById(R.id.profileFragmentPhone);
        Email = view.findViewById(R.id.profileFragmentEmail);
        SaveInfoButton = view.findViewById(R.id.profileFragmentSaveButton);
        showInfo();
        SaveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing) {
                    if (isAllFieldsFilled()) {
                        // Save the edited information
                        userModel = new UserModel();
                        userModel.setName(Name.getText().toString());
                        userModel.setAddress(Address.getText().toString());
                        userModel.setMobileNo(Phone.getText().toString());
                        userModel.setEmail(Email.getText().toString());

                        // Update the data in Firebase
                        db.setValue(userModel);

                        // Disable editing after saving
                        toggleEditing();
                    } else {
                        Toast.makeText(getActivity(), "All fields are required.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    toggleEditing();
                }
            }
        });

        return view;
    }

    private void showInfo() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);
                if (userModel != null) {
                    finalShow();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }

    private void finalShow() {
        Name.setText(userModel.getName());
        Email.setText(userModel.getEmail());
        if (userModel.getAddress() != null) {
            Address.setText(userModel.getAddress());
        }
        if (userModel.getMobileNo() != null) {
            Phone.setText(userModel.getMobileNo());
        }
    }

    private void toggleEditing() {
        if (isEditing) {
            // Disable editing
            disable(Name);
            disable(Address);
            disable(Phone);
            disable(Email);
            SaveInfoButton.setText("Edit Information");
        } else {
            // Enable editing
            editable(Name);
            editable(Address);
            editable(Phone);
            editable(Email);
            SaveInfoButton.setText("Save Information");
        }
        isEditing = !isEditing;
    }

    private void editable(EditText editText) {
        editText.setEnabled(true); // Enable editing
        editText.setFocusable(true); // Make focusable
        editText.setFocusableInTouchMode(true); // Enable focusable in touch mode
    }

    private void disable(EditText editText) {
        editText.setEnabled(false); // Disable editing
        editText.setFocusable(false); // Make not focusable
        editText.setFocusableInTouchMode(false); // Disable focusable in touch mode
    }

    private boolean isAllFieldsFilled() {
        return !Name.getText().toString().isEmpty() &&
                !Address.getText().toString().isEmpty() &&
                !Phone.getText().toString().isEmpty() &&
                !Email.getText().toString().isEmpty();
    }
}
