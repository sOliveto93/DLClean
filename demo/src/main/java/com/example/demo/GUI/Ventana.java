package com.example.demo.GUI;


import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;

import com.example.demo.utils.ImageLoader;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Optional;

@Component
public class Ventana  extends JFrame  {
    private final EventBus eventBus;

    private final Map<String, BasePanel> paneles;
    private BasePanel panelActual;


    public Ventana(EventBus eventBus,Map<String,BasePanel> paneles){
        this.eventBus=eventBus;

        this.paneles=paneles;

        suscribirseAEVento("mainPanel");
        suscribirseAEVento("panelProductos");
        suscribirseAEVento("panelCrudProductos");
        suscribirseAEVento("panelVentaRapida");
        suscribirseAEVento("panelReporteMasVendido");
        suscribirseAEVento("panelReporteVentas");
        suscribirseAEVento("panelGestionCaja");
        configurarVentana();
    }
    private void configurarVentana(){
        this.setTitle("DLClean - Sistema de Inventario");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
    }

    public void cambiarPanel(String nombre) {
        if (panelActual != null)
            this.remove(panelActual);
        panelActual = paneles.get(nombre);
        this.add(panelActual, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
        panelActual.init();
    }
    public void suscribirseAEVento(String evento){
        eventBus.subscribe(evento, () -> {
            cambiarPanel(evento);
        });
    }
}
