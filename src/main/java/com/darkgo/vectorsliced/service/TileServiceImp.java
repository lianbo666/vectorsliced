package com.darkgo.vectorsliced.service;

import com.darkgo.vectorsliced.bean.BoundBox;
import com.darkgo.vectorsliced.bean.Tile;
import com.darkgo.vectorsliced.bean.TileBox;
import com.darkgo.vectorsliced.mapper.TileMapper;
import com.darkgo.vectorsliced.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;

@Service("mapService")
public class TileServiceImp implements TileService {

    /**
     * 保存路径
     */
    private static final String MAP_PATH = "./map/";

    /**
     * 开启保存数据
     */
    private static boolean SAVE = true;

    /**
     * 数据库表名称
     */
    private static final String[] TABLE_NAME = {
            "county_point",
            "prefecture_bou",
            "prefecture_point",
            "province_bou",
            "province_point",
            "regime_bou",
            "regime_point"
    };

    /**
     * 对 SQL 添加限制条件
     */
    private String[] where = {
            "WHERE (T.beg_yr>100 And T.end_yr<800 And T.type_py='Zhou')",
            "WHERE (T.beg_yr>100 And T.end_yr<800 And T.type_py='Xian')",

    };

    /**
     * 自定义 SQL
     *
     */
    private String [] customSQL={
      "SELECT ST_AsMVT ( tile, 'points' ) tile FROM (" +
              "SELECT st_asmvtgeom (" +
              " T.wkb_geometry, st_makeenvelope (xmin, ymin, xmax, ymax, 4326 ), 4096, 0, TRUE ) AS geom FROM county_point T )" +
              " AS tile WHERE tile.geom IS NOT NULL;"
    };


    @Autowired
    TileMapper tileMapper;

    /**
     * 从 {数据库} 或 {缓存} 中生成二进制数据
     *
     * @param x
     * @param y
     * @param z
     * @return 瓦片数据
     * @throws IOException 文件读取异常
     */
    public byte[] getTiles(@PathVariable int year, @PathVariable String layer, @PathVariable int x, @PathVariable int y, @PathVariable int z) throws IOException {
        String filePath = year + "/" + layer + "/" + z + "/" + x + "/" + y + ".mvt";
        File file = new File(MAP_PATH + filePath);
        byte[] tiles;

        if (!file.exists()) {
            //从数据库中查询
            tiles = getBytesFromDatabase(year, layer, x, y, z, filePath);
        } else {
            //从文件读取
            tiles = FileUtils.getBytesFromFile(file);
        }
        return tiles;
    }

    /**
     * 通过数据库查询瓦片数据
     *
     * @param layer    图层名称
     * @param x
     * @param y
     * @param z
     * @param filePath 文件路径
     * @return 瓦片数据
     */
    private byte[] getBytesFromDatabase(int year, String layer, @PathVariable int x, @PathVariable int y, @PathVariable int z, String filePath) {
        byte[] tiles;

        String table = "";
        //根据不同
        if (layer.equals("points")) {
            table = TABLE_NAME[0];
        } else if (layer.equals("polygon")) {
            table = TABLE_NAME[1];
        }

        //请求瓦片
        Tile tile = queryTile(year, table, layer, x, y, z);
        //通过 添加限制条件 where 查询数据
//        Tile tile = queryTile(where[0],year, table, layer, x, y, z);
//        Tile tile = queryTileByCustomSQL(x, y, z);


        tiles = tile.getTiles();
        //文件保存到本地
        if (SAVE) {
            FileUtils.byteToFile(tiles, MAP_PATH + filePath);
            System.out.println("保存数据" + MAP_PATH + filePath + "\n");
        }
        return tiles;
    }


    /**
     * 生成瓦片
     *
     * @param year      年
     * @param tableName 表的名称
     * @param layer     图层名称
     * @param x
     * @param y
     * @param zoom
     * @return 瓦片类
     */
    @Override
    public Tile queryTile(int year, String tableName, String layer, int x, int y, int zoom) {
        TileBox tileBox = new BoundBox().tile2boundBox(x, y, zoom);
        return tileMapper.queryTile(year, tableName, layer, tileBox.getXmin(), tileBox.getYmin(), tileBox.getXmax(), tileBox.getYmax());
    }


    /**
     *  生成所有瓦片
     *
     * @param year
     * @param layer
     * @param x
     * @param y
     * @param zoom
     */
    @Override
    public void queryAllTile(int year, String layer, int x, int y, int zoom) {
        String filePath = year + "/" + layer + "/" + zoom + "/" + x + "/" + y + ".mvt";
        //如果保存数据开关关闭 不会生成数据
        if (!SAVE) {
            return;
        }
        //生成所有tile
        getBytesFromDatabase(year, layer, x, y, zoom, filePath);

    }


    /**
     * 自定义 Where SQL 获取 Tile
     *
     * @param tableName
     * @param layer
     * @param x
     * @param y
     * @param zoom
     * @return
     */
    @Override
    public Tile queryTile(String where, int year, String tableName, String layer, int x, int y, int zoom) {
        TileBox tileBox = new BoundBox().tile2boundBox(x, y, zoom);
        return tileMapper.queryTileByCustomWhereSQL(where, tableName, layer, tileBox.getXmin(), tileBox.getYmin(), tileBox.getXmax(), tileBox.getYmax());
    }

    /**
     * 自定义通过 SQL 查询tile
     */

    @Override
    public Tile queryTileByCustomSQL(int x, int y, int zoom) {
        TileBox tileBox = new BoundBox().tile2boundBox(x, y, zoom);
        //自定义 SQL

        return tileMapper.queryTileByCustomSQL(customSQL[0].replace("xmin",tileBox.getXmin()+"")
                .replace("ymin",tileBox.getYmin()+"")
                .replace("xmax",tileBox.getXmax()+"")
                .replace("ymax",tileBox.getYmax()+""));
    }


    /**
     * 获取点
     *
     * @param year 年份
     * @param x
     * @param y
     * @param zoom
     * @return 瓦片类
     */
    @Override
    public Tile queryTileByPoints(int year, int x, int y, int zoom) {
        TileBox tileBox = new BoundBox().tile2boundBox(x, y, zoom);
        return tileMapper.queryTileByPoints(year, tileBox.getXmin(), tileBox.getYmin(), tileBox.getXmax(), tileBox.getYmax());
    }

    /**
     * 获取面
     *
     * @param x
     * @param y
     * @param zoom
     * @return 瓦片类
     */
    @Override
    public Tile queryProvinceTile(int x, int y, int zoom) {
        TileBox tileBox = new BoundBox().tile2boundBox(x, y, zoom);
        return tileMapper.queryProvinceTile(tileBox.getXmin(), tileBox.getYmin(), tileBox.getXmax(), tileBox.getYmax());
    }

}
