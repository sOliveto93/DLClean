package com.example.demo.GUI.modeloTabla;

import com.example.demo.Enum.EstadoCaja;
import com.example.demo.entity.Caja;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.util.List;

public class ModeloTablaCajas extends AbstractTableModel {

    private List<Caja> lista;
    private String[] columnas = {"ID", "Apertura", "Cierre", "Estado", "Total"};

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public ModeloTablaCajas(List<Caja> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (lista == null || lista.isEmpty() || rowIndex < 0 || rowIndex >= lista.size()) {
            return null;
        }
        Caja c = lista.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return c.getId();
            case 1:
                return c.getFechaHoraApertura();
            case 2:
                return c.getFechaHoraCierre();
            case 3:
                return c.getEstado();
            case 4:
                return c.getDineroTotal();
            default:
                return null;
        }

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;          // ID
            case 1:
                return LocalDateTime.class; // Apertura
            case 2:
                return LocalDateTime.class; // Cierre
            case 3:
                return EstadoCaja.class; // Estado
            case 4:
                return Double.class;        // Total
            default:
                return Object.class;
        }
    }
}