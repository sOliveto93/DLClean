package com.example.demo.service;

import com.example.demo.entity.DetalleVenta;
import com.example.demo.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;

@Service
public class DetalleVentaService {

    DetalleVentaRepository dvr;

    public DetalleVentaService(DetalleVentaRepository dvr){
        this.dvr=dvr;
    }

    public DetalleVenta create(DetalleVenta detalleVenta){
        return dvr.save(detalleVenta);
    }
}
