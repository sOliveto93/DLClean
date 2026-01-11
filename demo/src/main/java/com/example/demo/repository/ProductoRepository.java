package com.example.demo.repository;

import com.example.demo.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    Optional<Producto> findByCodigo(long codigo);
    Optional<Producto> findByCodigoBarra(String codigoBarra);
    Optional<List<Producto>> findByCategoria(String categoria);
    Optional<List<Producto>> findByNombreContainingIgnoreCase(String nombre);
    Optional<Producto>deleteByCodigo(long codigo);
    @Query("select p.codigoBarra from Producto p")
    List<String> findAllCodigoBarra();

}
