package com.gestionsimple.sistema_ventas.services;

import com.gestionsimple.sistema_ventas.model.Producto;

import java.util.List;

public interface ProductoService {
    List<String> obtenerTodasLasCategorias();
    List<Producto> obtenerProductosPorCategoria(String categoria);
    Producto obtenerProductoPorId(Long id);
    void actualizarProducto(Producto producto);
    Producto guardarProducto(Producto producto);
    List<Producto> obtenerTodosLosProductos();
    void eliminarProducto(Long id);
    void actualizarStock(Long productoId, double cantidad);
    boolean productoInvolucradoEnVenta(Long id); // Nuevo método
    
}
