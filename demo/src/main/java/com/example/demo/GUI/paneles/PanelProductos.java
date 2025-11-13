package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.PlantillaPanelProductos;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class PanelProductos extends PlantillaPanelProductos {

    public PanelProductos(ProductoService productoService, EventBus eventBus) {

        super(productoService,eventBus);
        inicializarComponentes();
    }

    @Override
    protected void inicializarComponentes() {
        this.setLayout(new BorderLayout());
        agregarPanelPlantilla(BorderLayout.CENTER);
        JPanel panelHeader=new JPanel(new BorderLayout());
        JButton btnMainPanel=new JButton("Inicio");
        btnMainPanel.addActionListener((e)->eventBus.publish("mainPanel"));
        JLabel titulo = new JLabel("Gestion de Productos", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        panelHeader.add(titulo, BorderLayout.CENTER);
        panelHeader.add(btnMainPanel,BorderLayout.WEST);
        this.add(panelHeader,BorderLayout.NORTH);
        super.configurarTabla();
        configurarSidePanels();
        agregarListenerSeleccionParaLista();

    }


    private void configurarSidePanels() {
        JPanel panelDeBusqueda = new JPanel(new BorderLayout());
        panelDeBusqueda.setBackground(Color.BLACK);

        JButton btnListar = new JButton("Listar todos");
        btnListar.addActionListener(e -> cargarProductos(null));
        panelDeBusqueda.add(btnListar, BorderLayout.NORTH);

        JTextField busqueda = new JTextField();
        panelDeBusqueda.add(busqueda, BorderLayout.CENTER);
        busqueda.addActionListener(e -> cargarProductos(busqueda.getText()));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> cargarProductos(busqueda.getText()));
        panelDeBusqueda.add(btnBuscar, BorderLayout.SOUTH);

        this.add(panelDeBusqueda, BorderLayout.WEST);
    }

    @Override
    protected void onProductoSeleccionado(Producto producto) {
        System.out.println(producto.getNombre());
    }

}
