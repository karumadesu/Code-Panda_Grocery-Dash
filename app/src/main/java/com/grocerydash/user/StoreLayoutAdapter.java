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

public class StoreLayoutAdapter extends RecyclerView.Adapter<StoreLayoutAdapter.ViewHolder> {
    Context context;
    ArrayList<StoreLayoutClass> storeLayoutClassList;
    StoreLayoutClass storeLayoutClass;

    public StoreLayoutAdapter(Context context, ArrayList<StoreLayoutClass> storeLayoutClassList) {
        this.context = context;
        this.storeLayoutClassList = storeLayoutClassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_store_tile, parent, false);

        return new StoreLayoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        storeLayoutClass = storeLayoutClassList.get(position);

        switch (storeLayoutClass.tileImage) {
            case 0:
                holder.tileImage.setImageResource(R.drawable.tile_floor);
                break;
            case 1:
                holder.tileImage.setImageResource(R.drawable.tile_wall);
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
                holder.tileImage.setImageResource(R.drawable.tile_cashier);
                break;
            case 9:
                holder.tileImage.setImageResource(R.drawable.tile_cashier_5);
                break;
            case 10:
                holder.tileImage.setImageResource(R.drawable.tile_cashier_6);
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
            case 1007:
                holder.tileImage.setImageResource(R.drawable.tile_milk_7);
                break;
            case 1101:
                holder.tileImage.setImageResource(R.drawable.tile_frozengoods_1);
                break;
            case 1102:
                holder.tileImage.setImageResource(R.drawable.tile_frozengoods_2);
                break;
            case 1103:
                holder.tileImage.setImageResource(R.drawable.tile_frozengoods_3);
                break;
            case 1104:
                holder.tileImage.setImageResource(R.drawable.tile_frozengoods_4);
                break;
            case 1105:
                holder.tileImage.setImageResource(R.drawable.tile_frozengoods_5);
                break;
            case 1106:
                holder.tileImage.setImageResource(R.drawable.tile_frozengoods_6);
                break;
            case 1107:
                holder.tileImage.setImageResource(R.drawable.tile_frozengoods_7);
                break;
            case 1108:
                holder.tileImage.setImageResource(R.drawable.tile_frozengoods_8);
                break;
            case 1206:
                holder.tileImage.setImageResource(R.drawable.tile_babyneeds_6);
                break;
            case 1305:
                holder.tileImage.setImageResource(R.drawable.tile_cleaningaids_5);
                break;
            case 1306:
                holder.tileImage.setImageResource(R.drawable.tile_cleaningaids_6);
                break;
            case 1401:
                holder.tileImage.setImageResource(R.drawable.tile_home_1);
                break;
            case 1402:
                holder.tileImage.setImageResource(R.drawable.tile_home_2);
                break;
            case 1403:
                holder.tileImage.setImageResource(R.drawable.tile_home_3);
                break;
            case 1404:
                holder.tileImage.setImageResource(R.drawable.tile_home_4);
                break;
            case 1405:
                holder.tileImage.setImageResource(R.drawable.tile_home_5);
                break;
            case 1406:
                holder.tileImage.setImageResource(R.drawable.tile_home_6);
                break;
            case 1407:
                holder.tileImage.setImageResource(R.drawable.tile_home_7);
                break;
            case 1408:
                holder.tileImage.setImageResource(R.drawable.tile_home_8);
                break;
            case 1505:
                holder.tileImage.setImageResource(R.drawable.tile_beverages_5);
                break;
            case 1506:
                holder.tileImage.setImageResource(R.drawable.tile_beverages_6);
                break;
            case 1509:
                holder.tileImage.setImageResource(R.drawable.tile_beverages_9);
                break;
            case 1605:
                holder.tileImage.setImageResource(R.drawable.tile_snacks_5);
                break;
            case 1606:
                holder.tileImage.setImageResource(R.drawable.tile_snacks_6);
                break;
            case 1705:
                holder.tileImage.setImageResource(R.drawable.tile_cannedgoods_5);
                break;
            case 1706:
                holder.tileImage.setImageResource(R.drawable.tile_cannedgoods_6);
                break;
            case 1801:
                holder.tileImage.setImageResource(R.drawable.tile_cookingessentials_1);
                break;
            case 1802:
                holder.tileImage.setImageResource(R.drawable.tile_cookingessentials_2);
                break;
            case 1803:
                holder.tileImage.setImageResource(R.drawable.tile_cookingessentials_3);
                break;
            case 1804:
                holder.tileImage.setImageResource(R.drawable.tile_cookingessentials_4);
                break;
            case 1805:
                holder.tileImage.setImageResource(R.drawable.tile_cookingessentials_5);
                break;
            case 1806:
                holder.tileImage.setImageResource(R.drawable.tile_cookingessentials_6);
                break;
            case 1807:
                holder.tileImage.setImageResource(R.drawable.tile_cookingessentials_7);
                break;
            case 1808:
                holder.tileImage.setImageResource(R.drawable.tile_cookingessentials_8);
                break;
            case 1905:
                holder.tileImage.setImageResource(R.drawable.tile_pantry_5);
                break;
            case 1906:
                holder.tileImage.setImageResource(R.drawable.tile_pantry_6);
                break;
            case 2005:
                holder.tileImage.setImageResource(R.drawable.tile_bakingneeds_5);
                break;
            case 2006:
                holder.tileImage.setImageResource(R.drawable.tile_bakingneeds_6);
                break;
            case 2101:
                holder.tileImage.setImageResource(R.drawable.tile_fsc_1);
                break;
            case 2102:
                holder.tileImage.setImageResource(R.drawable.tile_fsc_2);
                break;
            case 2103:
                holder.tileImage.setImageResource(R.drawable.tile_fsc_3);
                break;
            case 2104:
                holder.tileImage.setImageResource(R.drawable.tile_fsc_4);
                break;
            case 2105:
                holder.tileImage.setImageResource(R.drawable.tile_fsc_5);
                break;
            case 2106:
                holder.tileImage.setImageResource(R.drawable.tile_fsc_6);
                break;
            case 2107:
                holder.tileImage.setImageResource(R.drawable.tile_fsc_7);
                break;
            case 2108:
                holder.tileImage.setImageResource(R.drawable.tile_fsc_8);
                break;
            case 2205:
                holder.tileImage.setImageResource(R.drawable.tile_personalcare_5);
                break;
            case 2206:
                holder.tileImage.setImageResource(R.drawable.tile_personalcare_6);
                break;
            case 2306:
                holder.tileImage.setImageResource(R.drawable.tile_petneeds_6);
                break;
            case 2401:
                holder.tileImage.setImageResource(R.drawable.tile_meat_1);
                break;
            case 2402:
                holder.tileImage.setImageResource(R.drawable.tile_meat_2);
                break;
            case 2403:
                holder.tileImage.setImageResource(R.drawable.tile_meat_3);
                break;
            case 2404:
                holder.tileImage.setImageResource(R.drawable.tile_meat_4);
                break;
            case 2405:
                holder.tileImage.setImageResource(R.drawable.tile_meat_5);
                break;
            case 2406:
                holder.tileImage.setImageResource(R.drawable.tile_meat_6);
                break;
            case 2407:
                holder.tileImage.setImageResource(R.drawable.tile_meat_7);
                break;
            case 2408:
                holder.tileImage.setImageResource(R.drawable.tile_meat_8);
                break;
            case 2501:
                holder.tileImage.setImageResource(R.drawable.tile_poultry_1);
                break;
            case 2502:
                holder.tileImage.setImageResource(R.drawable.tile_poultry_2);
                break;
            case 2503:
                holder.tileImage.setImageResource(R.drawable.tile_poultry_3);
                break;
            case 2504:
                holder.tileImage.setImageResource(R.drawable.tile_poultry_4);
                break;
            case 2505:
                holder.tileImage.setImageResource(R.drawable.tile_poultry_5);
                break;
            case 2506:
                holder.tileImage.setImageResource(R.drawable.tile_poultry_6);
                break;
            case 2507:
                holder.tileImage.setImageResource(R.drawable.tile_poultry_7);
                break;
            case 2508:
                holder.tileImage.setImageResource(R.drawable.tile_poultry_8);
                break;
            case 2601:
                holder.tileImage.setImageResource(R.drawable.tile_seafoods_1);
                break;
            case 2602:
                holder.tileImage.setImageResource(R.drawable.tile_seafoods_2);
                break;
            case 2603:
                holder.tileImage.setImageResource(R.drawable.tile_seafoods_3);
                break;
            case 2604:
                holder.tileImage.setImageResource(R.drawable.tile_seafoods_4);
                break;
            case 2605:
                holder.tileImage.setImageResource(R.drawable.tile_seafoods_5);
                break;
            case 2606:
                holder.tileImage.setImageResource(R.drawable.tile_seafoods_6);
                break;
            case 2607:
                holder.tileImage.setImageResource(R.drawable.tile_seafoods_7);
                break;
            case 2608:
                holder.tileImage.setImageResource(R.drawable.tile_seafoods_8);
                break;
            case 2701:
                holder.tileImage.setImageResource(R.drawable.tile_fruits_1);
                break;
            case 2702:
                holder.tileImage.setImageResource(R.drawable.tile_fruits_2);
                break;
            case 2703:
                holder.tileImage.setImageResource(R.drawable.tile_fruits_3);
                break;
            case 2704:
                holder.tileImage.setImageResource(R.drawable.tile_fruits_4);
                break;
            case 2705:
                holder.tileImage.setImageResource(R.drawable.tile_fruits_5);
                break;
            case 2706:
                holder.tileImage.setImageResource(R.drawable.tile_fruits_6);
                break;
            case 2707:
                holder.tileImage.setImageResource(R.drawable.tile_fruits_7);
                break;
            case 2708:
                holder.tileImage.setImageResource(R.drawable.tile_fruits_8);
                break;
            case 2801:
                holder.tileImage.setImageResource(R.drawable.tile_vegetables_1);
                break;
            case 2802:
                holder.tileImage.setImageResource(R.drawable.tile_vegetables_2);
                break;
            case 2803:
                holder.tileImage.setImageResource(R.drawable.tile_vegetables_3);
                break;
            case 2804:
                holder.tileImage.setImageResource(R.drawable.tile_vegetables_4);
                break;
            case 2805:
                holder.tileImage.setImageResource(R.drawable.tile_vegetables_5);
                break;
            case 2806:
                holder.tileImage.setImageResource(R.drawable.tile_vegetables_6);
                break;
            case 2807:
                holder.tileImage.setImageResource(R.drawable.tile_vegetables_7);
                break;
            case 2808:
                holder.tileImage.setImageResource(R.drawable.tile_vegetables_8);
                break;
            case 2905:
                holder.tileImage.setImageResource(R.drawable.tile_dairy_5);
                break;
            case 3005:
                holder.tileImage.setImageResource(R.drawable.tile_deli_5);
                break;
            case 3101:
                holder.tileImage.setImageResource(R.drawable.tile_recommended_1);
                break;
            case 3102:
                holder.tileImage.setImageResource(R.drawable.tile_recommended_2);
                break;
            case 3103:
                holder.tileImage.setImageResource(R.drawable.tile_recommended_3);
                break;
            case 3104:
                holder.tileImage.setImageResource(R.drawable.tile_recommended_4);
                break;
            case 3105:
                holder.tileImage.setImageResource(R.drawable.tile_recommended_5);
                break;
            case 3106:
                holder.tileImage.setImageResource(R.drawable.tile_recommended_6);
                break;
            case 3107:
                holder.tileImage.setImageResource(R.drawable.tile_recommended_7);
                break;
            case 3108:
                holder.tileImage.setImageResource(R.drawable.tile_recommended_8);
                break;
            case 3204:
                holder.tileImage.setImageResource(R.drawable.tile_egg_4);
                break;
            case 3207:
                holder.tileImage.setImageResource(R.drawable.tile_egg_7);
                break;
            case 3303:
                holder.tileImage.setImageResource(R.drawable.tile_sugar_3);
                break;
            case 3307:
                holder.tileImage.setImageResource(R.drawable.tile_sugar_7);
                break;
            case 3401:
                holder.tileImage.setImageResource(R.drawable.tile_rice_1);
                break;
            case 3402:
                holder.tileImage.setImageResource(R.drawable.tile_rice_2);
                break;
            case 3403:
                holder.tileImage.setImageResource(R.drawable.tile_rice_3);
                break;
            case 3407:
                holder.tileImage.setImageResource(R.drawable.tile_rice_7);
                break;
            case 3408:
                holder.tileImage.setImageResource(R.drawable.tile_rice_8);
                break;
            case 3502:
                holder.tileImage.setImageResource(R.drawable.tile_schoolsupplies_2);
                break;
            case 3508:
                holder.tileImage.setImageResource(R.drawable.tile_schoolsupplies_8);
                break;
            case 3605:
                holder.tileImage.setImageResource(R.drawable.tile_bowl_5);
                break;
            case 3606:
                holder.tileImage.setImageResource(R.drawable.tile_bowl_6);
                break;
            case 3701:
                holder.tileImage.setImageResource(R.drawable.tile_basket_1);
                break;
            case 3702:
                holder.tileImage.setImageResource(R.drawable.tile_basket_2);
                break;
            case 3705:
                holder.tileImage.setImageResource(R.drawable.tile_basket_5);
                break;
            case 3706:
                holder.tileImage.setImageResource(R.drawable.tile_basket_6);
                break;
            case 3707:
                holder.tileImage.setImageResource(R.drawable.tile_basket_7);
                break;
            case 3708:
                holder.tileImage.setImageResource(R.drawable.tile_basket_8);
                break;
            case 3805:
                holder.tileImage.setImageResource(R.drawable.tile_sachet_5);
                break;
            case 3905:
                holder.tileImage.setImageResource(R.drawable.tile_noodles_5);
                break;
            case 3906:
                holder.tileImage.setImageResource(R.drawable.tile_noodles_6);
                break;
            case 4005:
                holder.tileImage.setImageResource(R.drawable.tile_chocolate_5);
                break;
            case 4006:
                holder.tileImage.setImageResource(R.drawable.tile_chocolate_6);
                break;
            case 4105:
                holder.tileImage.setImageResource(R.drawable.tile_biscuit_5);
                break;
            case 4106:
                holder.tileImage.setImageResource(R.drawable.tile_biscuit_6);
                break;
            case 4206:
                holder.tileImage.setImageResource(R.drawable.tile_diaper_6);
                break;
            case 4305:
                holder.tileImage.setImageResource(R.drawable.tile_laundry_5);
                break;
            case 4306:
                holder.tileImage.setImageResource(R.drawable.tile_laundry_6);
                break;
            case 4406:
                holder.tileImage.setImageResource(R.drawable.tile_tools_6);
                break;
            case 4505:
                holder.tileImage.setImageResource(R.drawable.tile_spray_5);
                break;
            case 4606:
                holder.tileImage.setImageResource(R.drawable.tile_napkin_6);
                break;
            case 4705:
                holder.tileImage.setImageResource(R.drawable.tile_soap_5);
                break;
            case 4706:
                holder.tileImage.setImageResource(R.drawable.tile_soap_6);
                break;
            case 4806:
                holder.tileImage.setImageResource(R.drawable.tile_toothpaste_6);
                break;
            case 4905:
                holder.tileImage.setImageResource(R.drawable.tile_shampoo_5);
                break;
            case 4906:
                holder.tileImage.setImageResource(R.drawable.tile_shampoo_6);
                break;
            case 5005:
                holder.tileImage.setImageResource(R.drawable.tile_homeessentials_5);
                break;
            case 5006:
                holder.tileImage.setImageResource(R.drawable.tile_homeessentials_6);
                break;
            case 5105:
                holder.tileImage.setImageResource(R.drawable.tile_sanitary_5);
                break;
            case 5205:
                holder.tileImage.setImageResource(R.drawable.tile_facecare_5);
                break;
            case 5301:
                holder.tileImage.setImageResource(R.drawable.tile_gondola_1);
                break;
            case 5302:
                holder.tileImage.setImageResource(R.drawable.tile_gondola_2);
                break;
            case 5303:
                holder.tileImage.setImageResource(R.drawable.tile_gondola_3);
                break;
            case 5304:
                holder.tileImage.setImageResource(R.drawable.tile_gondola_4);
                break;
            case 5305:
                holder.tileImage.setImageResource(R.drawable.tile_gondola_5);
                break;
            case 5306:
                holder.tileImage.setImageResource(R.drawable.tile_gondola_6);
                break;
            case 5307:
                holder.tileImage.setImageResource(R.drawable.tile_gondola_7);
                break;
            case 5308:
                holder.tileImage.setImageResource(R.drawable.tile_gondola_8);
                break;
        }

//        holder.f.setText(String.valueOf(storeLayoutClass.fCost));
//        holder.g.setText(String.valueOf(storeLayoutClass.gCost));
//        holder.h.setText(String.valueOf(storeLayoutClass.hCost));
    }

    @Override
    public int getItemCount() {
        return storeLayoutClassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
