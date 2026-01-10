package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.entity.Venta;
import com.example.demo.utils.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PanelReporteVentaDiarioActual extends BasePanel {

    private double total;
    private JLabel totalLabel;
    private ImageLoader imageLoader;
    private BufferedImage img;
    public PanelReporteVentaDiarioActual(EventBus eventBus, List<Venta> ventas){
        super(eventBus);
        imageLoader=ImageLoader.getInstance();
        Optional<BufferedImage>op=imageLoader.getImage("dineroPNG");
        op.ifPresent(img->this.img=img);

        if (ventas == null) ventas = new ArrayList<>();
        total = 0;
        for (Venta v : ventas) total += v.getTotal();

        inicializarComponentes();
    }

    @Override
    protected void inicializarComponentes() {
        // Panel principal con FlowLayout centrado
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20)); // centrado, espacio horizontal 5, vertical 20
        this.setBackground(new Color(144, 238, 144)); // verde pastel
        this.setBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 2));

        // Símbolo de dólar
        JLabel simboloLabel = new JLabel(new ImageIcon(img.getScaledInstance(64,64,Image.SCALE_SMOOTH)));
        simboloLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        simboloLabel.setForeground(Color.WHITE);

        // Total
        totalLabel = new JLabel(String.format("%.2f", total));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        totalLabel.setForeground(Color.WHITE);

        // Añadimos los labels al panel
        this.add(simboloLabel);
        this.add(totalLabel);

        // Suscripción al EventBus
        suscribirEventos();
    }

    @Override
    public void init() {

    }

    public void suscribirEventos(){
        eventBus.subscribe("ventaCreada", (data) -> sumarTotalDiario((Venta) data));
    }

    public void sumarTotalDiario(Venta venta){
        total += venta.getTotal();
        SwingUtilities.invokeLater(() -> totalLabel.setText(String.format("%.2f", total)));
    }
}
