package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.Compra;
import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.model.Proveedor;
import com.gestionsimple.sistema_ventas.service.CompraService;
import com.gestionsimple.sistema_ventas.service.ProductoService;
import com.gestionsimple.sistema_ventas.service.ProveedorService;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    @Autowired
    private CompraService compraService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/compras")
    public String listCompras(Model model) {
        logger.info("Listando todas las compras");
        model.addAttribute("compras", compraService.getAllCompras());
        return "compras";
    }

    @GetMapping("/compras/nueva")
    public String createCompraForm(Model model) {
        logger.info("Creando formulario para nueva compra");
        model.addAttribute("compra", new Compra());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        model.addAttribute("proveedores", proveedorService.getAllProveedores());
        return "crear_compra";
    }

    @PostMapping("/compras")
    public String saveCompra(@ModelAttribute("compra") Compra compra) {
        logger.info("Guardando nueva compra: {}", compra);

        Proveedor proveedor = proveedorService.getProveedorById(compra.getProveedor().getId());

        if (proveedor != null) {
            if (!compra.isPagado()) {
                proveedor.setDeuda(proveedor.getDeuda() + compra.getTotal());
                proveedorService.saveProveedor(proveedor);
            }
        } else {
            logger.error("Proveedor no encontrado: {}", compra.getProveedor().getId());
        }

        // Actualizar el stock del producto
        Producto producto = productoService.obtenerProductoPorId(compra.getProducto().getId());
        if (producto != null) {
            if (compra.getEsPesable()) {
                producto.setStock(producto.getStock() + (int) compra.getCantidad()); // Suma la cantidad en kilos
            } else {
                producto.setStock(producto.getStock() + (int) compra.getCantidad()); // Suma la cantidad en unidades
            }
            
            // Actualizar el precio de compra al último registrado
         // Actualizar el precio de compra al último registrado
            producto.setPrecioCompra(BigDecimal.valueOf(compra.getprecioCompra()));

            productoService.guardarProducto(producto);
        } else {
            logger.error("Producto no encontrado: {}", compra.getProducto().getId());
        }

        compraService.saveCompra(compra);
        return "redirect:/compras";
    }

}
