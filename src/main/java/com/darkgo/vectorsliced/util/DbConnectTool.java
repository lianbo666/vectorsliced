package com.darkgo.vectorsliced.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author 陈永多
 * @ClassName DbConnectTool
 * @description TODO
 * @Date 2019/9/2 15:21
 * @Version 1.0
 **/
public class DbConnectTool {
    private final static String driver = "org.postgresql.Driver";
    private final static String url = "jdbc:postgresql://172.17.30.208:5432/mapdb?characterEncoding=utf-8";
    private final static String username = "mapowner";
    private final static String password = "123456";
    private static Connection conn=null;
    private static DbConnectTool instance = null;

    static {
        try {
            Class.forName(driver);
            conn= DriverManager.getConnection(url, username ,password);
            conn.setAutoCommit(false); // 取消自动提交
            System.out.println("Opened postgresql database successfully");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DbConnectTool(){}

    private static DbConnectTool getInstance(){
        if(instance==null){
            synchronized (DbConnectTool.class) {
                if(instance==null){
                    return new DbConnectTool();
                }
            }
        }
        return instance;
    }

    public static Connection getConnection() throws Exception {
        return conn;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getConnection().getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
