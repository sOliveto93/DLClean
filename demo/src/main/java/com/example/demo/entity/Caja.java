package com.example.demo.entity;

import com.example.demo.Enum.EstadoCaja;
import com.example.demo.dto.TotalesCajaDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Caja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHoraApertura;

    private LocalDateTime fechaHoraCierre;
    @Column(nullable = false)
    private double efectivoApertura;

    private double efectivoCierre;

    private double dineroTotal;
    private double totalDineroEfectivo;
    private double totalDineroMP;
    private double totalDineroTarjetas;
    private double totalDineroTransferencias;
    private double retiros;
    private double diferencia;
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCaja estado;
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

    public double getEfectivoEsperado() {
        return efectivoApertura + totalDineroEfectivo - retiros;
    }


    public double getEfectivoCierre() {
        return efectivoCierre;
    }



    public double getDineroTotal() {
        return dineroTotal;
    }
    public double getTotalDineroEfectivo(){
        return totalDineroEfectivo;
    }

    public double getTotalDineroMP() {
        return totalDineroMP;
    }



    public double getTotalDineroTarjetas() {
        return totalDineroTarjetas;
    }


    public double getTotalDineroTransferencias() {
        return totalDineroTransferencias;
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
        return  diferencia;
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
                ", fechaHoraCierre=" + fechaHoraCierre +
                ", efectivoApertura=" + efectivoApertura +
                ", efectivoCierre=" + efectivoCierre +
                ", dineroTotal=" + dineroTotal +
                ", totalDineroEfectivo=" + totalDineroEfectivo +
                ", totalDineroMP=" + totalDineroMP +
                ", totalDineroTarjetas=" + totalDineroTarjetas +
                ", totalDineroTransferencias=" + totalDineroTransferencias +
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
    public void cerrar(
            double efectivoReal,
            TotalesCajaDto totales,
            String observaciones
    ) {
        if (this.estado != EstadoCaja.ABIERTA) {
            throw new IllegalStateException("La caja no está abierta");
        }

        this.efectivoCierre = efectivoReal;
        this.observaciones = observaciones;

        this.totalDineroEfectivo=totales.getTotalEfectivo();
        this.totalDineroMP = totales.getTotalMP();
        this.totalDineroTarjetas = totales.getTotalTarjetas();
        this.totalDineroTransferencias = totales.getTotalTransferencias();

        double efectivoEsperado = this.efectivoApertura + this.totalDineroEfectivo - this.retiros;


        this.dineroTotal =
                totales.getTotalEfectivo()
                        + totales.getTotalMP()
                        + totales.getTotalTarjetas()
                        + totales.getTotalTransferencias();
        this.diferencia = efectivoReal - efectivoEsperado;

        this.estado = EstadoCaja.CERRADA;
        this.fechaHoraCierre = LocalDateTime.now();
    }
    public static Caja abrirNueva(double efectivoInicial) {
        Caja caja = new Caja();
        caja.abrir(efectivoInicial);
        return caja;
    }
}
