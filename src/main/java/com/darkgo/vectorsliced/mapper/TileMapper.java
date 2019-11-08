package com.darkgo.vectorsliced.mapper;

import com.darkgo.vectorsliced.bean.Tile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface TileMapper {


    @Select("SELECT " +
            "ST_AsMVT ( tile, 'points' ) tile " +
            "FROM " +
            "( " +
            "SELECT " +
            "st_asmvtgeom ( T.wkb_geometry, st_makeenvelope (#{xmin,jdbcType=NUMERIC}, #{ymin,jdbcType=NUMERIC}, #{xmax,jdbcType=NUMERIC},#{ymax,jdbcType=NUMERIC}, 4326 ), 4096, 0, TRUE ) AS geom " +
            "FROM " +
            "county_point T " +
            "WHERE " +
            "( T.beg_yr < #{year,jdbcType=NUMERIC} AND T.end_yr > #{year,jdbcType=NUMERIC} )" +
            ") AS tile " +
            "WHERE " +
            "tile.geom IS NOT NULL;")
    @Results(value = {
            @Result(property = "tiles", column = "tile", jdbcType = JdbcType.BINARY)
    })
    Tile queryTileByPoints(int year, double xmin, double ymin, double xmax, double ymax);


    @Select("SELECT " +
            "ST_AsMVT ( tile, 'polygon' ) polygon " +
            "FROM " +
            "(" +
            "SELECT " +
            "st_asmvtgeom ( T.wkb_geometry, st_makeenvelope (#{xmin,jdbcType=NUMERIC}, #{ymin,jdbcType=NUMERIC}, #{xmax,jdbcType=NUMERIC},#{ymax,jdbcType=NUMERIC}, 4326 ), 4096, 0, TRUE ) AS geom " +
            "FROM " +
            "province_bou T " +
            ") AS tile " +
            "WHERE " +
            "tile.geom IS NOT NULL;")
    @Results(value = {
            @Result(property = "tiles", column = "polygon", jdbcType = JdbcType.BINARY)
    })
    Tile queryProvinceTile(double xmin, double ymin, double xmax, double ymax);


    //layer 跟MapBox-GL 的source-layer有关
    @Select("SELECT " +
            "ST_AsMVT ( tile, #{layer,jdbcType=CHAR} ) tile " +
            "FROM" +
            "(" +
            "SELECT " +
            "T.name_py," +
            "st_asmvtgeom ( T.wkb_geometry, st_makeenvelope (#{xmin,jdbcType=NUMERIC}, #{ymin,jdbcType=NUMERIC}, #{xmax,jdbcType=NUMERIC},#{ymax,jdbcType=NUMERIC}, 4326 ), 4096, 0, TRUE ) AS geom " +
            "FROM " +
            "${tableName} T " +
            "WHERE " +
            "( T.beg_yr < #{year,jdbcType=NUMERIC} AND T.end_yr > #{year,jdbcType=NUMERIC} )" +
            ") AS tile " +
            "WHERE " +
            "tile.geom IS NOT NULL;")
    @Results(value = {
            @Result(property = "tiles", column = "tile", jdbcType = JdbcType.BINARY)
    })
    Tile queryTile(int year, String tableName, String layer, double xmin, double ymin, double xmax, double ymax);


    @Select("SELECT " +
            "ST_AsMVT ( tile, #{layer,jdbcType=CHAR} ) tile " +
            "FROM" +
            "(" +
            "SELECT " +
            "T.name_ch AS name_ch, T.name_py AS name_py, T.beg_yr AS beg_yr, T.end_yr AS end_yr, T.color AS color," +
            "st_asmvtgeom ( T.wkb_geometry, st_makeenvelope (#{xmin,jdbcType=NUMERIC}, #{ymin,jdbcType=NUMERIC}, #{xmax,jdbcType=NUMERIC},#{ymax,jdbcType=NUMERIC}, 4326 ), 4096, 0, TRUE ) AS geom " +
            "FROM " +
            "${tableName} T " +
            "${where} " +
            ") AS tile " +
            "WHERE " +
            "tile.geom IS NOT NULL;")
    @Results(value = {
            @Result(property = "tiles", column = "tile", jdbcType = JdbcType.BINARY)
    })
    Tile queryTileByCustomWhereSQL(String where, String tableName, String layer, double xmin, double ymin, double xmax, double ymax);


    @Select("${sql}")
    @Results(value = {
            @Result(property = "tiles", column = "tile", jdbcType = JdbcType.BINARY)
    })
    Tile queryTileByCustomSQL(String sql);

}
