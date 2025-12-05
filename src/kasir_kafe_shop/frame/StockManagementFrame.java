package kasir_kafe_shop.frame;

import kasir_kafe_shop.dao.ProdukDAO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import kasir_kafe_shop.handler.StockManagementHandler;

public class StockManagementFrame extends JFrame {

    private MainFrame mainFrame;
    private JPanel panelKonten;
    private JPanel panelHeader;
    private String kategoriAktif = "Makanan";

    private StockManagementHandler stockHandler;

    public StockManagementFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setTitle("Kelola Stock Produk");
        setSize(1000, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(mainFrame);
        setLayout(new BorderLayout());

        // HEADER KATEGORI — SIMPAN REFERENSI!
        panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelHeader.setBackground(new Color(31, 40, 128));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] kategori = {"Makanan", "Minuman", "Cemilan"};
        for (String kat : kategori) {
            JLabel lbl = new JLabel(kat);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lbl.setOpaque(true);
            lbl.setPreferredSize(new Dimension(140, 45));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            stockHandler = new StockManagementHandler(this, lbl, kat);

            if (kat.equals("Makanan")) {
                lbl.setBackground(new Color(80, 100, 200));
            } else {
                lbl.setBackground(new Color(31, 40, 128));
            }

            lbl.addMouseListener(stockHandler);

            panelHeader.add(lbl);
        }

        panelKonten = new JPanel(new GridLayout(0, 5, 20, 20));
        panelKonten.setBackground(new Color(245, 248, 255));
        panelKonten.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panelHeader, BorderLayout.NORTH);
        add(new JScrollPane(panelKonten), BorderLayout.CENTER);

        gantiKategori("Makanan");
        setVisible(true);
    }

    public void gantiKategori(String kategori) {
        kategoriAktif = kategori;
        panelKonten.removeAll();

        // UPDATE WARNA HEADE
        for (Component comp : panelHeader.getComponents()) {
            if (comp instanceof JLabel lbl) {
                String text = lbl.getText();
                if (text.equals(kategori)) {
                    lbl.setBackground(new Color(80, 100, 200));
                } else {
                    lbl.setBackground(new Color(31, 40, 128));
                }

            }
        }

        ArrayList<ProdukDAO.ProdukDB> list;

        switch (kategori) {
            case "Makanan":
                list = mainFrame.daftarMakanan;
                break;

            case "Minuman":
                list = mainFrame.daftarMinuman;
                break;

            case "Cemilan":
                list = mainFrame.daftarCemilan;
                break;

            default:
                list = new ArrayList<>();
                break;
        }

        for (ProdukDAO.ProdukDB p : list) {
            panelKonten.add(buatCardStock(p));
        }

        panelKonten.revalidate();
        panelKonten.repaint();
    }

    private JPanel buatCardStock(ProdukDAO.ProdukDB p) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(12, 12));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(260, 380)); 

        // BORDER + SHADOW EFFECT (cantik banget)
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 255), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // GAMBAR — LEBIH LEBAR
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setBackground(new Color(240, 248, 255));
        imgPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 130, 255), 2));
        imgPanel.setPreferredSize(new Dimension(240, 160));

        String path = p.getImagePath();
        if (path != null && !path.trim().isEmpty()) {
            java.net.URL url = getClass().getResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH);
                JLabel lblImg = new JLabel(new ImageIcon(img));
                lblImg.setHorizontalAlignment(SwingConstants.CENTER);
                imgPanel.add(lblImg, BorderLayout.CENTER);
            } else {
                JLabel lblNoImg = new JLabel("Gambar Tidak Ada", SwingConstants.CENTER);
                lblNoImg.setForeground(Color.GRAY);
                lblNoImg.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                imgPanel.add(lblNoImg, BorderLayout.CENTER);
            }
        } else {
            JLabel lblNoImg = new JLabel("No Image", SwingConstants.CENTER);
            lblNoImg.setForeground(Color.LIGHT_GRAY);
            lblNoImg.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            imgPanel.add(lblNoImg, BorderLayout.CENTER);
        }

        // INFO: NAMA & HARGA
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JLabel lblNama = new JLabel("<html><center><b>" + p.nama + "</b></center></html>");
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNama.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblHarga = new JLabel("Rp " + formatRupiah(p.harga));
        lblHarga.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblHarga.setForeground(new Color(31, 40, 128));
        lblHarga.setHorizontalAlignment(SwingConstants.CENTER);

        panelInfo.add(lblNama, BorderLayout.NORTH);
        panelInfo.add(lblHarga, BorderLayout.SOUTH);

        // STOCK DENGAN WARNA
        JLabel lblStock = new JLabel("Stock: " + p.stock);
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblStock.setHorizontalAlignment(SwingConstants.CENTER);

        if (p.stock > 10) {
            lblStock.setForeground(new Color(40, 167, 69));   // hijau
        } else if (p.stock > 0) {
            lblStock.setForeground(new Color(255, 152, 0));   // orange
        } else {
            lblStock.setForeground(Color.RED);               // merah
        }

        // INPUT & TOMBOL
        JPanel panelInput = new JPanel(new BorderLayout(10, 0));
        panelInput.setBackground(Color.WHITE);

        JTextField txtTambah = new JTextField("0");
        txtTambah.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtTambah.setHorizontalAlignment(SwingConstants.CENTER);
        txtTambah.setPreferredSize(new Dimension(100, 45));

        JButton btnTambah = new JButton("Tambah Stock");
        btnTambah.setBackground(new Color(31, 40, 128));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTambah.setFocusPainted(false);
        btnTambah.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Handler tombol tambah stock
        stockHandler = new StockManagementHandler(this, p, txtTambah, lblStock);
        btnTambah.addActionListener(stockHandler);

        panelInput.add(txtTambah, BorderLayout.CENTER);
        panelInput.add(btnTambah, BorderLayout.EAST);

        // SUSUN SEMUA
        card.add(imgPanel, BorderLayout.NORTH);
        card.add(panelInfo, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBackground(Color.WHITE);
        panelBottom.add(lblStock, BorderLayout.NORTH);
        panelBottom.add(Box.createVerticalStrut(15));
        panelBottom.add(panelInput, BorderLayout.SOUTH);

        card.add(panelBottom, BorderLayout.SOUTH);

        return card;
    }

    public String getKategoriAktif() {
        return kategoriAktif;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    private String formatRupiah(int angka) {
        return String.format("%,d", angka).replace(',', '.');
    }

    public static void main(String[] args) {
        new StockManagementFrame(new MainFrame(""));
    }
}
