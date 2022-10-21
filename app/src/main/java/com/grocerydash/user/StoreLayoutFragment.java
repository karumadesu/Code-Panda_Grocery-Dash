package com.grocerydash.user;

import static java.lang.Math.ceil;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StoreLayoutFragment extends Fragment {
    int scrollValue;
    TextView textViewCurrentProductName, textViewNextProductName;
    ImageView imageViewCurrentProductImage;
    Button previousButton, nextButton;
    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;
    HorizontalScrollView horizontalScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_store_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).imageButtonBack.setVisibility(View.VISIBLE);
        nestedScrollView = view.findViewById(R.id.nestedScrollView_storeLayout);
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView_storeLayout);
        textViewCurrentProductName = view.findViewById(R.id.textView_currentProductName);
        textViewNextProductName = view.findViewById(R.id.textView_nextProductName);
        imageViewCurrentProductImage = view.findViewById(R.id.imageView_currentProductImage);
        previousButton = view.findViewById(R.id.button_previousItem);
        nextButton = view.findViewById(R.id.button_nextItem);

        recyclerView = view.findViewById(R.id.recyclerView_storeLayout);
        recyclerView.setAdapter(((MainActivity)getActivity()).storeLayoutAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), ((MainActivity)getActivity()).numberOfColumns){
            @Override
            public void onLayoutCompleted(final RecyclerView.State state){
                super.onLayoutCompleted(state);

                scrollValue = recyclerView.getChildAt(0).getMeasuredHeight();
                nestedScrollView.smoothScrollTo(0, scrollValue * 79);
                horizontalScrollView.smoothScrollTo((int) (36 * scrollValue + ((scrollValue / 3) * 2)), 0);
            }
        });
        recyclerView.setHasFixedSize(true);

        previousButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
        previousButton.setClickable(false);
    }
}