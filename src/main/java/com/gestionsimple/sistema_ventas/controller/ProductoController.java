package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.model.Compra;
import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.model.Categoria;
import com.gestionsimple.sistema_ventas.service.ProductoService;
import com.gestionsimple.sistema_ventas.service.CategoriaService;
import com.gestionsimple.sistema_ventas.service.CompraService;
import com.gestionsimple.sistema_ventas.service.DetalleVentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    // Mostrar todos los productos
    @GetMapping
    public String mostrarProductos(Model model) {
        logger.info("Mostrando todos los productos");
        List<Producto> productos = productoService.obtenerTodosLosProductos();

        for (Producto producto : productos) {
            logger.info("Producto: {} - Precio de venta: {}", producto.getNombre(), producto.getPrecioVenta());

            if (producto.getPrecioCompra().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal diferencia = producto.getPrecioVenta().subtract(producto.getPrecioCompra());
                BigDecimal porcentajeRentabilidad = diferencia
                        .divide(producto.getPrecioCompra(), RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                producto.setPorcentajeRentabilidad(porcentajeRentabilidad);
            }

            producto.calcularGananciaUnitaria();
            producto.calcularGananciaTotal();
            producto.calcularDineroTotalRecaudado();
        }

        model.addAttribute("productos", productos);
        return "productos";
    }

    // Mostrar formulario de creación de producto
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        logger.info("Mostrando formulario de creación de producto");
        Producto nuevoProducto = new Producto();
        nuevoProducto.setActivo(true); // Establecer activo por defecto
        model.addAttribute("producto", nuevoProducto);
        model.addAttribute("categorias", categoriaService.getAllCategorias());
        return "crear_producto";
    }

    // Guardar producto
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto, Model model) {
        logger.info("Guardando producto: {}", producto);
        producto.setFechaRegistro(LocalDate.now());
        producto.setActivo(true); // Asegurar que esté activo al guardar
        productoService.guardarProducto(producto);
        model.addAttribute("mensaje", "Producto guardado exitosamente");
        return "redirect:/productos";
    }

    // Mostrar formulario de edición de producto
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        logger.info("Mostrando formulario de edición para el producto con ID: {}", id);
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto == null) {
            logger.error("Producto con ID {} no encontrado", id);
            model.addAttribute("error", "Producto no encontrado");
            return "redirect:/productos";
        }
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.getAllCategorias());
        return "editar_producto";
    }

    // Procesar edición de producto
    @PostMapping("/editar")
    public String procesarEdicion(@ModelAttribute("producto") Producto producto, Model model) {
        logger.info("Actualizando producto: {}", producto);
        productoService.actualizarProducto(producto);
        model.addAttribute("mensaje", "Producto actualizado exitosamente");
        return "redirect:/productos";
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, Model model) {
        logger.info("Eliminando producto con ID: {}", id);

        // Verificar si el producto está asociado a detalles de venta
        List<DetalleVenta> detallesVenta = detalleVentaService.obtenerDetallesVentaPorProductoId(id);
        if (!detallesVenta.isEmpty()) {
            // Producto asociado a detalles de venta, mostrar mensaje de advertencia
            model.addAttribute("mensaje", "No se puede eliminar el producto porque está asociado a ventas.");
            // Obtener y retornar la lista actualizada de productos
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            model.addAttribute("productos", productos);
            logger.debug("Productos obtenidos: {}", productos);
            return "productos";
        }

        productoService.eliminarProducto(id);
        model.addAttribute("mensaje", "Producto eliminado exitosamente");
        return "redirect:/productos";
    }
    
    // Activar/desactivar producto
    @PostMapping("/activar/{id}")
    public String actualizarEstadoActivo(@PathVariable("id") Long id, @RequestParam("activo") boolean activo) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            producto.setActivo(activo);
            productoService.guardarProducto(producto);
        }
        return "redirect:/productos";
    }

    // Importar compras
    @PostMapping("/{id}/importarCompras")
    public String importarCompras(@PathVariable Long id, @RequestParam("compraId") Long compraId) {
        Producto producto = productoService.obtenerProductoPorId(id);
        Compra compra = compraService.getCompraById(compraId);

        if (producto != null && compra != null) {
            if (compra.getEsPesable()) {
                producto.setStock(producto.getStock() + (int) compra.getCantidad());
            } else {
                producto.setStock(producto.getStock() + (int) compra.getCantidad());
            }
            productoService.guardarProducto(producto);
        }

        return "redirect:/productos";
    }

    // Actualizar stock
    @PostMapping("/{id}/actualizarStock")
    public String actualizarStock(@RequestParam("id") Long id, @RequestParam("newStock") int newStock, RedirectAttributes redirectAttributes) {
        productoService.actualizarStock(id, newStock);
        redirectAttributes.addFlashAttribute("message", "Stock actualizado correctamente.");
        return "redirect:/productos";
    }

    // Actualizar precio de compra
    @PostMapping("/{id}/actualizarPrecioCompra")
    public String actualizarPrecioCompra(@RequestParam("id") Long id, @RequestParam("newPrecioCompra") Double newPrecioCompra, RedirectAttributes redirectAttributes) {
        productoService.actualizarPrecioCompra(id, newPrecioCompra);
        redirectAttributes.addFlashAttribute("message", "Precio de compra actualizado correctamente.");
        return "redirect:/productos";
    }

    
    // Mostrar rentabilidad
    @GetMapping("/rentabilidad")
    public String mostrarRentabilidad(Model model) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);
        return "rentabilidad";
    }

 // Actualizar datos de rentabilidad
    @PostMapping("/{id}/actualizarRentabilidadDatos")
    @ResponseBody
    public ResponseEntity<String> actualizarRentabilidadDatos(@PathVariable("id") Long idProducto, @RequestBody Map<String, Double> datos) {
        Producto producto = productoService.obtenerProductoPorId(idProducto);

        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }

        try {
            producto.setPorcentajeRentabilidad(BigDecimal.valueOf(datos.getOrDefault("porcentajeRentabilidad", 0.0)));
            producto.setPrecioVenta(BigDecimal.valueOf(datos.getOrDefault("precioVenta", 0.0)));
            producto.setGananciaTotal(BigDecimal.valueOf(datos.getOrDefault("gananciaTotal", 0.0)));
            producto.setGananciaUnitaria(BigDecimal.valueOf(datos.getOrDefault("gananciaUnitaria", 0.0)));
            producto.setInversionTotal(BigDecimal.valueOf(datos.getOrDefault("inversionTotal", 0.0)));
            producto.setDineroTotalRecaudado(BigDecimal.valueOf(datos.getOrDefault("dineroTotalRecaudado", 0.0)));
            producto.setGrasaDesperdicio(BigDecimal.valueOf(datos.getOrDefault("grasaDesperdicio", 0.0)));
            producto.setOtrosDesperdicios(BigDecimal.valueOf(datos.getOrDefault("otrosDesperdicios", 0.0)));

            productoService.guardarProducto(producto);
            return ResponseEntity.ok("Datos actualizados correctamente.");
        } catch (Exception e) {
            logger.error("Error al actualizar datos de rentabilidad", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar datos");
        }
    }

}
