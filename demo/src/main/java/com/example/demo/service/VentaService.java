package com.example.demo.service;

import com.example.demo.entity.DetalleVenta;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Venta;
import com.example.demo.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    VentaRepository vr;
    public VentaService(VentaRepository vr){
        this.vr=vr;
    }

    public List<Venta> getAll(){
        return vr.findAll();
    }
    public Optional<Venta> getById(Long id){
        return vr.findById(id);
    }
    public List<Venta> getByFecha(LocalDate fecha){
        LocalDateTime inicio=fecha.atStartOfDay();
        LocalDateTime fin=fecha.atTime(LocalTime.MAX);
        return vr.findByFechaBetween(inicio,fin);
    }
    public Venta create(LocalDateTime fecha, List<DetalleVenta> detalles){
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un detalle");
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setFecha(fecha);
        double totalAux = 0;

        // Validación stock y cálculo subtotal en memoria
        for (DetalleVenta d : detalles) {
            Producto p = d.getProducto();

            if (p.getStock() < d.getCantidad()) {
                throw new IllegalArgumentException(
                        "No hay stock suficiente para el producto: " + p.getNombre()
                );
            }

            // Reducir stock en memoria
            p.setStock(p.getStock() - d.getCantidad());
            // subtotal

            // relacion bidireccional
            d.setVenta(nuevaVenta);

            totalAux += d.getSubTotal();
        }

        nuevaVenta.setDetalles(detalles);
        nuevaVenta.setTotal(totalAux);


        return vr.save(nuevaVenta);
    }

    public Optional<Venta> delete(Long id){
        Optional<Venta> op=vr.findById(id);
        if(op.isPresent()){
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
        Double total= vr.totalVentasEntreFechas(start, end);
        return total != null ?total:0.0;
    }
}
