package view;

import javax.swing.*;
import java.awt.*;

public class THListCellRenderer implements ListCellRenderer {

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        JLabel listCellRendererComponent = (JLabel) this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        return listCellRendererComponent;
    }

}
