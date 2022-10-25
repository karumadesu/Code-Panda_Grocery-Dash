package com.grocerydash.user;

import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class StoreLayoutFragment extends Fragment {
    int counter, columnCount;
    boolean onFirstItem;
    TextView textViewCurrentProductName, textViewNextProductName;
    ImageView imageViewCurrentProductImage;
    Button previousButton, nextButton;
    RecyclerView recyclerView;
    FixedGridLayoutManager fixedGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_store_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        counter = 0;
        onFirstItem = true;
        columnCount = ((MainActivity)getActivity()).numberOfColumns;

        ((MainActivity)getActivity()).imageButtonBack.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).progressBar.setVisibility(View.INVISIBLE);

        textViewCurrentProductName = view.findViewById(R.id.textView_currentProductName);
        textViewNextProductName = view.findViewById(R.id.textView_nextProductName);
        imageViewCurrentProductImage = view.findViewById(R.id.imageView_currentProductImage);

        fixedGridLayoutManager = new FixedGridLayoutManager();
        fixedGridLayoutManager.setTotalColumnCount(((MainActivity)getActivity()).numberOfColumns);

        recyclerView = view.findViewById(R.id.recyclerView_storeLayout);
        recyclerView.setLayoutManager(fixedGridLayoutManager);
        recyclerView.setAdapter(((MainActivity)getActivity()).storeLayoutAdapter);

        previousButton = view.findViewById(R.id.button_previousItem);
        previousButton.setOnClickListener(v -> {
            if(counter > 0){
                counter--;
                updateBottomSheet();
            }
        });

        nextButton = view.findViewById(R.id.button_nextItem);
        nextButton.setOnClickListener(v -> {
            counter++;
            updateBottomSheet();
        });

        updateBottomSheet();
    }

    public void updateBottomSheet(){
        GroceryListClass listItem = ((MainActivity)getActivity()).groceryList.get(counter);

        textViewCurrentProductName.setText(listItem.getProductName());

        Picasso.get().load(String.valueOf(listItem.getProductImageUrl()))
                .resize(360, 360)
                .centerCrop()
                .into(imageViewCurrentProductImage);

        if(counter + 1 < ((MainActivity)getActivity()).groceryList.size()){
            textViewNextProductName.setVisibility(View.VISIBLE);
            textViewNextProductName.setText("Next Item: " + ((MainActivity)getActivity()).groceryList.get(counter + 1).getProductName());

            nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
            nextButton.setText("Next");
            nextButton.setOnClickListener(v -> {
                counter++;
                updateBottomSheet();
            });
        }
        else{
            textViewNextProductName.setVisibility(View.GONE);
            nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            nextButton.setText("Done!");
            nextButton.setOnClickListener(v -> {
                ListCompletedFragment listCompletedFragment = new ListCompletedFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top)
                        .replace(R.id.frameLayout_noSearchView, listCompletedFragment)
                        .commit();
            });
        }

        if(counter > 0){
            previousButton.setBackground(getResources().getDrawable(R.drawable.bg_bordered_rectangle));
            previousButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
            previousButton.setPadding(0, 0, 0, 0);
            previousButton.setTextColor(getResources().getColor(R.color.orange));
            previousButton.setClickable(true);
        }
        else{
            previousButton.setBackground(getResources().getDrawable(R.drawable.bg_white_rounded_rectangle));
            previousButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
            previousButton.setPadding(0, 0, 0, 0);
            previousButton.setTextColor(getResources().getColor(R.color.white));
            previousButton.setClickable(false);
        }
    }
}