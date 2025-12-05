package kasir_kafe_shop.handler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.*;
import javax.swing.*;

import kasir_kafe_shop.frame.MainFrame;
import kasir_kafe_shop.frame.PelangganFrame;
import kasir_kafe_shop.frame.StockManagementFrame;

public class BuatPanelHandler implements MouseListener, ActionListener {

    private MainFrame frame;

    // Target komponen yang berbeda-beda
    private JLabel lblKategori;
    private String namaKategori;

    private JLabel lblUser;
    private JPopupMenu popupMenu;

    private JLabel lblPelangganInfo;

    private JPanel cardProduk;
    private int indexProduk;

    private JLabel lblJumlah;
    private int indexJumlah;

    // Konstruktor untuk kategori
    public BuatPanelHandler(MainFrame frame, JLabel lblKategori, 
                            String namaKategori) {
        this.frame = frame;
        this.lblKategori = lblKategori;
        this.namaKategori = namaKategori;
    }

    public BuatPanelHandler(MainFrame frame) {
        this.frame = frame;
    }

    // Konstruktor untuk label user (popup menu)
    public BuatPanelHandler(MainFrame frame, JLabel lblUser, JPopupMenu menu) {
        this.frame = frame;
        this.lblUser = lblUser;
        this.popupMenu = menu;
    }

    // Konstruktor untuk label pelanggan info
    public BuatPanelHandler(MainFrame frame, JLabel lblPelangganInfo) {
        this.frame = frame;
        this.lblPelangganInfo = lblPelangganInfo;
    }

    // Konstruktor untuk card produk
    public BuatPanelHandler(MainFrame frame, JPanel cardProduk, int indexProduk) {
        this.frame = frame;
        this.cardProduk = cardProduk;
        this.indexProduk = indexProduk;
    }

    // Konstruktor untuk label jumlah
    public BuatPanelHandler(MainFrame frame, JLabel lblJumlah, int indexJumlah) {
        this.frame = frame;
        this.lblJumlah = lblJumlah;
        this.indexJumlah = indexJumlah;
    }

    // MOUSE CLICK
    @Override
    public void mouseClicked(MouseEvent e) {

        // === Klik kategori ===
        if (lblKategori != null && namaKategori != null) {
            frame.gantiKategori(namaKategori);
            return;
        }

        // === Klik user (menu popup) ===
        if (lblUser != null && popupMenu != null) {
            popupMenu.show(lblUser, e.getX(), e.getY());
            return;
        }

        // === Klik pelanggan info ===
        if (lblPelangganInfo != null) {
            new PelangganFrame(frame);
            return;
        }

        // === Klik card produk ===
        if (cardProduk != null) {
            frame.tambahItemKePesanan(indexProduk);
            return;
        }

        // === Klik label jumlah ===
        if (lblJumlah != null) {
            frame.tambahJumlahProduk(indexJumlah);
        }
    }

    // MOUSE ENTERED
    @Override
    public void mouseEntered(MouseEvent e) {

        // Hover kategori
        if (lblKategori != null && namaKategori != null) {
            if (!namaKategori.equals(frame.getKategoriAktif())) {
                lblKategori.setBackground(frame.warnaAksen);
            }
            return;
        }

        // Hover user label
        if (lblUser != null) {
            lblUser.setForeground(frame.warnaAksen);
            lblUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return;
        }

        // Hover pelanggan info
        if (lblPelangganInfo != null) {
            lblPelangganInfo.setForeground(frame.warnaAksen);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

        // Hover keluar kategori
        if (lblKategori != null && namaKategori != null) {
            if (!namaKategori.equals(frame.getKategoriAktif())) {
                lblKategori.setBackground(frame.warnaPrimer);
            }
            return;
        }

        // Hover keluar user label
        if (lblUser != null) {
            lblUser.setForeground(Color.WHITE);
            return;
        }

        // Hover keluar pelanggan
        if (lblPelangganInfo != null) {
            lblPelangganInfo.setForeground(Color.WHITE);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object src = e.getSource();

        // Pastikan benar-benar JMenuItem
        if (src instanceof JMenuItem) {

            JMenuItem item = (JMenuItem) src;
            String txt = item.getText();

            if (txt.equals("Riwayat Transaksi")) {
                frame.bukaRiwayatFrame();
            } else if (txt.equals("Tambah Stock")) {
                new StockManagementFrame(frame);
            } else if (txt.equals("Log Out")) {
                JOptionPane.showMessageDialog(frame, "Terima kasih!");
                frame.dispose();
                new kasir_kafe_shop.frame.LoginFrame();
            } else if (txt.equals("Exit")) {
                System.exit(0);
            }
        }
    }
}
