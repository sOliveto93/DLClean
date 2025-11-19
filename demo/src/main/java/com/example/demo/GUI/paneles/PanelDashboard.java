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
        this.setPreferredSize(new Dimension(300, 300));
        this.setBackground(new Color(45, 45, 45, 150));

        setLayout(new GridLayout(7, 5, 3, 3)); // 35 celdas
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
        this.removeAll();

        cargarMapa(ventas);

        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.minusDays(30);

        for (LocalDate d = inicio; !d.isAfter(hoy); d = d.plusDays(1)) {
            double total = mapVentas.getOrDefault(d, 0.0);
            this.add(new CeldaDashBoard(d, total));
        }

        this.revalidate();
        this.repaint();
    }
}
