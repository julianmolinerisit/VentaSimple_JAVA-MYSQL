package com.gestionsimple.sistema_ventas.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String contacto;
    private String direccion;
    private String telefono;
    private String email;
    private Double deuda = 0.0; // Inicialización por defecto

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    private List<Compra> compras;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    private List<PagosProveedores> pagos; // Lista de pagos realizados al proveedor

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getDeuda() {
        return deuda;
    }

    public void setDeuda(Double deuda) {
        this.deuda = deuda;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public List<PagosProveedores> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagosProveedores> pagos) {
        this.pagos = pagos;
    }

    // Calcular la deuda actual basada en las compras y pagos
    public Double calcularDeudaActual() {
        Double totalCompras = compras.stream().mapToDouble(Compra::getTotal).sum();
        Double totalPagos = pagos.stream().mapToDouble(PagosProveedores::getMonto).sum();
        return totalCompras - totalPagos;
    }
}
