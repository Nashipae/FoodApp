package Meadberry_Farms.example.foodapp.viewHolder;

import Meadberry_Farms.example.foodapp.Interface.ItemClickListener;
import android.example.foodapp.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtPName, txtPQuantity, txtPPrice;
    public ImageView image;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtPName = itemView.findViewById(R.id.cartProductName);
        txtPQuantity = itemView.findViewById(R.id.cartProductQuantity);
        txtPPrice = itemView.findViewById(R.id.cartProdPrice);
        image = itemView.findViewById(R.id.prodImage);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.Onclick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
