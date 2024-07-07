package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.model.Venta;
import com.gestionsimple.sistema_ventas.services.ProductoService;
import com.gestionsimple.sistema_ventas.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/caja")
public class CajaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public String mostrarFormularioVenta(Model model) {
        List<String> categorias = productoService.obtenerTodasLasCategorias();
        model.addAttribute("categorias", categorias);
        return "caja";  // El nombre del archivo HTML
    }

    @GetMapping("/productos")
    @ResponseBody
    public List<Producto> obtenerProductosPorCategoria(@RequestParam String categoria) {
        return productoService.obtenerProductosPorCategoria(categoria);
    }

    @PostMapping("/procesar")
    public String procesarVenta(@RequestParam Map<String, String> requestParams, Model model) {
        Venta venta = new Venta();
        venta.setFechaHora(LocalDateTime.now());
        venta.setMetodoPago(requestParams.get("metodoPago"));

        List<DetalleVenta> detallesVenta = new ArrayList<>();
        double total = 0;

        // Iteramos sobre los parámetros para obtener los productos y sus cantidades
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            if (entry.getKey().startsWith("producto_")) {
                Long productoId = Long.parseLong(entry.getKey().split("_")[1]);
                int cantidad = Integer.parseInt(entry.getValue());
                Producto producto = productoService.obtenerProductoPorId(productoId);

                if (producto != null && cantidad > 0) {
                    DetalleVenta detalleVenta = new DetalleVenta();
                    detalleVenta.setProducto(producto);
                    detalleVenta.setCantidad(cantidad);
                    detalleVenta.setVenta(venta);
                    detallesVenta.add(detalleVenta);

                    // Actualizamos el stock del producto
                    producto.setCantidad(producto.getCantidad() - cantidad);
                    productoService.guardarProducto(producto);  // Usar guardarProducto en lugar de actualizarProducto

                    total += producto.getPrecio() * cantidad;
                }
            }
        }

        venta.setDetallesVenta(detallesVenta);
        venta.setTotal(total);

        // Calculamos el vuelto si el método de pago es efectivo
        if ("efectivo".equals(requestParams.get("metodoPago"))) {
            double montoRecibido = Double.parseDouble(requestParams.get("efectivo"));
            venta.setMontoRecibido(montoRecibido);
            venta.setVuelto(montoRecibido - total);
        }

        ventaService.guardarVenta(venta);

        model.addAttribute("mensaje", "Venta procesada exitosamente");
        model.addAttribute("total", total);
        model.addAttribute("vuelto", venta.getVuelto());

        return "caja";  // El nombre del archivo HTML
    }
}
