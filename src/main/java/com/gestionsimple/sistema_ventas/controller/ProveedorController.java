package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.Proveedor;
import com.gestionsimple.sistema_ventas.service.ProveedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest; // Usa javax si estás usando una versión anterior

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorController.class);

    @Autowired
    private ProveedorService proveedorService;

    // Listar todos los proveedores
    @GetMapping
    public String listProveedores(Model model) {
        logger.info("Listando todos los proveedores");
        model.addAttribute("proveedores", proveedorService.getAllProveedores());
        return "listaProveedores";
    }

    // Mostrar formulario para crear un nuevo proveedor
    @GetMapping("/nuevo")
    public String createProveedorForm(Model model, HttpServletRequest request) {
        logger.info("Creando formulario para nuevo proveedor");
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("requestURI", request.getRequestURI());
        return "crearProveedor";
    }

    // Guardar nuevo proveedor
    @PostMapping
    public String saveProveedor(@ModelAttribute("proveedor") Proveedor proveedor) {
        logger.info("Guardando nuevo proveedor: {}", proveedor);
        proveedorService.saveProveedor(proveedor);
        return "redirect:/proveedores";
    }

    // Mostrar formulario para editar un proveedor existente
    @GetMapping("/editar/{id}")
    public String editProveedorForm(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        logger.info("Editando proveedor con ID: {}", id);
        Proveedor proveedor = proveedorService.getProveedorById(id);
        if (proveedor != null) {
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("requestURI", request.getRequestURI());
            return "editarProveedor";
        } else {
            logger.error("Proveedor no encontrado con ID: {}", id);
            return "redirect:/proveedores";
        }
    }

    // Actualizar proveedor existente
    @PostMapping("/actualizar/{id}")
    public String updateProveedor(@PathVariable("id") Long id, @ModelAttribute("proveedor") Proveedor proveedor) {
        logger.info("Actualizando proveedor con ID: {}", id);
        Proveedor existingProveedor = proveedorService.getProveedorById(id);
        if (existingProveedor != null) {
            existingProveedor.setNombre(proveedor.getNombre());
            existingProveedor.setContacto(proveedor.getContacto());
            existingProveedor.setDireccion(proveedor.getDireccion());
            existingProveedor.setTelefono(proveedor.getTelefono());
            existingProveedor.setEmail(proveedor.getEmail());
            existingProveedor.setDeuda(proveedor.getDeuda());
            existingProveedor.setPagos(proveedor.getPagos());
            proveedorService.saveProveedor(existingProveedor);
            return "redirect:/proveedores";
        } else {
            logger.error("Proveedor no encontrado con ID: {}", id);
            return "redirect:/proveedores";
        }
    }

    // Eliminar un proveedor
    @GetMapping("/eliminar/{id}")
    public String deleteProveedor(@PathVariable("id") Long id) {
        logger.info("Eliminando proveedor con ID: {}", id);
        proveedorService.deleteProveedorById(id);
        return "redirect:/proveedores";
    }
}