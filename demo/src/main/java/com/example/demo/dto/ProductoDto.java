package com.example.demo.dto;

import com.example.demo.Enum.Categoria;

public class ProductoDto {

    private final String nombre;
    private final double precio;
    private final Categoria categoria;
    private final int stock;
    private final long codigo;

    private ProductoDto(Builder builder) {
        this.codigo=builder.codigo;
        this.nombre = builder.nombre;
        this.precio = builder.precio;
        this.categoria = builder.categoria;
        this.stock = builder.stock;
    }
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private long codigo;
        private String nombre;
        private double precio;
        private Categoria categoria;
        private int stock;

        public Builder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder setPrecio(double precio) {
            this.precio = precio;
            return this;
        }
        public Builder setCategoria(Categoria categoria) {
            this.categoria = categoria;
            return this;
        }
        public Builder setCodigo(long codigo){
            this.codigo=codigo;
            return this;
        }

        public Builder setStock(int stock) {
            this.stock = stock;
            return this;
        }
        public ProductoDto build(){
            if (nombre == null || nombre.isEmpty())
                throw new IllegalStateException("El nombre no puede estar vacío");
            if (precio < 0)
                throw new IllegalStateException("El precio no puede ser negativo");
            if (categoria == null)
                throw new IllegalStateException("Debe especificar una categoría");

            return new ProductoDto(this);
        }


    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getStock() {
        return stock;
    }
    public long getCodigo(){
        return codigo;
    }
}
