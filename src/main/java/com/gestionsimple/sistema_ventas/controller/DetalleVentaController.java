package com.gestionsimple.sistema_ventas.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gestionsimple.sistema_ventas.dto.VentaConDetallesDTO;
import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.services.DetalleVentaService;

@Controller
@RequestMapping("/detalles-venta")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @GetMapping
    public String mostrarTodosDetallesVenta(Model model) {
        List<DetalleVenta> detallesVenta = detalleVentaService.obtenerTodosDetallesVenta();
        List<VentaConDetallesDTO> ventasConDetalles = procesarDetallesVenta(detallesVenta);
        model.addAttribute("ventasConDetalles", ventasConDetalles);
        return "detalles_venta";
    }

    @GetMapping("/filtrar-por-fecha")
    public String mostrarDetallesVentaPorFecha(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora, // Cambiar a LocalDateTime
            Model model) {

        List<DetalleVenta> detallesVenta = detalleVentaService.obtenerDetallesVentaPorFecha(fechaHora);
        List<VentaConDetallesDTO> ventasConDetalles = procesarDetallesVenta(detallesVenta);
        model.addAttribute("ventasConDetalles", ventasConDetalles);
        return "detalles_venta";
    }

    @GetMapping("/buscar")
    public String buscarDetallesVenta(
            @RequestParam("query") String query,
            Model model) {

        List<DetalleVenta> detallesVenta = detalleVentaService.buscarDetallesVenta(query);
        List<VentaConDetallesDTO> ventasConDetalles = procesarDetallesVenta(detallesVenta);
        model.addAttribute("ventasConDetalles", ventasConDetalles);
        return "detalles_venta";
    }

    private List<VentaConDetallesDTO> procesarDetallesVenta(List<DetalleVenta> detallesVenta) {
        Map<Long, List<DetalleVenta>> mapDetallesPorVenta = new HashMap<>();
        for (DetalleVenta detalle : detallesVenta) {
            Long idVenta = detalle.getVenta().getId();
            if (!mapDetallesPorVenta.containsKey(idVenta)) {
                mapDetallesPorVenta.put(idVenta, new ArrayList<>());
            }
            mapDetallesPorVenta.get(idVenta).add(detalle);
        }

        // Formatear fecha y hora si es necesario en cada detalle
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mapDetallesPorVenta.forEach((idVenta, detalles) -> {
            detalles.forEach(detalle -> {
                String fechaFormateada = detalle.getVenta().getFechaHora().format(formatter);
                detalle.getVenta().setFechaHoraFormatted(fechaFormateada);
            });
        });

        // Calcular el total de la venta y pasar los datos al modelo
        List<VentaConDetallesDTO> ventasConDetalles = new ArrayList<>();
        mapDetallesPorVenta.forEach((idVenta, detalles) -> {
            VentaConDetallesDTO ventaConDetalles = new VentaConDetallesDTO();
            ventaConDetalles.setIdVenta(idVenta);
            ventaConDetalles.setFechaHoraFormatted(detalles.get(0).getVenta().getFechaHoraFormatted());
            ventaConDetalles.setMetodoPago(detalles.get(0).getVenta().getMetodoPago());
            ventaConDetalles.setMontoPagado(detalles.get(0).getVenta().getMontoPagado());
            ventaConDetalles.setVuelto(detalles.get(0).getVenta().getVuelto());

            double totalVenta = 0.0;
            for (DetalleVenta detalle : detalles) {
                totalVenta += detalle.getSubtotal();
            }
            ventaConDetalles.setTotal(totalVenta);

            ventaConDetalles.setDetallesVenta(detalles);
            ventasConDetalles.add(ventaConDetalles);
        });

        return ventasConDetalles;
    }
}
