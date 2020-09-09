package com.demo;

import android.util.Log;

import java.util.Random;

/**
 */
public enum DataSourceEnum {
    DATASOURCE;
    
    private DBConnection connection = null;
    
    private DataSourceEnum() {
        connection = new DBConnection();
    }
    
    public DBConnection getConnection() {
        
        Log.i("TAG", "getConnection: " + connection.toString());
        return connection;
    }
    
    class DBConnection {
        private String a;
        private String b;
        
        public DBConnection() {
            Log.i("TAG", "开始" + new Random().nextInt() * 1000);
            this.a = new Random().nextInt() * 1000 + "";
            this.b = new Random().nextInt() * 1000 + "";
        }
        
        public String getA() {
            return a;
        }
        
        public void setA(String a) {
            this.a = a;
        }
        
        public String getB() {
            return b;
        }
        
        public void setB(String b) {
            this.b = b;
        }
        
        @Override
        public String toString() {
            return "DBConnection{" + "a='" + a + '\'' + ", b='" + b + '\'' + '}';
        }
    }
}
