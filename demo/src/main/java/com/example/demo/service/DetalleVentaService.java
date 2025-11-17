package com.example.demo.service;

import com.example.demo.dto.ProductoMasVendidoDto;
import com.example.demo.entity.DetalleVenta;
import com.example.demo.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleVentaService {

    DetalleVentaRepository dvr;

    public DetalleVentaService(DetalleVentaRepository dvr) {
        this.dvr = dvr;
    }

    public DetalleVenta create(DetalleVenta detalleVenta) {
        return dvr.save(detalleVenta);
    }

    public List<ProductoMasVendidoDto> getProductosMasVendidos(LocalDateTime inicio, LocalDateTime fin) {
        return dvr.productosMasVendidos(inicio, fin);
    }
    public List<DetalleVenta> getDetallesByIdVenta(Long id) {
        return dvr.findByVentaId(id);
    }
}
