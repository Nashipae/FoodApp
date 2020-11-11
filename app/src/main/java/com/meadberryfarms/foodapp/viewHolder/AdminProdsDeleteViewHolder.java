package com.meadberryfarms.foodapp.viewHolder;

import com.meadberryfarms.foodapp.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminProdsDeleteViewHolder extends  RecyclerView.ViewHolder {

    public TextView pnametxt, pidtxt;
    public Button deletebtn;

    public AdminProdsDeleteViewHolder(@NonNull View itemView) {
        super(itemView);
        deletebtn = itemView.findViewById(R.id.deleteProduct);
        pnametxt = itemView.findViewById(R.id.Pname);
        pidtxt = itemView.findViewById(R.id.Pid);
    }
}
