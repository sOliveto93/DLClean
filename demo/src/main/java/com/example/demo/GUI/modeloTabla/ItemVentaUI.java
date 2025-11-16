package com.example.demo.GUI.modeloTabla;

import com.example.demo.entity.Producto;

public class ItemVentaUI {
    private Producto producto;
    private int cantidad;
    private double precioUnitario; // ← NUEVO campo editable
    public ItemVentaUI(Producto producto,int cantidad){
        this.producto=producto;
        this.cantidad=cantidad;
        this.precioUnitario = producto.getPrecio();//valor por defecto
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    @Override
    public String toString() {
        return "ItemVentaUI{" +
                "producto=" + producto +
                ", cantidad=" + cantidad +
                '}';
    }
}
