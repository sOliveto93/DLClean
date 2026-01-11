package com.example.demo.GUI.paneles;

import com.example.demo.Enum.Categoria;
import com.example.demo.GUI.base.PlantillaPanelProductos;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.GUI.modals.VentanaCrearProducto;
import com.example.demo.GUI.modals.VentanaEliminarProducto;
import com.example.demo.dto.ProductoDto;
import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


@Component
public class PanelCrudProductos extends PlantillaPanelProductos {

    JLabel labelProductoNombre;
    JLabel labelProductoCategoria;
    JLabel labelProductoPrecioCosto;
    JLabel labelProductoPrecio;
    JLabel labelProductoStock;
    JLabel labelProductoCodigo;
    JLabel labelProductoCodigoBarra;

    private JTextField txtNombreNuevo;
    private JTextField txtCategoriaNueva;
    private JTextField txtPrecioCostoNuevo;
    private JTextField txtPrecioNuevo;
    private JTextField txtStockNuevo;
    private JTextField txtCodigoBarraNuevo;

    private JTextField busqueda;

    private JButton btnConfirmarCambios;
    private JButton btnMainPanel;
    private JButton btnListar;
    private JButton btnBuscar;
    private JButton btnCrearProducto;
    private JButton btnEliminarProducto;

    private Producto productoSeleccionado;

    public PanelCrudProductos(ProductoService productoService,VentaService ventaService, EventBus eventBus) {
        super(productoService,ventaService,eventBus);
        inicializarComponentes();
        productoSeleccionado = null;
    }

