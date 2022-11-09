package com.grocerydash.user;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class StoreLayoutFragment extends Fragment {
    int counter, edgeCount, columnCount, rowCount, currentShelfNumber, nextShelfNumber, entranceTile;
    int[] solidTileImages;
    boolean goalReached;
    double runningPrice;
    GroceryListClass currentItem, nextItem;
    ArrayList <StoreLayoutClass> storeLayout, openList;
    ArrayList <GroceryListClass> groceryList;
    TextView textViewCurrentProductName, textViewNextProductName, textViewRunningPrice;
    ImageView imageViewCurrentProductImage;
    ImageButton positionButton;
    Button previousButton, nextButton;
    RecyclerView recyclerView;
    FixedGridLayoutManager fixedGridLayoutManager;
    StoreLayoutClass startTile, goalTile, currentTile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_store_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        counter = 0;
        edgeCount = 0;
        runningPrice = 0;
        entranceTile = 4373;
        goalReached = false;
        columnCount = ((MainActivity)getActivity()).numberOfColumns;
        rowCount = ((MainActivity)getActivity()).numberOfRows;
        storeLayout = ((MainActivity)getActivity()).storeLayoutList;
        groceryList = ((MainActivity)getActivity()).groceryList;
        solidTileImages = new int[] {
                1, 8, 9, 10, 1007, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1206, 1305, 1306,
                1401, 1402, 1403, 1404, 1405, 1406, 1407, 1408, 1505, 1506, 1509, 1605, 1606,
                1705, 1706, 1801, 1802, 1803, 1804, 1805, 1806, 1807, 1808, 1905, 1906, 2005,
                2006, 2101, 2102, 2103, 2104, 2105, 2106, 2107, 2108, 2205, 2206, 2306, 2401,
                2402, 2403, 2404, 2405, 2406, 2407, 2408, 2501, 2502, 2503, 2504, 2505, 2506,
                2507, 2508, 2601, 2602, 2603, 2604, 2605, 2606, 2607, 2608, 2701, 2702, 2703,
                2704, 2705, 2706, 2707, 2708, 2801, 2802, 2803, 2804, 2805, 2806, 2807, 2808,
                2905, 3005, 3101, 3102, 3103, 3104, 3105, 3106, 3107, 3108, 3204, 3207, 3303,
                3307, 3401, 3402, 3403, 3407, 3408, 3502, 3508, 3605, 3606, 3701, 3702, 3705,
                3706, 3707, 3708, 3805, 3905, 3906, 4005, 4006, 4105, 4106, 4206, 4305, 4306,
                4406, 4505, 4606, 4705, 4706, 4806, 4905, 4906, 5005, 5006, 5105, 5205, 5301,
                5302, 5303, 5304, 5305, 5306, 5307, 5308
        };

        ((MainActivity)getActivity()).storeLayoutList.clear();
        ((MainActivity)getActivity()).graphEdges.clear();
        ((MainActivity)getActivity()).readStoreLayoutFile();
        ((MainActivity)getActivity()).imageButtonBack.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).searchViewSearchProducts.setVisibility(View.INVISIBLE);

        textViewCurrentProductName = view.findViewById(R.id.textView_currentProductName);
        textViewNextProductName = view.findViewById(R.id.textView_nextProductName);
        imageViewCurrentProductImage = view.findViewById(R.id.imageView_currentProductImage);

        textViewRunningPrice = view.findViewById(R.id.textView_runningPrice);
        textViewRunningPrice.setText("₱" + String.format("%.2f", runningPrice));

        fixedGridLayoutManager = new FixedGridLayoutManager();
        fixedGridLayoutManager.setTotalColumnCount(((MainActivity)getActivity()).numberOfColumns);

        recyclerView = view.findViewById(R.id.recyclerView_storeLayout);
        recyclerView.setLayoutManager(fixedGridLayoutManager);
        recyclerView.setAdapter(((MainActivity)getActivity()).storeLayoutAdapter);

        previousButton = view.findViewById(R.id.button_previousItem);
        previousButton.setOnClickListener(v -> {
            if(counter > 0){
                startTile.isStartingTile = false;
                startTile.setTileImage(6);

                goalTile.isGoalTile = false;
                goalTile.setTileImage(6);

                counter--;
                runningPrice -= Double.parseDouble(currentItem.getProductPrice()) * currentItem.getProductQuantity();
                aStarAlgorithm();
            }
        });

        nextButton = view.findViewById(R.id.button_nextItem);
        nextButton.setOnClickListener(v -> {
            startTile.isStartingTile = false;
            goalTile.isGoalTile = false;

            runningPrice += Double.parseDouble(nextItem.getProductPrice()) * nextItem.getProductQuantity();
            counter++;
            aStarAlgorithm();
        });

        positionButton = view.findViewById(R.id.imageButton_position);
        positionButton.setOnClickListener(v -> {
            recyclerView.smoothScrollToPosition((startTile.tileXCoordinate * columnCount) + startTile.tileYCoordinate);
        });

        nearestNeighborAlgorithm();
        twoOptAlgorithm();

        ((MainActivity)getActivity()).storeLayoutList.clear();
        ((MainActivity)getActivity()).graphEdges.clear();
        ((MainActivity)getActivity()).readStoreLayoutFile();

        aStarAlgorithm();
    }

    public void getCosts(StoreLayoutClass tile){
        double dx = Math.abs(tile.tileXCoordinate - startTile.tileXCoordinate);
        double dy = Math.abs(tile.tileYCoordinate - startTile.tileYCoordinate);
        tile.gCost = dx + dy;

        dx = Math.abs(tile.tileXCoordinate - goalTile.tileXCoordinate);
        dy = Math.abs(tile.tileYCoordinate - goalTile.tileYCoordinate);
        tile.hCost = dx + dy;

        tile.fCost = tile.gCost + tile.hCost;
    }

    public void openTile(StoreLayoutClass tile){
        if(!tile.isOpenTile && !tile.isClosedTile && !tile.isSolidTile){
            tile.setAsOpen();
//            tile.tileImage = 9;
//            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((tile.tileXCoordinate * columnCount) + tile.tileYCoordinate);
            tile.parentTile = currentTile;
            openList.add(tile);
        }
    }

    public void backTrack(){
        StoreLayoutClass current = goalTile;
        ArrayList<StoreLayoutClass> edgePath = new ArrayList<>();

        while(current != startTile){
            current = current.parentTile;

            if(current != startTile){
                edgePath.add(current);
            }
        }

        if(!edgePath.isEmpty()){
            Collections.reverse(edgePath);
        }

        for(int i = 0; i < edgePath.size(); i++){
            for(int j = 0; j < edgePath.size(); j++){
                if(edgePath.get(i).tileXCoordinate == edgePath.get(j).tileXCoordinate && (Math.abs(edgePath.get(i).tileYCoordinate - edgePath.get(j).tileYCoordinate) > 1)){
                    int iterator = i;
                    int newCount = 0;
                    int bestCount = 0;
                    int counterY = edgePath.get(i).tileYCoordinate;
                    boolean hasObstacle = false;

                    while(edgePath.get(iterator) != edgePath.get(j)){
                        if(i > j){
                            iterator--;
                        }
                        else{
                            iterator++;
                        }

                        bestCount++;
                    }

                    while(counterY != edgePath.get(j).tileYCoordinate){
                        if (storeLayout.get((edgePath.get(i).tileXCoordinate * columnCount) + counterY).isSolidTile) {
                            hasObstacle = true;
                            break;
                        }

                        if(counterY < edgePath.get(j).tileYCoordinate){
                            counterY++;
                        }
                        else{
                            counterY--;
                        }

                        newCount++;
                    }

                    if(!hasObstacle && (newCount < bestCount)){
                        int k = i;

                        while(edgePath.get(k).tileYCoordinate != edgePath.get(j).tileYCoordinate){
                            int previousY = edgePath.get(k).tileYCoordinate;

                            if(counterY < edgePath.get(j).tileYCoordinate){
                                k++;
                            }
                            else{
                                k--;
                            }

                            if(previousY == edgePath.get(k).tileYCoordinate){
                                edgePath.remove(k);
                            }
                            else{
                                edgePath.get(k).setTileXCoordinate(edgePath.get(j).tileXCoordinate);
                            }
                        }
                    }
                    else{
                        break;
                    }
                }
            }
        }

        for(StoreLayoutClass i : edgePath){
            storeLayout.get((i.tileXCoordinate * columnCount) + i.tileYCoordinate).setAsPath();
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((i.tileXCoordinate * columnCount) + i.tileYCoordinate);
        }

        if(!edgePath.isEmpty()){
            ((MainActivity)getActivity()).graphEdges.add(edgePath);
        }

        goalTile.isGoalTile = false;
        startTile.isStartingTile = false;
    }

    public void search(){
        if(currentTile != goalTile){
            while(!goalReached){
                int x = currentTile.tileXCoordinate;
                int y = currentTile.tileYCoordinate;

                currentTile.setAsClosed();
                openList.remove(currentTile);

                if(x - 1 >= 0){
                    openTile(storeLayout.get(((x - 1) * columnCount) + y));
                }
                if(y - 1 >= 0){
                    openTile(storeLayout.get((x * columnCount) + (y - 1)));
                }
                if(x + 1 < rowCount){
                    openTile(storeLayout.get(((x + 1) * columnCount) + y));
                }
                if(y + 1 < columnCount){
                    openTile(storeLayout.get((x * columnCount) + (y + 1)));
                }
//            if(x - 1 >= 0 && y - 1 >= 0){
//                openTile(storeLayout.get(((x - 1) * columnCount) + (y - 1)));
//            }
//            if(y - 1 >= 0 && x + 1 < rowCount){
//                openTile(storeLayout.get(((x + 1) * columnCount) + (y - 1)));
//            }
//            if(x + 1 < rowCount && y + 1 < columnCount){
//                openTile(storeLayout.get(((x + 1) * columnCount) + (y + 1)));
//            }
//            if(y + 1 < columnCount && x - 1 >= 0){
//                openTile(storeLayout.get(((x - 1) * columnCount) + (y + 1)));
//            }

                int bestTileIndex = 0;
                double bestTileCost = 9999;

                for(int i = 0; i < openList.size(); i++){
                    if(openList.get(i).fCost < bestTileCost){
                        bestTileIndex = i;
                        bestTileCost = openList.get(i).fCost;
                    }
                    else if(openList.get(i).fCost == bestTileCost){
                        if(openList.get(i).gCost < openList.get(bestTileIndex).gCost){
                            bestTileIndex = i;
                        }
                    }
                }

                currentTile = openList.get(bestTileIndex);

                if(currentTile == goalTile){
                    goalReached = true;
                    backTrack();
                }
            }
        }
    }

    public void resetStates(){
        openList = new ArrayList<>();
        goalReached = false;

        for(StoreLayoutClass i : storeLayout){
            i.isOpenTile = false;
            i.isClosedTile = false;
            i.parentTile = null;

            if(i.tileImage == 6){
                i.tileImage = 7;
                ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((i.tileXCoordinate * columnCount) + i.tileYCoordinate);
            }
            else if(i.tileImage == 5 && !i.isGoalTile){
                i.tileImage = 7;
                ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((i.tileXCoordinate * columnCount) + i.tileYCoordinate);
            }
            else if(i.tileImage == 4 && !i.isStartingTile){
                i.tileImage = 7;
                ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((i.tileXCoordinate * columnCount) + i.tileYCoordinate);
            }
        }
    }

    public void aStarAlgorithm(){
        if(counter == 0){
            nextItem = groceryList.get(counter);
            nextShelfNumber = ((nextItem.getProductX() * columnCount) + nextItem.getProductY());

            startTile = storeLayout.get(entranceTile);
            startTile.setAsStart();
            currentTile = startTile;
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged(entranceTile);

            goalTile = storeLayout.get(nextShelfNumber);
            if(ArrayUtils.contains(solidTileImages, goalTile.tileImage)){
                if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                    goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                    goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                }
            }
            goalTile.setAsGoal();
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((goalTile.tileXCoordinate * columnCount) + goalTile.tileYCoordinate);

            resetStates();

            for(StoreLayoutClass i : storeLayout){
                getCosts(i);

                if(ArrayUtils.contains(solidTileImages, i.tileImage)){
                    i.setAsSolid();
                }
            }

            search();

            Handler handler = new Handler();
            handler.postDelayed(() -> recyclerView.smoothScrollToPosition(4373), 500);
        }
        else{
            currentItem = groceryList.get(counter - 1);
            currentShelfNumber = ((currentItem.getProductX() * columnCount) + currentItem.getProductY());

            nextItem = groceryList.get(counter);
            nextShelfNumber = ((nextItem.getProductX() * columnCount) + nextItem.getProductY());

            startTile = storeLayout.get(currentShelfNumber);
            if(ArrayUtils.contains(solidTileImages, startTile.tileImage)){
                if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage)){
                    startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY());
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage)){
                    startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY());
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                    startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                    startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                    startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                    startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                    startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                    startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1));
                }
            }
            startTile.setAsStart();
            currentTile = startTile;
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((startTile.tileXCoordinate * columnCount) + startTile.tileYCoordinate);

            goalTile = storeLayout.get(nextShelfNumber);
            if(ArrayUtils.contains(solidTileImages, goalTile.tileImage)){
                if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                    goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                    goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                }
                else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                }
            }
            goalTile.setAsGoal();
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((goalTile.tileXCoordinate * columnCount) + goalTile.tileYCoordinate);

            resetStates();

            for(StoreLayoutClass i : storeLayout){
                getCosts(i);

                if(ArrayUtils.contains(solidTileImages, i.tileImage)){
                    i.setAsSolid();
                }
            }

            search();
        }

        // Update bottom Sheet Product Image
        Picasso.get().load(String.valueOf(nextItem.getProductImageUrl()))
                .resize(360, 360)
                .centerCrop()
                .into(imageViewCurrentProductImage);

        textViewRunningPrice.setText("₱" + String.format("%.2f", runningPrice));
        Log.d("", "" + runningPrice);

        // Update Bottom Sheet Product Details
        if(counter + 1 < ((MainActivity)getActivity()).groceryList.size()){
            textViewCurrentProductName.setText(nextItem.getProductName());
            textViewNextProductName.setVisibility(View.VISIBLE);
            textViewNextProductName.setText("Next Item: " + groceryList.get(counter + 1).getProductName());

            nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
            nextButton.setText("Next");
            nextButton.setOnClickListener(w -> {
                startTile.isStartingTile = false;
                goalTile.isGoalTile = false;

                runningPrice += Double.parseDouble(nextItem.getProductPrice()) * nextItem.getProductQuantity();
                counter++;
                aStarAlgorithm();
            });
        }
        else{
            textViewCurrentProductName.setText(nextItem.getProductName());
            textViewNextProductName.setVisibility(View.GONE);

            nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            nextButton.setText("Done!");
            nextButton.setOnClickListener(w -> {
                ListCompletedFragment listCompletedFragment = new ListCompletedFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top)
                        .replace(R.id.frameLayout_noToolbar, listCompletedFragment)
                        .commit();

                Handler handler = new Handler();
                handler.postDelayed(() -> getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_noSearchView, new Fragment())
                        .commit(), 500);
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

    public void nearestNeighborAlgorithm(){
        double dx, dy, newDistance;
        double bestDistance = 9999;
        int bestIndex = 0;

        for(int i = 0; i < groceryList.size(); i++){
            dx = Math.abs(storeLayout.get(entranceTile).tileXCoordinate -
                    storeLayout.get((groceryList.get(i).getProductX() * columnCount) + groceryList.get(i).getProductY()).tileXCoordinate);
            dy = Math.abs(storeLayout.get(entranceTile).tileYCoordinate -
                    storeLayout.get((groceryList.get(i).getProductX() * columnCount) + groceryList.get(i).getProductY()).tileYCoordinate);
            newDistance = dx + dy;

            if(bestDistance > newDistance) {
                bestDistance = newDistance;
                bestIndex = i;
            }
        }
        Collections.swap(groceryList, 0, bestIndex);
        groceryList.get(0).setVisited(true);

        for(int i = 0; i < groceryList.size() - 1; i++){
            bestDistance = 9999;

            for(int j = 0; j < groceryList.size(); j++){
                if(!groceryList.get(j).isVisited){
                    dx = Math.abs(storeLayout.get((groceryList.get(i).getProductX() * columnCount) + groceryList.get(i).getProductY()).tileXCoordinate -
                            storeLayout.get((groceryList.get(j).getProductX() * columnCount) + groceryList.get(j).getProductY()).tileXCoordinate);
                    dy = Math.abs(storeLayout.get((groceryList.get(i).getProductX() * columnCount) + groceryList.get(i).getProductY()).tileYCoordinate -
                            storeLayout.get((groceryList.get(j).getProductX() * columnCount) + groceryList.get(j).getProductY()).tileYCoordinate);
                    newDistance = dx + dy;

                    if(bestDistance > newDistance){
                        bestDistance = newDistance;
                        bestIndex = j;
                    }
                }
            }
            Collections.swap(groceryList, i + 1, bestIndex);
            groceryList.get(i + 1).setVisited(true);
        }

        for(GroceryListClass i : groceryList){
            i.setVisited(false);
        }
    }

    public void twoOptAlgorithm(){
        boolean foundImprovement = true;

        while(foundImprovement){
            int bestDistance = 0;
            boolean firstItem = true;

            for(int i = 0; i < groceryList.size(); i++){
                if(firstItem){
                    nextItem = groceryList.get(i);
                    nextShelfNumber = ((nextItem.getProductX() * columnCount) + nextItem.getProductY());

                    startTile = storeLayout.get(entranceTile);
                    startTile.setAsStart();
                    currentTile = startTile;

                    goalTile = storeLayout.get(nextShelfNumber);
                    if(ArrayUtils.contains(solidTileImages, goalTile.tileImage)){
                        if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                        }
                    }
                    goalTile.setAsGoal();

                    resetStates();

                    for(StoreLayoutClass j : storeLayout){
                        getCosts(j);

                        if(ArrayUtils.contains(solidTileImages, j.tileImage)){
                            j.setAsSolid();
                        }
                    }

                    search();

                    firstItem = false;
                }
                else{
                    currentItem = groceryList.get(i - 1);
                    currentShelfNumber = ((currentItem.getProductX() * columnCount) + currentItem.getProductY());

                    nextItem = groceryList.get(i);
                    nextShelfNumber = ((nextItem.getProductX() * columnCount) + nextItem.getProductY());

                    startTile = storeLayout.get(currentShelfNumber);
                    if(ArrayUtils.contains(solidTileImages, startTile.tileImage)){
                        if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage)){
                            startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY());
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage)){
                            startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY());
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                            startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                            startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                            startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                            startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                            startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                            startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1));
                        }
                    }
                    startTile.setAsStart();
                    currentTile = startTile;

                    goalTile = storeLayout.get(nextShelfNumber);
                    if(ArrayUtils.contains(solidTileImages, goalTile.tileImage)){
                        if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                        }
                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                        }
                    }
                    goalTile.setAsGoal();

                    resetStates();

                    for(StoreLayoutClass j : storeLayout){
                        getCosts(j);

                        if(ArrayUtils.contains(solidTileImages, j.tileImage)){
                            j.setAsSolid();
                        }
                    }

                    search();
                }
            }

            for(ArrayList<StoreLayoutClass> edge : ((MainActivity)getActivity()).graphEdges){
                bestDistance += edge.size();
            }

            ArrayList<ArrayList<StoreLayoutClass>> bestGraph = new ArrayList<>(((MainActivity)getActivity()).graphEdges);
            ((MainActivity)getActivity()).graphEdges.clear();

            Log.d("", "BEST: " + bestDistance);

            for(int i = 0; i < bestGraph.size(); i++){
                for(int j = 0; j < bestGraph.size(); j++){
                    foundImprovement = false;

                    if(i != j){
                        boolean hasCommonTile = false;
                        for(StoreLayoutClass edge1 : bestGraph.get(i)){
                            for(StoreLayoutClass edge2 : bestGraph.get(j)){
                                if(edge1.tileXCoordinate == edge2.tileXCoordinate && edge1.tileYCoordinate == edge2.tileYCoordinate){
                                    hasCommonTile = true;
                                    break;
                                }
                            }
                        }

                        if(hasCommonTile){
                            ArrayList<GroceryListClass> temp1 = new ArrayList<>(groceryList);
                            Collections.swap(temp1, i, j);

                            int newDistance = 0;
                            boolean isFirst = true;

                            for(int k = 0; k < temp1.size(); k++){
                                if(isFirst){
                                    nextItem = temp1.get(k);
                                    nextShelfNumber = ((nextItem.getProductX() * columnCount) + nextItem.getProductY());

                                    startTile = storeLayout.get(entranceTile);
                                    startTile.setAsStart();
                                    currentTile = startTile;

                                    goalTile = storeLayout.get(nextShelfNumber);
                                    if(ArrayUtils.contains(solidTileImages, goalTile.tileImage)){
                                        if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                    }
                                    goalTile.setAsGoal();

                                    resetStates();

                                    for(StoreLayoutClass l : storeLayout){
                                        getCosts(l);

                                        if(ArrayUtils.contains(solidTileImages, l.tileImage)){
                                            l.setAsSolid();
                                        }
                                    }

                                    search();

                                    isFirst = false;
                                }
                                else{
                                    currentItem = temp1.get(k - 1);
                                    currentShelfNumber = ((currentItem.getProductX() * columnCount) + currentItem.getProductY());

                                    nextItem = temp1.get(k);
                                    nextShelfNumber = ((nextItem.getProductX() * columnCount) + nextItem.getProductY());

                                    startTile = storeLayout.get(currentShelfNumber);
                                    if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage)){
                                        startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY());
                                    }
                                    else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage)){
                                        startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY());
                                    }
                                    else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                                        startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1));
                                    }
                                    else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                                        startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1));
                                    }
                                    else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                                        startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1));
                                    }
                                    else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage)){
                                        startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1));
                                    }
                                    else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                                        startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1));
                                    }
                                    else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage)){
                                        startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1));
                                    }
                                    startTile.setAsStart();
                                    currentTile = startTile;

                                    goalTile = storeLayout.get(nextShelfNumber);
                                    if(ArrayUtils.contains(solidTileImages, goalTile.tileImage)){
                                        if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                        else if(!ArrayUtils.contains(solidTileImages ,storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage)){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                    }
                                    goalTile.setAsGoal();

                                    resetStates();

                                    for(StoreLayoutClass l : storeLayout){
                                        getCosts(l);

                                        if(ArrayUtils.contains(solidTileImages, l.tileImage)){
                                            l.setAsSolid();
                                        }
                                    }

                                    search();
                                }
                            }

                            for(ArrayList<StoreLayoutClass> edge : ((MainActivity)getActivity()).graphEdges){
                                newDistance += edge.size();
                            }
                            Log.d("", "NEW: " + newDistance);

                            if(bestDistance > newDistance){
                                bestDistance = newDistance;
                                groceryList = new ArrayList<>(temp1);
                                foundImprovement = true;
                            }
                            else{
                                ((MainActivity)getActivity()).graphEdges.clear();
                            }
                        }
                    }
                }
            }
            Log.d("", "NEW BEST: " + bestDistance);
        }
        ((MainActivity)getActivity()).groceryList = groceryList;
    }
}