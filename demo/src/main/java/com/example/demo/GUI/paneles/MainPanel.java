package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainPanel extends BasePanel {
    JButton botonCrudProducto,botonProductos,botonVentaRapida, botonReporteMasVendido,botonReporteVentas;


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
        botonReporteMasVendido =new JButton("Reportes Mas Vendidos");
        botonReporteVentas =new JButton("Reporte Ventas");
        botonProductos.addActionListener((e)->eventBus.publish("panelProductos"));
        botonCrudProducto.addActionListener((e)->eventBus.publish("panelCrudProductos"));
        botonVentaRapida.addActionListener(e->eventBus.publish("panelVentaRapida"));
        botonReporteMasVendido.addActionListener((e)->eventBus.publish("panelReporteMasVendido"));
        botonReporteVentas.addActionListener((e)->eventBus.publish("panelReporteVentas"));
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(botonProductos);
        panelBotones.add(botonCrudProducto);
        panelBotones.add(botonVentaRapida);
        panelBotones.add(botonReporteMasVendido);
        panelBotones.add(botonReporteVentas);
        this.add(panelBotones, BorderLayout.CENTER);
    }
}
