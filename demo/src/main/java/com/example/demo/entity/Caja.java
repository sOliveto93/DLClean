package com.example.demo.entity;

import com.example.demo.Enum.EstadoCaja;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Caja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaHoraApertura;
    private LocalDateTime fechaHoraCierre;
    private double efectivoApertura;
    private double efectivoCierre;
    private double totalEfectivoSistema;
    private double totalMP;
    private double totalTarjetas;
    private double totalTranferencias;
    private double retiros;
    private String observaciones;
    private EstadoCaja estado;
    private double diferencia;

    protected Caja() {
    }


    public Long getId() {
        return id;
    }



    public LocalDateTime getFechaHoraApertura() {
        return fechaHoraApertura;
    }



    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }



    public double getEfectivoApertura() {
        return efectivoApertura;
    }



    public double getEfectivoCierre() {
        return efectivoCierre;
    }



    public double getTotalEfectivoSistema() {
        return totalEfectivoSistema;
    }


    public double getTotalMP() {
        return totalMP;
    }



    public double getTotalTarjetas() {
        return totalTarjetas;
    }


    public double getTotalTranferencias() {
        return totalTranferencias;
    }


    public double getRetiros() {
        return retiros;
    }


    public String getObservaciones() {
        return observaciones;
    }


    public EstadoCaja getEstado() {
        return estado;
    }

    public double getDiferencia() {
        return efectivoCierre - totalEfectivoSistema;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Caja caja = (Caja) o;
        return Objects.equals(id, caja.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Caja{" +
                "id=" + id +
                ", fechaHoraApertura=" + fechaHoraApertura +
                ", FechaHoraCierre=" + fechaHoraCierre +
                ", efectivoApertura=" + efectivoApertura +
                ", efectivoCierre=" + efectivoCierre +
                ", totalEfectivoSistema=" + totalEfectivoSistema +
                ", totalMP=" + totalMP +
                ", totalTarjetas=" + totalTarjetas +
                ", totalTranferencias=" + totalTranferencias +
                ", retiros=" + retiros +
                ", observaciones='" + observaciones + '\'' +
                ", estado=" + estado +
                '}';
    }
    private void abrir(double efectivoInicial) {
        if (this.estado == EstadoCaja.ABIERTA) {
            throw new IllegalStateException("La caja ya está abierta");
        }
        this.efectivoApertura = efectivoInicial;
        this.fechaHoraApertura = LocalDateTime.now();
        this.estado = EstadoCaja.ABIERTA;
    }
    public void cerrar(double efectivoReal, String observaciones) {
        if (this.estado != EstadoCaja.ABIERTA) {
            throw new IllegalStateException("No se puede cerrar una caja que no está abierta");
        }

        this.efectivoCierre = efectivoReal;
        this.fechaHoraCierre = LocalDateTime.now();
        this.observaciones = observaciones;
        this.diferencia = efectivoReal - this.totalEfectivoSistema;
        this.estado = EstadoCaja.CERRADA;
    }

    public static Caja abrirNueva(double efectivoInicial) {
        Caja caja = new Caja();
        caja.abrir(efectivoInicial);
        return caja;
    }
}
