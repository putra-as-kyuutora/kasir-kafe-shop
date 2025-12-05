package kasir_kafe_shop.dao;

import kasir_kafe_shop.util.DBConnection;
import java.sql.*;

public class KaryawanDAO {

    public static String login(String username, String password) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                // DATABASE MATI TETAP TAMPILKAN PESAN SALAH LOGIN
                return null;
            }

            String sql = "SELECT username FROM karyawan WHERE username = ? "
                          + "AND password = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            // DATABASE ERROR TETAP KEMBALIKAN NULL BIAR TAMPIL PESAN SALAH LOGIN
            System.err.println("Database error saat login: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // TUTUP KONEKSI KALAU ADA
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return null;
    }

    public static boolean tambahKaryawan(String username, String password) {
        String sql = "INSERT INTO karyawan(username, password) VALUES(?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
