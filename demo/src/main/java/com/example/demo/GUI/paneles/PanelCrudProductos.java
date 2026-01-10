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
    JLabel labelProductoPrecio;
    JLabel labelProductoStock;
    JLabel labelProductoCodigo;

    private JTextField txtNombreNuevo;
    private JTextField txtCategoriaNueva;
    private JTextField txtPrecioNuevo;
    private JTextField txtStockNuevo;

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
        labelProductoPrecio.setText(String.valueOf(producto.getPrecioVenta()));
        labelProductoStock.setText(String.valueOf(producto.getStock()));
        //para usarlo internamente luego
        setProductoSeleccionado(producto);
    }

    public JPanel configurarPanelEdicion() {
        JPanel panelDeEdicion = new JPanel();
        panelDeEdicion.setLayout(new BorderLayout());
        panelDeEdicion.setBackground(Color.BLACK);

        JLabel titulo = new JLabel("panel de edicion");
        JPanel panelDatosProducto = new JPanel();
        panelDatosProducto.setLayout(new GridLayout(6, 3, 5, 5));
        panelDatosProducto.setBackground(Color.white);

        labelProductoNombre = new JLabel("-");
        labelProductoCategoria = new JLabel("-");
        labelProductoPrecio = new JLabel("-");
        labelProductoStock = new JLabel("-");
        labelProductoCodigo = new JLabel("-");

        txtNombreNuevo = new JTextField();
        txtCategoriaNueva = new JTextField();
        txtPrecioNuevo = new JTextField();
        txtStockNuevo = new JTextField();


        btnConfirmarCambios = new JButton("confirmar cambios");
        btnCrearProducto= new JButton("Crear Producto");
        btnEliminarProducto=new JButton("Eliminar Producto");

        panelDeEdicion.add(titulo, BorderLayout.NORTH);

        panelDatosProducto.add(new JLabel("codigo:"));
        panelDatosProducto.add(labelProductoCodigo);
        panelDatosProducto.add(new JLabel());

        panelDatosProducto.add(new JLabel("Nombre:"));
        panelDatosProducto.add(labelProductoNombre);
        panelDatosProducto.add(txtNombreNuevo);

        panelDatosProducto.add(new JLabel("Categoria"));
        panelDatosProducto.add(labelProductoCategoria);
        panelDatosProducto.add(txtCategoriaNueva);

        panelDatosProducto.add(new JLabel("Precio"));
        panelDatosProducto.add(labelProductoPrecio);
        panelDatosProducto.add(txtPrecioNuevo);

        panelDatosProducto.add(new JLabel("Stock"));
        panelDatosProducto.add(labelProductoStock);
        panelDatosProducto.add(txtStockNuevo);

        panelDatosProducto.add(btnConfirmarCambios);
        panelDatosProducto.add(btnCrearProducto);
        panelDatosProducto.add(btnEliminarProducto);
        panelDeEdicion.add(panelDatosProducto, BorderLayout.CENTER);

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
        if (!seModifico) {
            JOptionPane.showMessageDialog(this, "No hay cambios para aplicar");
            return false;
        }

        return true;
    }
    public void limpiarCamposEdicion(){
        txtNombreNuevo.setText("");
        txtCategoriaNueva.setText("");
        txtPrecioNuevo.setText("");
        txtStockNuevo.setText("");
    }
    public void actualizarLabels(){
        labelProductoCodigo.setText("-");
        labelProductoNombre.setText("-");
        labelProductoCategoria.setText("-");
        labelProductoPrecio.setText("-");
        labelProductoStock.setText("-");

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
