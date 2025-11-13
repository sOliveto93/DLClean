package com.example.demo.repository;

import com.example.demo.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    Optional<Producto> findByCodigo(long codigo);
    Optional<List<Producto>> findByCategoria(String categoria);
    Optional<List<Producto>> findByNombreContainingIgnoreCase(String nombre);
    Optional<Producto>deleteByCodigo(long codigo);

}
