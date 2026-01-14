package com.example.demo.repository;

import com.example.demo.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja,Long> {
    Optional<Caja> findTopByOrderByFechaHoraAperturaDesc();

    List<Caja> findTop10ByOrderByFechaHoraAperturaDesc();
    @Query("SELECT COALESCE(SUM(c.dineroTotal), 0) FROM Caja c WHERE (:inicio IS NULL OR c.fechaHoraCierre >= :inicio) AND (:fin IS NULL OR c.fechaHoraCierre < :fin)")
    Double totalCajaEntreFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

}
