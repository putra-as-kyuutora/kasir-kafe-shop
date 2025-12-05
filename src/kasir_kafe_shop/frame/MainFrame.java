package kasir_kafe_shop.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import kasir_kafe_shop.panel.BuatPanel;
import kasir_kafe_shop.util.ModelTableTidakBisaDiubah;
import kasir_kafe_shop.model.Pelanggan;
import kasir_kafe_shop.dao.ProdukDAO;
import kasir_kafe_shop.dao.TransaksiDAO;
import kasir_kafe_shop.handler.BuatPanelHandler;

public class MainFrame extends JFrame implements ActionListener {

    public JPanel panelUtama, panelKiri, panelKanan, panelKategori;
    public JPanel panelMenuMakanan, panelMenuMinuman, panelMenuCemilan;
    public JScrollPane scrollMakanan, scrollMinuman, scrollCemilan;
    public JTable table;
    public ModelTableTidakBisaDiubah modelTabel;
    public JLabel labelTotal, labelKembalian, lblPelangganInfo;
    public JTextField txfTunai;
    public JButton btnHitung, btnReset, btnHapus, btnBayar;

    public JLabel lblMakanan, lblMinuman, lblCemilan;
    public String kategoriAktif = "Makanan";

    public ArrayList<ProdukDAO.ProdukDB> daftarMakanan;
    public ArrayList<ProdukDAO.ProdukDB> daftarMinuman;
    public ArrayList<ProdukDAO.ProdukDB> daftarCemilan;
    public ArrayList<ProdukDAO.ProdukDB> daftarSemuaProduk;

    protected ArrayList<Integer> jumlahBeli;
    public ArrayList<JLabel> labelJumlah, labelStock;
    

    public String usernameLogin;
    protected boolean hasTransaksi = false;

    public final Color warnaPrimer = new Color(31, 40, 128);
    protected final Color warnaSekunder = new Color(45, 55, 160);
    public final Color warnaAksen = new Color(100, 130, 255);
    public final Color warnaLatar = new Color(245, 248, 255);
    public final Color warnaCard = Color.WHITE;
    public final Color warnaAktif = new Color(80, 100, 200);
    protected final Color warnaKembalian = new Color(40, 167, 69);

    protected BuatPanel buatPanel;
    protected BuatPanelHandler buatPanelHandler;

