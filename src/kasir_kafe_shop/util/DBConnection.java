package kasir_kafe_shop.util;

import java.sql.*;

public class DBConnection {

    private static Connection conn;

    public static Connection getConnection() {
        String host = "localhost";
        String port = "3306";
        String usr = "root";
        String pwd = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/kasir_kafe_db";
            conn = DriverManager.getConnection(url, usr, pwd);
            System.out.println("Koneksi ke database berhasil terbentuk");
        } catch (Exception ex) {
            System.out.println("Koneksi ke database gagal...");
            System.out.println(ex.getMessage());
        }

        return conn;
    }

    public static void main(String[] args) {
        getConnection();
    }
}
