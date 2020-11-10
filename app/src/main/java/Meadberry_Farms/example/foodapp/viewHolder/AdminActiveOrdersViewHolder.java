package Meadberry_Farms.example.foodapp.viewHolder;

import Meadberry_Farms.example.foodapp.Interface.ItemClickListener;
import android.example.foodapp.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminActiveOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name, desc, orderDate, deliveryDate, status, quantity, price, total, userID, userAddress;
    public Button accept,reject;
    public ItemClickListener itemClickListener;


    public AdminActiveOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.userOrderName);
        userID = itemView.findViewById(R.id.userID);
        userAddress = itemView.findViewById(R.id.userAddress);
        desc = itemView.findViewById(R.id.userOrderDesc);
        orderDate = itemView.findViewById(R.id.userOrderDate);
        deliveryDate = itemView.findViewById(R.id.userOrderDeliveryDate);
        status = itemView.findViewById(R.id.userOrderStatus);
        quantity = itemView.findViewById(R.id.userOrderQuantity);
        price = itemView.findViewById(R.id.userOrderPrice);
        total = itemView.findViewById(R.id.userOrderTotal);
        accept = itemView.findViewById(R.id.adminAllowOrder);
        reject = itemView.findViewById(R.id.adminRejectOrder);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.Onclick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}