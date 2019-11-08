package com.darkgo.vectorsliced.bean;

public class BoundBox {
   private double north; //ymin
   private double south; //ymax
   private double east; //xmax
    private double west; //xmin

    /**
     * 瓦片获得范围
     * **/
    public TileBox tile2boundBox(final int x, final int y, final int zoom) {
//        BoundBox bb = new BoundBox();
        TileBox bb = new TileBox();

        bb.setYmax(tile2lat(y, zoom));
        bb.setYmin(tile2lat(y + 1, zoom));
        bb.setXmin(tile2lon(x, zoom));
        bb.setXmax(tile2lon(x + 1, zoom));
        return bb;
    }

    /**
     * 瓦片转换经度
     * **/
    private static double tile2lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    /**
     *瓦片转换纬度
     * */
    private static double tile2lat(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }

    /**
     * 根据经纬度和缩放等级，求得瓦片路径
     * **/
    public TileBox getTileNumber(final double lat, final double lon, final int zoom) {
        int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
        int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
        if (xtile < 0)
            xtile=0;
        if (xtile >= (1<<zoom))
            xtile=((1<<zoom)-1);
        if (ytile < 0)
            ytile=0;
        if (ytile >= (1<<zoom))
            ytile=((1<<zoom)-1);
        return tile2boundBox(xtile,ytile,zoom);
    }
}