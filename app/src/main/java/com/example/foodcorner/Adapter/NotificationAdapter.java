package com.example.foodcorner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcorner.Models.NotificationModel;
import com.example.foodcorner.R;

import java.util.List;
import java.util.Objects;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationModel> notificationList;

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
         TextView orderIdTextView, statusTextView,TimeStamp;
         ImageView Image;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            TimeStamp=itemView.findViewById(R.id.timestamp);
            orderIdTextView = itemView.findViewById(R.id.notificationItemOrderId);
            statusTextView = itemView.findViewById(R.id.notificationItemStatus);
            Image = itemView.findViewById(R.id.notificationItemImage);
        }

        public void bind(NotificationModel notification) {
            orderIdTextView.setText(notification.getOrderId());
            statusTextView.setText(notification.getStatus());
            TimeStamp.setText(notification.getTimeStamp());
            if(Objects.equals(notification.getStatus(), "Your Order is Completed")){
                Image.setImageResource(R.drawable.truck);
            }
            else{
                Image.setImageResource(R.drawable.sademoji);

            }

        }
    }
}
