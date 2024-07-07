package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Definimos las categorías disponibles
    private final List<String> categorias = Arrays.asList(
            "Cortes de Cerdo", "Cortes de Ternera", "Pollo",
            "Mercadería", "Insumos", "Bultos"
    );

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categorias); // Pasamos las categorías al modelo
        return "crear_producto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto, Model model) {
        // Guardar o actualizar el producto
        productoService.guardarProducto(producto);
        
        model.addAttribute("mensaje", "Producto guardado exitosamente");
        return "redirect:/productos";
    }

    @GetMapping
    public String mostrarProductos(Model model) {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);
        return "productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto == null) {
            model.addAttribute("error", "Producto no encontrado");
            return "redirect:/productos";
        }
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias); // Pasamos las categorías al modelo
        return "editar_producto";
    }

    @PostMapping("/editar")
    public String procesarEdicion(@ModelAttribute("producto") Producto producto, Model model) {
        // Validar si el campo descripción está vacío
        if (producto.getDescripcion() == null || producto.getDescripcion().isEmpty()) {
            producto.setDescripcion("Sin descripción");
        }
        productoService.actualizarProducto(producto);
        model.addAttribute("mensaje", "Producto actualizado exitosamente");
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, Model model) {
        productoService.eliminarProducto(id);
        model.addAttribute("mensaje", "Producto eliminado exitosamente");
        return "redirect:/productos";
    }
}
