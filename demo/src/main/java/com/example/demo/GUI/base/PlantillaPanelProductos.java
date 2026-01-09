package com.example.demo.GUI.base;

import com.example.demo.Enum.MetodoPago;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.GUI.modeloTabla.ItemVentaUI;
import com.example.demo.GUI.modeloTabla.ModeloTablaProductos;
import com.example.demo.GUI.modeloTabla.ModeloTablaVentas;
import com.example.demo.dto.ProductoDto;
import com.example.demo.dto.VentaDto;
import com.example.demo.entity.DetalleVenta;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.exception.ProductoDuplicadoException;
import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

public abstract class PlantillaPanelProductos extends BasePanel {
    protected JTable tabla;
    protected ModeloTablaProductos modeloTabla;
    protected final ProductoService productoService;
    protected final VentaService ventaService;
    protected JPanel panelCentral;

    public PlantillaPanelProductos(ProductoService productoService,VentaService ventaService,EventBus eventBus){
        super(eventBus);
        this.productoService = productoService;
        this.ventaService=ventaService;
        panelCentral = new JPanel(new BorderLayout());

    }
    protected abstract void inicializarComponentes();

    protected void configurarTabla() {
        modeloTabla = new ModeloTablaProductos(null);
        tabla = new JTable(modeloTabla);
        tabla.setPreferredScrollableViewportSize(new Dimension(800, 400));
        tabla.setFont(new Font("SansSerif", Font.BOLD, 16));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        ZebraTableCellRenderer zebra = new ZebraTableCellRenderer();

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(zebra);
        }
        tabla.getColumnModel()
                .getColumn(3)
                .setCellRenderer(new RendererStock());


        TableRowSorter<ModeloTablaProductos> sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        panelCentral.add(new JScrollPane(tabla), BorderLayout.CENTER);

    }


    protected void cargarProductos(String busqueda) {
        List<Producto> lista = new ArrayList<>();
        if (busqueda == null || busqueda.isEmpty()) {
            lista = productoService.getAll();
        } else {
            if (isNumero(busqueda)) {
                Optional<Producto> optional = getByCodigo(busqueda);
                if (optional.isPresent()) {
                    lista.add(optional.get());
                }
            } else {
                Optional<List<Producto>> optional = productoService.getByNameIgnoreCase(busqueda);
                if (optional.isPresent()) {
                    lista = optional.get();
                }
            }
        }
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron productos para la búsqueda.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

        modeloTabla.setProductos(lista);
    }
    protected boolean isNumero(String data) {
        try {
            Double.parseDouble(data);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    protected abstract void onProductoSeleccionado(Producto producto);

    public boolean modificarProducto(Producto producto){
        return productoService.updateProducto(producto);
    }
    public Producto crearProducto(ProductoDto dto){
        try {
            return productoService.create(dto);
        } catch (ProductoDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean eliminarProducto(Producto producto){
        return productoService.deleteByCodigo(producto.getCodigo());
    }
    public Optional<Producto> getByCodigo(String codigo){
        return productoService.getByCodigo(Long.parseLong(codigo));
    }
    public void agregarPanelPlantilla(String align){
        this.add(panelCentral, align);
    }

    protected void agregarListenerSeleccionParaLista() {
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    Producto producto = modeloTabla.getProductoAt(tabla.convertRowIndexToModel(fila));
                    onProductoSeleccionado(producto);
                }
            }
        });
    }
    protected Venta crearVenta(ModeloTablaVentas modeloDetalle, JComboBox<MetodoPago> metodoPago) {

        List<DetalleVenta> detalles = new ArrayList<>();
        double total = 0;

        for (ItemVentaUI item : modeloDetalle.getProductos()) {
            if(item.getProducto()!=null) {
                DetalleVenta d = new DetalleVenta();
                d.setProducto(item.getProducto());
                d.setCantidad(item.getCantidad());
                d.setPrecioUnitario(
                        item.getPrecioUnitario() > 0 ?
                                item.getPrecioUnitario() :
                                item.getProducto().getPrecioVenta()
                );

                detalles.add(d);
                total += d.getCantidad() * d.getPrecioUnitario();
            }
        }

        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en la venta");
            return null;
        }

        VentaDto ventaDto = VentaDto.builder()
                .setFecha(LocalDateTime.now())
                .setTotal(total)
                .setMetodoPago((MetodoPago) metodoPago.getSelectedItem())
                .setDetalles(detalles) // ← MUY IMPORTANTE
                .build();

        return ventaService.create(ventaDto);

    }
    protected JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField txtBuscar = new JTextField(15);
        JButton btnListar = new JButton("Listar");

        btnListar.addActionListener(e -> {
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) {
                cargarProductos(null);
            } else {
                cargarProductos(texto);
            }
        });
        txtBuscar.addActionListener(e -> btnListar.doClick());


        panel.add(new JLabel("Buscar:"));
        panel.add(txtBuscar);

        panel.add(btnListar);

        return panel;
    }

}
