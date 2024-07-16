package com.gestionsimple.sistema_ventas.services;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import java.util.List;

public interface DetalleVentaService {
    List<DetalleVenta> obtenerDetallesVentaPorIdVenta(Long idVenta);
    List<DetalleVenta> obtenerDetallesVentaPorProductoId(Long productoId);
    List<DetalleVenta> obtenerTodosDetallesVenta(); // Nuevo método
}
