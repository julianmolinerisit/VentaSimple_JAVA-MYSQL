package com.gestionsimple.sistema_ventas.services;

import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<String> obtenerTodasLasCategorias() {
        return productoRepository.findAllCategories();
    }

    @Override
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public void actualizarProducto(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public void guardarProducto(Producto producto) {  // Implementar este método
        productoRepository.save(producto);
    }
    
    @Override
    public List<Producto> obtenerTodosLosProductos() {  // Implementar este método
        return (List<Producto>) productoRepository.findAll();
    }
    
    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
