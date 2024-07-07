package com.gestionsimple.sistema_ventas.services;

import com.gestionsimple.sistema_ventas.model.Producto;

import java.util.List;

public interface ProductoService {
    List<String> obtenerTodasLasCategorias();
    List<Producto> obtenerProductosPorCategoria(String categoria);
    Producto obtenerProductoPorId(Long id);
    void actualizarProducto(Producto producto);
    void guardarProducto(Producto producto);  // Agregar este método
    List<Producto> obtenerTodosLosProductos();  // Agregar este método
    void eliminarProducto(Long id);  // Agregar este método

}
