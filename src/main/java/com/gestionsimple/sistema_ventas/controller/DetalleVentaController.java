package com.gestionsimple.sistema_ventas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.services.DetalleVentaService;

@Controller
@RequestMapping("/detalles-venta")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @GetMapping
    public String mostrarTodosDetallesVenta(Model model) {
        // Obtener todos los detalles de venta disponibles
        List<DetalleVenta> detallesVenta = detalleVentaService.obtenerTodosDetallesVenta();

        model.addAttribute("detallesVenta", detallesVenta);

        return "detalles_venta"; // Se asume que detalles_venta.html manejará la lista de detalles de venta
    }
}
