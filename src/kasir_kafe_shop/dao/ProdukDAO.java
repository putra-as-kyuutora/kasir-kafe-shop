package kasir_kafe_shop.dao;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import kasir_kafe_shop.util.DBConnection;

public class ProdukDAO {
    
    public static class ProdukDB {

        public final int id;
        public final String nama;
        public final int harga;
        public final String kategori;
        public int stock;
        public final String imagePath; 

        public ProdukDB(int id, String nama, int harga, String kategori,
                int stock, String imagePath) {
            this.id = id;
            this.nama = nama;
            this.harga = harga;
            this.kategori = kategori;
            this.stock = stock;
            this.imagePath = (imagePath != null && !imagePath.isEmpty())
                    ? imagePath : "/img/default.jpg";
        }

        // Getter
        public String getImagePath() {
            return imagePath;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public void kurangiStock(int qty) {
            this.stock = Math.max(0, this.stock - qty);
        }

        public void tambahStock(int qty) {
            this.stock += qty;
        }
    }

    // AMBIL PRODUK BERDASARKAN KATEGORI
    public static ArrayList<ProdukDB> getByKategori(String kategori) {
        ArrayList<ProdukDB> list = new ArrayList<>();
        String sql = "SELECT id, nama, harga, kategori, stock, image_path FROM "
                + "produk WHERE kategori = ? ORDER BY nama";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kategori);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ProdukDB(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getInt("harga"),
                        rs.getString("kategori"),
                        rs.getInt("stock"),
                        rs.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal memuat produk: "
                    + e.getMessage(), "Error Database",
                    JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    // KURANGI STOCK (dipakai saat bayar)
    public static boolean kurangiStock(String nama, int qty) {
        String sql = "UPDATE produk SET stock = stock - ? WHERE nama = ? "
                + "AND stock >= ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, qty);
            ps.setString(2, nama);
            ps.setInt(3, qty);

            int affected = ps.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TAMBAH STOCK (dipakai di frame Tambah Stock)
    public static boolean tambahStock(String nama, int qty) {
        String sql = "UPDATE produk SET stock = stock + ? WHERE nama = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, qty);
            ps.setString(2, nama);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE IMAGE PATH 
    public static boolean updateImagePath(String nama, String newPath) {
        String sql = "UPDATE produk SET image_path = ? WHERE nama = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPath);
            ps.setString(2, nama);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
