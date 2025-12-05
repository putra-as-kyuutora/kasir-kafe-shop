package kasir_kafe_shop.frame;

import javax.swing.*;
import java.awt.*;
import kasir_kafe_shop.dao.KaryawanDAO;
import kasir_kafe_shop.handler.LoginFrameHandler;

public class LoginFrame extends JFrame {

    private JTextField txfUsername;
    private JPasswordField txfPassword;
    private JButton btnLogin;
    private JLabel lblSignUp;

    private LoginFrameHandler loginHandler;

    // TEMA BIRU NAVY ELEGAN
    private final Color warnaPrimer = new Color(31, 40, 128);
    private final Color warnaAksen = new Color(100, 130, 255);
    private final Color warnaLatar = new Color(245, 248, 255);
    private final Color warnaCard = Color.WHITE;

    public LoginFrame() {
        setTitle("Login");
        setSize(450, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Background utama
        JPanel panelUtama = new JPanel(new GridBagLayout());
        panelUtama.setBackground(warnaLatar);

        // Card login
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(null);
        panelLogin.setPreferredSize(new Dimension(380, 370));
        panelLogin.setBackground(warnaCard);

        // Judul
        JLabel lblJudul = new JLabel("LOGIN", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblJudul.setForeground(warnaPrimer);
        lblJudul.setBounds(0, 25, 380, 50);
        panelLogin.add(lblJudul);

        JLabel lblSub = new JLabel("Masuk ke sistem kasir", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSub.setForeground(new Color(100, 110, 140));
        lblSub.setBounds(0, 75, 380, 20);
        panelLogin.add(lblSub);

        // Username & Password
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(warnaPrimer);
        lblUser.setBounds(40, 120, 100, 30);
        panelLogin.add(lblUser);

        txfUsername = new JTextField();
        txfUsername.setBounds(40, 150, 300, 45);
        txfUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelLogin.add(txfUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setForeground(warnaPrimer);
        lblPass.setBounds(40, 200, 100, 30);
        panelLogin.add(lblPass);

        txfPassword = new JPasswordField();
        txfPassword.setBounds(40, 230, 300, 45);
        txfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelLogin.add(txfPassword);

        // Tombol Login
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(warnaPrimer);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(40, 290, 300, 50);
        btnLogin.setFocusPainted(false);

        // Link Sign Up
        JLabel lblText = new JLabel("Belum punya akun?");
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblText.setForeground(new Color(100, 110, 140));
        lblText.setBounds(100, 350, 150, 20);
        panelLogin.add(lblText);

        lblSignUp = new JLabel("Daftar di sini");
        lblSignUp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSignUp.setForeground(warnaAksen);
        lblSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSignUp.setBounds(240, 350, 100, 20);
        loginHandler = new LoginFrameHandler(this, btnLogin, lblSignUp);

        btnLogin.addActionListener(loginHandler);
        lblSignUp.addMouseListener(loginHandler);

        panelLogin.add(lblSignUp);
        panelLogin.add(btnLogin);
        panelUtama.add(panelLogin);

        add(panelUtama, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prosesLogin() {
        String username = txfUsername.getText().trim();
        String password = new String(txfPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String valid = KaryawanDAO.login(username, password);
        if (valid != null) {
            JOptionPane.showMessageDialog(this, "Selamat datang, " + valid + "!",
                    "Login Berhasil", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MainFrame(valid);
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password salah!\nAtau database sedang offline.",
                    "Login Gagal", JOptionPane.ERROR_MESSAGE);
            txfPassword.setText("");
            txfUsername.requestFocus();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
