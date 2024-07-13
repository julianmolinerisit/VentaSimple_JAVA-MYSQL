package com.gestionsimple.sistema_ventas.repository;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByProductoId(Long productoId);

    List<DetalleVenta> findByVentaId(Long ventaId);

}
