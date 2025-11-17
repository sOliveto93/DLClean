package com.example.demo.repository;

import com.example.demo.entity.DetalleVenta;
import com.example.demo.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface VentaRepository extends JpaRepository<Venta,Long> {

    List<Venta> findByFechaBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT DISTINCT v FROM Venta v JOIN v.detalles d WHERE d.producto.codigo = :codigo")
    List<Venta> findByProductoCodigo(@Param("codigo") long codigo);
    @Query("SELECT SUM(v.total) from Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    Double totalVentasEntreFechas(@Param("inicio") LocalDateTime inicio,@Param("fin") LocalDateTime fin);


}
