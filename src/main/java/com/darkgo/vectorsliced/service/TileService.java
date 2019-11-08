package com.darkgo.vectorsliced.service;

import com.darkgo.vectorsliced.bean.Tile;

public interface TileService {

    Tile queryTileByPoints(int year, int x, int y, int zoom);

    Tile queryProvinceTile(int x, int y, int zoom);

    Tile queryTile(int year, String tableName, String layer, int x, int y, int zoom);

    void queryAllTile(int year, String layer, int x, int y, int zoom);

    Tile queryTile(String where,int year,String tableName, String layer, int x, int y, int zoom);

    Tile queryTileByCustomSQL(int x, int y, int zoom);
}
