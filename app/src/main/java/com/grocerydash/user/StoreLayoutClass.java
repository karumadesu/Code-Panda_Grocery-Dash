package com.grocerydash.user;

public class StoreLayoutClass {
    int xCoordinate, yCoordinate, tileImage, distance;
    boolean isObstacle;

    public StoreLayoutClass(int xCoordinate, int yCoordinate, int tileImage, int distance, boolean isObstacle) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.tileImage = tileImage;
        this.distance = distance;
        this.isObstacle = isObstacle;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public int getTileImage() {
        return tileImage;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setTileImage(int tileImage) {
        this.tileImage = tileImage;
    }
}
