package com.example.demo.GUI.modeloTabla;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaVentas extends AbstractTableModel {

    private final String[] columnas= {"Codigo", "Nombre", "Precio Unitario", "Cantidad","Subtotal"};
    private List<ItemVentaUI> lista;

    public ModeloTablaVentas(){
        this.lista=new ArrayList<>();
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
        ItemVentaUI item=lista.get(rowIndex);
        switch (columnIndex){
            case 0: return item.getProducto().getCodigo();
            case 1: return item.getProducto().getNombre();
            case 2: return item.getPrecioUnitario();
            case 3: return item.getCantidad();
            case 4 : return item.getSubtotal();
            default: return null;
        }
    }

    public void setProductos(List<ItemVentaUI> lista){
        this.lista = lista != null ? lista : new ArrayList<>();
        fireTableDataChanged();//actualiza la tabla
    }
    public void addProducto(ItemVentaUI item){
        this.lista.add(item);
        fireTableDataChanged();
    }
    public List<ItemVentaUI> getProductos(){
        return this.lista;
    }
    public ItemVentaUI getProductoAt(int rowIndex){
        if(lista == null || rowIndex < 0 ||rowIndex >= lista.size()){
            return null;
        }
        return lista.get(rowIndex);
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 2 || col == 3;
    }
    @Override
    public void setValueAt(Object aValue, int row, int col) {
        if(col == 2) {
            try {
                double precio = Math.max(0, Double.parseDouble(aValue.toString()));
                lista.get(row).setPrecioUnitario(precio);
                fireTableRowsUpdated(row, row);
            } catch (NumberFormatException e) {}
        }

        if(col == 3) {
            try {
                int cantidad = Math.max(1, Integer.parseInt(aValue.toString()));
                lista.get(row).setCantidad(cantidad);
                fireTableRowsUpdated(row, row);
            } catch (NumberFormatException e) {}
        }

    }
}
