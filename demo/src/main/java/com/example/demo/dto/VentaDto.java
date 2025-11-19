package com.example.demo.dto;

import com.example.demo.Enum.MetodoPago;
import com.example.demo.entity.DetalleVenta;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

public class VentaDto {

    private final LocalDateTime fecha;
    private final double total;
    private final List<DetalleVenta> detalles;
    private final MetodoPago metodoPago;

    private VentaDto(Builder builder){
        fecha=builder.fecha;
        total=builder.total;
        detalles=builder.detalles;
        metodoPago=builder.metodoPago;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {

        private LocalDateTime fecha;
        private double total;
        private List<DetalleVenta> detalles;

        private MetodoPago metodoPago;

        public Builder setFecha(LocalDateTime fecha) {
            this.fecha = fecha;
            return this;
        }
        public Builder setTotal(double total) {
            this.total=total;
            return this;
        }
        public Builder setDetalles(List<DetalleVenta>detalles) {
            this.detalles=detalles;
            return this;
        }
        public Builder setMetodoPago(MetodoPago metodoPago) {
            this.metodoPago=metodoPago;
            return this;
        }
        public VentaDto build(){
            return new VentaDto(this);
        }
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
}
