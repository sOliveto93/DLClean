package com.example.demo.GUI.paneles;

import com.example.demo.GUI.base.BasePanel;
import com.example.demo.GUI.listener.EventBus;
import com.example.demo.GUI.modeloTabla.ModeloTablaReportesProductoMasVendido;
import com.example.demo.dto.ProductoMasVendidoDto;
import com.example.demo.service.DetalleVentaService;
import com.example.demo.utils.ExportarCsv;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PanelReporteMasVendido extends BasePanel {

    private DetalleVentaService dvs;
    private List<ProductoMasVendidoDto> listaReporte;
    private ModeloTablaReportesProductoMasVendido modelo;

    private JTable tablaDetalle;
    private JButton btnMainPanel;

    public PanelReporteMasVendido(DetalleVentaService detalleVentaService, EventBus eventBus) {
        super(eventBus);

        this.dvs = detalleVentaService;
        listaReporte = new ArrayList<>();
        inicializarComponentes();
    }

    @Override
    protected void inicializarComponentes() {
        this.setLayout(new BorderLayout());

        configurarTabla();
        JPanel panelHeader = configurarPanelHeader();

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        this.add(panelHeader, BorderLayout.NORTH);
        this.add(panelCentral, BorderLayout.CENTER);

        //listaReporte = dvs.getProductosMasVendidos(null, null);
        listaReporte=new ArrayList<>();
        modelo.setProductos(listaReporte);

    }

    public void configurarTabla() {
        modelo = new ModeloTablaReportesProductoMasVendido(listaReporte);
        tablaDetalle = new JTable(modelo);
        TableRowSorter<ModeloTablaReportesProductoMasVendido> sorter = new TableRowSorter<>(modelo);
        tablaDetalle.setRowSorter(sorter);

    }

    public JPanel configurarPanelHeader() {
        JPanel panel = new JPanel(new BorderLayout());

        btnMainPanel = new JButton("Inicio");
        btnMainPanel.addActionListener(e -> eventBus.publish("mainPanel"));
        panel.add(btnMainPanel, BorderLayout.WEST);

        JSpinner spinnerInicio = new JSpinner(new SpinnerDateModel());
        spinnerInicio.setEditor(new JSpinner.DateEditor(spinnerInicio, "yyyy-MM-dd"));

        JSpinner spinnerFin = new JSpinner(new SpinnerDateModel());
        spinnerFin.setEditor(new JSpinner.DateEditor(spinnerFin, "yyyy-MM-dd"));

        JButton btnFiltrar = new JButton("Filtrar");

        btnFiltrar.addActionListener(e -> {
            try {
                LocalDate inicio = null;
                LocalDate fin = null;

                if (spinnerInicio.getValue() != null) {
                    Date dInicio = (Date) spinnerInicio.getValue();
                    inicio = dInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                if (spinnerFin.getValue() != null) {
                    Date dFin = (Date) spinnerFin.getValue();
                    fin = dFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                LocalDateTime inicioDT = inicio != null ? inicio.atStartOfDay() : null;
                LocalDateTime finDT = fin != null ? fin.atTime(23, 59, 59) : null;

                // aca reemplazar para mostrar solo lo filtrado
                listaReporte=dvs.getProductosMasVendidos(inicioDT, finDT);
                modelo.setProductos(listaReporte);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        JButton btnExportar = new JButton("Exportar CSV");

        btnExportar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int opcion = chooser.showSaveDialog(null);

            if (opcion == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        ExportarCsv.exportar(listaReporte, archivo);
                        return null;
                    }

                    @Override
                    protected void done() {
                        JOptionPane.showMessageDialog(null, "Exportación finalizada");
                    }
                };
                worker.execute();
            }

        });
        JPanel panelFiltros = new JPanel();
        panelFiltros.add(new JLabel("Inicio:"));
        panelFiltros.add(spinnerInicio);
        panelFiltros.add(new JLabel("Fin:"));
        panelFiltros.add(spinnerFin);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnExportar);
        panel.add(panelFiltros, BorderLayout.CENTER);

        return panel;
    }

}
