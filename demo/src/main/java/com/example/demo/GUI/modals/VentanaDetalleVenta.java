package com.example.demo.GUI.modals;


import com.example.demo.entity.DetalleVenta;
import com.example.demo.entity.Venta;
import com.example.demo.utils.ImageLoader;
import com.example.demo.utils.PrintTicket58;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class VentanaDetalleVenta extends JDialog {

    ImageLoader imageLoader;
    JButton botonImprimir;
    JButton btnObservacion;
    List<DetalleVenta> detalles;
    Venta venta;
    public VentanaDetalleVenta(JFrame owner, Venta venta, List<DetalleVenta> detalles) {
        super(owner, "Detalle de Venta #" + (venta != null ? venta.getId() : "N/A"), true);
        this.detalles=detalles;
        this.venta=venta;
        imageLoader=ImageLoader.getInstance();
        botonImprimir=new JButton(new ImageIcon(imageLoader.getImage("impresora1PNG").get().getScaledInstance(32,32,Image.SCALE_SMOOTH)));
        btnObservacion=new JButton(new ImageIcon(imageLoader.getImage("reportesPNG").get().getScaledInstance(32,32,Image.SCALE_SMOOTH)));
        this.setSize(500, 400);
        this.setLocationRelativeTo(owner);
        this.setLayout(new BorderLayout());

        // Información general de la venta
        JPanel panelInfo = new JPanel(new GridLayout(0, 2, 5, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        configurarEventos();
        panelInfo.add(new JLabel("ID:"));
        panelInfo.add(new JLabel(venta != null && venta.getId() != null ? String.valueOf(venta.getId()) : "N/A"));
        panelInfo.add(new JLabel("Fecha:"));
        panelInfo.add(new JLabel(venta != null && venta.getFecha() != null ? String.valueOf(venta.getFecha()) : "N/A"));
        panelInfo.add(new JLabel("Método de pago:"));
        panelInfo.add(new JLabel(venta != null && venta.getMetodoPago() != null ? String.valueOf(venta.getMetodoPago()) : "N/A"));
        panelInfo.add(new JLabel("Total:"));
        panelInfo.add(new JLabel(venta != null ? String.valueOf(venta.getTotal()) : "0"));
        this.add(panelInfo, BorderLayout.NORTH);

        // Tabla con los detalles de la venta
        String[] columnas = {"Codigo","Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        if(detalles != null) {
            for (DetalleVenta dv : detalles) {
                Object[] fila = {
                        dv.getProducto() != null ? dv.getProducto().getCodigo() : "N/A",
                        dv.getProducto() != null ? dv.getProducto().getNombre() : "N/A",
                        dv.getCantidad(),
                        dv.getPrecioUnitario(),
                        dv.getSubTotal()
                };
                modeloTabla.addRow(fila);
            }
        }

        JTable tabla = new JTable(modeloTabla);
        this.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> this.dispose());
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(botonImprimir);
        panelBoton.add(btnCerrar);
        panelBoton.add(btnObservacion);
        this.add(panelBoton, BorderLayout.SOUTH);

        this.setVisible(true);
    }
    private void configurarEventos(){
        botonImprimir.addActionListener(e->imprimir());
        btnObservacion.addActionListener(e->mostrarObservacion());
    }

    private void mostrarObservacion() {
        JTextArea area = new JTextArea(venta.getObservacion());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(300, 150));
        JOptionPane.showMessageDialog(this, scroll, "Observaciones", JOptionPane.INFORMATION_MESSAGE);
    }


    public void imprimir(){
       String ticket= PrintTicket58.generarTicket(detalles,venta.getTotal(),venta.getFecha());
       System.out.println("-----------------------------------");
       System.out.println(ticket);
        System.out.println("-----------------------------------");
        PrintTicket58.imprimir(ticket);
    }
}
