package kasir_kafe_shop.dao;

import java.io.File;
import java.io.FileWriter;
import kasir_kafe_shop.util.DBConnection;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TransaksiDAO {

    public static void simpanTransaksi(String pelanggan, int total, int tunai, 
                                       int kembalian, String kasir, 
                                       DefaultTableModel modelTabel, String catatan) {
        
        String sqlHeader = "INSERT INTO transaksi(pelanggan, catatan, total, "
                            + "tunai, kembalian, kasir) VALUES(?, ?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO transaksi_detail(transaksi_id, produk_nama, "
                            + "qty, harga_satuan, subtotal) VALUES(?, ?, ?, ?, ?)";

        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            long transaksiId = 0;

            try (PreparedStatement ps = c.prepareStatement(sqlHeader, 
                 Statement.RETURN_GENERATED_KEYS)) {
                
                ps.setString(1, pelanggan);
                ps.setString(2, catatan);
                ps.setInt(3, total);
                ps.setInt(4, tunai);
                ps.setInt(5, kembalian);
                ps.setString(6, kasir);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        transaksiId = keys.getLong(1);
                    }
                }
            }

            try (PreparedStatement psDetail = c.prepareStatement(sqlDetail)) {
                for (int i = 0; i < modelTabel.getRowCount(); i++) {
                    String nama = (String) modelTabel.getValueAt(i, 0);
                    int qty = (Integer) modelTabel.getValueAt(i, 1);
                    int harga = Integer.parseInt(((String) modelTabel.getValueAt
                                (i, 2)).replaceAll("[^0-9]", ""));
                    int subtotal = harga * qty;

                    psDetail.setLong(1, transaksiId);
                    psDetail.setString(2, nama);
                    psDetail.setInt(3, qty);
                    psDetail.setInt(4, harga);
                    psDetail.setInt(5, subtotal);
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            c.commit();
            System.out.println("Transaksi berhasil disimpan! ID: " + transaksiId);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal simpan transaksi: " + e.getMessage());
        }
    }

    public static void lihatDetailTransaksi(int id, JFrame parent) {
        String sqlHeader = "SELECT * FROM transaksi WHERE id = ?";
        String sqlDetail = "SELECT * FROM transaksi_detail WHERE transaksi_id = ? ORDER BY id";

        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement psH = c.prepareStatement(sqlHeader); 
             PreparedStatement psD = c.prepareStatement(sqlDetail)) {

            psH.setInt(1, id);
            ResultSet rsH = psH.executeQuery();
            if (!rsH.next()) {
                JOptionPane.showMessageDialog(parent, "Transaksi tidak ditemukan!", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pelanggan = rsH.getString("pelanggan");
            String kasir = rsH.getString("kasir");
            int total = rsH.getInt("total");
            int tunai = rsH.getInt("tunai");
            int kembalian = rsH.getInt("kembalian");
            String tanggal = rsH.getTimestamp("tanggal")
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            // CATATAN DARI DATABASE — PASTI ADA!
            String catatan = rsH.getString("catatan");
            if (catatan == null || catatan.trim().isEmpty()) {
                catatan = "Tidak ada catatan";
            }

            StringBuilder nota = new StringBuilder();
            nota.append("<html><pre style='font-family: Consolas; font-size: 13px;'>\n");
            nota.append("════════════════════════════════════\n");
            nota.append("       KAFE GENZIGMA      \n");
            nota.append("════════════════════════════════════\n\n");
            nota.append(String.format("%-15s: %s%n", "ID Transaksi", id));
            nota.append(String.format("%-15s: %s%n", "Tanggal", tanggal));
            nota.append(String.format("%-15s: %s%n", "Kasir", kasir));
            nota.append(String.format("%-15s: %s%n", "Pelanggan", pelanggan));
            nota.append(String.format("%-15s: %s%n", "Catatan", catatan));
            nota.append("\n");
            nota.append("------------------------------------\n");
            nota.append("          DAFTAR PESANAN            \n");
            nota.append("------------------------------------\n");

            psD.setInt(1, id);
            ResultSet rsD = psD.executeQuery();
            boolean adaDetail = false;
            while (rsD.next()) {
                adaDetail = true;
                String nama = rsD.getString("produk_nama");
                int qty = rsD.getInt("qty");
                int harga = rsD.getInt("harga_satuan");
                int subtotal = rsD.getInt("subtotal");

                nota.append(String.format("  %-18s %2d x Rp %8s = Rp %10s%n",
                        nama.length() > 18 ? nama.substring(0, 15) + "..." : nama,
                        qty,
                        formatRupiah(harga),
                        formatRupiah(subtotal)
                ));
            }

            if (!adaDetail) {
                nota.append("\t\tTidak ada pesanan)\n");
            }

            nota.append("------------------------------------\n");
            nota.append(String.format("%-15s: Rp %12s%n", "TOTAL", formatRupiah(total)));
            nota.append(String.format("%-15s: Rp %12s%n", "TUNAI", formatRupiah(tunai)));
            nota.append(String.format("%-15s: Rp %12s%n", "KEMBALIAN", formatRupiah(kembalian)));
            nota.append("\n");
            nota.append("     Terima kasih atas kunjungannya!\n");
            nota.append("        Selamat datang kembali!        \n");
            nota.append("════════════════════════════════════\n");
            nota.append("</pre></html>");

            JOptionPane.showMessageDialog(parent, nota.toString(),
                    "Nota Transaksi #" + id, JOptionPane.PLAIN_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Gagal memuat nota!", "Error", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void cetakStrukTransaksi(int id) {

        File folder = new File("riwayat_transaksi");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String sqlHeader = "SELECT * FROM transaksi WHERE id = ?";
        String sqlDetail = "SELECT * FROM transaksi_detail WHERE transaksi_id = ?";

        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement psH = c.prepareStatement(sqlHeader); 
             PreparedStatement psD = c.prepareStatement(sqlDetail)) {

            psH.setInt(1, id);
            ResultSet rsH = psH.executeQuery();
            if (!rsH.next()) {
                JOptionPane.showMessageDialog(null, "Transaksi tidak ditemukan!", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pelanggan = rsH.getString("pelanggan");
            String kasir = rsH.getString("kasir");
            int total = rsH.getInt("total");
            int tunai = rsH.getInt("tunai");
            int kembalian = rsH.getInt("kembalian");
            String tanggal = rsH.getTimestamp("tanggal").toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            String catatan = rsH.getString("catatan");
            if (catatan == null || catatan.trim().isEmpty()) {
                catatan = "Tidak ada catatan";
            }

            // NAMA FILE CANTIK & AMAN
            String safePelanggan = pelanggan.replaceAll("[^a-zA-Z0-9_-]", "_");
            String fileName = String.format("riwayat_transaksi/struk_%03d_%s_%s.txt",
                    id,
                    rsH.getTimestamp("tanggal").toLocalDateTime().
                    format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")),
                    safePelanggan.length() > 15 ? safePelanggan.substring(0, 15) : safePelanggan
            );

            try (FileWriter fw = new FileWriter(fileName)) {
                fw.write("════════════════════════════════════\n");
                fw.write("       KAFE GENZIGMA      \n");
                fw.write("════════════════════════════════════\n\n");
                fw.write(String.format("%-15s: %s%n", "ID Transaksi", id));
                fw.write(String.format("%-15s: %s%n", "Tanggal", tanggal));
                fw.write(String.format("%-15s: %s%n", "Kasir", kasir));
                fw.write(String.format("%-15s: %s%n", "Pelanggan", pelanggan));
                fw.write(String.format("%-15s: %s%n", "Catatan", catatan));
                fw.write("\n");
                fw.write("------------------------------------\n");
                fw.write("          DAFTAR PESANAN            \n");
                fw.write("------------------------------------\n");

                psD.setInt(1, id);
                ResultSet rsD = psD.executeQuery();
                while (rsD.next()) {
                    fw.write(String.format("  %-18s %2d x Rp %8s = Rp %10s%n",
                            rsD.getString("produk_nama"),
                            rsD.getInt("qty"),
                            formatRupiah(rsD.getInt("harga_satuan")),
                            formatRupiah(rsD.getInt("subtotal"))
                    ));
                }

                fw.write("------------------------------------\n");
                fw.write(String.format("%-15s: Rp %12s%n", "TOTAL", formatRupiah(total)));
                fw.write(String.format("%-15s: Rp %12s%n", "TUNAI", formatRupiah(tunai)));
                fw.write(String.format("%-15s: Rp %12s%n", "KEMBALIAN", formatRupiah(kembalian)));
                fw.write("\n");
                fw.write("     Terima kasih atas kunjungannya!\n");
                fw.write("        Selamat datang kembali!        \n");
                fw.write("════════════════════════════════════\n");
            }

            JOptionPane.showMessageDialog(null,
                    "<html><h3>Struk berhasil disimpan!</h3>"
                    + "Folder: <b>riwayat_transaksi/</b><br>"
                    + "File: <b>" + new File(fileName).getName() + "</b></html>",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mencetak struk!", "Error", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean hapusTransaksi(int id) {
        String sql = "DELETE FROM transaksi WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String formatRupiah(int angka) {
        return String.format("%,d", angka).replace(',', '.');
    }
}
