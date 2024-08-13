package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.PagosProveedores;
import com.gestionsimple.sistema_ventas.model.Proveedor;
import com.gestionsimple.sistema_ventas.service.PagosProveedoresService;
import com.gestionsimple.sistema_ventas.service.ProveedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PagosProveedoresController {

    private static final Logger logger = LoggerFactory.getLogger(PagosProveedoresController.class);

    @Autowired
    private PagosProveedoresService pagosProveedoresService;
    
    @Autowired
    private ProveedorService proveedorService;

    // Listar todos los pagos a proveedores
    @GetMapping("/pagosProveedores")
    public String listPagosProveedores(Model model) {
        logger.info("Listando todos los pagos a proveedores");
        model.addAttribute("pagosProveedores", pagosProveedoresService.getAllPagosProveedores());
        return "pagosProveedores";
    }
    
    // Mostrar formulario para nuevo pago a proveedor
    @GetMapping("/pagosProveedores/nuevo")
    public String createPagoProveedoresForm(Model model) {
        logger.info("Creando formulario para nuevo pago a proveedor");
        model.addAttribute("pagoProveedores", new PagosProveedores());
        model.addAttribute("proveedores", proveedorService.getAllProveedores());
        return "crear_pagoProveedores";
    }
    
    // Guardar nuevo pago a proveedor
    @PostMapping("/pagosProveedores")
    public String savePagoProveedores(@ModelAttribute("pagoProveedores") PagosProveedores pagoProveedores) {
        logger.info("Guardando nuevo pago a proveedor: {}", pagoProveedores);
        
        Proveedor proveedor = pagoProveedores.getProveedor();
        proveedor.setDeuda(proveedor.calcularDeudaActual() - pagoProveedores.getMonto());
        proveedorService.saveProveedor(proveedor);

        pagosProveedoresService.savePagoProveedores(pagoProveedores);
        return "redirect:/pagosProveedores";
    }

    // Ver detalles de pagos por proveedor
    @GetMapping("/proveedor/{id}/pagosProveedores")
    public String listPagosProveedoresByProveedor(@PathVariable("id") Long proveedorId, Model model) {
        logger.info("Listando pagos para proveedor con ID: {}", proveedorId);
        Proveedor proveedor = proveedorService.getProveedorById(proveedorId);
        if (proveedor != null) {
            model.addAttribute("pagosProveedores", pagosProveedoresService.getPagosByProveedor(proveedorId));
            model.addAttribute("proveedor", proveedor);
        } else {
            model.addAttribute("error", "Proveedor no encontrado");
        }
        return "pagosProveedores_por_proveedor";
    }
}