    public MainFrame(String username) {
        this.usernameLogin = username;
        daftarMakanan = ProdukDAO.getByKategori("Makanan");
        daftarMinuman = ProdukDAO.getByKategori("Minuman");
        daftarCemilan = ProdukDAO.getByKategori("Cemilan");

        daftarSemuaProduk = new ArrayList<>();
        daftarSemuaProduk.addAll(daftarMakanan);
        daftarSemuaProduk.addAll(daftarMinuman);
        daftarSemuaProduk.addAll(daftarCemilan);

        int totalProduk = daftarSemuaProduk.size();
        jumlahBeli = new ArrayList<>(Collections.nCopies(totalProduk, 0));
        labelJumlah = new ArrayList<>(Collections.nCopies(totalProduk, null));
        labelStock = new ArrayList<>(Collections.nCopies(totalProduk, null));

        buatPanel = new BuatPanel(this);
        buatPanelHandler = new BuatPanelHandler(this, lblPelangganInfo);

        setTitle("Sistem Kasir Kafe GeZigma");
        setSize(1240, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buatPanel.buatKategori();
        buatPanel.buatPanelUtama();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void gantiKategori(String kategori) {
        kategoriAktif = kategori;

        if ("Makanan".equals(kategori)) {
            lblMakanan.setBackground(warnaAktif);
        } else {
            lblMakanan.setBackground(warnaPrimer);
        }

        if ("Minuman".equals(kategori)) {
            lblMinuman.setBackground(warnaAktif);
        } else {
            lblMinuman.setBackground(warnaPrimer);
        }

        if ("Cemilan".equals(kategori)) {
            lblCemilan.setBackground(warnaAktif);
        } else {
            lblCemilan.setBackground(warnaPrimer);
        }

        // Ganti panel produk yang ditampilkan
        panelKiri.removeAll();

        if ("Makanan".equals(kategori)) {
            panelKiri.add(scrollMakanan);
        } else if ("Minuman".equals(kategori)) {
            panelKiri.add(scrollMinuman);
        } else if ("Cemilan".equals(kategori)) {
            panelKiri.add(scrollCemilan);
        }

        panelKiri.revalidate();
        panelKiri.repaint();
    }

    public void tambahPesanan(int index) {
        ProdukDAO.ProdukDB p = daftarSemuaProduk.get(index);
        if (p.stock <= 0) {
            JOptionPane.showMessageDialog(this, "Stock " + p.nama + " habis!", 
                                          "Stock Habis", JOptionPane.WARNING_MESSAGE);
            return;
        }
        jumlahBeli.set(index, jumlahBeli.get(index) + 1);
        p.stock--;
        labelJumlah.get(index).setText("Jumlah: " + jumlahBeli.get(index));
        labelStock.get(index).setText("Stock: " + p.stock);
        updateTabel(index);
    }

    public void ubahJumlah(int index) {
        String input = JOptionPane.showInputDialog(this, "Masukkan jumlah baru:", 
                                                   jumlahBeli.get(index));
        if (input != null && !input.isEmpty()) {
            try {
                int baru = Integer.parseInt(input);
                if (baru < 0) {
                    return;
                }

                int selisih = baru - jumlahBeli.get(index);
                ProdukDAO.ProdukDB p = daftarSemuaProduk.get(index);

                if (p.stock - selisih < 0) {
                    JOptionPane.showMessageDialog(this, "Stock tidak cukup!", 
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                p.stock -= selisih;
                jumlahBeli.set(index, baru);
                labelJumlah.get(index).setText("Jumlah: " + baru);
                labelStock.get(index).setText("Stock: " + p.stock);
                updateTabel(index);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private void updateTabel(int index) {
        ProdukDAO.ProdukDB p = daftarSemuaProduk.get(index);
        int qty = jumlahBeli.get(index);
        int subtotal = p.harga * qty;

        boolean found = false;
        for (int i = 0; i < modelTabel.getRowCount(); i++) {
            if (modelTabel.getValueAt(i, 0).equals(p.nama)) {
                if (qty == 0) {
                    modelTabel.removeRow(i);
                } else {
                    modelTabel.setValueAt(qty, i, 1);
                    modelTabel.setValueAt("Rp " + formatRupiah(subtotal), i, 3);
                }
                found = true;
                break;
            }
        }
        if (!found && qty > 0) {
            modelTabel.addRow(new Object[]{p.nama, qty, "Rp " + formatRupiah(p.harga), 
                              "Rp " + formatRupiah(subtotal)});
        }
        hitungTotal();
    }

    private void hitungTotal() {
        int total = 0;
        for (int i = 0; i < modelTabel.getRowCount(); i++) {
            String s = (String) modelTabel.getValueAt(i, 3);
            total += Integer.parseInt(s.replaceAll("[^0-9]", ""));
        }
        labelTotal.setText("Rp " + formatRupiah(total));
    }

    protected void hitungKembalian() {
        try {
            int total = Integer.parseInt(labelTotal.getText().replaceAll("[^0-9]", ""));
            String input = txfTunai.getText().trim().replaceAll("[^0-9]", "");
            if (input.isEmpty()) {
                labelKembalian.setText("Masukkan tunai!");
                labelKembalian.setForeground(Color.RED);
                return;
            }
            int tunai = Integer.parseInt(input);
            int kembalian = tunai - total;
            if (kembalian < 0) {
                labelKembalian.setForeground(Color.RED);
                labelKembalian.setText("Uang anda kurang Rp " + formatRupiah(Math.abs(kembalian)));
            } else {
                labelKembalian.setForeground(warnaKembalian);
                labelKembalian.setText("Rp " + formatRupiah(kembalian));
            }
        } catch (Exception e) {
            labelKembalian.setText("Input tidak valid");
            labelKembalian.setForeground(Color.RED);
        }
    }

    protected void resetSemua() {
        for (int i = 0; i < jumlahBeli.size(); i++) {
            jumlahBeli.set(i, 0);
            labelJumlah.get(i).setText("Jumlah: 0");
        }
        modelTabel.setRowCount(0);
        labelTotal.setText("Rp 0");
        labelKembalian.setText("Rp 0");
        labelKembalian.setForeground(warnaKembalian);
        txfTunai.setText("");
        lblPelangganInfo.setText("Pelanggan: (isi data pelanggan)");
        Pelanggan.resetCurrentPelanggan();
    }

    protected void hapusItem() {
        int baris = table.getSelectedRow();
        if (baris < 0) {
            return;
        }

        String nama = (String) modelTabel.getValueAt(baris, 0);
        for (int i = 0; i < daftarSemuaProduk.size(); i++) {
            if (daftarSemuaProduk.get(i).nama.equals(nama)) {
                int qty = jumlahBeli.get(i);
                daftarSemuaProduk.get(i).stock += qty;
                jumlahBeli.set(i, 0);
                labelJumlah.get(i).setText("Jumlah: 0");
                labelStock.get(i).setText("Stock: " + daftarSemuaProduk.get(i).stock);
                break;
            }
        }
        modelTabel.removeRow(baris);
        hitungTotal();
    }

    private void prosesBayar() {
        if (Pelanggan.getCurrentPelanggan() == null) {
            JOptionPane.showMessageDialog(this, "Harap masukkan data pelanggan "
                    + "                   terlebih dahulu!", "Peringatan", 
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }

        int total = Integer.parseInt(labelTotal.getText().replaceAll("[^0-9]", ""));
        if (total == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada pesanan!", "Info", 
                                          JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String inputTunai = txfTunai.getText().trim();
        if (inputTunai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah uang tunai!", 
                                          "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int tunai;
        try {
            tunai = Integer.parseInt(inputTunai.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input tunai tidak valid!\nContoh: "
                                          + "500000 atau 500.000", "Error", 
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tunai < total) {
            JOptionPane.showMessageDialog(this, "Uang anda kurang Rp " 
                                          + formatRupiah(total - tunai) + "!", 
                                          "Uang Tidak Cukup", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int kembalian = tunai - total;

        if (JOptionPane.showConfirmDialog(this,
                "<html><b>Konfirmasi Pembayaran</b><br><br>"
                + "Total Belanja : Rp " + formatRupiah(total) + "<br>"
                + "Uang Tunai    : Rp " + formatRupiah(tunai) + "<br>"
                + "Kembalian     : <font color='green'><b>Rp " + formatRupiah(kembalian) 
                                   + "</b></font><br><br>"
                + "Lanjutkan?</html>",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Pelanggan c = Pelanggan.getCurrentPelanggan();
            String pelangganInfo = c.getNama() + " (Meja " + c.getNomorMeja() + ")";

            // AMBIL CATATAN
            String catatan = "";
            if (c.getCatatan() != null && !c.getCatatan().trim().isEmpty()) {
                catatan = c.getCatatan().trim();
            }

            // KURANGI STOCK DARI DATABASE
            for (int i = 0; i < modelTabel.getRowCount(); i++) {
                String nama = (String) modelTabel.getValueAt(i, 0);
                int qty = (Integer) modelTabel.getValueAt(i, 1);
                ProdukDAO.kurangiStock(nama, qty);
            }

            // SIMPAN TRANSAKSI + CATATAN
            TransaksiDAO.simpanTransaksi(pelangganInfo, total, tunai, kembalian,
                                         usernameLogin, modelTabel, catatan);

            JOptionPane.showMessageDialog(this,
                    "<html><h2>Pembayaran Berhasil!</h2><br>"
                    + "Terima kasih, " + c.getNama() + "!<br>"
                    + "Kembalian: <b>Rp " + formatRupiah(kembalian) + "</b><br><br>"
                    + "<i>Struk dapat dicetak dari menu <b>Riwayat Transaksi</b></i></html>",
                    "Transaksi Selesai", JOptionPane.INFORMATION_MESSAGE);

            hasTransaksi = true;
            resetSemua();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi!\n" 
                                          + ex.getMessage(), "Error", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updatePelangganInfo(String nama, String meja) {
        lblPelangganInfo.setText("Pelanggan: " + nama + " (Meja " + meja + ")");
    }

    public void bukaRiwayatFrame() {
        new RiwayatFrame();
    }

    public void tambahItemKePesanan(int index) {
        tambahPesanan(index);
    }

    public void tambahJumlahProduk(int index) {
        ubahJumlah(index);
    }

    public String getKategoriAktif() {
        return kategoriAktif;
    }

    public String formatRupiah(int angka) {
        return String.format("%,d", angka).replace(',', '.');
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHitung) {
            hitungKembalian();
        } else if (e.getSource() == btnReset) {
            resetSemua();
        } else if (e.getSource() == btnHapus) {
            hapusItem();
        } else if (e.getSource() == btnBayar) {
            prosesBayar();
        }
    }
    
    public static void main(String[] args) {
        new MainFrame("Test");
    }
}
