package com.grocerydash.user;

public class StoreLayoutClass{
    int tileXCoordinate, tileYCoordinate, tileImage;
    double gCost, hCost, fCost;
    boolean isStartingTile, isGoalTile, isSolidTile, isOpenTile, isClosedTile;
    StoreLayoutClass parentTile;

    public StoreLayoutClass(int tileXCoordinate, int tileYCoordinate, int tileImage){
        this.tileXCoordinate = tileXCoordinate;
        this.tileYCoordinate = tileYCoordinate;
        this.tileImage = tileImage;
    }

    public void setAsStart(){
        tileImage = 4;
        isStartingTile = true;
    }

    public void setAsGoal(){
        tileImage = 5;
        isGoalTile = true;
    }

    public void setAsSolid(){
        isSolidTile = true;
    }

    public void setAsOpen(){
        isOpenTile = true;
    }

    public void setAsClosed(){
        isClosedTile = true;
    }

    public void setAsPath(){
        tileImage = 6;
    }

    public void setTileImage(int tileImage) {
        this.tileImage = tileImage;
    }

    public void setTileXCoordinate(int tileXCoordinate) {
        this.tileXCoordinate = tileXCoordinate;
    }

    public void setTileYCoordinate(int tileYCoordinate) {
        this.tileYCoordinate = tileYCoordinate;
    }
}
