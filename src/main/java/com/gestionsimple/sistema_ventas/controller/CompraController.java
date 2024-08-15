package com.gestionsimple.sistema_ventas.controller;

import com.gestionsimple.sistema_ventas.model.Compra;
import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.model.Proveedor;
import com.gestionsimple.sistema_ventas.model.contabilidad.AsientoContable;
import com.gestionsimple.sistema_ventas.model.contabilidad.CuentaContable;
import com.gestionsimple.sistema_ventas.service.CompraService;
import com.gestionsimple.sistema_ventas.service.ProductoService;
import com.gestionsimple.sistema_ventas.service.ProveedorService;
import com.gestionsimple.sistema_ventas.service.contabilidad.AsientoContableService;
import com.gestionsimple.sistema_ventas.service.contabilidad.CuentaContableService;

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

    @Autowired
    private AsientoContableService asientoContableService;

    @Autowired
    private CuentaContableService cuentaContableService;

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
            return "error";
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
            producto.setPrecioCompra(BigDecimal.valueOf(compra.getPrecioCompra()));
            productoService.guardarProducto(producto);
        } else {
            logger.error("Producto no encontrado: {}", compra.getProducto().getId());
            return "error";
        }

        compraService.saveCompra(compra);

        // Registrar asientos contables
        try {
            // Obtener cuentas contables
            CuentaContable cuentaInventario = cuentaContableService.obtenerCuentaPorNombre("Inventario");
            CuentaContable cuentaProveedor = cuentaContableService.obtenerCuentaPorNombre("Proveedor");
            CuentaContable cuentaCaja = cuentaContableService.obtenerCuentaPorNombre("Caja");
            CuentaContable cuentaGasto = cuentaContableService.obtenerCuentaPorNombre("Gasto");

            // Registro de aumento de inventario
            asientoContableService.registrarAsiento(cuentaInventario.getId(), compra.getTotal(),
                    "Ingreso por compra. Total: " + compra.getTotal(), "Compra realizada. Total: " + compra.getTotal(), "COMPRA");

            // Registro de cuenta por pagar al proveedor
            if (!compra.isPagado()) {
                asientoContableService.registrarAsiento(cuentaProveedor.getId(), -compra.getTotal(),
                        "Deuda con proveedor por compra. Total: " + compra.getTotal(), "Compra realizada. Deuda con proveedor", "COMPRA");
            } else {
                // Registro del pago si es pagado en efectivo
                if ("EFECTIVO".equals(compra.getMedioDePago())) {
                    asientoContableService.registrarAsiento(cuentaCaja.getId(), -compra.getTotal(),
                            "Egreso por pago de compra. Total: " + compra.getTotal(), "Compra pagada en efectivo", "COMPRA");
                }
            }

            // Registro de gasto (si aplica)
            double gastoTotal = calcularGastoTotal(compra);
            if (gastoTotal > 0) {
                asientoContableService.registrarAsiento(cuentaGasto.getId(), gastoTotal,
                        "Gasto por compra. Total: " + gastoTotal, "Compra realizada. Gasto", "COMPRA");
            }

        } catch (RuntimeException e) {
            logger.error("Error al obtener cuentas contables: {}", e.getMessage());
            return "error";
        } catch (Exception e) {
            logger.error("Error al registrar el asiento contable: {}", e.getMessage());
            return "error";
        }

        return "redirect:/compras";
    }

    private double calcularGastoTotal(Compra compra) {
        // Verifica si el total es null y retorna 0.0 si lo es, de lo contrario retorna el valor del total
        return compra.getTotal() != null ? compra.getTotal() : 0.0;
    }

}
