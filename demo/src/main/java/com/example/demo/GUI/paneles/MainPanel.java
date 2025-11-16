package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainPanel extends BasePanel {
    JButton botonCrudProducto,botonProductos,botonVentaRapida,botonReportes;


    public MainPanel(EventBus eventBus){
        super(eventBus);
        inicializarComponentes();
    }


    @Override
    protected void inicializarComponentes() {

        JLabel titulo = new JLabel("Bienvenido a DLClean", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(titulo, BorderLayout.NORTH);

        botonCrudProducto=new JButton("CRUD PRODUCTOS");
        botonProductos=new JButton("Inventario");
        botonVentaRapida=new JButton("Venta Rapida");
        botonReportes=new JButton("Reportes");
        botonProductos.addActionListener((e)->eventBus.publish("panelProductos"));
        botonCrudProducto.addActionListener((e)->eventBus.publish("panelCrudProductos"));
        botonVentaRapida.addActionListener(e->eventBus.publish("panelVentaRapida"));
        botonReportes.addActionListener((e)->eventBus.publish("panelReportes"));
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(botonProductos);
        panelBotones.add(botonCrudProducto);
        panelBotones.add(botonVentaRapida);
        panelBotones.add(botonReportes);
        this.add(panelBotones, BorderLayout.CENTER);
    }
}
