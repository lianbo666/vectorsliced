package com.darkgo.vectorsliced.entity;

/**
 * @author 陈永多
 * @ClassName TileBound
 * @description TODO
 * @Date 2019/8/21 16:26
 * @Version 1.0
 **/
public class TileBound {
    private double north; //ymin
    private double south; //ymax
    private double east; //xmax
    private double west; //xmin

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

    @Override
    public String toString() {
        return "TileBound{" +
                "north=" + north +
                ", south=" + south +
                ", east=" + east +
                ", west=" + west +
                '}';
    }
}
