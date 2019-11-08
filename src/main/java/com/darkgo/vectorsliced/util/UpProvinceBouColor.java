package com.darkgo.vectorsliced.util;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 陈永多
 * @ClassName UpProvinceBouColor
 * @description TODO
 * @Date 2019/9/2 15:38
 * @Version 1.0
 **/
public class UpProvinceBouColor {
    private static POIFSFileSystem fs;
    private static HSSFWorkbook wb;
    private static HSSFSheet sheet;
    private static HSSFRow row;

    public static void main(String[] args) {
        try {
//            UpProvinceBouColor excelReader = new UpProvinceBouColor();

            // 获取文件输入流
            InputStream is = new FileInputStream("E:\\names.xls");
            List<String> list = readExcel(is);
//            System.out.println(list.size());
//            System.out.println(list.get(0));
//            System.out.println(list.get(list.size()-1));

            Connection conn = DbConnectTool.getConnection();
            String table_name = "province_bou";
            int count = 1; //计数器
            for (String name : list){
                String sql = "UPDATE "+table_name+" SET color=? WHERE name_ch=?";
                PreparedStatement prep = conn.prepareStatement(sql);
                prep.setString(1, getColor());
                prep.setString(2, name);
                prep.addBatch();
                prep.executeBatch();
                System.out.println("执行第---"+(count++)+"---条,=>"+name);
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> readExcel(InputStream is) {
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取Excel表中具体某一页表格对象
        sheet = wb.getSheetAt(0); // 由0开始，是sheet1，即：未确权
        // 获取表格的数据总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        // 获取某一行表格的总列数
        //int colNum = row.getPhysicalNumberOfCells();

        List<String> list  =new ArrayList<>();
        for (int i=0; i<=rowNum; i++){
            row = sheet.getRow(i);
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String cellContent = row.getCell(0).getStringCellValue();
            //System.out.println("第"+(i+1)+"行："+cellContent);
            if (cellContent != null && !("".equals(cellContent))){
                list.add(cellContent.trim());
            }
        }
        //System.out.println(list);
        return list;
    }

    /** 获取指定长度的16进制字符串. */
    public static String randomHexStr(int len) {
        try {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < len; i++) {
                //随机生成0-15的数值并转换成16进制
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return result.toString().toUpperCase();
        } catch (Exception e) {
            System.out.println("获取16进制字符串异常，返回默认...");
            return "00CCCC";
        }
    }

    //随机生成颜色代码
    private static String getColor() {
        //红色
        String red;
        //绿色
        String green;
        //蓝色
        String blue;
        //生成随机对象
        Random random = new Random();
        //生成红色颜色代码
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成绿色颜色代码
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成蓝色颜色代码
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

        //判断红色代码的位数
        red = red.length() == 1 ? "0" + red : red;
        //判断绿色代码的位数
        green = green.length() == 1 ? "0" + green : green;
        //判断蓝色代码的位数
        blue = blue.length() == 1 ? "0" + blue : blue;
        //生成十六进制颜色值
        String color = "#" + red + green + blue;
        return color;
    }
}
