package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.model.Venta;
import com.gestionsimple.sistema_ventas.services.ProductoService;
import com.gestionsimple.sistema_ventas.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import java.util.List;

@Controller
public class CajaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    @GetMapping("/caja")
    public String mostrarCaja() {
        return "caja";
    }

    @GetMapping("/productos/categoria/{categoria}")
    @ResponseBody
    public List<Producto> obtenerProductosPorCategoria(@PathVariable String categoria) {
        return productoService.obtenerProductosPorCategoria(categoria);
    }

    @PostMapping("/ventas")
    @ResponseBody
    public String registrarVenta(@RequestBody Venta venta) {
        // Asignar la fecha y hora actual del sistema al campo fechaHora
        venta.setFechaHora(LocalDateTime.now());

        double totalVenta = 0.0;
        
        for (DetalleVenta detalle : venta.getDetallesVenta()) {
            Producto producto = productoService.obtenerProductoPorId(detalle.getProducto().getId());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
            
            totalVenta += detalle.getSubtotal(); // Sumar al total de la venta
        }

        // Asignar el total calculado a la venta
        venta.setTotal(totalVenta);

        ventaService.guardarVenta(venta);

        // Actualizar el stock de los productos vendidos
        for (DetalleVenta detalle : venta.getDetallesVenta()) {
            productoService.actualizarStock(detalle.getProducto().getId(), detalle.getCantidad());
        }

        return "Venta registrada con éxito";
    }

}
