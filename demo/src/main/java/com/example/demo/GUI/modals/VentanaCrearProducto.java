package com.example.demo.GUI.modals;

import com.example.demo.Enum.Categoria;
import com.example.demo.dto.ProductoDto;

import javax.swing.*;
import java.awt.*;

public class VentanaCrearProducto extends JDialog {

    private JTextField txtNombre;
    private JComboBox<Categoria> comboCategoria;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtCodigo;
    private JButton btnCrear;
    private JButton btnCancelar;

    private ProductoDto productoCreado;

    public VentanaCrearProducto(JFrame ventanaPadre) {
        super(ventanaPadre, true);
        inicializarComponentes();
        configurarEventos();
        this.pack();//autoajuste
        this.setLocationRelativeTo(ventanaPadre);
    }

    public void inicializarComponentes() {
        setLayout(new BorderLayout());
        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 5, 5));

        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Categoría:"));
        comboCategoria = new JComboBox<>(Categoria.values());
        panelCampos.add(comboCategoria);

        panelCampos.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelCampos.add(txtPrecio);

        panelCampos.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        panelCampos.add(txtStock);

        panelCampos.add(new JLabel("Codigo:"));
        txtCodigo=new JTextField();
        panelCampos.add(txtCodigo);

        btnCrear = new JButton("Crear");
        btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrear);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void configurarEventos() {
        btnCrear.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String codigoTxt=txtCodigo.getText().trim();
            String precioTxt = txtPrecio.getText().trim();
            String stockTxt = txtStock.getText().trim();

            if (nombre.isEmpty() ||  codigoTxt.isEmpty() || precioTxt.isEmpty() || stockTxt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            try {
                double precio = Double.parseDouble(precioTxt);
                int stock = Integer.parseInt(stockTxt);
                long codigo=Long.parseLong(codigoTxt);
                Categoria categoriaSeleccionada = (Categoria) comboCategoria.getSelectedItem();

                productoCreado = new ProductoDto.Builder()
                        .setNombre(nombre)
                        .setCategoria(categoriaSeleccionada)
                        .setPrecio(precio)
                        .setStock(stock)
                        .setCodigo(codigo)
                        .build();


                dispose(); // Cierra la ventana

            } catch (IllegalArgumentException  ex) {
                JOptionPane.showMessageDialog(this, "Argumentos invalidos en precio o stock");
            }
            catch (IllegalStateException  ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> {
            productoCreado = null;
            dispose();
        });
    }


    public ProductoDto getProductoCreado() {
        return productoCreado;
    }
}
