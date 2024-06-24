package com.example.groceryexpirytrackingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    List<ItemVer1> itemList;
    Context context;

    public ItemAdapter(Context context, List<ItemVer1> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(itemList.get(position).getName());
        holder.barcode.setText(itemList.get(position).getBarcode());
        holder.dayLeft.setText(itemList.get(position).getExp_date());
        String expire=itemList.get(position).getExp_date();


        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Item_Update.class);
                intent.putExtra("Image",itemList.get(position).getImageUrl());
                intent.putExtra("Name",itemList.get(position).getName());
                intent.putExtra("Exp",itemList.get(position).getExp_date());
                intent.putExtra("PDate",itemList.get(position).getPurchasedate());
                intent.putExtra("Barcode",itemList.get(position).getBarcode());
                intent.putExtra("NumItem",itemList.get(position).getNumOfItem());
                intent.putExtra("key",itemList.get(position).getKey());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void searchItemInformation(ArrayList<ItemVer1> searchList){
        itemList=searchList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,dayLeft,barcode;
        CardView itemCard;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.row_item_name);
            barcode=itemView.findViewById(R.id.row_barcode);
            dayLeft=itemView.findViewById(R.id.number_days);
            itemCard=itemView.findViewById(R.id.Itemcard1);

        }

}
}


