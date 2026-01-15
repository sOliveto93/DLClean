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
import com.example.demo.utils.ImageLoader;
import com.example.demo.utils.Toast;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.util.Optional;


@Component
public class PanelCrudProductos extends PlantillaPanelProductos {

    private JLabel labelProductoNombre;
    private JLabel labelProductoCategoria;
    private JLabel labelProductoPrecioCosto;
    private JLabel labelProductoPrecio;
    private JLabel labelProductoStock;
    private JLabel labelProductoCodigo;
    private JLabel labelProductoCodigoBarra;
    private JLabel labelMarkup;

    private JTextField txtNombreNuevo;
    private JComboBox<Categoria> comboCategoriaNueva;
    private JTextField txtPrecioCostoNuevo;
    private JTextField txtPrecioNuevo;
    private JTextField txtStockNuevo;
    private JTextField txtCodigoBarraNuevo;

    private JTextField busqueda;
    private JCheckBox chkEliminarCodigoBarra;


    private JButton btnConfirmarCambios;
    private JButton btnMainPanel;
    private JButton btnListar;
    private JButton btnBuscar;
    private JButton btnCrearProducto;
    private JButton btnEliminarProducto;


    private Producto productoSeleccionado;

    private ImageLoader imageLoader;
    public PanelCrudProductos(ProductoService productoService,VentaService ventaService, EventBus eventBus) {
        super(productoService,ventaService,eventBus);
        imageLoader=ImageLoader.getInstance();
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
        if(producto.getPrecioCosto() !=0 ){
            double markup=(producto.getPrecioVenta()-producto.getPrecioCosto())/producto.getPrecioCosto()*100;
            labelMarkup.setText(String.format("Markup: %.1f%%",markup));
        }else{
            labelMarkup.setText("");
        }

        comboCategoriaNueva.setSelectedItem(producto.getCategoria());

        chkEliminarCodigoBarra.setEnabled(producto.getCodigoBarra() != null);
        chkEliminarCodigoBarra.setSelected(false);
        txtCodigoBarraNuevo.setEnabled(true);
        txtCodigoBarraNuevo.setText("");


        //para usarlo internamente luego
        setProductoSeleccionado(producto);
    }
    private void agregarCampoVertical(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String nombreCampo,
            JLabel valorActual,
            JComponent nuevoValor
    ) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel campo = new JPanel();
        campo.setLayout(new BoxLayout(campo, BoxLayout.Y_AXIS));
        campo.setBackground(Color.WHITE);

