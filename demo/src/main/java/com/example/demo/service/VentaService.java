package com.example.demo.service;

import com.example.demo.dto.VentaDto;
import com.example.demo.entity.DetalleVenta;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.VentaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    VentaRepository vr;
    ProductoService productoService;

    public VentaService(VentaRepository vr, ProductoService productoService) {
        this.vr = vr;
        this.productoService = productoService;
    }

    public List<Venta> getAll() {
        return vr.findAll();
    }

    public Optional<Venta> getById(Long id) {
        return vr.findById(id);
    }

    public List<Venta> getByFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return vr.findByFechaBetween(inicio, fin);
    }


    @Transactional
    public Venta create(VentaDto dto) {

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un detalle");
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setFecha(dto.getFecha());
        nuevaVenta.setMetodoPago(dto.getMetodoPago());

        //double totalAux = 0;

        for (DetalleVenta d : dto.getDetalles()) {

            Producto p = productoService.getByCodigo(
                    d.getProducto().getCodigo()
            ).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            if (p.getStock() < d.getCantidad()) {
                // se permite, pero se registra el faltante
                int faltante = d.getCantidad() - p.getStock();
                System.out.println("Venta con stock negativo para producto {" + p.getCodigo() + "}. Faltante: {" + faltante + "}");
            }


            p.setStock(p.getStock() - d.getCantidad());

            // asegurar bidireccionalidad
            d.setVenta(nuevaVenta);
            d.setProducto(p);


            // usar el precio que viene del dto (el usuario puede haber aplicado descuento)
            d.setPrecioUnitario(d.getPrecioUnitario());

           // totalAux += d.getSubTotal();
        }

        nuevaVenta.setDetalles(dto.getDetalles());
        nuevaVenta.setTotal(dto.getTotal());

        // Save venta y detalles (cascade)
        return vr.save(nuevaVenta);
    }

    public Optional<Venta> delete(Long id) {
        Optional<Venta> op = vr.findById(id);
        if (op.isPresent()) {
            vr.delete(op.get());
        }
        return op;
    }

    public List<Venta> getByProducto(long codigo) {
        return vr.findByProductoCodigo(codigo);
    }

    public double getTotalVentas(LocalDate inicio, LocalDate fin) {
        LocalDateTime start = inicio.atStartOfDay();
        LocalDateTime end = fin.atTime(LocalTime.MAX);
        //puede devolver null por eso usamos el wraper DOUBLE
        Double total = vr.totalVentasEntreFechas(start, end);
        return total != null ? total : 0.0;
    }
}
