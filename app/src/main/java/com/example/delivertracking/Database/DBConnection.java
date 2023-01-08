package com.example.delivertracking.Database;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    Connection con = null;
    public Connection createConnection() {
        String ip, port, db, un, pass, url=null;
        ip = "220.247.207.187";
        port = "1433";
        db = "MobileApp";
        un = "sa";
        pass = "Sasa@22";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            url = "jdbc:jtds:sqlserver://"+ip+":"+port+";databaseName="+db+";user="+un+";password="+pass+";";
            con = DriverManager.getConnection(url);
        } catch (Exception e) {
            Log.e("Error: ",e.getMessage());
        }
        return con;
    }
}
