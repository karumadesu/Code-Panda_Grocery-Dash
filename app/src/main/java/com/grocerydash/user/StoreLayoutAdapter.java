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
                holder.tileImage.setImageResource(R.drawable.tile_floor);
                break;
            case 1:
                holder.tileImage.setImageResource(R.drawable.tile_shelf);
                break;
            case 2:
                holder.tileImage.setImageResource(R.drawable.tile_wall);
                break;
            case 3:
                holder.tileImage.setImageResource(R.drawable.tile_star);
                break;
            case 4:
                holder.tileImage.setImageResource(R.drawable.tile_user);
                break;
            case 5:
                holder.tileImage.setImageResource(R.drawable.tile_goal);
                break;
            case 6:
                holder.tileImage.setImageResource(R.drawable.tile_currentpath);
                break;
            case 7:
                holder.tileImage.setImageResource(R.drawable.tile_previouspath);
                break;
            case 8:
                holder.tileImage.setImageResource(R.drawable.ic_check);
                break;
            case 11:
                holder.tileImage.setImageResource(R.drawable.tile_1_a);
                break;
            case 12:
                holder.tileImage.setImageResource(R.drawable.tile_1_b);
                break;
            case 13:
                holder.tileImage.setImageResource(R.drawable.tile_1_c);
                break;
            case 14:
                holder.tileImage.setImageResource(R.drawable.tile_1_d);
                break;
            case 21:
                holder.tileImage.setImageResource(R.drawable.tile_2_a);
                break;
            case 22:
                holder.tileImage.setImageResource(R.drawable.tile_2_b);
                break;
            case 23:
                holder.tileImage.setImageResource(R.drawable.tile_2_c);
                break;
            case 24:
                holder.tileImage.setImageResource(R.drawable.tile_2_d);
                break;
            case 31:
                holder.tileImage.setImageResource(R.drawable.tile_3_a);
                break;
            case 32:
                holder.tileImage.setImageResource(R.drawable.tile_3_b);
                break;
            case 41:
                holder.tileImage.setImageResource(R.drawable.tile_4_a);
                break;
            case 42:
                holder.tileImage.setImageResource(R.drawable.tile_4_b);
                break;
            case 43:
                holder.tileImage.setImageResource(R.drawable.tile_4_c);
                break;
            case 44:
                holder.tileImage.setImageResource(R.drawable.tile_4_d);
                break;
            case 51:
                holder.tileImage.setImageResource(R.drawable.tile_5_a);
                break;
            case 52:
                holder.tileImage.setImageResource(R.drawable.tile_5_b);
                break;
            case 61:
                holder.tileImage.setImageResource(R.drawable.tile_6_a);
                break;
            case 62:
                holder.tileImage.setImageResource(R.drawable.tile_6_b);
                break;
            case 63:
                holder.tileImage.setImageResource(R.drawable.tile_6_c);
                break;
            case 64:
                holder.tileImage.setImageResource(R.drawable.tile_6_d);
                break;
            case 71:
                holder.tileImage.setImageResource(R.drawable.tile_7_a);
                break;
            case 72:
                holder.tileImage.setImageResource(R.drawable.tile_7_b);
                break;
            case 81:
                holder.tileImage.setImageResource(R.drawable.tile_8_a);
                break;
            case 82:
                holder.tileImage.setImageResource(R.drawable.tile_8_b);
                break;
            case 83:
                holder.tileImage.setImageResource(R.drawable.tile_8_c);
                break;
            case 84:
                holder.tileImage.setImageResource(R.drawable.tile_8_d);
                break;
            case 91:
                holder.tileImage.setImageResource(R.drawable.tile_9_a);
                break;
            case 92:
                holder.tileImage.setImageResource(R.drawable.tile_9_b);
                break;
            case 101:
                holder.tileImage.setImageResource(R.drawable.tile_10_a);
                break;
            case 102:
                holder.tileImage.setImageResource(R.drawable.tile_10_b);
                break;
            case 103:
                holder.tileImage.setImageResource(R.drawable.tile_10_c);
                break;
            case 104:
                holder.tileImage.setImageResource(R.drawable.tile_10_d);
                break;
            case 121:
                holder.tileImage.setImageResource(R.drawable.tile_12_a);
                break;
            case 122:
                holder.tileImage.setImageResource(R.drawable.tile_12_b);
                break;
            case 123:
                holder.tileImage.setImageResource(R.drawable.tile_12_c);
                break;
            case 124:
                holder.tileImage.setImageResource(R.drawable.tile_12_d);
                break;
            case 141:
                holder.tileImage.setImageResource(R.drawable.tile_14_a);
                break;
            case 142:
                holder.tileImage.setImageResource(R.drawable.tile_14_b);
                break;
            case 143:
                holder.tileImage.setImageResource(R.drawable.tile_14_c);
                break;
            case 144:
                holder.tileImage.setImageResource(R.drawable.tile_14_d);
                break;
            case 145:
                holder.tileImage.setImageResource(R.drawable.tile_14_e);
                break;
            case 146:
                holder.tileImage.setImageResource(R.drawable.tile_14_f);
                break;
            case 151:
                holder.tileImage.setImageResource(R.drawable.tile_15_a);
                break;
            case 152:
                holder.tileImage.setImageResource(R.drawable.tile_15_b);
                break;
            case 153:
                holder.tileImage.setImageResource(R.drawable.tile_15_c);
                break;
            case 154:
                holder.tileImage.setImageResource(R.drawable.tile_15_d);
                break;
            case 161:
                holder.tileImage.setImageResource(R.drawable.tile_16_a);
                break;
            case 162:
                holder.tileImage.setImageResource(R.drawable.tile_16_b);
                break;
            case 163:
                holder.tileImage.setImageResource(R.drawable.tile_16_c);
                break;
            case 164:
                holder.tileImage.setImageResource(R.drawable.tile_16_d);
                break;
            case 165:
                holder.tileImage.setImageResource(R.drawable.tile_16_e);
                break;
            case 166:
                holder.tileImage.setImageResource(R.drawable.tile_16_f);
                break;
            case 171:
                holder.tileImage.setImageResource(R.drawable.tile_17_a);
                break;
            case 172:
                holder.tileImage.setImageResource(R.drawable.tile_17_b);
                break;
            case 173:
                holder.tileImage.setImageResource(R.drawable.tile_17_c);
                break;
            case 174:
                holder.tileImage.setImageResource(R.drawable.tile_17_d);
                break;
            case 181:
                holder.tileImage.setImageResource(R.drawable.tile_18_a);
                break;
            case 182:
                holder.tileImage.setImageResource(R.drawable.tile_18_b);
                break;
            case 183:
                holder.tileImage.setImageResource(R.drawable.tile_18_c);
                break;
            case 184:
                holder.tileImage.setImageResource(R.drawable.tile_18_d);
                break;
            case 185:
                holder.tileImage.setImageResource(R.drawable.tile_18_e);
                break;
            case 186:
                holder.tileImage.setImageResource(R.drawable.tile_18_f);
                break;
            case 191:
                holder.tileImage.setImageResource(R.drawable.tile_19_a);
                break;
            case 192:
                holder.tileImage.setImageResource(R.drawable.tile_19_b);
                break;
            case 193:
                holder.tileImage.setImageResource(R.drawable.tile_19_c);
                break;
            case 194:
                holder.tileImage.setImageResource(R.drawable.tile_19_d);
                break;
            case 201:
                holder.tileImage.setImageResource(R.drawable.tile_20_a);
                break;
            case 202:
                holder.tileImage.setImageResource(R.drawable.tile_20_b);
                break;
            case 203:
                holder.tileImage.setImageResource(R.drawable.tile_20_c);
                break;
            case 204:
                holder.tileImage.setImageResource(R.drawable.tile_20_d);
                break;
            case 205:
                holder.tileImage.setImageResource(R.drawable.tile_20_e);
                break;
            case 206:
                holder.tileImage.setImageResource(R.drawable.tile_20_f);
                break;
            case 211:
                holder.tileImage.setImageResource(R.drawable.tile_21_a);
                break;
            case 212:
                holder.tileImage.setImageResource(R.drawable.tile_21_b);
                break;
            case 213:
                holder.tileImage.setImageResource(R.drawable.tile_21_c);
                break;
            case 214:
                holder.tileImage.setImageResource(R.drawable.tile_21_d);
                break;
            case 221:
                holder.tileImage.setImageResource(R.drawable.tile_22_a);
                break;
            case 222:
                holder.tileImage.setImageResource(R.drawable.tile_22_b);
                break;
            case 223:
                holder.tileImage.setImageResource(R.drawable.tile_22_c);
                break;
            case 224:
                holder.tileImage.setImageResource(R.drawable.tile_22_d);
                break;
            case 230:
                holder.tileImage.setImageResource(R.drawable.tile_23_a);
                break;
            case 231:
                holder.tileImage.setImageResource(R.drawable.tile_23_b);
                break;
            case 232:
                holder.tileImage.setImageResource(R.drawable.tile_23_c);
                break;
            case 233:
                holder.tileImage.setImageResource(R.drawable.tile_23_d);
                break;
            case 234:
                holder.tileImage.setImageResource(R.drawable.tile_23_e);
                break;
            case 235:
                holder.tileImage.setImageResource(R.drawable.tile_23_f);
                break;
            case 236:
                holder.tileImage.setImageResource(R.drawable.tile_23_g);
                break;
            case 237:
                holder.tileImage.setImageResource(R.drawable.tile_23_h);
                break;
            case 238:
                holder.tileImage.setImageResource(R.drawable.tile_23_i);
                break;
            case 239:
                holder.tileImage.setImageResource(R.drawable.tile_23_j);
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
