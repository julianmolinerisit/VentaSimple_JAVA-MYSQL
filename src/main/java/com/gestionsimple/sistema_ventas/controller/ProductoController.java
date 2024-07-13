package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.model.DetalleVenta; // Importa DetalleVenta desde el paquete correcto
import com.gestionsimple.sistema_ventas.services.ProductoService;
import com.gestionsimple.sistema_ventas.services.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    private final List<String> categorias = Arrays.asList(
            "Cortes de Cerdo", "Cortes de Ternera", "Pollo",
            "Mercadería", "Insumos", "Bultos"
    );

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        logger.info("Mostrando formulario de creación de producto");
        Producto nuevoProducto = new Producto();
        nuevoProducto.setActivo(true); // Establecer activo por defecto
        model.addAttribute("producto", nuevoProducto);
        model.addAttribute("categorias", categorias);
        return "crear_producto";
    }
    
    
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto, Model model) {
        logger.info("Guardando producto: {}", producto);
        // Aquí se debe asegurar que el estado activo sea manejado correctamente
        producto.setActivo(true); // Opcionalmente, puedes asegurarte de que esté activo al guardar
        productoService.guardarProducto(producto);
        model.addAttribute("mensaje", "Producto guardado exitosamente");
        return "redirect:/productos";
    }


    @GetMapping
    public String mostrarProductos(Model model) {
        logger.info("Mostrando todos los productos");
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);
        logger.debug("Productos obtenidos: {}", productos);
        return "productos";
    }

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
        model.addAttribute("categorias", categorias);
        return "editar_producto";
    }

    @PostMapping("/editar")
    public String procesarEdicion(@ModelAttribute("producto") Producto producto, Model model) {
        logger.info("Actualizando producto: {}", producto);
        productoService.actualizarProducto(producto);
        model.addAttribute("mensaje", "Producto actualizado exitosamente");
        return "redirect:/productos";
    }

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



    
    // Actualizar estado activo
    @PostMapping("/activar/{id}")
    public String actualizarEstadoActivo(@PathVariable("id") Long id, @RequestParam("activo") boolean activo) {
        Producto producto = productoService.obtenerProductoPorId(id);
        producto.setActivo(activo);
        productoService.guardarProducto(producto);
        return "redirect:/productos";
    }

}
