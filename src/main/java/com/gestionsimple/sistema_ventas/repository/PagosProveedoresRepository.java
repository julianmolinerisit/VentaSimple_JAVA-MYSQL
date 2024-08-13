package com.gestionsimple.sistema_ventas.repository;

import com.gestionsimple.sistema_ventas.model.PagosProveedores;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagosProveedoresRepository extends JpaRepository<PagosProveedores, Long> {
    List<PagosProveedores> findByProveedorId(Long proveedorId);
}
