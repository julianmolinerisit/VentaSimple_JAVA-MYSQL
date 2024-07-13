package com.gestionsimple.sistema_ventas.services;

import java.util.List;

import com.gestionsimple.sistema_ventas.model.Venta;

public interface VentaService {
    void guardarVenta(Venta venta);
    Venta obtenerVentaPorId(Long id);
	List<Venta> obtenerTodasVentas();

}
