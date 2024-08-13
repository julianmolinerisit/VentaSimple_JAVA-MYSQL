package com.gestionsimple.sistema_ventas.service;

import com.gestionsimple.sistema_ventas.model.PagosProveedores;
import java.util.List;

public interface PagosProveedoresService {
    List<PagosProveedores> getAllPagosProveedores();
    List<PagosProveedores> getPagosByProveedor(Long proveedorId);
    void savePagoProveedores(PagosProveedores pago);
    PagosProveedores getPagoProveedoresById(Long id);
    void deletePagoProveedoresById(Long id);
}
