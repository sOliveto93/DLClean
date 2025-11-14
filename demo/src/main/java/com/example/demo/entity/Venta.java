package com.example.demo.entity;

import com.example.demo.Enum.MetodoPago;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private double total;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles;
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;
    public Venta() {
        detalles=new ArrayList<>();
    }

    public Venta(Long id, LocalDateTime fecha, double total, List<DetalleVenta> detalles,MetodoPago metodoPago) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.detalles = detalles;
        this.metodoPago=metodoPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public MetodoPago getMetodoPago(){
        return this.metodoPago;
    }
    public void setMetodoPago(MetodoPago metodoPago){
        this.metodoPago=metodoPago;
    }
    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", total=" + total +
                ", detalles=" + detalles +
                ", metodo de pago=" + metodoPago +
                '}';
    }
}
