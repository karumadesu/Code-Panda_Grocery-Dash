package com.grocerydash.user;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class GroceryListFragment extends Fragment {
    TextView totalPrice, listEmpty, budget;
    Button backButton, goButton;
    RecyclerView recyclerViewGroceryList;
    LinearLayoutManager layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_grocery_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(RecyclerView.VERTICAL);

        listEmpty = view.findViewById(R.id.textView_groceryListEmpty);

        budget = view.findViewById(R.id.textView_groceryListBudget);
        budget.setText("₱" + String.format("%.2f", ((MainActivity)getActivity()).budget));
        budget.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Set a budget for your list");

            EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            FrameLayout frame = new FrameLayout(getActivity());
            FrameLayout.LayoutParams parameters = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parameters.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            parameters.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            input.setLayoutParams(parameters);

            frame.addView(input);
            builder.setPositiveButton("OK", (dialog, which) -> {
                ((MainActivity) getActivity()).budget = Double.parseDouble(input.getText().toString());
                budget.setText("₱" + String.format("%.2f", ((MainActivity)getActivity()).budget));

                if(((MainActivity)getActivity()).totalPrice > ((MainActivity)getActivity()).budget){
                    totalPrice.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    totalPrice.setTextColor(getResources().getColor(R.color.green));
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.setView(frame);
            builder.show();
        });

        totalPrice = view.findViewById(R.id.textView_groceryListTotalPrice);
        totalPrice.setText("₱" + String.format("%.2f", ((MainActivity)getActivity()).totalPrice));
        if(((MainActivity)getActivity()).totalPrice > ((MainActivity)getActivity()).budget){
            totalPrice.setTextColor(getResources().getColor(R.color.red));
        }
        else{
            totalPrice.setTextColor(getResources().getColor(R.color.green));
        }

        recyclerViewGroceryList = view.findViewById(R.id.recyclerView_shoppingList);
        recyclerViewGroceryList.setLayoutManager(layout);
        recyclerViewGroceryList.setAdapter(((MainActivity)getActivity()).groceryListAdapter);

        backButton = view.findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> {
            ((MainActivity)getActivity()).currentlyAtCart = 0;

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                    .replace(R.id.frameLayout_noSearchView, new Fragment())
                    .commit();

            HomeFragment homeFragment = new HomeFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout_withSearchView, homeFragment)
                    .commit();
        });

        goButton = view.findViewById(R.id.button_readyToGo);
        goButton.setOnClickListener(v -> {
            ((MainActivity)getActivity()).currentlyAtCart = 3;
            goButton.setClickable(false);

            StoreLayoutFragment storeLayoutFragment = new StoreLayoutFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                    .replace(R.id.frameLayout_noSearchView, storeLayoutFragment)
                    .commit();
        });

        if(((MainActivity)getActivity()).groceryList.isEmpty()){
            goButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
            goButton.setClickable(false);
        }
        else{
            goButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
            goButton.setClickable(true);
        }

        checkListContent();
    }

    public void checkListContent(){
        if(((MainActivity)getActivity()).groceryList.isEmpty()){
            String[] kaomojis = getResources().getStringArray(R.array.kaomojis);

            listEmpty.setText(kaomojis[(int)(Math.random() * 7)] + "\n\nYour list is empty!");
            listEmpty.setVisibility(View.VISIBLE);
        }
    }
}