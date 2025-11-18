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

        setPreferredSize(new Dimension(15, 15));
        setBackground(color);

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getColor(nivel));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private int calcularNivel(double total) {
        if (total == 0) return 0;
        if (total < 5000) return 1;
        if (total < 20000) return 2;
        if (total < 50000) return 3;
        return 4;
    }

    private Color getColor(int nivel) {
        // tu lógica
        switch (nivel) {
            case 0:
                return new Color(220, 220, 220);
            case 1:
                return new Color(150, 200, 150);
            case 2:
                return new Color(100, 180, 100);
            case 3:
                return new Color(50, 160, 50);
            default:
                return new Color(20, 120, 20);
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getNivel() {
        return nivel;
    }
}


