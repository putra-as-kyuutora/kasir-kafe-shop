package kasir_kafe_shop.handler;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import kasir_kafe_shop.dao.ProdukDAO;
import kasir_kafe_shop.frame.StockManagementFrame;

public class StockManagementHandler implements MouseListener, ActionListener {

    private final StockManagementFrame stockFrame;

    // === Untuk kategori ===
    private JLabel lblKategori;
    private String kategori;

    // === Untuk tombol tambah stock ===
    private ProdukDAO.ProdukDB produk;
    private JTextField txtTambah;
    private JLabel lblStock;

    //  CONSTRUCTOR UNTUK KATEGORI
    public StockManagementHandler(StockManagementFrame stockFrame, JLabel lblKategori, String kategori) {
        this.stockFrame = stockFrame;
        this.lblKategori = lblKategori;
        this.kategori = kategori;
    }

    //  CONSTRUCTOR UNTUK TAMBAH STOCK
    public StockManagementHandler(StockManagementFrame stockFrame, ProdukDAO.ProdukDB produk, JTextField txtTambah, JLabel lblStock) {
        this.stockFrame = stockFrame;
        this.produk = produk;
        this.txtTambah = txtTambah;
        this.lblStock = lblStock;
    }

    //  MOUSE EVENTS (Kategori)
    @Override
    public void mouseClicked(MouseEvent e) {
        if (kategori != null) {
            stockFrame.gantiKategori(kategori);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (kategori != null && !kategori.equals(stockFrame.getKategoriAktif())) {
            lblKategori.setBackground(new Color(100, 130, 255));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (kategori != null && !kategori.equals(stockFrame.getKategoriAktif())) {
            lblKategori.setBackground(new Color(31, 40, 128));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    //  ACTION LISTENER (Tambah Stock)
    @Override
    public void actionPerformed(ActionEvent e) {

        if (produk == null) {
            return;
        }

        try {
            int jumlah = Integer.parseInt(txtTambah.getText().trim());
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(stockFrame,
                        "Masukkan jumlah lebih dari 0!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (ProdukDAO.tambahStock(produk.nama, jumlah)) {

                produk.setStock(produk.stock + jumlah);

                lblStock.setText("Stock: " + produk.stock);
                lblStock.setForeground(
                        produk.stock > 10 ? new Color(40, 167, 69)
                                : produk.stock > 0 ? Color.ORANGE
                                        : Color.RED
                );

                // Update di MainFrame
                int index = stockFrame.getMainFrame().daftarSemuaProduk.indexOf(produk);
                if (index != -1 && stockFrame.getMainFrame().labelStock.get(index) != null) {
                    stockFrame.getMainFrame().labelStock.get(index).setText("Stock: " + produk.stock);
                }

                JOptionPane.showMessageDialog(stockFrame,"Stock " + produk.nama + " berhasil ditambah " + jumlah
                                              + "!\nTotal stock: " + produk.stock, "Sukses", JOptionPane.INFORMATION_MESSAGE);

                txtTambah.setText("0");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(stockFrame,
                    "Masukkan angka yang valid!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getKategori() {
        return kategori;
    }

    public JLabel getLabelKategori() {
        return lblKategori;
    }
}
