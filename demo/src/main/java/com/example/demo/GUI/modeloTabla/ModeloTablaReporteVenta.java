package com.example.demo.GUI.modeloTabla;

import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaReporteVenta extends AbstractTableModel {
    private final String[] columnas= {"id", "metodo pago","total"};
    private List<Venta> lista;

    public ModeloTablaReporteVenta(List<Venta> lista){
        this.lista = lista != null ? lista : new ArrayList<>();
    }


    @Override
    public int getRowCount() {
        return lista != null ? lista.size() : 0;
    }

    @Override
    public String getColumnName(int col){
        return columnas[col];
    }
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Venta v = lista.get(rowIndex);

        switch (columnIndex) {
            case 0: return v.getId();
            case 1: return v.getMetodoPago().name(); // <-- ahora es String
            case 2: return v.getTotal();
            default: return null;
        }
    }

    public void setProductos(List<Venta> lista){
        this.lista = lista != null ? lista : new ArrayList<>();

        fireTableDataChanged();//actualiza la tabla
    }
    //para no tener que definir comparadores a mano para el tabla row sorter
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Long.class;    // Codigo
            case 1: return String.class;  // Nombre
            case 2: return Double.class;  // Precio

            default: return Object.class;
        }
    }
    public Venta getVentaAt(int rowIndex){
        if(lista == null || rowIndex < 0 ||rowIndex >= lista.size()){
            return null;
        }
        return lista.get(rowIndex);
    }
}
