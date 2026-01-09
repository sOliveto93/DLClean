package com.example.demo.service;

import com.example.demo.Enum.EstadoCaja;
import com.example.demo.dto.CajaDto;
import com.example.demo.entity.Caja;
import com.example.demo.repository.CajaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CajaService {

    CajaRepository cr;

    public CajaService(CajaRepository cr) {
        this.cr = cr;
    }

    public Optional<Caja> getById(Long id){
        return cr.findById(id);
    }
    public List<Caja> getAll(){
        return cr.findAll();
    }
    public Caja abrirCaja(double efectivoInicial) {
        Optional<Caja> ultimaCaja = cr.findTopByOrderByFechaHoraAperturaDesc();
        if(ultimaCaja.isPresent() && ultimaCaja.get().getEstado() == EstadoCaja.ABIERTA){
            return ultimaCaja.get();
        }
        Caja nuevaCaja=Caja.abrirNueva(efectivoInicial);
        return cr.save(nuevaCaja);
    }
    public void cerrarCaja(CajaDto dto){
        Caja caja = getById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Caja no encontrada"));

        caja.cerrar(dto.getEfectivoReal(), dto.getObservaciones());
        cr.save(caja);
        }


}
