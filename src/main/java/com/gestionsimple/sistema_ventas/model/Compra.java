package com.gestionsimple.sistema_ventas.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
public class Compra {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Date fecha;

	private double total;
	private double precioCompra;
	@Column(nullable = false)
	private Boolean esPesable; // Para indicar si el producto es por peso o unitario
	private double cantidad;
	private String medioDePago;
	private boolean pagado; // Para saber si la compra fue pagada o no

	@ManyToOne(fetch = FetchType.LAZY) // Usar carga lenta para evitar cargar innecesariamente los datos del proveedor
	@JoinColumn(name = "producto_id", nullable = false)
	private Producto producto;

	@ManyToOne(fetch = FetchType.LAZY) // Usar carga lenta para evitar cargar innecesariamente los datos del proveedor
	@JoinColumn(name = "proveedor_id", nullable = false)
	private Proveedor proveedor;

	// Getters y Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getprecioCompra() {
		return precioCompra;
	}

	public void setprecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidadKilos) {
		this.cantidad = cantidadKilos;
	}

	public String getMedioDePago() {
		return medioDePago;
	}

	public void setMedioDePago(String medioDePago) {
		this.medioDePago = medioDePago;
	}

	public boolean isPagado() {
		return pagado;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Boolean getEsPesable() {
		return esPesable;
	}

	public void setEsPesable(Boolean esPesable) {
		this.esPesable = esPesable;
	}
}
