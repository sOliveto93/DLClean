package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.PlantillaPanelProductos;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class PanelProductos extends PlantillaPanelProductos {

    public PanelProductos(ProductoService productoService, VentaService ventaService, EventBus eventBus) {

        super(productoService, ventaService, eventBus);
        inicializarComponentes();
    }

    @Override
    protected void inicializarComponentes() {
        this.setLayout(new BorderLayout());
        agregarPanelPlantilla(BorderLayout.CENTER);
        JPanel panelHeader = new JPanel(new BorderLayout());
        JButton btnMainPanel = new JButton("Inicio");
        btnMainPanel.addActionListener((e) -> eventBus.publish("mainPanel"));
        JLabel titulo = new JLabel("Gestion de Productos", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panelHeader.add(titulo, BorderLayout.CENTER);
        panelHeader.add(btnMainPanel, BorderLayout.WEST);
        panelHeader.add(super.crearPanelBusqueda(), BorderLayout.SOUTH);
        this.add(panelHeader, BorderLayout.NORTH);
        super.configurarTabla();

        agregarListenerSeleccionParaLista();

    }

    @Override
    public void init() {

    }


    @Override
    protected void onProductoSeleccionado(Producto producto) {
        System.out.println(producto.getNombre());
    }

}
