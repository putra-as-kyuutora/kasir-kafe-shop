package kasir_kafe_shop.handler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import kasir_kafe_shop.frame.LoginFrame;
import kasir_kafe_shop.frame.SignUpFrame;

/**
 *
 * @author putra
 */
public class SignUpFrameHandler implements ActionListener, MouseListener {

    private SignUpFrame signUpFrame;
    private JLabel lblLogin;
    private JButton btnSignUp;

    public SignUpFrameHandler(SignUpFrame signUpFrame, JLabel lblLogin, JButton btnSignUp) {
        this.signUpFrame = signUpFrame;
        this.lblLogin = lblLogin;
        this.btnSignUp = btnSignUp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (btnSignUp == e.getSource()) {
            signUpFrame.prosesSignUp();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        signUpFrame.dispose();
        new LoginFrame();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        lblLogin.setText("<html><u>Daftar di sini</u></html>");
        lblLogin.setForeground(new Color(45, 55, 160));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        lblLogin.setText("Daftar di sini");
        lblLogin.setForeground(new Color(100, 130, 255));

    }

}