        JLabel label = new JLabel(nombreCampo);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));

        valorActual.setFont(new Font("SansSerif", Font.PLAIN, 12));
        valorActual.setForeground(Color.DARK_GRAY);

        campo.add(label);
        campo.add(Box.createVerticalStrut(2));
        campo.add(valorActual);

        if (nuevoValor != null) {
            campo.add(Box.createVerticalStrut(4));
            nuevoValor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
            campo.add(nuevoValor);
        }

        panel.add(campo, gbc);
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int row,
                             String nombreCampo, JLabel valorActual, java.awt.Component nuevoValor) {

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

        // ===== INICIALIZACIÓN DE LABELS Y CAMPOS =====
        labelProductoCodigo = new JLabel("-");
        labelProductoNombre = new JLabel("-");
        labelProductoCategoria = new JLabel("-");
        labelProductoPrecioCosto = new JLabel("-");
        labelProductoPrecio = new JLabel("-");
        labelProductoStock = new JLabel("-");
        labelProductoCodigoBarra = new JLabel("-");
        labelMarkup = new JLabel("-");

        txtNombreNuevo = new JTextField();
        comboCategoriaNueva = new JComboBox<>(Categoria.values());
        comboCategoriaNueva.setSelectedIndex(-1);
        txtPrecioCostoNuevo = new JTextField();
        txtPrecioNuevo = new JTextField();
        txtStockNuevo = new JTextField();
        txtCodigoBarraNuevo = new JTextField();

        chkEliminarCodigoBarra = new JCheckBox("Eliminar código de barras");
        chkEliminarCodigoBarra.setBackground(Color.WHITE);
        chkEliminarCodigoBarra.setEnabled(false);

        // ===== FILAS =====

        // Código
        agregarCampoVertical(panelDatosProducto, gbc, row++, "Código", labelProductoCodigo, null);

        // Nombre con botón copiar
        gbc.gridy = row++;
        JPanel panelNombre = new JPanel();
        panelNombre.setLayout(new BoxLayout(panelNombre, BoxLayout.X_AXIS));
        panelNombre.setBackground(Color.WHITE);
        JButton btnCopiarNombre = new JButton("");
        Optional<BufferedImage> opNombre=imageLoader.getImage("reportesPNG");
        opNombre.ifPresent(img->btnCopiarNombre.setIcon(new ImageIcon(img.getScaledInstance(16,16,Image.SCALE_SMOOTH))));

        btnCopiarNombre.addActionListener(e -> {
            copiarAlPortapapeles(labelProductoNombre.getText());
            Toast.mostrarToast("Nombre copiado");
        });
        panelNombre.add(labelProductoNombre);
        panelNombre.add(Box.createHorizontalStrut(5));
        panelNombre.add(btnCopiarNombre);
        panelDatosProducto.add(panelNombre, gbc);

        // Nombre editable
        agregarCampoVertical(panelDatosProducto, gbc, row++, "Nombre", new JLabel(""), txtNombreNuevo);

        // Categoría
        agregarCampoVertical(panelDatosProducto, gbc, row++, "Categoría", labelProductoCategoria, comboCategoriaNueva);

        // Costo
        agregarCampoVertical(panelDatosProducto, gbc, row++, "Costo", labelProductoPrecioCosto, txtPrecioCostoNuevo);

        // Markup
        gbc.gridy = row++;
        JPanel markupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        markupPanel.setBackground(Color.WHITE);
        markupPanel.add(labelMarkup);
        panelDatosProducto.add(markupPanel, gbc);

        // Precio
        agregarCampoVertical(panelDatosProducto, gbc, row++, "Precio", labelProductoPrecio, txtPrecioNuevo);

        // Stock
        agregarCampoVertical(panelDatosProducto, gbc, row++, "Stock", labelProductoStock, txtStockNuevo);

        // Código de barras con botón copiar
        gbc.gridy = row++;
        JPanel panelCodigoBarra = new JPanel();
        panelCodigoBarra.setLayout(new BoxLayout(panelCodigoBarra, BoxLayout.X_AXIS));
        panelCodigoBarra.setBackground(Color.WHITE);
        JButton btnCopiarCodigoBarra = new JButton("");

        Optional<BufferedImage> opCodigoBarra=imageLoader.getImage("reportesPNG");
        opCodigoBarra.ifPresent(img->btnCopiarCodigoBarra.setIcon(new ImageIcon(img.getScaledInstance(16,16,Image.SCALE_SMOOTH))));

        btnCopiarCodigoBarra.addActionListener(e -> {
            copiarAlPortapapeles(labelProductoCodigoBarra.getText());
            Toast.mostrarToast("Codigo Barra copiado");
        });
        panelCodigoBarra.add(labelProductoCodigoBarra);
        panelCodigoBarra.add(Box.createHorizontalStrut(5));
        panelCodigoBarra.add(btnCopiarCodigoBarra);
        panelDatosProducto.add(panelCodigoBarra, gbc);

        // Código de barras editable
        agregarCampoVertical(panelDatosProducto, gbc, row++, "Código de barras", new JLabel(""), txtCodigoBarraNuevo);

        // Checkbox eliminar
        gbc.gridy = row++;
        JPanel chkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        chkPanel.setBackground(Color.WHITE);
        chkPanel.add(chkEliminarCodigoBarra);
        panelDatosProducto.add(chkPanel, gbc);

        // Scroll
        JScrollPane scroll = new JScrollPane(panelDatosProducto);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // scroll suave
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);
        panelDeEdicion.add(scroll, BorderLayout.CENTER);

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


    private void copiarAlPortapapeles(String texto) {
        if (texto != null && !texto.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(texto), null);

        } else {
            // limpiar portapapeles
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(""), null);
        }
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
        Categoria nuevaCategoria = (Categoria)comboCategoriaNueva.getSelectedItem();
        if(nuevaCategoria != null && !nuevaCategoria.equals(productoTemporal.getCategoria())){
            productoTemporal.setCategoria(nuevaCategoria);
            seModifico=true;
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
        if (chkEliminarCodigoBarra.isSelected()) {
            productoTemporal.setCodigoBarra(null);
            seModifico = true;
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
        comboCategoriaNueva.setSelectedIndex(-1);
        txtPrecioCostoNuevo.setText("");
        txtPrecioNuevo.setText("");
        txtStockNuevo.setText("");
        txtCodigoBarraNuevo.setText("");
        chkEliminarCodigoBarra.setSelected(false);
        chkEliminarCodigoBarra.setEnabled(false);
    }
    public void actualizarLabels(){
        labelProductoCodigo.setText("-");
        labelProductoNombre.setText("-");
        labelProductoCategoria.setText("-");
        labelProductoPrecioCosto.setText("-");
        labelProductoPrecio.setText("-");
        labelProductoStock.setText("-");
        labelProductoCodigoBarra.setText("-");
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
        chkEliminarCodigoBarra.addActionListener(e -> {
            if (!chkEliminarCodigoBarra.isEnabled()) {
                return;
            }

            boolean eliminar = chkEliminarCodigoBarra.isSelected();
            txtCodigoBarraNuevo.setEnabled(!eliminar);

            if (eliminar) {
                txtCodigoBarraNuevo.setText("");
            }
        });

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
