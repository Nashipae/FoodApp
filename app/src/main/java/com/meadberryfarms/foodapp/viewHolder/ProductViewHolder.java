package com.meadberryfarms.foodapp.viewHolder;


import com.meadberryfarms.foodapp.Interface.ItemClickListener;
import com.meadberryfarms.foodapp.R;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView prodImage;
    public TextView prodDesc, prodName, prodPrice, prodWeight;
    public ItemClickListener listener;
    public Button prodViewMore;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        prodImage = (ImageView) itemView.findViewById(R.id.prodImage);
        prodName = (TextView) itemView.findViewById(R.id.prodName);
        prodPrice = (TextView) itemView.findViewById(R.id.prodPrice);
        prodViewMore = itemView.findViewById(R.id.prodViewMore);
        prodWeight = itemView.findViewById(R.id.prodWeight);

    }

    public void setItemClickListener(ItemClickListener listner){
        this.listener = listner;

    }

    @Override
    public void onClick(View view) {
        listener.Onclick(view, getAdapterPosition(), false);
    }
}
