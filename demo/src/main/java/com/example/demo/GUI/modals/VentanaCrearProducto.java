package com.example.demo.GUI.modals;

import com.example.demo.Enum.Categoria;
import com.example.demo.dto.ProductoDto;

import javax.swing.*;
import java.awt.*;

public class VentanaCrearProducto extends JDialog {

    private JTextField txtNombre;
    private JComboBox<Categoria> comboCategoria;
    private JTextField txtPrecioCosto;
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
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 8, 8));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Categoría:"));
        comboCategoria = new JComboBox<>(Categoria.values());
        panelCampos.add(comboCategoria);

        panelCampos.add(new JLabel("Precio Costo:"));
        txtPrecioCosto = new JTextField();
        panelCampos.add(txtPrecioCosto);

        panelCampos.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelCampos.add(txtPrecio);

        panelCampos.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        panelCampos.add(txtStock);

        panelCampos.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panelCampos.add(txtCodigo);

        btnCrear = new JButton("Crear");
        btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panelBotones.add(btnCrear);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void configurarEventos() {
        btnCrear.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String codigoTxt=txtCodigo.getText().trim();
            String precioCostoTxt = txtPrecioCosto.getText().trim();
            String precioTxt = txtPrecio.getText().trim();
            String stockTxt = txtStock.getText().trim();

            if (nombre.isEmpty() ||  codigoTxt.isEmpty() || precioCostoTxt.isEmpty() || precioTxt.isEmpty() || stockTxt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            try {
                double precioCosto=Double.parseDouble(precioCostoTxt);
                double precio = Double.parseDouble(precioTxt);
                int stock = Integer.parseInt(stockTxt);
                long codigo=Long.parseLong(codigoTxt);
                Categoria categoriaSeleccionada = (Categoria) comboCategoria.getSelectedItem();

                productoCreado = new ProductoDto.Builder()
                        .setNombre(nombre)
                        .setCategoria(categoriaSeleccionada)
                        .setPrecioCosto(precioCosto)
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
