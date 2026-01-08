package com.example.demo.GUI.modals;

import com.example.demo.entity.Producto;

import javax.swing.*;
import java.awt.*;

public class VentanaEliminarProducto extends JDialog {
    private JButton btnConfirmar;
    private JButton btnCancelar;

    private Producto producto;
    public VentanaEliminarProducto(JFrame ventanaPadre,Producto producto){
        super(ventanaPadre,true);
        this.producto=producto;
        inicializarComponentes();
        configurarEventos();
        this.pack();//autoajuste
        this.setLocationRelativeTo(ventanaPadre);
    }
    public void inicializarComponentes(){
        setLayout(new BorderLayout());
        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 5, 5));

        panelCampos.add(new JLabel("Codigo: "+ producto.getCodigo()));
        panelCampos.add(new JLabel("Nombre: "+producto.getNombre()));
        panelCampos.add(new JLabel("Categoría: "+producto.getCategoria().toString()));
        panelCampos.add(new JLabel("Precio: "+producto.getPrecioVenta()));
        panelCampos.add(new JLabel("Stock: "+ producto.getStock()));

        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    public void configurarEventos(){

        btnConfirmar.addActionListener(e->{
            dispose();
        });

        btnCancelar.addActionListener(e->{
            producto = null;
            dispose();
        });
    }
    public Producto getProductoAEliminar(){
        return producto;
    }
}
