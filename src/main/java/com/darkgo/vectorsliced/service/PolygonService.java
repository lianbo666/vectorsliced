package com.darkgo.vectorsliced.service;

/**
 * @author 陈永多
 * @ClassName PolygonService
 * @description TODO
 * @Date 2019/8/21 17:17
 * @Version 1.0
 **/
public interface PolygonService {

    byte[] getVectorTiles(String layer, int x, int y, int z);

    byte[] getVectorTilesByYear(int year, String layer, int x, int y, int z);
}
