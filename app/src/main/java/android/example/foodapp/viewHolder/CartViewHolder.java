package android.example.foodapp.viewHolder;

import android.example.foodapp.Interface.ItemClickListener;
import android.example.foodapp.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtPName, txtPQuantity, txtPPrice;
    public Button btnRemove;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtPName = itemView.findViewById(R.id.cartProductName);
        txtPQuantity = itemView.findViewById(R.id.cartProductQuantity);
        txtPPrice = itemView.findViewById(R.id.cartProdPrice);
        btnRemove = itemView.findViewById(R.id.cartRemoveBtn);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.Onclick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
