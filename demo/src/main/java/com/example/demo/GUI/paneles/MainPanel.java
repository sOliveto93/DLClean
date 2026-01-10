package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.config.Config;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;
import com.example.demo.utils.ImageLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MainPanel extends BasePanel {
    private JButton botonCrudProducto,botonProductos,botonVentaRapida, botonReporteMasVendido,botonReporteVentas,botonGestionCaja;
    private Image backGround;

    private ImageLoader imageLoader;
    private VentaService ventaService;
    private ProductoService ps;

    public MainPanel(EventBus eventBus, VentaService ventaService, ProductoService ps){
        super(eventBus);
        this.ventaService=ventaService;
        this.ps=ps;

        imageLoader=ImageLoader.getInstance();
        Optional<BufferedImage> op=imageLoader.getImage("backgroundImageJPG");
        if(op.isPresent()){
            backGround =op.get();
        }
    }

    @PostConstruct
    public void init() {
        inicializarComponentes();
    }
    @Override
    protected void inicializarComponentes() {

        this.setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Bienvenido a DLClean", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        this.add(titulo, BorderLayout.NORTH);

        // Creamos botones
        botonCrudProducto = crearBoton("CRUD PRODUCTOS", new Color(100, 181, 246),"presentacion2PNG");
        botonProductos = crearBoton("Inventario", new Color(129, 199, 132),"impresora1PNG");
        botonVentaRapida = crearBoton("Venta Rápida", new Color(255, 224, 130),"dineroPNG");
        botonReporteMasVendido = crearBoton("Reportes Mas Vendidos", new Color(239, 154, 154),"presentacion1PNG");
        botonReporteVentas = crearBoton("Reporte Ventas", new Color(186, 104, 200),"reportesPNG");
        botonGestionCaja = crearBoton("Gestion Caja", new Color(186, 104, 200),"chanchitoPNG");

        botonProductos.addActionListener(e -> eventBus.publish("panelProductos"));
        botonCrudProducto.addActionListener(e -> eventBus.publish("panelCrudProductos"));
        botonVentaRapida.addActionListener(e -> eventBus.publish("panelVentaRapida"));
        botonReporteMasVendido.addActionListener(e -> eventBus.publish("panelReporteMasVendido"));
        botonReporteVentas.addActionListener(e -> eventBus.publish("panelReporteVentas"));
        botonGestionCaja.addActionListener(e -> eventBus.publish("panelGestionCaja"));

        JPanel panelBotones = new JPanel(new GridLayout(3, 12, 15, 15));
        panelBotones.setOpaque(false);
        panelBotones.add(botonProductos);
        panelBotones.add(botonCrudProducto);
        panelBotones.add(botonVentaRapida);
        panelBotones.add(botonReporteMasVendido);
        panelBotones.add(botonReporteVentas);
        panelBotones.add(botonGestionCaja);

        this.add(panelBotones, BorderLayout.NORTH);

        // Panel contenedor para dashboard y reporte diario
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        //grid para los paneles de abajo

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // margen entre componentes

        JPanel panelTotalDiario=inicializarPanelTotalDiario(ventaService.getByFecha(LocalDate.now()),gbc);
        panelCentral.add(panelTotalDiario,gbc);
        // PanelDashboard en la derecha
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1; // ocupa todo el espacio disponible
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        JPanel panelDashboard = inicializarDashboard(ventaService.getVentaSUltimos30Dias());
        panelCentral.add(panelDashboard, gbc);

        this.add(panelCentral, BorderLayout.CENTER);
    }

    public JPanel inicializarPanelTotalDiario(List<Venta> ventas,GridBagConstraints gbc){
        // PanelReporteVentaDiarioActual en la izquierda
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // ocupa espacio justo
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        PanelReporteVentaDiarioActual panelReporteVentaDiarioActual = new PanelReporteVentaDiarioActual(eventBus,ventas);
        return panelReporteVentaDiarioActual;
    }
    public JPanel inicializarDashboard(List<Venta> ventas) {
        if (ventas == null) ventas = new ArrayList<>();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        // Para que el componente pueda "elegir" la esquina abajo-derecha:
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Anclar a abajo-derecha
        gbc.anchor = GridBagConstraints.SOUTHEAST;

        panel.add(new PanelDashboard(ventas,eventBus), gbc);

        return panel;
    }


    /**
     * Crea un botón con estilo moderno
     */
    private JButton crearBoton(String texto, Color color,String keyImg) {
        JButton boton = new JButton(texto);
        //boton.setPreferredSize(new Dimension(200, 60)); // ancho x alto
        boton.setMinimumSize(new Dimension(200, 60));
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        if(!keyImg.isEmpty()){
            Optional<BufferedImage> op=imageLoader.getImage(keyImg);
            op.ifPresent(img->boton.setIcon(new ImageIcon(img.getScaledInstance(40,40,Image.SCALE_SMOOTH))));
        }
        boton.setFocusPainted(false); // elimina el borde de foco
        boton.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));// padding interno
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Opcional: efecto de hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        return boton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backGround != null) {
            Graphics2D g2 = (Graphics2D) g.create();

            // Habilitar escalado suave
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibujar la imagen escalada al tamaño del panel
            g2.drawImage(backGround, 0, 0, getWidth(), getHeight(), this);

            g2.dispose();
        }
    }

}
