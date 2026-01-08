package com.example.demo.GUI.modeloTabla;

import com.example.demo.entity.Producto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaProductos extends AbstractTableModel {

    private final String[] columnas= {"Codigo", "Nombre", "Precio", "Stock","Codigo Barra"};
    private List<Producto> lista;
    public ModeloTablaProductos(List<Producto> lista){
        this.lista = lista != null ? lista : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return lista != null ? lista.size() : 0;
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
        Producto p=lista.get(rowIndex);
       switch (columnIndex){
           case 0: return p.getCodigo();
           case 1: return p.getNombre();
           case 2: return p.getPrecioVenta();
           case 3: return p.getStock();
           case 4: return p.getCodigoBarra();
           default: return null;
       }
    }
    @Override
    public String getColumnName(int col){
        return columnas[col];
    }
    //para no tener que definir comparadores a mano
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Long.class;    // Codigo
            case 1: return String.class;  // Nombre
            case 2: return Double.class;  // Precio
            case 3: return Integer.class; // Stock
            case 4: return String.class;
            default: return Object.class;
        }
    }

    public void setProductos(List<Producto> lista){
        this.lista = lista != null ? lista : new ArrayList<>();
        fireTableDataChanged();//actualiza la tabla
    }
    public List<Producto> getProdcutos(){
        return this.lista;
    }
    public Producto getProductoAt(int rowIndex){
        if(lista == null || rowIndex < 0 ||rowIndex >= lista.size()){
            return null;
        }
        return lista.get(rowIndex);
    }
}
