package com.darkgo.vectorsliced.entity;

import java.util.Arrays;

/**
 * @author 陈永多
 * @ClassName Tile
 * @description TODO
 * @Date 2019/8/21 15:54
 * @Version 1.0
 **/
public class Tile {

    private byte[] tiles;

    public byte[] getTiles() {
        return tiles;
    }

    public void setTiles(byte[] tiles) {
        this.tiles = tiles;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }
}
