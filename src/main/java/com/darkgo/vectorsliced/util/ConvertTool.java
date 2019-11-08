package com.darkgo.vectorsliced.util;

import com.darkgo.vectorsliced.entity.TileBound;

/**
 * @author 陈永多
 * @ClassName ConvertTool
 * @description TODO
 * @Date 2019/8/21 16:24
 * @Version 1.0
 **/
public class ConvertTool {

    public static TileBound xyToLonlat(final int x, final int y, final int zoom){
        TileBound tb = new TileBound();
        double east = x2Lon(x+1, zoom);
        tb.setEast(east);
        double west = x2Lon(x, zoom);
        tb.setWest(west);
        double north = y2Lat(y, zoom);
        tb.setNorth(north);
        double south = y2Lat(y+1, zoom);
        tb.setSouth(south);
        return tb;
    }

    /**
     * 瓦片转换经度
     * **/
    private static double x2Lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    /**
     *瓦片转换纬度
     * */
    private static double y2Lat(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }
}
