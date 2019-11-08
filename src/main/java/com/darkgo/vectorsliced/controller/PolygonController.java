package com.darkgo.vectorsliced.controller;

import com.darkgo.vectorsliced.service.PolygonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 陈永多
 * @ClassName PolygonController
 * @description TODO
 * @Date 2019/8/21 17:15
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/polygons")
public class PolygonController {

    @Autowired
    private PolygonService polygonService;

    @RequestMapping(value = "/{layer}/{z}/{x}/{y}",
            method = RequestMethod.GET,
            produces = "application/x-protobuf"
    )
    public void  getVectorTiles(HttpServletResponse response, @PathVariable String layer,
                                @PathVariable int x, @PathVariable int y, @PathVariable int z) throws IOException {
        //设置响应头
        //response.resetBuffer();
        response.reset();
        response.addHeader("Content-Type", "application/x-protobuf");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        //请求瓦片数据
        byte[] tiles = polygonService.getVectorTiles(layer, x, y, z);

        //输出到屏幕
        OutputStream os = response.getOutputStream();
        os.write(tiles);
        os.flush();
        os.close();
    }

    @RequestMapping(value = "/{year}/{layer}/{z}/{x}/{y}",
            method = RequestMethod.GET,
            produces = "application/x-protobuf"
    )
    public void  getVectorTilesByYear(HttpServletResponse response, @PathVariable int year, @PathVariable String layer,
                                      @PathVariable int x, @PathVariable int y, @PathVariable int z) throws IOException {
        //设置响应头
        //response.resetBuffer();
        response.reset();
        response.addHeader("Content-Type", "application/x-protobuf");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        //请求瓦片数据
        byte[] tiles = polygonService.getVectorTilesByYear(year, layer, x, y, z);

        //输出到屏幕
        OutputStream os = response.getOutputStream();

        os.write(tiles);
        os.flush();
        os.close();
    }
}
