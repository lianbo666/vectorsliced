package com.darkgo.vectorsliced.mapper;

import com.darkgo.vectorsliced.entity.Tile;
import com.darkgo.vectorsliced.entity.TileBound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

/**
 * @author 陈永多
 * @ClassName PolygonMapper
 * @description TODO
 * @Date 2019/8/21 17:32
 * @Version 1.0
 **/
@Component
@Mapper
public interface PolygonMapper {
    //layer 跟MapBox-GL 的source-layer有关
    @Select("SELECT\n" +
            "\tST_AsMVT (tile, #{layer,jdbcType=CHAR}) tile \n" +
            "FROM\n" +
            "\t(\n"  +
            "SELECT\n" +
            "\tT.name_ch AS name_ch, T.beg_yr AS beg_yr, T.end_yr AS end_yr, T.color_type AS color_type, T.color AS color, \n" +
            "\tst_asmvtgeom ( T.wkb_geometry, st_makeenvelope (#{tb.west ,jdbcType=NUMERIC}, #{tb.south ,jdbcType=NUMERIC}, #{tb.east ,jdbcType=NUMERIC},#{tb.north,jdbcType=NUMERIC}, 4326 ), 4096, 0, TRUE ) AS geom \n" +
            "FROM\n" +
            "\t${tableName} T) AS tile \n" +
            "WHERE\n" +
            "\ttile.geom IS NOT NULL;")
    @Results(value = {
            @Result(property = "tiles", column = "tile", jdbcType = JdbcType.BINARY)
    })
    Tile selectVectorTiles(String tableName, String layer, TileBound tb);

    @Select("SELECT\n" +
            "\tST_AsMVT ( tile, #{layer,jdbcType=CHAR} ) tile \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\tT.name_ch AS name_ch, T.name_py AS name_py, T.beg_yr AS beg_yr, T.end_yr AS end_yr, T.color AS color, \n" +
            "\tst_asmvtgeom ( T.wkb_geometry, st_makeenvelope (#{tb.west ,jdbcType=NUMERIC}, #{tb.south ,jdbcType=NUMERIC}, #{tb.east ,jdbcType=NUMERIC},#{tb.north,jdbcType=NUMERIC}, 4326 ), 4096, 0, TRUE ) AS geom \n" +
            "FROM\n" +
            "\t${tableName}  AS T \n" +
            "WHERE\n" +
            "( T.beg_yr <= #{year,jdbcType=NUMERIC} AND T.end_yr >= #{year,jdbcType=NUMERIC} )\n" +
            "\t) AS tile \n" +
            "WHERE\n" +
            "\ttile.geom IS NOT NULL;")
    @Results(value = {
            @Result(property = "tiles", column = "tile", jdbcType = JdbcType.BINARY)
    })
    Tile selectVectorTilesByYear(int year, String tableName, String layer, TileBound tb);
}
