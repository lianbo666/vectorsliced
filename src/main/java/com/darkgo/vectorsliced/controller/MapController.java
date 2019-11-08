package com.darkgo.vectorsliced.controller;

import com.darkgo.vectorsliced.service.TileServiceImp;
import com.darkgo.vectorsliced.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class MapController {


    @Autowired
    TileServiceImp tileService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String map() {
        return "map";
    }


    /**
     * MapBox-GL 请求瓦片
     *
     * @param response
     * @param year     年份
     * @param layer    图层
     * @param x
     * @param y
     * @param z
     * @throws IOException
     */
    @RequestMapping(value = "/tiles/{year}/{layer}/{z}/{x}/{y}", method = RequestMethod.GET, produces = "application/x-protobuf")
    public void vectorTile(HttpServletResponse response, @PathVariable int year, @PathVariable String layer, @PathVariable int x, @PathVariable int y, @PathVariable int z) throws IOException {
        //添加请求头
        response.addHeader("Content-Type", "application/x-protobuf");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");


        //请求瓦片数据
        byte[] tiles = tileService.getTiles(year, layer, x, y, z);

        //输出到屏幕
        OutputStream os = response.getOutputStream();
        os.write(tiles);
        os.flush();
        os.close();

    }

    /**
     * 生成目标 Zoom 的所有 MVT 缓存数据，并计算生成所有瓦片消耗的时间。
     *
     * 生成规则详解：
     * <p>
     * Zoom levels
     * zoom 参数 默认从 0 - 18 层
     *
     * Zoom 每层生成的瓦片数量
     * </p>
     * zoom     level	                        tile coverage	number of tiles	tile size(*) in degrees
     * 0	    1 tile covers whole world	        1 tile	            360° x 170.1022°
     * 1	    2 × 2 tiles	                        4 tiles	            180° x 85.0511°
     * 2	    4 × 4 tiles	                        16 tiles	        90° x [variable]
     * n	    2n × 2n tiles	                    22n tiles	        360/2n° x [variable]
     * <p>
     * 2n 是指数 2 的 n 次方
     * </p>
     *
     * X and Y
     * X goes from 0 (left edge is 180 °W) to 2zoom − 1 (right edge is 180 °E)
     * Y goes from 0 (top edge is 85.0511 °N) to 2zoom − 1 (bottom edge is 85.0511 °S)
     */
    @RequestMapping(value = "/all/{zoom}", method = RequestMethod.GET)
    @ResponseBody
    public void produceAllTile(HttpServletResponse response, @PathVariable int zoom) {
        FileUtils.print("./produceTime.log", "1");
        //循环生成所有 zoom 的瓦片数据
        for (int z = 0; z < zoom; z++) {
            //x 从 0 到 Math.pow（2，zoom） -1
            for (int x = 0; x < Math.pow(2, z); x++) {
                //y 从 0 到 Math.pow（2，zoom） -1
                for (int y = 0; y < Math.pow(2, z); y++) {
                    //当前只是生成200年，一个 layer 的所有数据
                    tileService.queryAllTile(200, "points", x, y, z);
                    try {
                        response.getWriter().println("200/" + "points/" + z + "/" + x + "/" + y + ".mvt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        FileUtils.print("./produceTime.log", "2");
    }


}
