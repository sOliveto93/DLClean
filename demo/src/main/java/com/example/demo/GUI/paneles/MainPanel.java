package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.config.Config;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MainPanel extends BasePanel {
    JButton botonCrudProducto,botonProductos,botonVentaRapida, botonReporteMasVendido,botonReporteVentas;
    Image backGround;
    Config config=Config.getInstance("config/config.properties");
    VentaService ventaService;
    ProductoService ps;
    public MainPanel(EventBus eventBus, VentaService ventaService, ProductoService ps){
        super(eventBus);
        this.ventaService=ventaService;
    this.ps=ps;

        String rutaImagen = config.get("backgroundImage");
        try {
            backGround = ImageIO.read(new File(rutaImagen));
        } catch (IOException e) {
            backGround = null; // fallback si no se encuentra la imagen
            System.err.println("No se pudo cargar la imagen de fondo: " + rutaImagen);
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
        titulo.setForeground(Color.WHITE); // que contraste con el fondo oscuro
        this.add(titulo, BorderLayout.NORTH);

        // Creamos botones
        botonCrudProducto = crearBoton("CRUD PRODUCTOS", new Color(100, 181, 246)); // azul pastel
        botonProductos = crearBoton("Inventario", new Color(129, 199, 132));       // verde pastel
        botonVentaRapida = crearBoton("Venta Rápida", new Color(255, 224, 130));  // amarillo pastel
        botonReporteMasVendido = crearBoton("Reportes Mas Vendidos", new Color(239, 154, 154)); // rojo pastel
        botonReporteVentas = crearBoton("Reporte Ventas", new Color(186, 104, 200)); // morado pastel


        // Asociamos acciones
        botonProductos.addActionListener(
                (e)->eventBus.publish("panelProductos"));
        botonCrudProducto.addActionListener((e)->eventBus.publish("panelCrudProductos"));
        botonVentaRapida.addActionListener(e->eventBus.publish("panelVentaRapida"));
        botonReporteMasVendido.addActionListener((e)->eventBus.publish("panelReporteMasVendido"));
        botonReporteVentas.addActionListener((e)->eventBus.publish("panelReporteVentas"));

        // Panel contenedor transparente
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setOpaque(false);

        panelBotones.add(botonProductos);
        panelBotones.add(botonCrudProducto);
        panelBotones.add(botonVentaRapida);
        panelBotones.add(botonReporteMasVendido);
        panelBotones.add(botonReporteVentas);

        this.add(panelBotones, BorderLayout.NORTH);
        JPanel panelDashBoard = inicializarDashboard(ventaService.getAll());
        this.add(panelDashBoard,BorderLayout.CENTER);

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

        panel.add(new PanelDashboard(ventas), gbc);

        return panel;
    }


    /**
     * Crea un botón con estilo moderno
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        //boton.setPreferredSize(new Dimension(200, 60)); // ancho x alto
        boton.setMinimumSize(new Dimension(200, 60));
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
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
