package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.CeldaDashBoard;
import com.example.demo.entity.Venta;
import net.bytebuddy.asm.Advice;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelDashboard extends JPanel {
   Map<LocalDate,Double>mapVentas;
    public PanelDashboard(List<Venta> ventas) {
        mapVentas=new HashMap<LocalDate,Double>();
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
    }
    public void cargarMapa(List<Venta> ventas){
        for(Venta v:ventas){
            LocalDate fecha= LocalDate.from(v.getFecha());
            double total=v.getTotal();
            mapVentas.merge(fecha,total,(viejo,nuevo)->viejo+nuevo);
        }

    }



}
