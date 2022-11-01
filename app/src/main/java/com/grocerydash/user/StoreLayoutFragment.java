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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class StoreLayoutFragment extends Fragment {
    int counter, edgeCount, columnCount, rowCount, currentShelfNumber, nextShelfNumber, currentTileX, currentTileY, entranceTile, prevStartImage, prevGoalImage;
    boolean goalReached;
    GroceryListClass currentItem, nextItem;
    ArrayList <StoreLayoutClass> storeLayout, openList;
    ArrayList <GroceryListClass> groceryList;
    TextView textViewCurrentProductName, textViewNextProductName;
    ImageView imageViewCurrentProductImage;
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
        entranceTile = 4373;
        goalReached = false;
        columnCount = ((MainActivity)getActivity()).numberOfColumns;
        rowCount = ((MainActivity)getActivity()).numberOfRows;
        storeLayout = ((MainActivity)getActivity()).storeLayoutList;
        groceryList = ((MainActivity)getActivity()).groceryList;

        ((MainActivity)getActivity()).storeLayoutList.clear();
        ((MainActivity)getActivity()).graphEdges.clear();
        ((MainActivity)getActivity()).readStoreLayoutFile();
        ((MainActivity)getActivity()).imageButtonBack.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).searchViewSearchProducts.setVisibility(View.INVISIBLE);

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
                startTile.isStartingTile = false;
                startTile.setTileImage(6);

                goalTile.isGoalTile = false;
                goalTile.setTileImage(6);

                counter--;
                updateView();
            }
        });

        nextButton = view.findViewById(R.id.button_nextItem);
        nextButton.setOnClickListener(v -> {
            startTile.isStartingTile = false;
            goalTile.isGoalTile = false;

            counter++;
            updateView();
        });

