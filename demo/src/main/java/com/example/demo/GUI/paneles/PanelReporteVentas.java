package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.GUI.modals.VentanaDetalleVenta;
import com.example.demo.GUI.modeloTabla.ModeloTablaReporteVenta;
import com.example.demo.entity.DetalleVenta;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.service.DetalleVentaService;
import com.example.demo.service.VentaService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PanelReporteVentas extends BasePanel {
    private VentaService vs;
    private DetalleVentaService dvr;
    private List<Venta> listaReporte;
    private ModeloTablaReporteVenta modelo;
    private JTable tablaDetalle;
    private JButton btnMainPanel;
    private double total;
    JLabel totalLabel;
    public PanelReporteVentas(VentaService vs, DetalleVentaService dvr, EventBus eventBus){
        super(eventBus);
        this.vs=vs;
        this.dvr=dvr;
        listaReporte=new ArrayList<>();
        inicializarComponentes();
    }


     @Override
    protected void inicializarComponentes() {
        this.setLayout(new BorderLayout());

        // 1. Inicializar la lista de ventas
        listaReporte = vs.getByFecha(LocalDate.now());
        if (listaReporte == null) listaReporte = new ArrayList<>();

        // 2. Inicializar la tabla y el modelo con la lista
        configurarTabla();

        // 3. Paneles
        JPanel panelHeader = configurarPanelHeader();
        JPanel panelFooter = configurarPanelFooter(); // totalLabel ya inicializado
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        this.add(panelHeader, BorderLayout.NORTH);
        this.add(panelCentral, BorderLayout.CENTER);
        this.add(panelFooter, BorderLayout.SOUTH);

        // 4. Setea los productos en el modelo y recalcula total
        if (modelo != null) {
            modelo.setProductos(listaReporte);
        }
        recalcularTotal(listaReporte);
    }



    public void configurarTabla() {
        modelo = new ModeloTablaReporteVenta(listaReporte);
        tablaDetalle = new JTable(modelo);
        TableRowSorter<ModeloTablaReporteVenta> sorter = new TableRowSorter<>(modelo);
        tablaDetalle.setRowSorter(sorter);
        agregarListenerDobleClick();
    }

    public JPanel configurarPanelHeader() {
        JPanel panel = new JPanel(new BorderLayout());

        btnMainPanel = new JButton("Inicio");
        btnMainPanel.addActionListener(e -> eventBus.publish("mainPanel"));
        panel.add(btnMainPanel, BorderLayout.WEST);

        JSpinner spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd"));


        JButton btnFiltrar = new JButton("Filtrar");

        btnFiltrar.addActionListener(e -> {
            try {
                LocalDate fecha = null;


                if (spinnerFecha.getValue() != null) {
                    Date dInicio = (Date) spinnerFecha.getValue();
                    fecha = dInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }

                List<Venta> ventasFiltradas = vs.getByFecha(fecha);
                modelo.setProductos(ventasFiltradas);
                recalcularTotal(ventasFiltradas);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        JPanel panelFiltros = new JPanel();
        panelFiltros.add(new JLabel("Inicio:"));
        panelFiltros.add(spinnerFecha);

        panelFiltros.add(btnFiltrar);

        panel.add(panelFiltros, BorderLayout.CENTER);

        return panel;
    }
    public JPanel configurarPanelFooter(){
        JPanel panel=new JPanel();

        JLabel label=new JLabel("Total acumulado: ");

        totalLabel=new JLabel(String.valueOf(getTotal()));
        panel.add(label);
        panel.add(totalLabel);
        return panel;
    }
    public void recalcularTotal(List<Venta> lista){
        total = 0;
        if (lista != null) {
            for(Venta v: lista){
                if (v != null) total += v.getTotal();
            }
        }
        setTotal(total);
        if(totalLabel != null){
            totalLabel.setText(String.valueOf(getTotal()));
        }
    }


    public void setTotal(double total) {
        this.total = total;
    }
    public double getTotal(){
        return this.total;
    }

    public void agregarListenerDobleClick(){
        tablaDetalle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    int fila = tablaDetalle.getSelectedRow();
                    if(fila != -1){
                        Venta venta = modelo.getVentaAt(tablaDetalle.convertRowIndexToModel(fila));
                        onVentaSeleccionado(venta);
                    }
                }
            }
        });
    }
    public void onVentaSeleccionado(Venta venta){
        List<DetalleVenta> detalles = dvr.getDetallesByIdVenta(venta.getId());
        JFrame ventanaPadre=(JFrame) SwingUtilities.getWindowAncestor(this);
        new VentanaDetalleVenta(ventanaPadre, venta, detalles);
    }

}