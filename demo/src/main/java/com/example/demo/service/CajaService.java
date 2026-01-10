package com.example.demo.service;

import com.example.demo.Enum.EstadoCaja;
import com.example.demo.Enum.MetodoPago;
import com.example.demo.dto.CajaDto;
import com.example.demo.dto.TotalesCajaDto;
import com.example.demo.entity.Caja;
import com.example.demo.entity.Venta;
import com.example.demo.repository.CajaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CajaService {

    CajaRepository cr;
    VentaService vs;
    public CajaService(CajaRepository cr,VentaService vs) {
        this.cr = cr;
        this.vs=vs;
    }

    public Optional<Caja> getById(Long id){
        return cr.findById(id);
    }
    public List<Caja> getAll(){
        return cr.findAll();
    }
    public Optional<Caja> getUltimaCaja(){
       return  cr.findTopByOrderByFechaHoraAperturaDesc();
    }
    public Caja abrirCaja(double efectivoInicial) {
        Optional<Caja> ultimaCaja =getUltimaCaja();
        if(ultimaCaja.isPresent() && ultimaCaja.get().getEstado() == EstadoCaja.ABIERTA){
            return ultimaCaja.get();
        }
        Caja nuevaCaja=Caja.abrirNueva(efectivoInicial);
        return cr.save(nuevaCaja);
    }
    public Caja cerrarCaja(CajaDto dto){
        Caja caja = getById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Caja no encontrada"));

        caja.cerrar(dto.getEfectivoReal(),calcularTotalesCaja(caja) ,dto.getObservaciones());
        return cr.save(caja);
        }
    public TotalesCajaDto calcularTotalesCaja(Caja caja) {
        LocalDateTime inicio = caja.getFechaHoraApertura();
        LocalDateTime fin = caja.getFechaHoraCierre() != null
                ? caja.getFechaHoraCierre()
                : LocalDateTime.now();

        List<Venta> ventas = vs.getVentasEntreFechas(inicio, fin);

        double efectivo = ventas.stream()
                    .filter(v->v.getMetodoPago() == MetodoPago.EFECTIVO)
                    .mapToDouble(v->v.getTotal())
                    .sum();

        double mp = ventas.stream()
                .filter(v -> v.getMetodoPago() == MetodoPago.MERCADOPAGO)
                .mapToDouble(v->v.getTotal())
                .sum();

        double tarjetas = ventas.stream()
                .filter(v -> v.getMetodoPago() == MetodoPago.TARJETA)
                .mapToDouble(v->v.getTotal())
                .sum();

        double transferencias = ventas.stream()
                .filter(v -> v.getMetodoPago() == MetodoPago.TRANSFERENCIA)
                .mapToDouble(v->v.getTotal())
                .sum();

        return new TotalesCajaDto(efectivo, mp, tarjetas, transferencias);
    }

}
