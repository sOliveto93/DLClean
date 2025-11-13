package com.example.demo.GUI.paneles;

import com.example.demo.Enum.Categoria;
import com.example.demo.Enum.MetodoPago;
import com.example.demo.GUI.base.PlantillaPanelProductos;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.GUI.tablas.ItemVentaUI;
import com.example.demo.GUI.tablas.ModeloTablaProductos;
import com.example.demo.GUI.tablas.ModeloTablaVentas;
import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class PanelVentaRapida extends PlantillaPanelProductos {

    private JTable tablaDetalle;
    private ModeloTablaVentas modeloDetalle;
    private JButton btnMainPanel;

    private Producto productoSeleccionado;

    public PanelVentaRapida(ProductoService productoService, EventBus eventBus){
        super(productoService,eventBus);
        inicializarComponentes();
        productoSeleccionado = null;
    }

    @Override
    protected void inicializarComponentes() {
        this.setLayout(new BorderLayout());
        agregarPanelPlantilla(BorderLayout.WEST);
        JPanel panelHeader=new JPanel(new BorderLayout());
        JPanel panelDetalleVenta=configurarPanelDetalleVenta();
        JLabel titulo = new JLabel("VENTA PANEL", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnMainPanel=new JButton("Inicio");
        btnMainPanel.addActionListener(e->eventBus.publish("mainPanel"));
        super.configurarTabla();



        configurarSidePanels();
        panelHeader.add(btnMainPanel, BorderLayout.WEST);
        panelHeader.add(titulo, BorderLayout.CENTER);
        this.add(panelHeader,BorderLayout.NORTH);
        this.add(panelDetalleVenta,BorderLayout.CENTER);
        cargarProductos(null);
        agregarListenerDobleClick();
    }

    public void configurarSidePanels(){}

    @Override
    protected void onProductoSeleccionado(Producto producto) {
        if(producto == null) return;

        for (ItemVentaUI item : modeloDetalle.getProdcutos()) {
            if(item.getProducto().getCodigo() == producto.getCodigo()){
                item.setCantidad(item.getCantidad() + 1); // suma 1
                modeloDetalle.fireTableDataChanged();
                return;
            }
        }

        modeloDetalle.addProducto(new ItemVentaUI(producto, 1));
    }
    public JPanel configurarPanelDetalleVenta(){
        JPanel panel=new JPanel(new BorderLayout());
        modeloDetalle=new ModeloTablaVentas();
        tablaDetalle=new JTable(modeloDetalle);
        panel.add(new JScrollPane(tablaDetalle),BorderLayout.CENTER);
        panel.add( new JComboBox<>(MetodoPago.values()),BorderLayout.SOUTH);
        return panel;
    }
    public void agregarListenerDobleClick(){
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    int fila = tabla.getSelectedRow();
                    if(fila != -1){
                        Producto producto = modeloTabla.getProductoAt(tabla.convertRowIndexToModel(fila));
                        onProductoSeleccionado(producto);
                    }
                }
            }
        });
    }
}
