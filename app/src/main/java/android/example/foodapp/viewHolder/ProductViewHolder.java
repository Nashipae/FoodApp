package android.example.foodapp.viewHolder;


import android.example.foodapp.Interface.ItemClickListener;
import android.example.foodapp.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//    public ImageView prodImage;
    public TextView prodDesc, prodName, prodPrice;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
//        prodImage = (ImageView) itemView.findViewById(R.id.prodImage);
        prodDesc = (TextView) itemView.findViewById(R.id.prodDesc);
//        prodName = (ImageView) itemView.findViewById(R.id.prodName);
        prodPrice = (TextView) itemView.findViewById(R.id.prodPrice);

    }

    public void setItemClickListener(ItemClickListener listner){
        this.listener = listner;

    }

    @Override
    public void onClick(View view) {
        listener.Onclick(view, getAdapterPosition(), false);
    }
}
