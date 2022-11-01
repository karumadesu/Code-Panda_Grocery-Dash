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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StoreLayoutAdapter extends RecyclerView.Adapter<StoreLayoutAdapter.ViewHolder>{
    Context context;
    ArrayList<StoreLayoutClass> storeLayoutClassList;
    StoreLayoutClass storeLayoutClass;

    public StoreLayoutAdapter(Context context, ArrayList<StoreLayoutClass> storeLayoutClassList){
        this.context = context;
        this.storeLayoutClassList = storeLayoutClassList;
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
        storeLayoutClass = storeLayoutClassList.get(position);

        switch(storeLayoutClass.tileImage){
            case 0:
                holder.tileImage.setImageResource(R.drawable.ic_floor);
                break;
            case 1:
                holder.tileImage.setImageResource(R.drawable.ic_basket);
                break;
            case 2:
                holder.tileImage.setImageResource(R.drawable.ic_wall);
                break;
            case 3:
                holder.tileImage.setImageResource(R.drawable.ic_star);
                break;
            case 4:
                holder.tileImage.setImageResource(R.drawable.ic_user);
                break;
            case 5:
                holder.tileImage.setImageResource(R.drawable.ic_goal);
                break;
            case 6:
                holder.tileImage.setImageResource(R.drawable.ic_currentpath);
                break;
            case 7:
                holder.tileImage.setImageResource(R.drawable.ic_previouspath);
                break;
            case 8:
                holder.tileImage.setImageResource(R.drawable.ic_check);
                break;
        }

//        holder.f.setText(String.valueOf(storeLayoutClass.fCost));
//        holder.g.setText(String.valueOf(storeLayoutClass.gCost));
//        holder.h.setText(String.valueOf(storeLayoutClass.hCost));
    }

    @Override
    public int getItemCount(){
        return storeLayoutClassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView tileImage;
//        TextView f,g,h;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tileImage = itemView.findViewById(R.id.imageView_storeTile);
//            f = itemView.findViewById(R.id.fcost);
//            g = itemView.findViewById(R.id.gcost);
//            h = itemView.findViewById(R.id.hcost);
        }
    }
}
