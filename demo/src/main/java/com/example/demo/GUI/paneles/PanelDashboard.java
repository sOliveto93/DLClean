package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.base.CeldaDashBoard;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.entity.Venta;
import net.bytebuddy.asm.Advice;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelDashboard extends BasePanel {
    Map<LocalDate, Double> mapVentas;
    List<Venta> ventas;
    JPanel grilla;
    public PanelDashboard(List<Venta> ventas, EventBus eventBus) {
        super(eventBus);
        this.ventas = (ventas == null) ? new ArrayList<>() : ventas;
        suscribirEventos();
        inicializarComponentes();
        refrescarUI();
    }

    public void cargarMapa(List<Venta> ventas) {
        if (ventas == null) return; // nada que cargar
        for (Venta v : ventas) {
            if (v == null || v.getFecha() == null) continue; // ignorar venta inválida
            LocalDate fecha = LocalDate.from(v.getFecha());
            double total = v.getTotal();
            mapVentas.merge(fecha, total, Double::sum);
        }
    }


    /*@Override
    protected void inicializarComponentes() {
        mapVentas = new HashMap<LocalDate, Double>();
        this.setPreferredSize(new Dimension(300, 300));
        this.setBackground(new Color(45, 45, 45, 150));
        cargarMapa(ventas);
        setLayout(new GridLayout(7, 5, 3, 3)); // filas, columnas, hgap, vgap

        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.minusDays(30);

        for (LocalDate d = inicio; !d.isAfter(hoy); d = d.plusDays(1)) {
            double total = mapVentas.getOrDefault(d, 0.0);
            this.add(new CeldaDashBoard(d, total));
        }
    }*/
    @Override
    protected void inicializarComponentes() {
        mapVentas = new HashMap<>();

        setLayout(new BorderLayout());
        setOpaque(false);

        add(crearTitulo(), BorderLayout.NORTH);

       grilla = new JPanel(new GridLayout(7, 5, 6, 6));
        grilla.setOpaque(false);
        grilla.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));

        this.add(grilla, BorderLayout.CENTER);

        //this.setPreferredSize(new Dimension(360, 400));
    }


    @Override
    public void init() {

    }
    private JLabel crearTitulo() {
        JLabel titulo = new JLabel("Ventas — últimos 30 días");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(60, 60, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 12, 8, 12));
        return titulo;
    }
    public void suscribirEventos(){
        eventBus.subscribe("ventaCreada",(data)->{

            agregarVenta(data);
        });
    }
    private void agregarVenta(Venta venta) {
        if (venta == null || venta.getFecha() == null) return;

        ventas.add(venta);
        mapVentas.clear();  // limpio antes de recalcular todo
        refrescarUI();
    }


    private void refrescarUI() {
        grilla.removeAll();

        cargarMapa(ventas);

        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.minusDays(29);

        for (LocalDate d = inicio; !d.isAfter(hoy); d = d.plusDays(1)) {
            double total = mapVentas.getOrDefault(d, 0.0);
            grilla.add(new CeldaDashBoard(d, total));
        }

        grilla.revalidate();
        grilla.repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2.dispose();
    }

}
