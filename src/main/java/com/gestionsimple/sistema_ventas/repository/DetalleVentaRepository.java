package com.gestionsimple.sistema_ventas.repository;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByProductoId(Long productoId);

    List<DetalleVenta> findByVentaId(Long ventaId);

    List<DetalleVenta> findByFecha(LocalDate fechaHoy);

    List<DetalleVenta> findByVentaFechaHora(LocalDateTime fechaHora);
    
    @Query("SELECT d FROM DetalleVenta d WHERE d.venta.fechaHora BETWEEN :startOfDay AND :endOfDay")
    List<DetalleVenta> findByVentaFechaHoraBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
