package kasir_kafe_shop.frame;

import javax.swing.*;
import java.awt.*;
import kasir_kafe_shop.handler.SignUpFrameHandler;

public class SignUpFrame extends JFrame {

    private JTextField txfUsername;
    private JPasswordField txfPassword;
    private JButton btnSignUp;
    private JLabel lblLogin;

    private SignUpFrameHandler signUpHandler;

    // Warna tema matching dengan kasir
    private Color warnaHeader = new Color(244, 67, 54);
    private Color warnaBg = new Color(245, 245, 245);
    private Color warnaCard = Color.WHITE;
    private Color warnaCoklat = new Color(78, 52, 46);

    public SignUpFrame() {
        // Setup frame
        setTitle("Sign Up - Sistem Kasir Kafe Shop");
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panel utama dengan background
        JPanel panelUtama = new JPanel(new GridBagLayout());
        panelUtama.setBackground(warnaBg);

        // Panel sign up card
        JPanel panelSignUp = new JPanel();
        panelSignUp.setLayout(null);
        panelSignUp.setPreferredSize(new Dimension(380, 350));
        panelSignUp.setBackground(warnaCard);

        // Header Sign Up
        JLabel lblJudul = new JLabel("SIGN UP", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblJudul.setForeground(warnaCoklat);
        lblJudul.setBounds(0, 30, 380, 40);
        panelSignUp.add(lblJudul);

        JLabel lblSubjudul = new JLabel("Buat akun kasir baru", SwingConstants.CENTER);
        lblSubjudul.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubjudul.setForeground(Color.GRAY);
        lblSubjudul.setBounds(0, 75, 380, 20);
        panelSignUp.add(lblSubjudul);

        // Label Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setBounds(50, 120, 100, 25);
        panelSignUp.add(lblUsername);

        // TextField Username
        txfUsername = new JTextField();
        txfUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txfUsername.setBounds(50, 145, 280, 35);
        panelSignUp.add(txfUsername);

        // Label Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setBounds(50, 190, 100, 25);
        panelSignUp.add(lblPassword);

        // TextField Password
        txfPassword = new JPasswordField();
        txfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txfPassword.setBounds(50, 215, 280, 35);
        panelSignUp.add(txfPassword);

        // Button Sign Up
        btnSignUp = new JButton("Sign Up");
        btnSignUp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSignUp.setBackground(warnaHeader);
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setBounds(50, 270, 280, 40);
        btnSignUp.setFocusPainted(false);

        // Link ke Login
        JLabel lblText = new JLabel("Already have an account?");
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblText.setForeground(Color.GRAY);
        lblText.setBounds(100, 320, 160, 20);
        panelSignUp.add(lblText);

        lblLogin = new JLabel("Login");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLogin.setForeground(warnaHeader);
        lblLogin.setBounds(255, 320, 50, 20);

        signUpHandler = new SignUpFrameHandler(this, lblLogin, btnSignUp);

        btnSignUp.addActionListener(signUpHandler);
        lblLogin.addMouseListener(signUpHandler);
        panelSignUp.add(btnSignUp);
        panelSignUp.add(lblLogin);

        // Tambah panel sign up ke panel utama
        panelUtama.add(panelSignUp);

        add(panelUtama, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prosesSignUp() {
        String username = txfUsername.getText().trim();
        String password = new String(txfPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean berhasil = kasir_kafe_shop.dao.KaryawanDAO.tambahKaryawan(username, password);
        if (berhasil) {
            JOptionPane.showMessageDialog(this, "Registrasi berhasil!\nSilakan login.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Username sudah terdaftar!",
                    "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new SignUpFrame();
    }
}
