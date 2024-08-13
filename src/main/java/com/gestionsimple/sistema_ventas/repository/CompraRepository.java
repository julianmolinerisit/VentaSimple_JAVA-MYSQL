package com.gestionsimple.sistema_ventas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gestionsimple.sistema_ventas.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    Optional<Compra> findFirstByProductoIdOrderByFechaDesc(Long productoId);
}
