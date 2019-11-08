package com.darkgo.vectorsliced.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * @param contents 二进制数据
     * @param filePath 文件存放目录，包括文件名及其后缀，如D:\file\bike.jpg
     * @Title: byteToFile
     * @Description: 把二进制数据转成指定后缀名的文件，例如PDF，PNG等
     * @Author: Wiener
     * @Time: 2018-08-26 08:43:36
     */
    public static void byteToFile(byte[] contents, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream output = null;
        try {
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(contents);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            // 获取文件的父路径字符串
            File path = file.getParentFile();
            if (!path.exists()) {
                logger.info("文件夹不存在，创建。path={}", path);
                boolean isCreated = path.mkdirs();
                if (!isCreated) {
                    logger.error("创建文件夹失败，path={}", path);
                }
            }
            fos = new FileOutputStream(file);
            // 实例化OutputString 对象
            output = new BufferedOutputStream(fos);
            byte[] buffer = new byte[4096];
            int length = bis.read(buffer);
            while (length != -1) {
                output.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            output.flush();
        } catch (Exception e) {
            logger.error("输出文件流时抛异常，filePath={}", filePath, e);
        } finally {
            try {
                bis.close();
                fos.close();
                output.close();
            } catch (IOException e0) {
                logger.error("文件处理失败，filePath={}", filePath, e0);
            }
        }
    }

    /**
     * 写入文件
     * @param filePath
     * @param code
     * @return
     */
    public static boolean print(String filePath, String code) {
        try {
            File tofile = new File(filePath);
            FileWriter fw = new FileWriter(tofile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);


            Date date = new Date();//Java-取得服务器当前的各种具体时间
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int mil = calendar.get(Calendar.MILLISECOND);


            System.out.println(year);
            pw.append("版本号为：" + code);
            pw.println("");
            pw.append(year + "年" + month + "月" + day + "日 " + hour + "时" + minutes + "分" + mil + "毫秒");
            pw.println("");


            pw.close();
            bw.close();
            fw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 通过本地缓存文件查询瓦片数据
     *
     * @param file     文件
     * @return 瓦片数据
     * @throws IOException 文件读取异常
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        byte[] tiles;
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        //读取数据
        int len = bis.available();
        tiles = new byte[len];
        bis.read(tiles, 0, len);

        //关资源
        bis.close();
        fis.close();
        System.out.printf("使用缓存" + file.getPath()+ "\n");
        return tiles;
    }

    /**
     * 功能描述：从缓存中获取瓦片数据
     * @param filePath 瓦片数据路径
     * @param file 数据文件对象
     * @return
     * @throws IOException
     */
    public static byte[] getTilesFromCache (File file) throws IOException {
        byte[] tiles;
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        //读取数据
        int len = bis.available();
        tiles = new byte[len];
        bis.read(tiles, 0, len);

        //关资源
        bis.close();
        fis.close();
        System.out.printf("使用缓存" + file.getPath() + "\n");
        return tiles;
    }

}
