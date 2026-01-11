package com.example.demo.GUI.paneles;

import com.example.demo.Enum.EstadoCaja;
import com.example.demo.GUI.base.BasePanel;

import com.example.demo.GUI.listener.EventBus;
import com.example.demo.GUI.modeloTabla.ModeloTablaCajas;
import com.example.demo.dto.CajaDto;
import com.example.demo.dto.TotalesCajaDto;
import com.example.demo.entity.Caja;

import com.example.demo.service.CajaService;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@Component
public class PanelGestionCaja extends BasePanel {

    private CajaService cajaService;
    private JButton btnMainPanel;
    JButton btnAbrirCaja;
    JButton btnCerrarCaja;
    JLabel valEfectivoApertura;
    JLabel fechaHoraApertura;
    JLabel valTotalSistema;
    JLabel valEfectivoEsperado;
    JLabel valDiferencia;
    JLabel estadoCaja;
    JLabel observaciones;
    Caja cajaActual;
    Font labelFont = new Font("SansSerif", Font.PLAIN, 13);
    Font valueFont = new Font("SansSerif", Font.BOLD, 13);

    public PanelGestionCaja(CajaService cajaService, EventBus eventBus) {
        super(eventBus);
        this.cajaService = cajaService;
        inicializarComponentes();
    }

    @Override
    protected void inicializarComponentes() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        this.add(configurarPanelHeader(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(configurarPanelEstado(), BorderLayout.NORTH);
        centerPanel.add(configurarPanelListaCajas(), BorderLayout.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);
        this.add(configurarPanelBotones(), BorderLayout.SOUTH);


        configurarListener();
    }

    private JPanel configurarPanelListaCajas() {
        JPanel panel=new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Historial de Cajas"));
        ModeloTablaCajas modelo=new ModeloTablaCajas(cajaService.getLast10());
        JTable tablaCajas=new JTable(modelo);

        tablaCajas.setRowHeight(24);
        tablaCajas.setAutoCreateRowSorter(true);
        tablaCajas.setFillsViewportHeight(true);
        tablaCajas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        panel.add(new JScrollPane(tablaCajas),BorderLayout.CENTER);
        return panel;
    }

    private JPanel configurarPanelHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Cajas", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnMainPanel = new JButton("Inicio");
        panel.add(btnMainPanel, BorderLayout.WEST);
        panel.add(titulo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel configurarPanelEstado() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Caja Actual"));

        JLabel lblFechaHoraApertura = new JLabel("Fecha y hora de apertura:");
        fechaHoraApertura = new JLabel("---");
        JLabel lblEfectivoApertura = new JLabel("Efectivo inicial:");
        valEfectivoApertura = new JLabel("---");
        JLabel lblTotalSistema = new JLabel("Total ventas del sistema:");
        valTotalSistema = new JLabel("---");
        valTotalSistema.setToolTipText(
                "Suma de todas las ventas por todos los medios de pago"
        );
        JLabel lblEfectivoEsperado=new JLabel("Efectivo esperado:");
        valEfectivoEsperado=new JLabel("---");
        valEfectivoEsperado.setToolTipText(
                "Efectivo inicial + ventas en efectivo - retiros"
        );
        JLabel lblDiferencia = new JLabel("Diferencia en caja:");
        valDiferencia = new JLabel("---");
        JLabel lblestadoCaja = new JLabel("Estado de la caja:");
        estadoCaja = new JLabel("---");
        JLabel lblObservaciones=new JLabel("Observaciones:");
        observaciones=new JLabel("---");

        lblFechaHoraApertura.setFont(labelFont);
        fechaHoraApertura.setFont(valueFont);

        lblEfectivoApertura.setFont(labelFont);
        valEfectivoApertura.setFont(valueFont);

        lblTotalSistema.setFont(labelFont);
        valTotalSistema.setFont(valueFont);

        lblEfectivoEsperado.setFont(labelFont);
        valEfectivoEsperado.setFont(valueFont);

        lblDiferencia.setFont(labelFont);
        valDiferencia.setFont(valueFont);

        lblestadoCaja.setFont(labelFont);
        estadoCaja.setFont(valueFont);

        lblObservaciones.setFont(labelFont);
        observaciones.setFont(valueFont);

        panel.add(lblFechaHoraApertura);
        panel.add(fechaHoraApertura);
        panel.add(lblEfectivoApertura);
        panel.add(valEfectivoApertura);
        panel.add(lblTotalSistema);
        panel.add(valTotalSistema);
        panel.add(lblEfectivoEsperado);
        panel.add(valEfectivoEsperado);
        panel.add(lblDiferencia);
        panel.add(valDiferencia);
        panel.add(lblestadoCaja);
        panel.add(estadoCaja);
        panel.add(lblObservaciones);
        panel.add(observaciones);
        return panel;
    }

    private JPanel configurarPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAbrirCaja = new JButton("Abrir Caja");
        btnCerrarCaja = new JButton("Cerrar Caja");

        panel.add(btnAbrirCaja);
        panel.add(btnCerrarCaja);
        return panel;
    }

