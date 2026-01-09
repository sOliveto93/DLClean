package com.example.demo.GUI.paneles;

import com.example.demo.Enum.MetodoPago;
import com.example.demo.GUI.base.PlantillaPanelProductos;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.GUI.modeloTabla.ItemVentaUI;
import com.example.demo.GUI.modeloTabla.ModeloTablaVentas;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;
import org.springframework.stereotype.Component;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class PanelVentaRapida extends PlantillaPanelProductos {

    private JTable tablaDetalle;
    private ModeloTablaVentas modeloDetalle;
    private JButton btnMainPanel;
    private JButton btnConfirmarCompra;
    private JButton btnLimpiarCompra;
    private JComboBox<MetodoPago> comboBoxmetodoPago;
    private JLabel lblTotal;

    ProductoService productoService;

    public PanelVentaRapida(ProductoService productoService, VentaService ventaService, EventBus eventBus) {
        super(productoService, ventaService, eventBus);
        this.productoService = productoService;
        inicializarComponentes();

    }

    @Override
    protected void inicializarComponentes() {
        this.setLayout(new BorderLayout());
        agregarPanelPlantilla(BorderLayout.WEST);
        JPanel panelHeader = new JPanel(new BorderLayout());
        JPanel panelDetalleVenta = configurarPanelDetalleVenta();
        JLabel titulo = new JLabel("VENTA PANEL", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnMainPanel = new JButton("Inicio");
        btnMainPanel.addActionListener(e -> eventBus.publish("mainPanel"));
        //aca agregamos el panel productos de la plantilla
        super.configurarTabla();

        panelHeader.add(btnMainPanel, BorderLayout.WEST);
        panelHeader.add(titulo, BorderLayout.CENTER);
        panelHeader.add(super.crearPanelBusqueda(), BorderLayout.SOUTH);

        this.add(panelHeader, BorderLayout.NORTH);
        this.add(panelDetalleVenta, BorderLayout.CENTER);


        cargarProductos(null);
        configurarEventos();
        agregarListenerDobleClick();
    }


    @Override
    protected void onProductoSeleccionado(Producto producto) {
        if (producto == null) return;

        for (ItemVentaUI item : modeloDetalle.getProductos()) {
            if (item.getProducto() != null && item.getProducto().getCodigo() == producto.getCodigo()) {
                item.setCantidad(item.getCantidad() + 1); // suma 1
                modeloDetalle.fireTableDataChanged();
                return;
            }
        }
        // 2️⃣ Si NO existe → usar fila vacía
        for (ItemVentaUI item : modeloDetalle.getProductos()) {
            if (item.isVacio()) {
                item.setProducto(producto);
                item.setCantidad(1);
                item.setPrecioUnitario(producto.getPrecioVenta());

                modeloDetalle.agregarFilaVacia();
                modeloDetalle.fireTableDataChanged();

                return;
            }
        }
        //modeloDetalle.addProducto(new ItemVentaUI(producto, 1));
    }

    public JPanel configurarPanelDetalleVenta() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabla de detalle de venta
        modeloDetalle = new ModeloTablaVentas(productoService);
        tablaDetalle = new JTable(modeloDetalle);

        JTextField editorField = new JTextField();

        tablaDetalle.getColumnModel()
                .getColumn(0)
                .setCellEditor(new DefaultCellEditor(new JTextField()) {

                    private int editingRow = -1;

                    @Override
                    public java.awt.Component getTableCellEditorComponent(
                            JTable table, Object value, boolean isSelected, int row, int column) {

                        editingRow = row;   // 👈 guardamos ANTES
                        JTextField field = (JTextField)
                                super.getTableCellEditorComponent(table, value, isSelected, row, column);

                        field.setText("");
                        field.requestFocusInWindow();
                        return field;
                    }

                    @Override
                    public boolean stopCellEditing() {
                        boolean stopped = super.stopCellEditing();

                        SwingUtilities.invokeLater(() -> {

                            int nextRow = editingRow + 1;

                            if (nextRow >= tablaDetalle.getRowCount()) return;

                            tablaDetalle.changeSelection(nextRow, 0, false, false);
                            tablaDetalle.editCellAt(nextRow, 0);
                            tablaDetalle.requestFocusInWindow();
                        });

                        return stopped;
                    }
                });


        panel.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        // Panel inferior para combo y botones
        JPanel panelInferior = new JPanel(new BorderLayout());

        // Combo de método de pago arriba
        comboBoxmetodoPago = new JComboBox<>(MetodoPago.values());
        JPanel panelCombo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCombo.add(new JLabel("Método de Pago:"));
        panelCombo.add(comboBoxmetodoPago);
        panelInferior.add(panelCombo, BorderLayout.NORTH);
//panel total
        lblTotal = new JLabel("Total: $ 0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(lblTotal);

        panelInferior.add(panelTotal, BorderLayout.SOUTH);


        // Botones Confirmar y Borrar centrados
        btnConfirmarCompra = new JButton("Confirmar");
        btnLimpiarCompra = new JButton("Borrar");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.add(btnConfirmarCompra);
        panelBotones.add(btnLimpiarCompra);
        panelInferior.add(panelBotones, BorderLayout.CENTER);

        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    public void agregarListenerDobleClick() {
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tabla.getSelectedRow();
                    if (fila != -1) {
                        Producto producto = modeloTabla.getProductoAt(tabla.convertRowIndexToModel(fila));
                        onProductoSeleccionado(producto);
                    }
                }
            }
        });
    }

    public void configurarEventos() {
        btnConfirmarCompra.addActionListener(e -> {
            confirmarVenta();
        });
        btnLimpiarCompra.addActionListener(e -> {
            modeloDetalle.clear();
        });

        tablaDetalle.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke("DELETE"), "deleteRow");

        tablaDetalle.getActionMap().put("deleteRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaDetalle.getSelectedRow();
                if (row == -1) return;

                int modelRow = tablaDetalle.convertRowIndexToModel(row);
                ItemVentaUI item = modeloDetalle.getProductoAt(modelRow);

                modeloDetalle.removeItem(item);
            }
        });

        modeloDetalle.addTableModelListener(e -> actualizarTotal());

    }

    private void actualizarTotal() {
        double total = 0;

        for (ItemVentaUI item : modeloDetalle.getProductos()) {
            if (!item.isVacio()) {
                total += item.getSubtotal();
            }
        }

        lblTotal.setText(String.format("Total: $ %.2f", total));
    }

    public void confirmarVenta() {
        Venta ventaCreada = super.crearVenta(modeloDetalle, comboBoxmetodoPago);
        if (ventaCreada == null) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar la venta.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Venta Creada con exito",
                    "Confirmacion de Venta",
                    JOptionPane.INFORMATION_MESSAGE);
            modeloDetalle.clear();
            eventBus.publish("ventaCreada", ventaCreada);
        }
        cargarProductos(null);

    }



}
