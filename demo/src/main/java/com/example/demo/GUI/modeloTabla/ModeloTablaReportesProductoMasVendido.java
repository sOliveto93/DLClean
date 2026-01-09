package com.example.demo.GUI.modeloTabla;

import com.example.demo.dto.ProductoMasVendidoDto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaReportesProductoMasVendido extends AbstractTableModel {

    private final String[] columnas= {"Codigo", "Nombre", "Cantidad", "total"};
    private List<ProductoMasVendidoDto> lista;

    public ModeloTablaReportesProductoMasVendido(List<ProductoMasVendidoDto> lista){
        this.lista=lista;
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
//no lo usamos aca
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductoMasVendidoDto p= lista.get(rowIndex);

        switch (columnIndex){
            case 0: return p.getProducto().getCodigo();
            case 1: return p.getProducto().getNombre();
            case 2: return p.getCantidadVendida();
            case 3: return p.getTotalFacturado();
            default: return null;
        }
    }
    public void setProductos(List<ProductoMasVendidoDto> lista){
        this.lista = lista != null ? lista : new ArrayList<>();
        fireTableDataChanged();//actualiza la tabla
    }

    //para no tener que definir comparadores a mano para el tabla row sorter
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Long.class;    // Codigo
            case 1: return String.class;  // Nombre
            case 2: return Long.class;  // Cantidad
            case 3: return Double.class; // Stock
            default: return Object.class;
        }
    }
}
