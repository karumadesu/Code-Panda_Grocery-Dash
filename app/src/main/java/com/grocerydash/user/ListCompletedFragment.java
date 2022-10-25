package com.grocerydash.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ListCompletedFragment extends Fragment {
    TextView totalPrice;
    Button returnToHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_list_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        totalPrice = view.findViewById(R.id.textView_price);
        totalPrice.setText("₱" + String.format("%.2f", ((MainActivity)getActivity()).totalPrice));

        returnToHome = view.findViewById(R.id.button_returnToHome);
        returnToHome.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                    .replace(R.id.frameLayout_noToolbar, new Fragment())
                    .commit();

            HomeFragment homeFragment = new HomeFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout_withSearchView, homeFragment)
                    .commit();

            ((MainActivity)getActivity()).currentlyAtCart = 0;
            ((MainActivity)getActivity()).groceryList.clear();
        });
    }
}