//        ((MainActivity)getActivity()).imageButtonHome.setOnClickListener(v -> {
//            search();
//        });

        nearestNeighborAlgorithm();
        twoOptAlgorithm();

        ((MainActivity)getActivity()).storeLayoutList.clear();
        ((MainActivity)getActivity()).graphEdges.clear();
        ((MainActivity)getActivity()).readStoreLayoutFile();

        updateView();
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
//            tile.tileImage = 8;
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
                current.setAsPath();
                ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((current.tileXCoordinate * columnCount) + current.tileYCoordinate);
            }
        }

        Collections.reverse(edgePath);
        ((MainActivity)getActivity()).graphEdges.add(edgePath);

        goalTile.isGoalTile = false;
        startTile.isStartingTile = false;
    }

    public void search(){
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

    public void updateView(){
        if(counter == 0){
            nextItem = groceryList.get(counter);
            nextShelfNumber = ((nextItem.getProductX() * columnCount) + nextItem.getProductY());

            startTile = storeLayout.get(entranceTile);
            startTile.setAsStart();
            currentTile = startTile;
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged(entranceTile);

            goalTile = storeLayout.get(nextShelfNumber);
            if(goalTile.tileImage == 1 || goalTile.tileImage == 3){
                if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                }
                else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                }
                else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                    goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                }
                else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                    goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                }
                else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                }
            }
            goalTile.setAsGoal();
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((goalTile.tileXCoordinate * columnCount) + goalTile.tileYCoordinate);

            resetStates();

            for(StoreLayoutClass i : storeLayout){
                getCosts(i);

                if(i.tileImage == 1 || i.tileImage == 2 || i.tileImage == 3){
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
            if(startTile.tileImage == 1 || startTile.tileImage == 3){
                if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage != 1
                        && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage != 3){
                    startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY());
                }
                else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage != 1
                        && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage != 3){
                    startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY());
                }
                else if(storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                    startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1));
                }
                else if(storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                    startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1));
                }
                else if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                    startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1));
                }
                else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                    startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1));
                }
                else if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                    startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1));
                }
                else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                    startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1));
                }
            }
            startTile.setAsStart();
            currentTile = startTile;
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((startTile.tileXCoordinate * columnCount) + startTile.tileYCoordinate);

            goalTile = storeLayout.get(nextShelfNumber);
            if(goalTile.tileImage == 1 || goalTile.tileImage == 3){
                if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                }
                else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                }
                else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                    goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                }
                else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                    goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                }
                else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                }
                else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                        && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                    goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                }
            }
            goalTile.setAsGoal();
            ((MainActivity)getActivity()).storeLayoutAdapter.notifyItemChanged((goalTile.tileXCoordinate * columnCount) + goalTile.tileYCoordinate);

            resetStates();

            for(StoreLayoutClass i : storeLayout){
                getCosts(i);

                if(i.tileImage == 1 || i.tileImage == 2 || i.tileImage == 3){
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

        // Update Bottom Sheet Product Details
        if(counter + 1 < ((MainActivity)getActivity()).groceryList.size()){
            textViewCurrentProductName.setText(nextItem.getProductName());
            textViewNextProductName.setVisibility(View.VISIBLE);
            textViewNextProductName.setText("Next Item: " + groceryList.get(counter + 1).getProductName());

            nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
            nextButton.setText("Next");
            nextButton.setOnClickListener(w -> {
                counter++;
                updateView();
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
                    if(goalTile.tileImage == 1 || goalTile.tileImage == 3){
                        if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                        }
                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                        }
                        else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                        }
                        else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                        }
                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                        }
                    }
                    goalTile.setAsGoal();

                    resetStates();

                    for(StoreLayoutClass j : storeLayout){
                        getCosts(j);

                        if(j.tileImage == 1 || j.tileImage == 2 || j.tileImage == 3){
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
                    if(startTile.tileImage == 1 || startTile.tileImage == 3){
                        if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage != 1
                                && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage != 3){
                            startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY());
                        }
                        else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage != 1
                                && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage != 3){
                            startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY());
                        }
                        else if(storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                            startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1));
                        }
                        else if(storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                            startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                            startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                            startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                            startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1));
                        }
                        else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                            startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1));
                        }
                    }
                    startTile.setAsStart();
                    currentTile = startTile;

                    goalTile = storeLayout.get(nextShelfNumber);
                    if(goalTile.tileImage == 1 || goalTile.tileImage == 3){
                        if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                        }
                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                        }
                        else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                        }
                        else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                        }
                        else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                        }
                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                        }
                    }
                    goalTile.setAsGoal();

                    resetStates();

                    for(StoreLayoutClass j : storeLayout){
                        getCosts(j);

                        if(j.tileImage == 1 || j.tileImage == 2 || j.tileImage == 3){
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
                                    if(goalTile.tileImage == 1 || goalTile.tileImage == 3){
                                        if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                                        }
                                        else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                                && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                        else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                                && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                    }
                                    goalTile.setAsGoal();

                                    resetStates();

                                    for(StoreLayoutClass l : storeLayout){
                                        getCosts(l);

                                        if(l.tileImage == 1 || l.tileImage == 2 || l.tileImage == 3){
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
                                    if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage != 1
                                            && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY()).tileImage != 3){
                                        startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + currentItem.getProductY());
                                    }
                                    else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage != 1
                                            && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY()).tileImage != 3){
                                        startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + currentItem.getProductY());
                                    }
                                    else if(storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                                            && storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                                        startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() + 1));
                                    }
                                    else if(storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                                            && storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                                        startTile = storeLayout.get((currentItem.getProductX() * columnCount) + (currentItem.getProductY() - 1));
                                    }
                                    else if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                                            && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                                        startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() - 1));
                                    }
                                    else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 1
                                            && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1)).tileImage != 3){
                                        startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() - 1));
                                    }
                                    else if(storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                                            && storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                                        startTile = storeLayout.get(((currentItem.getProductX() + 1) * columnCount) + (currentItem.getProductY() + 1));
                                    }
                                    else if(storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 1
                                            && storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1)).tileImage != 3){
                                        startTile = storeLayout.get(((currentItem.getProductX() - 1) * columnCount) + (currentItem.getProductY() + 1));
                                    }
                                    startTile.setAsStart();
                                    currentTile = startTile;

                                    goalTile = storeLayout.get(nextShelfNumber);
                                    if(goalTile.tileImage == 1 || goalTile.tileImage == 3){
                                        if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + nextItem.getProductY());
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY()).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + nextItem.getProductY());
                                        }
                                        else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                                && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                        else if(storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                                && storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                                            goalTile = storeLayout.get((nextItem.getProductX() * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1)).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() - 1));
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() + 1) * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                        else if(storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 1
                                                && storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1)).tileImage != 3){
                                            goalTile = storeLayout.get(((nextItem.getProductX() - 1) * columnCount) + (nextItem.getProductY() + 1));
                                        }
                                    }
                                    goalTile.setAsGoal();

                                    resetStates();

                                    for(StoreLayoutClass l : storeLayout){
                                        getCosts(l);

                                        if(l.tileImage == 1 || l.tileImage == 2 || l.tileImage == 3){
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