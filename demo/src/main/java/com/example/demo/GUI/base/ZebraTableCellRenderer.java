package com.example.demo.GUI.base;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ZebraTableCellRenderer extends DefaultTableCellRenderer {

    private static final Color EVEN = new Color(245, 245, 245);
    private static final Color ODD  = new Color(252, 252, 252);

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );

        if (!isSelected) {
            c.setBackground(row % 2 == 0 ? EVEN : ODD);
            c.setForeground(Color.BLACK);
        }

        return c;
    }
}


