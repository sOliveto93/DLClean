package com.example.demo.repository;

import com.example.demo.dto.ProductoDto;
import com.example.demo.dto.ProductoMasVendidoDto;
import com.example.demo.entity.DetalleVenta;
import com.example.demo.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta,Long> {

    @Query("SELECT new com.example.demo.dto.ProductoMasVendidoDto(p, SUM(dv.cantidad),SUM(dv.precioUnitario * dv.cantidad)) " +
            "FROM DetalleVenta dv " +
            "JOIN dv.producto p " +
            "JOIN dv.venta v " +
            "WHERE (:inicio IS NULL OR v.fecha >= :inicio) " +
            "AND (:fin IS NULL OR v.fecha <= :fin) " +
            "GROUP BY p.id, p " +
            "ORDER BY SUM(dv.cantidad) DESC")
    List<ProductoMasVendidoDto> productosMasVendidos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
    @Query("SELECT d FROM DetalleVenta d WHERE d.venta.id = :ventaId")
    List<DetalleVenta> findByVentaId(@Param("ventaId") Long ventaId);
}

