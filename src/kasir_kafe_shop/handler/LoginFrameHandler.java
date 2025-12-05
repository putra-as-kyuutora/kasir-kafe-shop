package kasir_kafe_shop.handler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import kasir_kafe_shop.frame.LoginFrame;
import kasir_kafe_shop.frame.SignUpFrame;

/**
 *
 * @author putra
 */
public class LoginFrameHandler implements ActionListener, MouseListener {

    private LoginFrame loginFrame;
    private JLabel lblSignUp;
    private JButton btnLogin;

    public LoginFrameHandler(LoginFrame loginFrame, JButton btnLogin, JLabel lblSignUp) {
        this.loginFrame = loginFrame;
        this.btnLogin = btnLogin;
        this.lblSignUp = lblSignUp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (btnLogin == e.getSource()) {
            loginFrame.prosesLogin();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        loginFrame.dispose();
        new SignUpFrame();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        lblSignUp.setText("<html><u>Daftar di sini</u></html>");
        lblSignUp.setForeground(new Color(45, 55, 160));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        lblSignUp.setText("Daftar di sini");
        lblSignUp.setForeground(new Color(100, 130, 255));
    }

}
