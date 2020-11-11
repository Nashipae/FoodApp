package com.meadberryfarms.foodapp.viewHolder;

import com.meadberryfarms.foodapp.Interface.ItemClickListener;
import com.meadberryfarms.foodapp.R;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name, desc, orderDate, deliveryDate, status, quantity, price, total;
    public ItemClickListener itemClickListener;


    public UserOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.userOrderName);
        desc = itemView.findViewById(R.id.userOrderDesc);
        orderDate = itemView.findViewById(R.id.userOrderDate);
        deliveryDate = itemView.findViewById(R.id.userOrderDeliveryDate);
        status = itemView.findViewById(R.id.userOrderStatus);
        quantity = itemView.findViewById(R.id.userOrderQuantity);
        price = itemView.findViewById(R.id.userOrderPrice);
        total = itemView.findViewById(R.id.userOrderTotal);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.Onclick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}