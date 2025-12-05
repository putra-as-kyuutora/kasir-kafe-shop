package kasir_kafe_shop.panel;

import kasir_kafe_shop.util.ModelTableTidakBisaDiubah;
import kasir_kafe_shop.frame.MainFrame;
import kasir_kafe_shop.frame.LoginFrame;
import kasir_kafe_shop.frame.StockManagementFrame;
import kasir_kafe_shop.handler.BuatPanelHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import kasir_kafe_shop.dao.ProdukDAO;
import kasir_kafe_shop.handler.HoverHandler;

public class BuatPanel {

    private MainFrame frame;

    public BuatPanel(MainFrame frame) {
        this.frame = frame;
    }

    // Header kategori (navigasi) + user manu 
    public void buatKategori() {
        frame.panelKategori = new JPanel(new BorderLayout());
        frame.panelKategori.setBackground(frame.warnaPrimer);
        frame.panelKategori.setPreferredSize(new Dimension(1240, 50));

        // Kiri Tombol kategori
        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        kiri.setBackground(frame.warnaPrimer);

        frame.lblMakanan = buatLabelKategori("Makanan", true);
        frame.lblMinuman = buatLabelKategori("Minuman", false);
        frame.lblCemilan = buatLabelKategori("Cemilan", false);

        kiri.add(frame.lblMakanan);
        kiri.add(frame.lblMinuman);
        kiri.add(frame.lblCemilan);

        // Kanan: Username + Popup Menu
        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        kanan.setBackground(frame.warnaPrimer);

        JLabel lblUser = new JLabel("Kasir: " + frame.usernameLogin);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(Color.WHITE);
        lblUser.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        lblUser.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // BUAT POPUP MENU
        JPopupMenu popup = new JPopupMenu();
        JMenuItem itemRiwayat = new JMenuItem("Riwayat Transaksi");
        JMenuItem itemTambahStock = new JMenuItem("Tambah Stock");
        JMenuItem itemLogout = new JMenuItem("Log Out");
        JMenuItem itemExit = new JMenuItem("Exit");

        popup.add(itemRiwayat);
        popup.add(itemTambahStock);
        popup.addSeparator();
        popup.add(itemLogout);
        popup.add(itemExit);

        BuatPanelHandler handlerUserMenu = new BuatPanelHandler(frame);

        itemRiwayat.addActionListener(handlerUserMenu);
        itemTambahStock.addActionListener(handlerUserMenu);
        itemLogout.addActionListener(handlerUserMenu);
        itemExit.addActionListener(handlerUserMenu);

        // PASANG HANDLER KE lblUser
        BuatPanelHandler userHandler = new BuatPanelHandler(frame, lblUser, popup);
        lblUser.addMouseListener(userHandler);

        kanan.add(lblUser);
        frame.panelKategori.add(kiri, BorderLayout.WEST);
        frame.panelKategori.add(kanan, BorderLayout.EAST);
        frame.add(frame.panelKategori, BorderLayout.NORTH);
    }

    private JLabel buatLabelKategori(String nama, boolean aktif) {
        JLabel lbl = new JLabel(nama, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setPreferredSize(new Dimension(150, 50));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(aktif ? frame.warnaAktif : frame.warnaPrimer);

        // PASANG HANDLER KATEGORI
        BuatPanelHandler handler = new BuatPanelHandler(frame, lbl, nama);
        lbl.addMouseListener(handler);

        return lbl;
    }

    // ================== PANEL UTAMA ==================
    public void buatPanelUtama() {
        frame.panelUtama = new JPanel(new GridLayout(1, 2, 10, 10));
        frame.panelUtama.setBackground(frame.warnaLatar);
        frame.panelUtama.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buatPanelMenu();
        buatPanelPesanan();

        frame.panelUtama.add(frame.panelKiri);
        frame.panelUtama.add(frame.panelKanan);
        frame.add(frame.panelUtama, BorderLayout.CENTER);
    }

    //  MENU PRODUK 3 KATEGORI
    private void buatPanelMenu() {
        frame.panelKiri = new JPanel(new BorderLayout());
        frame.panelKiri.setBackground(frame.warnaLatar);

        // MAKANAN
        frame.panelMenuMakanan = new JPanel(new GridLayout(0, 3, 10, 10));
        frame.panelMenuMakanan.setBackground(frame.warnaLatar);
        frame.panelMenuMakanan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < frame.daftarMakanan.size(); i++) {
            frame.panelMenuMakanan.add(buatCardProduk(i, "Makanan"));
        }
        frame.scrollMakanan = new JScrollPane(frame.panelMenuMakanan);
        frame.scrollMakanan.setBorder(null);

        // MINUMAN
        frame.panelMenuMinuman = new JPanel(new GridLayout(0, 3, 10, 10));
        frame.panelMenuMinuman.setBackground(frame.warnaLatar);
        frame.panelMenuMinuman.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        int off1 = frame.daftarMakanan.size();
        for (int i = 0; i < frame.daftarMinuman.size(); i++) {
            frame.panelMenuMinuman.add(buatCardProduk(off1 + i, "Minuman"));
        }
        frame.scrollMinuman = new JScrollPane(frame.panelMenuMinuman);
        frame.scrollMinuman.setBorder(null);

        // CEMILAN
        frame.panelMenuCemilan = new JPanel(new GridLayout(0, 3, 10, 10));
        frame.panelMenuCemilan.setBackground(frame.warnaLatar);
        frame.panelMenuCemilan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        int off2 = off1 + frame.daftarMinuman.size();
        for (int i = 0; i < frame.daftarCemilan.size(); i++) {
            frame.panelMenuCemilan.add(buatCardProduk(off2 + i, "Cemilan"));
        }
        frame.scrollCemilan = new JScrollPane(frame.panelMenuCemilan);
        frame.scrollCemilan.setBorder(null);

        frame.panelKiri.add(frame.scrollMakanan);
    }

