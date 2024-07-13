package com.gestionsimple.sistema_ventas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.model.Venta;
import com.gestionsimple.sistema_ventas.services.DetalleVentaService;
import com.gestionsimple.sistema_ventas.services.VentaService;

@Controller
@RequestMapping("/detalles-venta")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private VentaService ventaService;

    @GetMapping("/{idVenta}")
    public String mostrarDetallesVenta(@PathVariable Long idVenta, Model model) {
        // Obtener detalles de venta por ID de venta
        List<DetalleVenta> detallesVenta = detalleVentaService.obtenerDetallesVentaPorIdVenta(idVenta);
        
        // Obtener la venta por ID para mostrar detalles relevantes
        Venta venta = ventaService.obtenerVentaPorId(idVenta);

        model.addAttribute("detallesVenta", detallesVenta);
        model.addAttribute("venta", venta);

        return "detalles_venta";
    }

    @GetMapping
    public String mostrarTodosDetallesVenta(Model model) {
        // Obtener todas las ventas disponibles
        List<Venta> ventas = ventaService.obtenerTodasVentas();

        model.addAttribute("ventas", ventas);

        return "detalles_venta"; // Se asume que detalles_venta.html manejará la lista de ventas
    }
}
