package com.gestionsimple.sistema_ventas.service;

import com.gestionsimple.sistema_ventas.model.Proveedor;
import java.util.List;

public interface ProveedorService {
    List<Proveedor> getAllProveedores();
    void saveProveedor(Proveedor proveedor);
    Proveedor getProveedorById(Long id);
    void deleteProveedorById(Long id);
}
