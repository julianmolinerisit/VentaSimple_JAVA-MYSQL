package com.gestionsimple.sistema_ventas.services.impl;

import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.repository.ProductoRepository;
import com.gestionsimple.sistema_ventas.services.ProductoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<String> obtenerTodasLasCategorias() {
        logger.info("Obteniendo todas las categorías de productos");
        return productoRepository.findAll()
                .stream()
                .map(Producto::getCategoria)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        logger.info("Obteniendo productos por categoría: {}", categoria);
        return productoRepository.findByCategoriaAndActivo(categoria, true);
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        logger.info("Obteniendo producto por ID: {}", id);
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public void actualizarProducto(Producto producto) {
        logger.info("Actualizando producto: {}", producto);
        productoRepository.save(producto);
    }

    @Override
    public void guardarProducto(Producto producto) {
        logger.info("Guardando producto: {}", producto);
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        logger.info("Obteniendo todos los productos");
        return productoRepository.findAll();
    }

    @Override
    public void eliminarProducto(Long id) {
        logger.info("Eliminando producto con ID: {}", id);
        productoRepository.deleteById(id);
    }

    @Override
    public void actualizarStock(Long productoId, double cantidad) {
        logger.info("Actualizando stock para producto con ID: {}, cantidad: {}", productoId, cantidad);
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setCantidad(producto.getCantidad() - (int) cantidad);
        productoRepository.save(producto);
    }

    @Override
    public boolean productoInvolucradoEnVenta(Long id) {
        // Implementación para verificar si un producto está involucrado en alguna venta
        // Puedes hacerlo consultando las ventas relacionadas al producto
        logger.info("Verificando si el producto con ID {} está involucrado en alguna venta", id);
        return false;
    }
    
    
    
}
