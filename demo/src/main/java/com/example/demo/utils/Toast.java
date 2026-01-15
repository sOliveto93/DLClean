package com.example.demo.utils;

import javax.swing.*;
import java.awt.*;

public class Toast {

    public static void mostrarToast(String mensaje) {
        // Crear ventana flotante
        JWindow toast = new JWindow();
        toast.setBackground(new Color(0,0,0,0)); // transparente
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 128, 0, 220)); // verde semitransparente
        panel.setBorder(BorderFactory.createLineBorder(Color.GREEN.darker(), 2));
        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        panel.add(label);
        toast.add(panel);
        toast.pack();

        // Posición inicial: abajo a la derecha
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - toast.getWidth() - 20;
        int yStart = screenSize.height;
        int yEnd = screenSize.height - toast.getHeight() - 50;
        toast.setLocation(x, yStart);
        toast.setVisible(true);

        // Animación de subida
        int delay = 10; // ms por frame
        new Timer(delay, new AbstractAction() {
            int y = yStart;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (y > yEnd) {
                    y -= 5; // velocidad
                    toast.setLocation(x, y);
                } else {
                    ((Timer)e.getSource()).stop();
                    // Timer para desaparecer después de 2s
                    new Timer(2000, ev -> {
                        // Animación de bajada
                        new Timer(delay, new AbstractAction() {
                            int y2 = yEnd;
                            @Override
                            public void actionPerformed(java.awt.event.ActionEvent ev2) {
                                if (y2 < yStart) {
                                    y2 += 5;
                                    toast.setLocation(x, y2);
                                } else {
                                    ((Timer)ev2.getSource()).stop();
                                    toast.dispose();
                                }
                            }
                        }).start();
                    }).start();
                }
            }
        }).start();
    }
}
