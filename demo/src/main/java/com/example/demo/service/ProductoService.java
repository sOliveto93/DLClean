package com.example.demo.service;

import com.example.demo.dto.ProductoDto;
import com.example.demo.entity.Producto;
import com.example.demo.exception.ProductoDuplicadoException;
import com.example.demo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private ProductoRepository pr;

    public ProductoService(ProductoRepository pr) {
        this.pr = pr;
    }

    public List<Producto> getAll() {
        return pr.findAll();
    }

    public Optional<Producto> getById(Long id) {
        return pr.findById(id);
    }

    public Optional<Producto> getByCodigo(long codigo) {
        return pr.findByCodigo(codigo);
    }

    public Optional<List<Producto>> getByNameIgnoreCase(String nombre) {
        return pr.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<List<Producto>> getByCategoria(String categoria) {
        return pr.findByCategoria(categoria);
    }

    public List<String> getAllCodigoBarra() {
        return pr.findAllCodigoBarra();
    }

    public Optional<Producto> getByCodigoBarra(String codigoBarra) {
        return pr.findByCodigoBarra(codigoBarra);
    }

    public Producto create(ProductoDto dto) {
        if (pr.findByCodigo(dto.getCodigo()).isPresent()) {
            throw new ProductoDuplicadoException("Ya existe un producto con el código " + dto.getCodigo());
        }
        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setCodigo(dto.getCodigo());
        p.setPrecioCosto(dto.getPrecioCosto());
        p.setPrecioVenta(dto.getPrecio());
        p.setCategoria(dto.getCategoria());
        p.setStock(dto.getStock());
        return pr.save(p);
    }

    public boolean deleteByCodigo(long codigo) {
        Optional<Producto> producto = pr.findByCodigo(codigo);
        if (producto.isPresent()) {
            pr.delete(producto.get());
            return true;
        }
        return false;
    }

    public boolean updateCodigoBarra(String codigoBarra, long codigo) {
        Optional<Producto> opViejo = getByCodigo(codigo);
        if (opViejo.isPresent()) {
            Producto nuevo = opViejo.get();
            nuevo.setCodigoBarra(codigoBarra);
            pr.save(nuevo);
            return true;
        }
        return false;
    }

    public boolean updateProducto(Producto nuevoProducto) {
        Optional<Producto> opViejo = getByCodigo(nuevoProducto.getCodigo());
        if (opViejo.isPresent()) {
            Producto pModificado = opViejo.get();
            pModificado.setNombre(nuevoProducto.getNombre());
            pModificado.setCategoria(nuevoProducto.getCategoria());
            pModificado.setPrecioCosto(nuevoProducto.getPrecioCosto());
            pModificado.setPrecioVenta(nuevoProducto.getPrecioVenta());
            pModificado.setStock(nuevoProducto.getStock());
            pModificado.setCodigoBarra(nuevoProducto.getCodigoBarra());
            pr.save(pModificado);
            return true;
        }
        return false;
    }

    public void crear(Producto producto) {
        pr.save(producto);
    }
}
