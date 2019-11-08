package com.darkgo.vectorsliced.service.impl;

import com.darkgo.vectorsliced.entity.Tile;
import com.darkgo.vectorsliced.entity.TileBound;
import com.darkgo.vectorsliced.mapper.PolygonMapper;
import com.darkgo.vectorsliced.service.PolygonService;
import com.darkgo.vectorsliced.util.ConvertTool;
import com.darkgo.vectorsliced.util.FileUtils;
import com.darkgo.vectorsliced.util.TableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author 陈永多
 * @ClassName PolygonServiceImpl
 * @description TODO
 * @Date 2019/8/21 17:17
 * @Version 1.0
 **/
@Service
public class PolygonServiceImpl implements PolygonService {
    //保存路径
    private static final String MAP_PATH = "E://MapCaches/";
    //开启保存数据
    private static final boolean SAVE = true;

    @Autowired
    public PolygonMapper polygonMapper;

    @Override
    public byte[] getVectorTiles(String layer, int x, int y, int z) {
        String filePath = "polygons/" + layer + "/" + z + "/" + x + "/" + y + ".mvt";
        File file = new File(MAP_PATH + filePath);
        byte[] tiles;
        if (!file.exists()) {
            //从数据库中查询
            tiles = getTilesFromDB(layer, x, y, z, filePath);
        } else {
            //从文件读取
            try {
                tiles = FileUtils.getTilesFromCache(file);
            } catch (IOException e) {
                tiles = getTilesFromDB(layer, x, y, z, filePath);
            }
        }
        return tiles;
    }

    @Override
    public byte[] getVectorTilesByYear(int year, String layer, int x, int y, int z) {
        String filePath = "polygons/" + year + "/" + layer + "/" + z + "/" + x + "/" + y + ".mvt";
        File file = new File(MAP_PATH + filePath);
        byte[] tiles;
        if (!file.exists()) {
            //从数据库中查询
            tiles = getTilesFromDB(year, layer, x, y, z, filePath);
        } else {
            //从文件读取
            try {
                tiles = FileUtils.getTilesFromCache(file);
            } catch (IOException e) {
                tiles = getTilesFromDB(layer, x, y, z, filePath);
            }
        }
        return tiles;
    }

    private byte[] getTilesFromDB(int year, String layer, int x, int y, int z, String filePath) {
        byte[] tiles;
        String table;
        if ("province".equalsIgnoreCase(layer)) {
            table = TableList.PROVINCE_BOU;
        } else if ("prefecture".equalsIgnoreCase(layer)){
            table = TableList.PREFECTURE_BOU;
        } else if ("regime".equalsIgnoreCase(layer)){
            table = TableList.REGIME_BOU;
        } else {
            table = TableList.PROVINCE_BOU;
        }

        //请求瓦片
        TileBound tb = ConvertTool.xyToLonlat(x, y, z);
        Tile tile = polygonMapper.selectVectorTilesByYear(year, table, layer, tb);
        tiles = tile.getTiles();
        //文件保存到本地
        if (SAVE) {
            FileUtils.byteToFile(tiles, MAP_PATH + filePath);
            System.out.printf("保存数据" + MAP_PATH + filePath + "\n");
        }
        return tiles;
    }

    /**
     * 功能描述：从数据库获取瓦片数据
     * @param x
     * @param y
     * @param z
     * @param filePath
     * @return
     */
    private byte[] getTilesFromDB (String layer, Integer x, Integer y, Integer z, String filePath){
        byte[] tiles;
        String table;
        if ("province".equalsIgnoreCase(layer)) {
            table = TableList.PROVINCE_BOU;
        } else if ("prefecture".equalsIgnoreCase(layer)){
            table = TableList.PREFECTURE_BOU;
        } else if ("regime".equalsIgnoreCase(layer)){
            table = TableList.REGIME_BOU;
        } else {
            table = TableList.PROVINCE_BOU;
        }

        //请求瓦片
        TileBound tb = ConvertTool.xyToLonlat(x, y, z);
        Tile tile = polygonMapper.selectVectorTiles(table, layer, tb);
        tiles = tile.getTiles();
        //文件保存到本地
        if (SAVE) {
            FileUtils.byteToFile(tiles, MAP_PATH + filePath);
            System.out.printf("保存数据" + MAP_PATH + filePath + "\n");
        }
        return tiles;
    }


}
