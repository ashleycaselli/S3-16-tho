package view.component;

import utils.Resources;

import javax.swing.*;
import java.awt.*;

/**
 * This is a panel containing a Logo Image as background.
 */
public class LogoPanel extends JPanel {

    public enum Position {
        CENTER, LEFT
    }

    private final Image backgroundImage;
    private final Position position;

    public LogoPanel(final Position position) {
        this.backgroundImage = new ImageIcon(getClass().getResource(Resources.LOGO_IMAGE)).getImage();
        this.position = position;
        Dimension size = new Dimension(this.backgroundImage.getWidth(null) / 2, this.backgroundImage.getHeight(null) / 2);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    @Override
    public void paintComponent(final Graphics g) {
        switch (this.position) {
            case LEFT:
                g.drawImage(this.backgroundImage, 0, 0, this.backgroundImage.getWidth(null) / 2, this.backgroundImage.getHeight(null) / 2, null);
                break;
            case CENTER:
                int x = (this.getWidth() - this.backgroundImage.getWidth(null)) / 2;
                g.drawImage(this.backgroundImage, -x, 0, this.backgroundImage.getWidth(null) / 2, this.backgroundImage.getHeight(null) / 2, null);
                break;
            default:
                g.drawImage(this.backgroundImage, 0, 0, this.backgroundImage.getWidth(null) / 2, this.backgroundImage.getHeight(null) / 2, null);
                break;
        }
    }

}
