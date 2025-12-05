package kasir_kafe_shop.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import kasir_kafe_shop.frame.PelangganFrame;

/**
 *
 * @author putra
 */
public class PelangganHandler implements ActionListener {

    JButton btnSimpan;
    PelangganFrame pelangganFrame;

    public PelangganHandler(JButton btnSimpan, PelangganFrame pelangganFrame) {
        this.btnSimpan = btnSimpan;
        this.pelangganFrame = pelangganFrame;
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(btnSimpan == e.getSource()) {
            pelangganFrame.prosesSimpan();
        }
        
    }
    
}