   private void cerrarCajaDialog() {
       if (cajaActual == null) {
           JOptionPane.showMessageDialog(
                   this,
                   "No existe una caja pendiente de cierre",
                   "Error",
                   JOptionPane.ERROR_MESSAGE
           );
           return;
       }

       JTextField efectivoField = new JTextField();
       JTextArea observacionesArea = new JTextArea(5, 20);
       observacionesArea.setLineWrap(true);
       observacionesArea.setWrapStyleWord(true);

       JPanel panel = new JPanel(new BorderLayout(5, 5));
       panel.add(new JLabel("Efectivo en caja:"), BorderLayout.NORTH);
       panel.add(efectivoField, BorderLayout.CENTER);
       panel.add(new JLabel("Observaciones:"), BorderLayout.SOUTH);

       JPanel obsPanel = new JPanel(new BorderLayout());
       obsPanel.add(panel, BorderLayout.NORTH);
       obsPanel.add(new JScrollPane(observacionesArea), BorderLayout.CENTER);

       int result = JOptionPane.showConfirmDialog(
               this,
               obsPanel,
               "Cerrar Caja",
               JOptionPane.OK_CANCEL_OPTION,
               JOptionPane.QUESTION_MESSAGE
       );

       if (result != JOptionPane.OK_OPTION) return;

       double efectivoCaja;
       try {
           efectivoCaja = Double.parseDouble(efectivoField.getText());
           if (efectivoCaja < 0) {
               throw new NumberFormatException();
           }
       } catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(
                   this,
                   "Ingrese un monto válido",
                   "Error",
                   JOptionPane.ERROR_MESSAGE
           );
           return;
       }

       String observaciones = observacionesArea.getText();

       CajaDto dto = new CajaDto(
               cajaActual.getId(),
               efectivoCaja,
               observaciones
       );
       if (observaciones.isEmpty()) {
           int confirm = JOptionPane.showConfirmDialog(
                   this,
                   "No ingresó observaciones.\n¿Desea cerrar la caja igualmente?",
                   "Confirmar cierre",
                   JOptionPane.YES_NO_OPTION,
                   JOptionPane.WARNING_MESSAGE
           );

           if (confirm != JOptionPane.YES_OPTION) return;
       }
       cajaActual=cajaService.cerrarCaja(dto);
       actualizarLabels();
       actualizarBotones();
       if (cajaActual.getDiferencia() != 0) {
           JOptionPane.showMessageDialog(
                   this,
                   "⚠ Se detecto una diferencia de "
                           + cajaActual.getDiferencia(),
                   "Cierre de caja",
                   JOptionPane.WARNING_MESSAGE
           );
       }

   }

    private void abrirCajaDialog() {
        String input = JOptionPane.showInputDialog(
                this,
                "Ingrese el efectivo inicial para abrir la caja:",
                "Abrir Caja",
                JOptionPane.QUESTION_MESSAGE
        );
        if (input == null) return;
        double efectivoInicial;
        try {
            efectivoInicial = Double.parseDouble(input);
            if (efectivoInicial < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "El efectivo inicial no puede ser negativo",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un valor numérico válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        Caja nuevaCaja = cajaService.abrirCaja(efectivoInicial);
        cajaActual=nuevaCaja;
        actualizarLabels();
        actualizarBotones();
    }


    private void configurarListener() {
        btnMainPanel.addActionListener((e) -> eventBus.publish("mainPanel"));
        btnAbrirCaja.addActionListener(e -> abrirCajaDialog());
        btnCerrarCaja.addActionListener(e -> cerrarCajaDialog());
    }

    @Override
    public void init() {
        Optional<Caja> op = cajaService.getUltimaCaja();
        if (op.isPresent()) {
            cajaActual = op.get();
            actualizarLabels();
            actualizarBotones();

        } else {
            limpiarLabels();
        }

    }

    private void limpiarLabels() {
        valEfectivoApertura.setText("---");
        fechaHoraApertura.setText("---");
        estadoCaja.setText("---");
        valTotalSistema.setText("---");
        valEfectivoEsperado.setText("---");
        valDiferencia.setText("---");
    }

    private void actualizarLabels() {
        if (cajaActual == null) {
            limpiarLabels();
            return;
        }
        valEfectivoApertura.setText(String.valueOf(cajaActual.getEfectivoApertura()));
        fechaHoraApertura.setText(String.valueOf(cajaActual.getFechaHoraApertura()));
        estadoCaja.setText(String.valueOf(cajaActual.getEstado()));
        valTotalSistema.setText(String.valueOf(cajaActual.getDineroTotal()));
        valEfectivoEsperado.setText(String.valueOf(cajaActual.getEfectivoEsperado()));
        observaciones.setText(String.valueOf(cajaActual.getObservaciones()));
        if(cajaActual.getDiferencia() < 0){
            valDiferencia.setForeground(Color.RED);
        }else{
            valDiferencia.setForeground(Color.GREEN);
        }
        valDiferencia.setText(String.valueOf(cajaActual.getDiferencia()));
    }

    private void actualizarBotones() {
        boolean cajaAbierta = cajaActual != null && cajaActual.getEstado() == EstadoCaja.ABIERTA;
        btnAbrirCaja.setEnabled(!cajaAbierta);
        btnCerrarCaja.setEnabled(cajaAbierta);
    }
}
