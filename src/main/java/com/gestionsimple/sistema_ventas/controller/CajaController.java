package com.gestionsimple.sistema_ventas.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.model.Producto;
import com.gestionsimple.sistema_ventas.model.Venta;
import com.gestionsimple.sistema_ventas.services.ProductoService;
import com.gestionsimple.sistema_ventas.services.VentaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CajaController {

	@Autowired
	private ProductoService productoService;

	@Autowired
	private VentaService ventaService;

	@GetMapping("/caja")
	public String showCajaPage(Model model, HttpSession session) {
		// Obtener el nombre de usuario desde la sesión
		
		String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        System.out.println("Nombre de usuario en sesión: " + nombreUsuario);

        if (nombreUsuario != null) {
            model.addAttribute("nombreUsuario", nombreUsuario);
        } else {
            model.addAttribute("nombreUsuario", "Invitado"); // Valor por defecto si el nombre del usuario no está en la sesión
        }
		return "caja"; // Asegúrate de tener una vista llamada "caja.html"
	}

	@GetMapping("/productos/categoria/{categoria}")
	@ResponseBody
	public List<Producto> obtenerProductosPorCategoria(@PathVariable String categoria) {
		return productoService.obtenerProductosPorCategoria(categoria);
	}

	@PostMapping("/ventas")
	@ResponseBody
	public String registrarVenta(@RequestBody Venta venta) {
		// Asignar la fecha y hora actual del sistema al campo fechaHora
		venta.setFechaHora(LocalDateTime.now());

		double totalVenta = 0.0;

		for (DetalleVenta detalle : venta.getDetallesVenta()) {
			Producto producto = productoService.obtenerProductoPorId(detalle.getProducto().getId());
			detalle.setNombreProducto(producto.getNombre());
			detalle.setPrecioUnitario(producto.getPrecio());
			detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());

			totalVenta += detalle.getSubtotal(); // Sumar al total de la venta
		}

		// Asignar el total calculado a la venta
		venta.setTotal(totalVenta);

		if ("EFECTIVO".equalsIgnoreCase(venta.getMetodoPago())) {
			// Calcular el vuelto si el método de pago es efectivo
			double vuelto = venta.getMontoPagado() - totalVenta;
			venta.setVuelto(vuelto);
		} else {
			// Si el método de pago no es efectivo, el monto pagado y el vuelto son 0
			venta.setMontoPagado(0);
			venta.setVuelto(0);
		}

		ventaService.guardarVenta(venta);

		// Actualizar el stock de los productos vendidos
		for (DetalleVenta detalle : venta.getDetallesVenta()) {
			productoService.actualizarStock(detalle.getProducto().getId(), detalle.getCantidad());
		}

		return "Venta registrada con éxito";
	}

	@GetMapping("/logout")
	public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return new RedirectView("/login");
	}


}