package com.example.demo.GUI.base;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;
public class RendererStock extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        // obtenemos el componente base
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // si la columna actual es la de Stock (la 3 en tu modelo)
        if (column == 3 && value instanceof Number) {
            int stock = ((Number) value).intValue();

            if (stock < 10) {
                c.setForeground(Color.RED);
            } else {
                c.setForeground(Color.BLACK);
            }
        } else {
            c.setForeground(Color.BLACK);
        }

        // mantenemos el color de fondo si está seleccionada
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
        } else {
            c.setBackground(Color.WHITE);
        }

        return c;
    }
}
