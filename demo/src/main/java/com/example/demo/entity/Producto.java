package com.example.demo.entity;

import com.example.demo.Enum.Categoria;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long codigo;
    private String nombre;
    private String descripcion;
    private double precioVenta;
    private double precioCosto;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private int stock;
    private boolean eliminado;
    private LocalDate fechaCreacion;
    private int stockMinimo;
    private int stockMaximo;
    @Column(length = 50) // suficiente para cualquier código lineal
    private String codigoBarra;
    private String urlImage;
    public Producto(){}

    public Producto(Long id, long codigo, String nombre,String descripcion, double precioVenta, double precioCosto, Categoria categoria, int stock, boolean eliminado, LocalDate fechaCreacion, int stockMinimo, int stockMaximo,String codigoBarra,String urlImage) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion=descripcion;
        this.precioVenta = precioVenta;
        this.precioCosto = precioCosto;
        this.categoria = categoria;
        this.stock = stock;
        this.eliminado = eliminado;
        this.fechaCreacion = fechaCreacion;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
       this.codigoBarra=codigoBarra;
       this.urlImage=urlImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(double precioCosto) {
        this.precioCosto = precioCosto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return codigo == producto.codigo;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(codigo);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precioVenta=" + precioVenta +
                ", precioCosto=" + precioCosto +
                ", categoria=" + categoria +
                ", stock=" + stock +
                ", eliminado=" + eliminado +
                ", fechaCreacion=" + fechaCreacion +
                ", stockMinimo=" + stockMinimo +
                ", stockMaximo=" + stockMaximo +
                ", codigoBarra='" + codigoBarra + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }

    /**
     * Calcula el porcentaje de ganancia sobre el costo actual.
     * Retorna 0 si el costo es 0 para evitar división por cero.
     */
    public double getPorcentajeGanancia() {
        if (precioCosto == 0) return 0;
        return ((precioVenta - precioCosto) / precioCosto) * 100;
    }
}
