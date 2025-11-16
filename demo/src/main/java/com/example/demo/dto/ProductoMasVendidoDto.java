package com.example.demo.dto;

import com.example.demo.entity.Producto;

import java.util.Objects;


public class ProductoMasVendidoDto {
    private Producto producto;
    private long cantidadVendida;
    private double totalFacturado;

    public ProductoMasVendidoDto(Producto producto,long cantidadVendida,double totalFacturado){
        this.producto=producto;
        this.cantidadVendida=cantidadVendida;
        this.totalFacturado=totalFacturado;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(long cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }
    public double getTotalFacturado(){
        return this.totalFacturado;
    }

    public void setTotalFacturado(double totalFacturado) {
        this.totalFacturado = totalFacturado;
    }

    @Override
    public String toString() {
        return "ProductoMasVendidoDto{" +
                "producto=" + producto +
                ", cantidadVendida=" + cantidadVendida +
                ", totalFacturado=" + totalFacturado +
                '}';
    }


}
