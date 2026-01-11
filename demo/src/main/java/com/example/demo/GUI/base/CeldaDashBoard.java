package com.example.demo.GUI.base;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class CeldaDashBoard extends JPanel {

    private LocalDate fecha;
    private int nivel;
    private double total;
    private Color color;

    public CeldaDashBoard(LocalDate fecha, double total) {
        this.fecha = fecha;
        this.total = total;
        this.nivel = calcularNivel(total);
        this.color = getColor(nivel);

        setPreferredSize(new Dimension(22,22));
        setToolTipText(
                "📅 Fecha: "+fecha + " — $" + String.format("%,.2f", total)
        );
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        CeldaDashBoard.this,
                        "📅 Fecha: " + fecha +
                                "\n💵 Total vendido: $" + total,
                        "Detalle del día",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                color = color.brighter();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                color = getColor(nivel);
                repaint();
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

        g2.setColor(new Color(0, 0, 0, 40));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

        g2.dispose();
    }


    private int calcularNivel(double total) {
        if (total == 0) return 0;
        if (total < 20000) return 1;
        if (total < 50000) return 2;
        if (total < 90000) return 3;
        return 4;
    }

    private Color getColor(int nivel) {
        // tu lógica
        switch (nivel) {
            case 0: return new Color(230, 230, 230);
            case 1: return new Color(198, 239, 206);
            case 2: return new Color(123, 201, 111);
            case 3: return new Color(56, 142, 60);
            default: return new Color(27, 94, 32);
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getNivel() {
        return nivel;
    }

}


