package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.entity.Venta;
import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;
import com.example.demo.utils.ImageLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MainPanel extends BasePanel {
    private JButton botonCrudProducto,botonProductos,botonVentaRapida, botonReporteMasVendido,botonReporteVentas,botonGestionCaja;
    private Image backGround,logo;

    private ImageLoader imageLoader;
    private VentaService ventaService;
    private ProductoService ps;

    public MainPanel(EventBus eventBus, VentaService ventaService, ProductoService ps){
        super(eventBus);
        this.ventaService=ventaService;
        this.ps=ps;

        imageLoader=ImageLoader.getInstance();
        backGround=cargarImagenes("backgroundPNG");
        logo=cargarImagenes("logoJPG");

    }
    public BufferedImage cargarImagenes(String key){
        Optional<BufferedImage> op=imageLoader.getImage(key);
        if(op.isPresent()){
            return op.get();
        }
        return null;
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

        JPanel panelBotones =crearBotones();

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
        configurarEventos();
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        JButton[] botones = {
                botonProductos = crearBoton("Inventario", new Color(129,199,132),"impresora1PNG"),
                botonCrudProducto = crearBoton("CRUD<br>Productos", new Color(100,181,246),"presentacion2PNG"),
                botonVentaRapida = crearBoton("Venta<br>Rápida", new Color(255,224,130),"dineroPNG"),
                botonReporteMasVendido = crearBoton("Más<br>Vendidos", new Color(239,154,154),"presentacion1PNG"),
                botonReporteVentas = crearBoton("Reporte<br>Ventas", new Color(186,104,200),"reportesPNG"),
                botonGestionCaja = crearBoton("Gestión<br>Caja", new Color(120,144,156),"chanchitoPNG")
        };

        int col = 0;
        int row = 0;

        for (JButton boton : botones) {
            gbc.gridx = col;
            gbc.gridy = row;
            panel.add(crearBotonFlotante(boton), gbc);


            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }

        return panel;
    }
    private JPanel crearBotonFlotante(JButton boton) {
        final int TOP_BASE = 8;
        final int BOTTOM_BASE = 14;

        final int TOP_HOVER = 4;
        final int BOTTOM_HOVER = 18;

        final int STEP = 1;      // suavidad
        final int DELAY = 15;    // ms (≈ 60 FPS)
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        final int[] top = {TOP_BASE};
        final int[] bottom = {BOTTOM_BASE};

        wrapper.setBorder(
                BorderFactory.createEmptyBorder(top[0], 8, bottom[0], 8)
        );

        JPanel sombra = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 45));
                g2.fillRoundRect(
                        4, 6,
                        getWidth() - 8,
                        getHeight() - 8,
                        16, 16
                );
                g2.dispose();
            }
        };

        sombra.setOpaque(false);
        sombra.setLayout(new BorderLayout());
        sombra.add(boton, BorderLayout.CENTER);

        wrapper.add(sombra, BorderLayout.CENTER);

        Timer[] animacion = new Timer[1];

        Runnable animarHover = () -> {
            if (animacion[0] != null && animacion[0].isRunning())
                animacion[0].stop();

            animacion[0] = new Timer(DELAY, e -> {
                boolean terminado = true;

                if (top[0] > TOP_HOVER) {
                    top[0] -= STEP;
                    terminado = false;
                }
                if (bottom[0] < BOTTOM_HOVER) {
                    bottom[0] += STEP;
                    terminado = false;
                }

                wrapper.setBorder(
                        BorderFactory.createEmptyBorder(top[0], 8, bottom[0], 8)
                );
                wrapper.revalidate();
                wrapper.repaint();

                if (terminado)
                    ((Timer) e.getSource()).stop();
            });
            animacion[0].start();
        };

        Runnable animarSalida = () -> {
            if (animacion[0] != null && animacion[0].isRunning())
                animacion[0].stop();

            animacion[0] = new Timer(DELAY, e -> {
                boolean terminado = true;

                if (top[0] < TOP_BASE) {
                    top[0] += STEP;
                    terminado = false;
                }
                if (bottom[0] > BOTTOM_BASE) {
                    bottom[0] -= STEP;
                    terminado = false;
                }

                wrapper.setBorder(
                        BorderFactory.createEmptyBorder(top[0], 8, bottom[0], 8)
                );
                wrapper.revalidate();
                wrapper.repaint();

                if (terminado)
                    ((Timer) e.getSource()).stop();
            });
            animacion[0].start();
        };

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animarHover.run();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animarSalida.run();
            }
        });

        return wrapper;
    }

    private void configurarEventos() {

        botonProductos.addActionListener(e -> eventBus.publish("panelProductos"));
        botonCrudProducto.addActionListener(e -> eventBus.publish("panelCrudProductos"));
        botonVentaRapida.addActionListener(e -> eventBus.publish("panelVentaRapida"));
        botonReporteMasVendido.addActionListener(e -> eventBus.publish("panelReporteMasVendido"));
        botonReporteVentas.addActionListener(e -> eventBus.publish("panelReporteVentas"));
        botonGestionCaja.addActionListener(e -> eventBus.publish("panelGestionCaja"));

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

        PanelDashboard dashboard = new PanelDashboard(ventas, eventBus);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

// margen externo → separa del fondo
        wrapper.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        wrapper.add(dashboard, BorderLayout.CENTER);

        panel.add(wrapper, gbc);

        return panel;
    }


    /**
     * Crea un botón con estilo moderno
     */
    private JButton crearBoton(String texto, Color color,String keyImg) {
        JButton boton = new JButton("<html><center>"+texto+ "</center></html>");
        boton.setPreferredSize(new Dimension(180, 80));
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        boton.setFocusPainted(false); // elimina el borde de foco
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if(!keyImg.isEmpty()){
            Optional<BufferedImage> op=imageLoader.getImage(keyImg);
            op.ifPresent(img->boton.setIcon(new ImageIcon(img.getScaledInstance(40,40,Image.SCALE_SMOOTH))));
        }

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
            g2.drawImage(logo, this.getWidth()/2-250, this.getHeight()/2-250, 500  ,  500, this);


            g2.dispose();
        }
    }

}