    @Override
    protected void inicializarComponentes() {
        this.setLayout(new BorderLayout());
        agregarPanelPlantilla(BorderLayout.CENTER);
        JPanel panelHeader=new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("CRUD PANEL", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnMainPanel=new JButton("Inicio");

        super.configurarTabla();
        configurarSidePanels();
        panelHeader.add(btnMainPanel, BorderLayout.WEST);
        panelHeader.add(titulo, BorderLayout.CENTER);
        panelHeader.add(super.crearPanelBusqueda(), BorderLayout.SOUTH);
        this.add(panelHeader,BorderLayout.NORTH);
        cargarProductos(null);
        agregarListenerSeleccionParaLista();
    }

    @Override
    public void init() {
        System.out.println("hola ----------------------------");
    }

    private void configurarSidePanels() {


        JPanel panelDeEdicion = configurarPanelEdicion();

        this.add(panelDeEdicion, BorderLayout.EAST);
    }

    @Override
    protected void onProductoSeleccionado(Producto producto) {

        labelProductoCodigo.setText(String.valueOf(producto.getCodigo()));
        labelProductoNombre.setText(producto.getNombre());
        labelProductoCategoria.setText(producto.getCategoria().toString());
        labelProductoPrecioCosto.setText(String.valueOf(producto.getPrecioCosto()));
        labelProductoPrecio.setText(String.valueOf(producto.getPrecioVenta()));
        labelProductoStock.setText(String.valueOf(producto.getStock()));
        labelProductoCodigoBarra.setText(producto.getCodigoBarra());
        //para usarlo internamente luego
        setProductoSeleccionado(producto);
    }
    private void agregarFila(JPanel panel, GridBagConstraints gbc, int row,
                             String nombreCampo, JLabel valorActual, JTextField nuevoValor) {

        gbc.gridy = row;

        gbc.gridx = 0;
        panel.add(new JLabel(nombreCampo + ":"), gbc);

        gbc.gridx = 1;
        panel.add(valorActual, gbc);

        gbc.gridx = 2;
        if (nuevoValor != null) {
            panel.add(nuevoValor, gbc);
        } else {
            panel.add(new JLabel(""), gbc);
        }
    }

    public JPanel configurarPanelEdicion() {
        JPanel panelDeEdicion = new JPanel(new BorderLayout(10, 10));
        panelDeEdicion.setPreferredSize(new Dimension(420, 0));
        panelDeEdicion.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Panel de edición", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(Color.DARK_GRAY);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelDeEdicion.add(titulo, BorderLayout.NORTH);

        JPanel panelDatosProducto = new JPanel(new GridBagLayout());
        panelDatosProducto.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // ===== INICIALIZACIÓN =====
        labelProductoCodigo = new JLabel("-");
        labelProductoNombre = new JLabel("-");
        labelProductoCategoria = new JLabel("-");
        labelProductoPrecioCosto = new JLabel("-");
        labelProductoPrecio = new JLabel("-");
        labelProductoStock = new JLabel("-");
        labelProductoCodigoBarra = new JLabel("-");

        txtNombreNuevo = new JTextField();
        txtCategoriaNueva = new JTextField();
        txtPrecioCostoNuevo = new JTextField();
        txtPrecioNuevo = new JTextField();
        txtStockNuevo = new JTextField();
        txtCodigoBarraNuevo = new JTextField();

        // ===== FILAS =====
        agregarFila(panelDatosProducto, gbc, row++, "Código", labelProductoCodigo, null);
        agregarFila(panelDatosProducto, gbc, row++, "Nombre", labelProductoNombre, txtNombreNuevo);
        agregarFila(panelDatosProducto, gbc, row++, "Categoría", labelProductoCategoria, txtCategoriaNueva);
        agregarFila(panelDatosProducto, gbc, row++, "Costo", labelProductoPrecioCosto, txtPrecioCostoNuevo);
        agregarFila(panelDatosProducto, gbc, row++, "Precio", labelProductoPrecio, txtPrecioNuevo);
        agregarFila(panelDatosProducto, gbc, row++, "Stock", labelProductoStock, txtStockNuevo);
        agregarFila(panelDatosProducto, gbc, row++, "Código Barra", labelProductoCodigoBarra, txtCodigoBarraNuevo);

        panelDeEdicion.add(panelDatosProducto, BorderLayout.CENTER);

        // ===== BOTONES =====
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnConfirmarCambios = new JButton("Confirmar cambios");
        btnCrearProducto = new JButton("Crear producto");
        btnEliminarProducto = new JButton("Eliminar producto");

        panelBotones.add(btnConfirmarCambios);
        panelBotones.add(btnCrearProducto);
        panelBotones.add(btnEliminarProducto);

        panelDeEdicion.add(panelBotones, BorderLayout.SOUTH);

        configurarEventos();
        return panelDeEdicion;
    }

    public void setProductoSeleccionado(Producto producto) {
        this.productoSeleccionado = producto;
    }

    public Producto getProductoSeleccionado() {
        return this.productoSeleccionado;
    }

    public boolean validarActualizacion( Producto productoTemporal) {
        boolean seModifico = false;
        if (!txtNombreNuevo.getText().trim().isEmpty()) {
            productoTemporal.setNombre(txtNombreNuevo.getText().trim());
            seModifico = true;
        }
        String nuevaCategoriaTxt = txtCategoriaNueva.getText().trim();
        if (!nuevaCategoriaTxt.isEmpty()) {
            try {
                Categoria nuevaCategoria = Categoria.valueOf(nuevaCategoriaTxt.toUpperCase());
                productoTemporal.setCategoria(nuevaCategoria);
                seModifico = true;
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Categoría inválida: " + nuevaCategoriaTxt);
                return false;
            }
        }

        String nuevoPrecioCosto = txtPrecioCostoNuevo.getText().trim();
        if (!nuevoPrecioCosto.isEmpty()) {
            if (isNumero(nuevoPrecioCosto)) {
                double valor=Double.parseDouble(nuevoPrecioCosto);
                if(valor >= 0){
                    productoTemporal.setPrecioCosto(valor);
                    seModifico = true;
                }
                else {
                    JOptionPane.showMessageDialog(this, "Precio Costo inválido: " + nuevoPrecioCosto);
                    return false;
                }
            }
        }

        String nuevoPrecio = txtPrecioNuevo.getText().trim();
        if (!nuevoPrecio.isEmpty()) {
            if (isNumero(nuevoPrecio)) {
                double valor=Double.parseDouble(nuevoPrecio);
                if(valor >= 0){
                    productoTemporal.setPrecioVenta(valor);
                    seModifico = true;
                }
                else {
                    JOptionPane.showMessageDialog(this, "Precio inválido: " + nuevoPrecio);
                    return false;
                }
            }
        }
        String nuevoStock = txtStockNuevo.getText().trim();
        if (!nuevoStock.isEmpty()) {
            if (isNumero(nuevoStock)) {
                int valor=Integer.parseInt(nuevoStock);
                if(valor >=0) {
                    productoTemporal.setStock(valor);
                    seModifico = true;
                }
                else {
                    JOptionPane.showMessageDialog(this, "Stock inválido: " + nuevoStock);
                    return false;
                }
            }
        }

        String nuevoCodigoBarra = txtCodigoBarraNuevo.getText().trim();
        if (!nuevoCodigoBarra.isEmpty()) {
            Optional<Producto> op = productoService.getByCodigoBarra(nuevoCodigoBarra);

            if (op.isPresent() && !op.get().getId().equals(productoTemporal.getId())) {
                JOptionPane.showMessageDialog(
                        this,
                        "El codigo de barra ya existe"
                );
                return false;
            }

            productoTemporal.setCodigoBarra(nuevoCodigoBarra);
            seModifico = true;
        }


        if (!seModifico) {
            JOptionPane.showMessageDialog(this, "No hay cambios para aplicar");
            return false;
        }

        return true;
    }
    public void limpiarCamposEdicion(){
        txtNombreNuevo.setText("");
        txtCategoriaNueva.setText("");
        txtPrecioCostoNuevo.setText("");
        txtPrecioNuevo.setText("");
        txtStockNuevo.setText("");
        txtCodigoBarraNuevo.setText("");
    }
    public void actualizarLabels(){
        labelProductoCodigo.setText("-");
        labelProductoNombre.setText("-");
        labelProductoCategoria.setText("-");
        labelProductoPrecioCosto.setText("-");
        labelProductoPrecio.setText("-");
        labelProductoStock.setText("-");
        labelProductoCodigoBarra.setText("-");
    }
    public void configurarEventos(){
        btnConfirmarCambios.addActionListener(e -> {
            Producto productoTemporal=getProductoSeleccionado();
            if(productoTemporal == null){
                JOptionPane.showMessageDialog(this, "Debe seleccionar un producto primero");
                return;
            }
            boolean valido = validarActualizacion(productoTemporal);
            if (!valido) return;

            boolean exito = modificarProducto(productoTemporal);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto modificado con éxito");
                cargarProductos(null); // refrescar la tabla
                limpiarCamposEdicion();
                actualizarLabels();
                setProductoSeleccionado(null);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar el producto");
            }
        });
        btnMainPanel.addActionListener((e)->eventBus.publish("mainPanel"));
        btnCrearProducto.addActionListener(e->crearProducto());
        btnEliminarProducto.addActionListener(e->eliminarProducto(labelProductoCodigo.getText()));
    }
    public void crearProducto(){
        JFrame ventanaPadre=(JFrame) SwingUtilities.getWindowAncestor(this);

        VentanaCrearProducto modal=new VentanaCrearProducto(ventanaPadre);
        modal.setVisible(true);

        ProductoDto dto=modal.getProductoCreado();

        if(dto != null){
            Producto guardado=crearProducto(dto);
            if(guardado != null){
                JOptionPane.showMessageDialog(this, "Producto creado con éxito");
                cargarProductos(null);
            }
        }
    }
    public void eliminarProducto(String codigo){
        VentanaEliminarProducto ventana;
        JFrame ventanaPadre=(JFrame) SwingUtilities.getWindowAncestor(this);

        if(codigo.isEmpty() || !isNumero(codigo)){
            JOptionPane.showMessageDialog(this, "Tienes que seleccionar un item de la lista");
        }else{
            Optional<Producto> op= super.getByCodigo(codigo);
            if(op.isPresent()){
                ventana=new VentanaEliminarProducto(ventanaPadre,op.get());
                ventana.setVisible(true);
                Producto productoConfirmado =ventana.getProductoAEliminar();
                //chequeamos que boton apreto
                if(productoConfirmado != null){
                    super.eliminarProducto(productoConfirmado);
                    JOptionPane.showMessageDialog(this, "Producto eliminado: " + productoConfirmado.getNombre());
                    cargarProductos(null);
                    actualizarLabels();
                }else {
                    JOptionPane.showMessageDialog(this, "Operación cancelada");
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto con codigo "+codigo);
            }
        }


    }
}
