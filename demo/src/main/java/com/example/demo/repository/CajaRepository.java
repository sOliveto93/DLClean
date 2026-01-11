package com.example.demo.repository;

import com.example.demo.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja,Long> {
    Optional<Caja> findTopByOrderByFechaHoraAperturaDesc();

    List<Caja> findTop10ByOrderByFechaHoraAperturaDesc();
}
