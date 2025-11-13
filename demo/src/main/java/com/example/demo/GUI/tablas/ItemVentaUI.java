package com.example.demo.GUI.tablas;

import com.example.demo.entity.Producto;

public class ItemVentaUI {
    private Producto producto;
    private int cantidad;

    public ItemVentaUI(Producto producto,int cantidad){
        this.producto=producto;
        this.cantidad=cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}
