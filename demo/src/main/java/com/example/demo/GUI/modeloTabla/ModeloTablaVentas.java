package com.example.demo.GUI.modeloTabla;

import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModeloTablaVentas extends AbstractTableModel {

    private final String[] columnas= {"Codigo", "Nombre", "Precio Unitario", "Cantidad","Subtotal"};
    private final List<ItemVentaUI> lista;
    private final ProductoService productoService;

    public ModeloTablaVentas(ProductoService productoService){
        this.productoService = productoService;
        this.lista = new ArrayList<>();
        agregarFilaVacia();

    }

    public void agregarFilaVacia() {
        lista.add(new ItemVentaUI(null, 0));
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
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemVentaUI item = lista.get(rowIndex);
        switch (columnIndex){
            case 0: return item.getProducto() != null ? item.getProducto().getCodigoBarra() : "";
            case 1: return item.getProducto() != null ? item.getProducto().getNombre() : "";
            case 2: return item.getPrecioUnitario();
            case 3: return item.getCantidad();
            case 4: return item.getSubtotal();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 0 || col == 2 || col == 3;
    }

    @Override
    public void setValueAt(Object aValue, int row, int col) {

        ItemVentaUI item = lista.get(row);

        // 🚫 fila vacía: solo se permite editar código
        if (item.isVacio() && col != 0) {
            return;
        }

        // =========================
        // CÓDIGO
        // =========================
        if (col == 0) {
            String codigo = aValue.toString().trim();
            if (codigo.isEmpty()) return;

            Optional<Producto> productoOpt = productoService.getByCodigoBarra(codigo);
            if (!productoOpt.isPresent()) return;

            Producto p = productoOpt.get();

            // 🛑 mismo producto en la misma fila
            if (!item.isVacio() && item.getProducto().equals(p)) {
                return;
            }

            // 🔍 buscar en otras filas
            for (int i = 0; i < lista.size(); i++) {
                ItemVentaUI it = lista.get(i);
                if (!it.isVacio() && it.getProducto().equals(p)) {
                    it.setCantidad(it.getCantidad() + 1);
                    fireTableRowsUpdated(i, i);
                    return;
                }
            }

            // 🆕 crear nuevo
            item.setProducto(p);
            item.setPrecioUnitario(p.getPrecioVenta());
            item.setCantidad(1);

            if (row == lista.size() - 1) {
                agregarFilaVacia();
                fireTableDataChanged();
            } else {
                fireTableRowsUpdated(row, row);
            }
            return;
        }

        // =========================
        // PRECIO UNITARIO
        // =========================
        if (col == 2) {
            try {
                double precio = Double.parseDouble(aValue.toString());
                if (precio <= 0) return;

                item.setPrecioUnitario(precio);
                fireTableRowsUpdated(row, row);
            } catch (NumberFormatException ignored) {}
            return;
        }

        // =========================
        // CANTIDAD
        // =========================
        if (col == 3) {
            try {
                int cantidad = Integer.parseInt(aValue.toString());
                if (cantidad <= 0) return;

                item.setCantidad(cantidad);
                fireTableRowsUpdated(row, row);
            } catch (NumberFormatException ignored) {}
        }
    }

    public void addProducto(ItemVentaUI item){
        lista.add(item);
        fireTableRowsInserted(lista.size() - 1, lista.size() - 1);
    }

    public void setProductos(List<ItemVentaUI> nuevaLista){
        lista.clear();
        if (nuevaLista != null) lista.addAll(nuevaLista);
        fireTableDataChanged();
    }

    public List<ItemVentaUI> getProductos(){
        return lista;
    }

    public ItemVentaUI getProductoAt(int rowIndex){
        if (rowIndex < 0 || rowIndex >= lista.size()) return null;
        return lista.get(rowIndex);
    }

    /** Borra un item de la lista y refresca la tabla */
    public void removeRow(int row) {
        if (row >= 0 && row < lista.size()) {
            lista.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    public void clear() {
        lista.clear();
        agregarFilaVacia();
        fireTableDataChanged();
    }
}