    private JPanel buatCardProduk(int globalIndex, String kategori) {
        ProdukDAO.ProdukDB p = frame.daftarSemuaProduk.get(globalIndex);
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(frame.warnaCard);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 255), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        // === GAMBAR ===
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setBackground(new Color(240, 245, 255));
        imgPanel.setPreferredSize(new Dimension(180, 120));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(p.getImagePath()));
            if (icon.getImage() != null) {
                Image img = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                JLabel lblGambar = new JLabel(new ImageIcon(img));
                lblGambar.setHorizontalAlignment(SwingConstants.CENTER);
                imgPanel.add(lblGambar, BorderLayout.CENTER);
            } else {
                imgPanel.add(new JLabel("No Image", SwingConstants.CENTER));
            }
        } catch (Exception ex) {
            imgPanel.add(new JLabel("No Image", SwingConstants.CENTER));
        }

        card.add(imgPanel, BorderLayout.CENTER);

        // === NAMA + HARGA ===
        JLabel lblNama = new JLabel("<html><center>" + p.nama
                + "<br><b style='color:" + toHex(frame.warnaPrimer) + "'>Rp " + frame.formatRupiah(p.harga) + "</b></center></html>");
        lblNama.setHorizontalAlignment(SwingConstants.CENTER);
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // === LABEL JUMLAH & STOCK ===
        frame.labelJumlah.set(globalIndex, new JLabel("Jumlah: 0", SwingConstants.CENTER));
        frame.labelJumlah.get(globalIndex).setForeground(new Color(70, 80, 140));
        frame.labelStock.set(globalIndex, new JLabel("Stock: " + p.stock, SwingConstants.CENTER));
        frame.labelStock.get(globalIndex).setForeground(new Color(40, 167, 69));

        // Handler untuk ubah jumlah
        BuatPanelHandler jumlahHandler = new BuatPanelHandler(frame, frame.labelJumlah.get(globalIndex), globalIndex);
        frame.labelJumlah.get(globalIndex).addMouseListener(jumlahHandler);

        JPanel bottom = new JPanel(new GridLayout(2, 1, 0, 4));
        bottom.setBackground(frame.warnaCard);
        bottom.add(frame.labelJumlah.get(globalIndex));
        bottom.add(frame.labelStock.get(globalIndex));

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(frame.warnaCard);
        info.add(lblNama, BorderLayout.CENTER);
        info.add(bottom, BorderLayout.SOUTH);
        card.add(info, BorderLayout.SOUTH);

        BuatPanelHandler cardHandler = new BuatPanelHandler(frame, card, globalIndex);
        card.addMouseListener(cardHandler);

        imgPanel.addMouseListener(cardHandler);

        HoverHandler hover = new HoverHandler(card, frame.warnaAksen);
        imgPanel.addMouseListener(hover);
        card.addMouseListener(hover);

        return card;
    }

    // ================== PANEL PESANAN & PEMBAYARAN ==================
    private void buatPanelPesanan() {
        frame.panelKanan = new JPanel(new BorderLayout());
        frame.panelKanan.setBackground(frame.warnaLatar);
        frame.panelKanan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(frame.warnaPrimer);
        header.setPreferredSize(new Dimension(600, 50));

        JLabel lblJudul = new JLabel("Daftar Pesanan", SwingConstants.LEFT);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        header.add(lblJudul, BorderLayout.WEST);

        // Label Pelanggan
        frame.lblPelangganInfo = new JLabel("Pelanggan: (isi data pelanggan)", SwingConstants.RIGHT);
        frame.lblPelangganInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        frame.lblPelangganInfo.setForeground(Color.WHITE);
        frame.lblPelangganInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        frame.lblPelangganInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // PASANG HANDLER KE LABEL PELANGGAN
        BuatPanelHandler pelangganHandler = new BuatPanelHandler(frame, frame.lblPelangganInfo);
        frame.lblPelangganInfo.addMouseListener(pelangganHandler);

        header.add(frame.lblPelangganInfo, BorderLayout.EAST);

        // Tabel
        String[] kolom = {"Nama", "Beli", "Harga", "Total"};
        frame.modelTabel = new ModelTableTidakBisaDiubah(kolom, 0);
        frame.table = new JTable(frame.modelTabel);
        frame.table.setRowHeight(35);
        frame.table.getTableHeader().setBackground(frame.warnaPrimer);
        frame.table.getTableHeader().setForeground(Color.WHITE);
        frame.table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollTable = new JScrollPane(frame.table);
        scrollTable.setBorder(BorderFactory.createLineBorder(frame.warnaPrimer));

        JPanel pembayaran = buatPanelPembayaran();

        frame.panelKanan.add(header, BorderLayout.NORTH);
        frame.panelKanan.add(scrollTable, BorderLayout.CENTER);
        frame.panelKanan.add(pembayaran, BorderLayout.SOUTH);
    }

    // ================== PANEL PEMBAYARAN ==================
    private JPanel buatPanelPembayaran() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(frame.warnaCard);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(frame.warnaPrimer),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Total
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(frame.warnaCard);
        totalPanel.add(new JLabel("Total:"), BorderLayout.WEST);
        frame.labelTotal = new JLabel("Rp 0");
        frame.labelTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        frame.labelTotal.setForeground(frame.warnaPrimer);
        frame.labelTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        totalPanel.add(frame.labelTotal, BorderLayout.EAST);
        p.add(totalPanel);

        // Tunai
        JPanel tunaiPanel = new JPanel(new BorderLayout(10, 0));
        tunaiPanel.setBackground(frame.warnaCard);
        tunaiPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        tunaiPanel.add(new JLabel("Tunai:"), BorderLayout.WEST);
        frame.txfTunai = new JTextField();
        frame.txfTunai.setPreferredSize(new Dimension(220, 45));
        tunaiPanel.add(frame.txfTunai, BorderLayout.CENTER);
        p.add(tunaiPanel);

        // Kembalian
        JPanel kembaliPanel = new JPanel(new BorderLayout(10, 0));
        kembaliPanel.setBackground(frame.warnaCard);
        kembaliPanel.add(new JLabel("Kembalian:"), BorderLayout.WEST);
        frame.labelKembalian = new JLabel("Rp 0");
        frame.labelKembalian.setFont(new Font("Segoe UI", Font.BOLD, 20));
        frame.labelKembalian.setForeground(new Color(40, 167, 69));
        frame.labelKembalian.setHorizontalAlignment(SwingConstants.RIGHT);
        kembaliPanel.add(frame.labelKembalian, BorderLayout.EAST);
        p.add(kembaliPanel);

        // Tombol
        JPanel tombol = new JPanel(new GridLayout(2, 2, 12, 12));
        tombol.setBackground(frame.warnaCard);
        tombol.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        frame.btnHitung = new JButton("Hitung");
        frame.btnHitung.setBackground(frame.warnaAksen);
        frame.btnHitung.setForeground(Color.WHITE);
        frame.btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 14));
        frame.btnHitung.addActionListener(frame);

        frame.btnHapus = new JButton("Hapus Item");
        frame.btnHapus.setBackground(new Color(220, 80, 80));
        frame.btnHapus.setForeground(Color.WHITE);
        frame.btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        frame.btnHapus.addActionListener(frame);

        frame.btnReset = new JButton("Reset");
        frame.btnReset.setBackground(new Color(100, 110, 160));
        frame.btnReset.setForeground(Color.WHITE);
        frame.btnReset.setFont(new Font("Segoe UI", Font.BOLD, 14));
        frame.btnReset.addActionListener(frame);

        frame.btnBayar = new JButton("Bayar");
        frame.btnBayar.setBackground(frame.warnaPrimer);
        frame.btnBayar.setForeground(Color.WHITE);
        frame.btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        frame.btnBayar.addActionListener(frame);

        tombol.add(frame.btnHitung);
        tombol.add(frame.btnHapus);
        tombol.add(frame.btnReset);
        tombol.add(frame.btnBayar);

        p.add(tombol);
        return p;
    }

    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
