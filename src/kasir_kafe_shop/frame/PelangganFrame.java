package kasir_kafe_shop.frame;

import javax.swing.*;
import java.awt.*;
import kasir_kafe_shop.handler.PelangganHandler;
import kasir_kafe_shop.model.Pelanggan;

public class PelangganFrame extends JFrame {

    private JTextField txfNama;
    private JComboBox<String> cmbNomorMeja;
    private JTextArea txaCatatan;
    private JButton btnSimpan;
    private MainFrame mainFrame;

    private PelangganHandler pelangganHandler;
    
    // Tema biru navy elegan (sama seperti aplikasi utama)
    private final Color warnaPrimer   = new Color(31, 40, 128);
    private final Color warnaLatar    = new Color(245, 248, 255);
    private final Color warnaAksen    = new Color(100, 130, 255);
    private final Color warnaCard     = Color.WHITE;

    public PelangganFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("Data Pelanggan");
        setSize(460, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panel utama dengan background lembut
        JPanel panelUtama = new JPanel(new GridBagLayout());
        panelUtama.setBackground(warnaLatar);

        // Card utama
        JPanel panelCard = new JPanel();
        panelCard.setLayout(null);
        panelCard.setPreferredSize(new Dimension(400, 460));
        panelCard.setBackground(warnaCard);

        // Judul
        JLabel lblJudul = new JLabel("DATA PELANGGAN", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblJudul.setForeground(warnaPrimer);
        lblJudul.setBounds(0, 15, 400, 50);
        panelCard.add(lblJudul);

        // Nama
        JLabel lblNama = new JLabel("Nama:");
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNama.setForeground(warnaPrimer);
        lblNama.setBounds(40, 80, 100, 30);
        panelCard.add(lblNama);

        txfNama = new JTextField();
        txfNama.setBounds(40, 110, 320, 45);
        txfNama.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelCard.add(txfNama);

        // Nomor Meja
        JLabel lblMeja = new JLabel("Nomor Meja:");
        lblMeja.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMeja.setForeground(warnaPrimer);
        lblMeja.setBounds(40, 170, 120, 30);
        panelCard.add(lblMeja);

        String[] meja = new String[20];
        for (int i = 0; i < 20; i++) meja[i] = String.valueOf(i + 1);
        cmbNomorMeja = new JComboBox<>(meja);
        cmbNomorMeja.setBounds(40, 200, 320, 45);
        cmbNomorMeja.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelCard.add(cmbNomorMeja);

        // Catatan
        JLabel lblCatatan = new JLabel("Catatan Tambahan:");
        lblCatatan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCatatan.setForeground(warnaPrimer);
        lblCatatan.setBounds(40, 260, 200, 30);
        panelCard.add(lblCatatan);

        txaCatatan = new JTextArea();
        txaCatatan.setLineWrap(true);
        txaCatatan.setWrapStyleWord(true);
        txaCatatan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollCatatan = new JScrollPane(txaCatatan);
        scrollCatatan.setBounds(40, 290, 320, 90);
        panelCard.add(scrollCatatan);

        // Tombol SIMPAN
        btnSimpan = new JButton("SIMPAN");
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSimpan.setBackground(warnaPrimer);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setBounds(40, 400, 320, 55); 
        btnSimpan.setFocusPainted(false);
        
        pelangganHandler = new PelangganHandler(btnSimpan, this);
        btnSimpan.addActionListener(pelangganHandler);
        panelCard.add(btnSimpan);

        panelUtama.add(panelCard);
        add(panelUtama);

        Pelanggan current = Pelanggan.getCurrentPelanggan();
        if (current != null) {
            txfNama.setText(current.getNama());
            cmbNomorMeja.setSelectedItem(current.getNomorMeja());
            txaCatatan.setText(current.getCatatan());
        }

        setLocationRelativeTo(mainFrame);
        setVisible(true);
    }

    public void prosesSimpan() {
        String nama = txfNama.getText().trim();
        String meja = (String) cmbNomorMeja.getSelectedItem();
        String catatan = txaCatatan.getText().trim();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pelanggan.tambahPelanggan(new Pelanggan(nama, meja, catatan));
        mainFrame.updatePelangganInfo(nama, meja);
        JOptionPane.showMessageDialog(this, "Data pelanggan tersimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    public static void main(String[] args) {
        MainFrame f = new MainFrame("");
        new PelangganFrame(f);
    }
}