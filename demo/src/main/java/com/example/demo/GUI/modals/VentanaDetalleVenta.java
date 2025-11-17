package com.example.demo.GUI.modals;


import com.example.demo.entity.DetalleVenta;
import com.example.demo.entity.Venta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class VentanaDetalleVenta extends JDialog {

    public VentanaDetalleVenta(JFrame owner, Venta venta, List<DetalleVenta> detalles) {
        super(owner, "Detalle de Venta #" + venta.getId(), true); // modal = true
        this.setSize(500, 400);
        this.setLocationRelativeTo(owner);
        this.setLayout(new BorderLayout());

        // Información general de la venta
        JPanel panelInfo = new JPanel(new GridLayout(0, 2, 5, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelInfo.add(new JLabel("ID:"));
        panelInfo.add(new JLabel(String.valueOf(venta.getId())));
        panelInfo.add(new JLabel("Fecha:"));
        panelInfo.add(new JLabel(String.valueOf(venta.getFecha())));
        panelInfo.add(new JLabel("Método de pago:"));
        panelInfo.add(new JLabel(String.valueOf(venta.getMetodoPago())));
        panelInfo.add(new JLabel("Total:"));
        panelInfo.add(new JLabel(String.valueOf(venta.getTotal())));
        this.add(panelInfo, BorderLayout.NORTH);

        // Tabla con los detalles de la venta
        String[] columnas = {"Codigo","Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        for (DetalleVenta dv : detalles) {
            Object[] fila = {
                    dv.getProducto().getCodigo(),
                    dv.getProducto().getNombre(),
                    dv.getCantidad(),
                    dv.getPrecioUnitario(),
                    dv.getSubTotal()
            };
            modeloTabla.addRow(fila);
        }

        JTable tabla = new JTable(modeloTabla);
        this.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> this.dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        this.add(panelBoton, BorderLayout.SOUTH);

        this.setVisible(true);
    }
}
