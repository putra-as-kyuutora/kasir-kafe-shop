package kasir_kafe_shop.frame;

import kasir_kafe_shop.dao.TransaksiDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import kasir_kafe_shop.util.DBConnection;

public class RiwayatFrame extends JFrame implements ActionListener {

    private JTable table;
    private DefaultTableModel model;
    
    private JButton btnDetail, btnCetak, btnHapus;

    public RiwayatFrame() {
        setTitle("Riwayat Transaksi - Kafe Shop");
        setSize(1100, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // JUDUL
        JLabel title = new JLabel("RIWAYAT TRANSAKSI", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(31, 40, 128));
        add(title, BorderLayout.NORTH);

        // TABEL
        String[] cols = {"No", "Tanggal", "Pelanggan", "Total", "Tunai", "Kembalian", "Kasir"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setBackground(new Color(31, 40, 128));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(100, 130, 255));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // TOMBOL DI BAWAH
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelTombol.setBackground(new Color(245, 248, 255));

        btnDetail = new JButton("Lihat Detail Transaksi");
        btnDetail.setBackground(new Color(31, 40, 128));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDetail.setPreferredSize(new Dimension(220, 45));
        btnDetail.addActionListener(this);

        btnCetak = new JButton("Cetak Struk");
        btnCetak.setBackground(new Color(40, 167, 69));
        btnCetak.setForeground(Color.WHITE);
        btnCetak.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCetak.setPreferredSize(new Dimension(180, 45));
        btnCetak.addActionListener(this);

        btnHapus = new JButton("Hapus Transaksi");
        btnHapus.setBackground(Color.RED);
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHapus.setPreferredSize(new Dimension(180, 45));
        btnHapus.addActionListener(this);

        panelTombol.add(btnDetail);
        panelTombol.add(btnCetak);
        panelTombol.add(btnHapus);

        add(panelTombol, BorderLayout.SOUTH);

        loadData();
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT id, tanggal, pelanggan, total, tunai, kembalian, kasir FROM transaksi ORDER BY id DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), 
                    rs.getTimestamp("tanggal").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                    rs.getString("pelanggan"),
                    "Rp " + formatRupiah(rs.getInt("total")),
                    "Rp " + formatRupiah(rs.getInt("tunai")),
                    "Rp " + formatRupiah(rs.getInt("kembalian")),
                    rs.getString("kasir")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// DI RiwayatFrame.java
    private void cetakStruk() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) table.getValueAt(row, 0);
        TransaksiDAO.cetakStrukTransaksi(id);
        JOptionPane.showMessageDialog(this, "Struk berhasil disimpan di folder riwayat_transaksi!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    private void lihatDetail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) table.getValueAt(row, 0);
        TransaksiDAO.lihatDetailTransaksi(id, this);
    }

    private void hapusTransaksi() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "<html><h3>Yakin menghapus transaksi?</h3>"
                + "ID: <b>" + id + "</b><br>"
                + "Pelanggan: <b>" + table.getValueAt(row, 2) + "</b><br><br>"
                + "Data akan hilang <u>PERMANEN</u>!</html>",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (TransaksiDAO.hapusTransaksi(id)) {
                loadData(); // RELOAD TABEL
                JOptionPane.showMessageDialog(this,
                        "Transaksi ID " + id + " berhasil dihapus permanen!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus transaksi!\nCoba restart aplikasi.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String formatRupiah(int angka) {
        return String.format("%,d", angka).replace(',', '.');
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(btnDetail == e.getSource()) {
            lihatDetail();
        } else if(btnCetak == e.getSource()) {
            cetakStruk();
        } else if(btnHapus == e.getSource()) {
            hapusTransaksi();
        }
        
    }
    
}
