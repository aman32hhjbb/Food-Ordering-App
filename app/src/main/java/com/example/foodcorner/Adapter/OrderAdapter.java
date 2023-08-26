package com.example.foodcorner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcorner.Activity.OrderDetailActivity;
import com.example.foodcorner.Models.OrderModel;
import com.example.foodcorner.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<OrderModel> orderList;
    String UserId;

    public OrderAdapter(Context context, List<OrderModel> orderList,String userid) {
        this.context = context;
        this.orderList = orderList;
        UserId=userid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.Timestamp.setText(order.getTimeStamp());
        holder.OrderId.setText(order.getOrderId());
        holder.Quantity.setText(order.getQuantity());
        holder.Total.setText(order.getTotalAmount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, OrderDetailActivity.class);
                intent.putExtra("OrderId",order.getOrderId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView OrderId,Quantity,Total,Timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Timestamp=itemView.findViewById(R.id.timestamp);
            OrderId = itemView.findViewById(R.id.OrderItemId);
            Quantity = itemView.findViewById(R.id.OrderItemQuantity);
            Total = itemView.findViewById(R.id.OrderItemTotal);
        }
    }
}

