package kasir_kafe_shop.handler;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class HoverHandler implements MouseListener {

    private JPanel card;
    private Color warnaAksen;

    public HoverHandler(JPanel card, Color warnaAksen) {
        this.card = card;
        this.warnaAksen = warnaAksen;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        card.setBorder(BorderFactory.createLineBorder(warnaAksen, 2));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 255), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
