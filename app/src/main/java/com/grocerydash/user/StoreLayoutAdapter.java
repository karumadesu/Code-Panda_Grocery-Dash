package com.grocerydash.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoreLayoutAdapter extends RecyclerView.Adapter<StoreLayoutAdapter.ViewHolder>{
    String[] storeLayout;
    Context context;

    public StoreLayoutAdapter(Context context, String[] storeLayout){
        this.context = context;
        this.storeLayout = storeLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_store_tile, parent, false);

        return new StoreLayoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){

    }

    @Override
    public int getItemCount(){
        return storeLayout.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView tileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tileImage = itemView.findViewById(R.id.imageView_storeTile);
        }
    }
}
