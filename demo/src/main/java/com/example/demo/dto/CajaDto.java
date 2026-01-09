package com.example.demo.dto;

import java.util.Objects;

public class CajaDto {

    private Long id;
    private double efectivoReal;
    private String observaciones;

    public CajaDto(){}

    public CajaDto(Long id, double efectivoReal,String observaciones){
        this.id=id;
        this.efectivoReal=efectivoReal;
        this.observaciones=observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public double getEfectivoReal() {
        return efectivoReal;
    }

    public void setEfectivoReal(double efectivoReal) {
        this.efectivoReal = efectivoReal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CajaDto cajaDto = (CajaDto) o;
        return Objects.equals(id, cajaDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CajaDto{" +
                "id=" + id +
                ", efectivoReal=" + efectivoReal +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
